package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ItemReference;
import com.mycompany.myapp.repository.ItemReferenceRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ItemReferenceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ItemReferenceResourceIT {

    private static final Long DEFAULT_REFERENCE_ID = 1L;
    private static final Long UPDATED_REFERENCE_ID = 2L;

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/item-references";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ItemReferenceRepository itemReferenceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restItemReferenceMockMvc;

    private ItemReference itemReference;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemReference createEntity(EntityManager em) {
        ItemReference itemReference = new ItemReference().referenceId(DEFAULT_REFERENCE_ID).type(DEFAULT_TYPE);
        return itemReference;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemReference createUpdatedEntity(EntityManager em) {
        ItemReference itemReference = new ItemReference().referenceId(UPDATED_REFERENCE_ID).type(UPDATED_TYPE);
        return itemReference;
    }

    @BeforeEach
    public void initTest() {
        itemReference = createEntity(em);
    }

    @Test
    @Transactional
    void createItemReference() throws Exception {
        int databaseSizeBeforeCreate = itemReferenceRepository.findAll().size();
        // Create the ItemReference
        restItemReferenceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemReference)))
            .andExpect(status().isCreated());

        // Validate the ItemReference in the database
        List<ItemReference> itemReferenceList = itemReferenceRepository.findAll();
        assertThat(itemReferenceList).hasSize(databaseSizeBeforeCreate + 1);
        ItemReference testItemReference = itemReferenceList.get(itemReferenceList.size() - 1);
        assertThat(testItemReference.getReferenceId()).isEqualTo(DEFAULT_REFERENCE_ID);
        assertThat(testItemReference.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void createItemReferenceWithExistingId() throws Exception {
        // Create the ItemReference with an existing ID
        itemReference.setId(1L);

        int databaseSizeBeforeCreate = itemReferenceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemReferenceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemReference)))
            .andExpect(status().isBadRequest());

        // Validate the ItemReference in the database
        List<ItemReference> itemReferenceList = itemReferenceRepository.findAll();
        assertThat(itemReferenceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllItemReferences() throws Exception {
        // Initialize the database
        itemReferenceRepository.saveAndFlush(itemReference);

        // Get all the itemReferenceList
        restItemReferenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemReference.getId().intValue())))
            .andExpect(jsonPath("$.[*].referenceId").value(hasItem(DEFAULT_REFERENCE_ID.intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));
    }

    @Test
    @Transactional
    void getItemReference() throws Exception {
        // Initialize the database
        itemReferenceRepository.saveAndFlush(itemReference);

        // Get the itemReference
        restItemReferenceMockMvc
            .perform(get(ENTITY_API_URL_ID, itemReference.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(itemReference.getId().intValue()))
            .andExpect(jsonPath("$.referenceId").value(DEFAULT_REFERENCE_ID.intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE));
    }

    @Test
    @Transactional
    void getNonExistingItemReference() throws Exception {
        // Get the itemReference
        restItemReferenceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewItemReference() throws Exception {
        // Initialize the database
        itemReferenceRepository.saveAndFlush(itemReference);

        int databaseSizeBeforeUpdate = itemReferenceRepository.findAll().size();

        // Update the itemReference
        ItemReference updatedItemReference = itemReferenceRepository.findById(itemReference.getId()).get();
        // Disconnect from session so that the updates on updatedItemReference are not directly saved in db
        em.detach(updatedItemReference);
        updatedItemReference.referenceId(UPDATED_REFERENCE_ID).type(UPDATED_TYPE);

        restItemReferenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedItemReference.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedItemReference))
            )
            .andExpect(status().isOk());

        // Validate the ItemReference in the database
        List<ItemReference> itemReferenceList = itemReferenceRepository.findAll();
        assertThat(itemReferenceList).hasSize(databaseSizeBeforeUpdate);
        ItemReference testItemReference = itemReferenceList.get(itemReferenceList.size() - 1);
        assertThat(testItemReference.getReferenceId()).isEqualTo(UPDATED_REFERENCE_ID);
        assertThat(testItemReference.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingItemReference() throws Exception {
        int databaseSizeBeforeUpdate = itemReferenceRepository.findAll().size();
        itemReference.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemReferenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, itemReference.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemReference))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemReference in the database
        List<ItemReference> itemReferenceList = itemReferenceRepository.findAll();
        assertThat(itemReferenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchItemReference() throws Exception {
        int databaseSizeBeforeUpdate = itemReferenceRepository.findAll().size();
        itemReference.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemReferenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemReference))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemReference in the database
        List<ItemReference> itemReferenceList = itemReferenceRepository.findAll();
        assertThat(itemReferenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamItemReference() throws Exception {
        int databaseSizeBeforeUpdate = itemReferenceRepository.findAll().size();
        itemReference.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemReferenceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemReference)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ItemReference in the database
        List<ItemReference> itemReferenceList = itemReferenceRepository.findAll();
        assertThat(itemReferenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateItemReferenceWithPatch() throws Exception {
        // Initialize the database
        itemReferenceRepository.saveAndFlush(itemReference);

        int databaseSizeBeforeUpdate = itemReferenceRepository.findAll().size();

        // Update the itemReference using partial update
        ItemReference partialUpdatedItemReference = new ItemReference();
        partialUpdatedItemReference.setId(itemReference.getId());

        partialUpdatedItemReference.referenceId(UPDATED_REFERENCE_ID);

        restItemReferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItemReference.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItemReference))
            )
            .andExpect(status().isOk());

        // Validate the ItemReference in the database
        List<ItemReference> itemReferenceList = itemReferenceRepository.findAll();
        assertThat(itemReferenceList).hasSize(databaseSizeBeforeUpdate);
        ItemReference testItemReference = itemReferenceList.get(itemReferenceList.size() - 1);
        assertThat(testItemReference.getReferenceId()).isEqualTo(UPDATED_REFERENCE_ID);
        assertThat(testItemReference.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateItemReferenceWithPatch() throws Exception {
        // Initialize the database
        itemReferenceRepository.saveAndFlush(itemReference);

        int databaseSizeBeforeUpdate = itemReferenceRepository.findAll().size();

        // Update the itemReference using partial update
        ItemReference partialUpdatedItemReference = new ItemReference();
        partialUpdatedItemReference.setId(itemReference.getId());

        partialUpdatedItemReference.referenceId(UPDATED_REFERENCE_ID).type(UPDATED_TYPE);

        restItemReferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItemReference.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItemReference))
            )
            .andExpect(status().isOk());

        // Validate the ItemReference in the database
        List<ItemReference> itemReferenceList = itemReferenceRepository.findAll();
        assertThat(itemReferenceList).hasSize(databaseSizeBeforeUpdate);
        ItemReference testItemReference = itemReferenceList.get(itemReferenceList.size() - 1);
        assertThat(testItemReference.getReferenceId()).isEqualTo(UPDATED_REFERENCE_ID);
        assertThat(testItemReference.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingItemReference() throws Exception {
        int databaseSizeBeforeUpdate = itemReferenceRepository.findAll().size();
        itemReference.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemReferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, itemReference.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemReference))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemReference in the database
        List<ItemReference> itemReferenceList = itemReferenceRepository.findAll();
        assertThat(itemReferenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchItemReference() throws Exception {
        int databaseSizeBeforeUpdate = itemReferenceRepository.findAll().size();
        itemReference.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemReferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemReference))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemReference in the database
        List<ItemReference> itemReferenceList = itemReferenceRepository.findAll();
        assertThat(itemReferenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamItemReference() throws Exception {
        int databaseSizeBeforeUpdate = itemReferenceRepository.findAll().size();
        itemReference.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemReferenceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(itemReference))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ItemReference in the database
        List<ItemReference> itemReferenceList = itemReferenceRepository.findAll();
        assertThat(itemReferenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteItemReference() throws Exception {
        // Initialize the database
        itemReferenceRepository.saveAndFlush(itemReference);

        int databaseSizeBeforeDelete = itemReferenceRepository.findAll().size();

        // Delete the itemReference
        restItemReferenceMockMvc
            .perform(delete(ENTITY_API_URL_ID, itemReference.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ItemReference> itemReferenceList = itemReferenceRepository.findAll();
        assertThat(itemReferenceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
