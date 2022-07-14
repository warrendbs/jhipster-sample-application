package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.BomChild;
import com.mycompany.myapp.repository.BomChildRepository;
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
 * Integration tests for the {@link BomChildResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BomChildResourceIT {

    private static final String DEFAULT_PRODUCT_ID = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_REVISION = "AAAAAAAAAA";
    private static final String UPDATED_REVISION = "BBBBBBBBBB";

    private static final Double DEFAULT_QUANTITY = 1D;
    private static final Double UPDATED_QUANTITY = 2D;

    private static final String DEFAULT_RELATION_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_RELATION_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/bom-children";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BomChildRepository bomChildRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBomChildMockMvc;

    private BomChild bomChild;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BomChild createEntity(EntityManager em) {
        BomChild bomChild = new BomChild()
            .productId(DEFAULT_PRODUCT_ID)
            .revision(DEFAULT_REVISION)
            .quantity(DEFAULT_QUANTITY)
            .relationType(DEFAULT_RELATION_TYPE);
        return bomChild;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BomChild createUpdatedEntity(EntityManager em) {
        BomChild bomChild = new BomChild()
            .productId(UPDATED_PRODUCT_ID)
            .revision(UPDATED_REVISION)
            .quantity(UPDATED_QUANTITY)
            .relationType(UPDATED_RELATION_TYPE);
        return bomChild;
    }

    @BeforeEach
    public void initTest() {
        bomChild = createEntity(em);
    }

    @Test
    @Transactional
    void createBomChild() throws Exception {
        int databaseSizeBeforeCreate = bomChildRepository.findAll().size();
        // Create the BomChild
        restBomChildMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bomChild)))
            .andExpect(status().isCreated());

        // Validate the BomChild in the database
        List<BomChild> bomChildList = bomChildRepository.findAll();
        assertThat(bomChildList).hasSize(databaseSizeBeforeCreate + 1);
        BomChild testBomChild = bomChildList.get(bomChildList.size() - 1);
        assertThat(testBomChild.getProductId()).isEqualTo(DEFAULT_PRODUCT_ID);
        assertThat(testBomChild.getRevision()).isEqualTo(DEFAULT_REVISION);
        assertThat(testBomChild.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testBomChild.getRelationType()).isEqualTo(DEFAULT_RELATION_TYPE);
    }

    @Test
    @Transactional
    void createBomChildWithExistingId() throws Exception {
        // Create the BomChild with an existing ID
        bomChild.setId(1L);

        int databaseSizeBeforeCreate = bomChildRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBomChildMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bomChild)))
            .andExpect(status().isBadRequest());

        // Validate the BomChild in the database
        List<BomChild> bomChildList = bomChildRepository.findAll();
        assertThat(bomChildList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBomChildren() throws Exception {
        // Initialize the database
        bomChildRepository.saveAndFlush(bomChild);

        // Get all the bomChildList
        restBomChildMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bomChild.getId().intValue())))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID)))
            .andExpect(jsonPath("$.[*].revision").value(hasItem(DEFAULT_REVISION)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.doubleValue())))
            .andExpect(jsonPath("$.[*].relationType").value(hasItem(DEFAULT_RELATION_TYPE)));
    }

    @Test
    @Transactional
    void getBomChild() throws Exception {
        // Initialize the database
        bomChildRepository.saveAndFlush(bomChild);

        // Get the bomChild
        restBomChildMockMvc
            .perform(get(ENTITY_API_URL_ID, bomChild.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bomChild.getId().intValue()))
            .andExpect(jsonPath("$.productId").value(DEFAULT_PRODUCT_ID))
            .andExpect(jsonPath("$.revision").value(DEFAULT_REVISION))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY.doubleValue()))
            .andExpect(jsonPath("$.relationType").value(DEFAULT_RELATION_TYPE));
    }

    @Test
    @Transactional
    void getNonExistingBomChild() throws Exception {
        // Get the bomChild
        restBomChildMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBomChild() throws Exception {
        // Initialize the database
        bomChildRepository.saveAndFlush(bomChild);

        int databaseSizeBeforeUpdate = bomChildRepository.findAll().size();

        // Update the bomChild
        BomChild updatedBomChild = bomChildRepository.findById(bomChild.getId()).get();
        // Disconnect from session so that the updates on updatedBomChild are not directly saved in db
        em.detach(updatedBomChild);
        updatedBomChild
            .productId(UPDATED_PRODUCT_ID)
            .revision(UPDATED_REVISION)
            .quantity(UPDATED_QUANTITY)
            .relationType(UPDATED_RELATION_TYPE);

        restBomChildMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBomChild.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBomChild))
            )
            .andExpect(status().isOk());

        // Validate the BomChild in the database
        List<BomChild> bomChildList = bomChildRepository.findAll();
        assertThat(bomChildList).hasSize(databaseSizeBeforeUpdate);
        BomChild testBomChild = bomChildList.get(bomChildList.size() - 1);
        assertThat(testBomChild.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testBomChild.getRevision()).isEqualTo(UPDATED_REVISION);
        assertThat(testBomChild.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testBomChild.getRelationType()).isEqualTo(UPDATED_RELATION_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingBomChild() throws Exception {
        int databaseSizeBeforeUpdate = bomChildRepository.findAll().size();
        bomChild.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBomChildMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bomChild.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bomChild))
            )
            .andExpect(status().isBadRequest());

        // Validate the BomChild in the database
        List<BomChild> bomChildList = bomChildRepository.findAll();
        assertThat(bomChildList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBomChild() throws Exception {
        int databaseSizeBeforeUpdate = bomChildRepository.findAll().size();
        bomChild.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBomChildMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bomChild))
            )
            .andExpect(status().isBadRequest());

        // Validate the BomChild in the database
        List<BomChild> bomChildList = bomChildRepository.findAll();
        assertThat(bomChildList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBomChild() throws Exception {
        int databaseSizeBeforeUpdate = bomChildRepository.findAll().size();
        bomChild.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBomChildMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bomChild)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BomChild in the database
        List<BomChild> bomChildList = bomChildRepository.findAll();
        assertThat(bomChildList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBomChildWithPatch() throws Exception {
        // Initialize the database
        bomChildRepository.saveAndFlush(bomChild);

        int databaseSizeBeforeUpdate = bomChildRepository.findAll().size();

        // Update the bomChild using partial update
        BomChild partialUpdatedBomChild = new BomChild();
        partialUpdatedBomChild.setId(bomChild.getId());

        partialUpdatedBomChild.productId(UPDATED_PRODUCT_ID).relationType(UPDATED_RELATION_TYPE);

        restBomChildMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBomChild.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBomChild))
            )
            .andExpect(status().isOk());

        // Validate the BomChild in the database
        List<BomChild> bomChildList = bomChildRepository.findAll();
        assertThat(bomChildList).hasSize(databaseSizeBeforeUpdate);
        BomChild testBomChild = bomChildList.get(bomChildList.size() - 1);
        assertThat(testBomChild.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testBomChild.getRevision()).isEqualTo(DEFAULT_REVISION);
        assertThat(testBomChild.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testBomChild.getRelationType()).isEqualTo(UPDATED_RELATION_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateBomChildWithPatch() throws Exception {
        // Initialize the database
        bomChildRepository.saveAndFlush(bomChild);

        int databaseSizeBeforeUpdate = bomChildRepository.findAll().size();

        // Update the bomChild using partial update
        BomChild partialUpdatedBomChild = new BomChild();
        partialUpdatedBomChild.setId(bomChild.getId());

        partialUpdatedBomChild
            .productId(UPDATED_PRODUCT_ID)
            .revision(UPDATED_REVISION)
            .quantity(UPDATED_QUANTITY)
            .relationType(UPDATED_RELATION_TYPE);

        restBomChildMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBomChild.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBomChild))
            )
            .andExpect(status().isOk());

        // Validate the BomChild in the database
        List<BomChild> bomChildList = bomChildRepository.findAll();
        assertThat(bomChildList).hasSize(databaseSizeBeforeUpdate);
        BomChild testBomChild = bomChildList.get(bomChildList.size() - 1);
        assertThat(testBomChild.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testBomChild.getRevision()).isEqualTo(UPDATED_REVISION);
        assertThat(testBomChild.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testBomChild.getRelationType()).isEqualTo(UPDATED_RELATION_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingBomChild() throws Exception {
        int databaseSizeBeforeUpdate = bomChildRepository.findAll().size();
        bomChild.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBomChildMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bomChild.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bomChild))
            )
            .andExpect(status().isBadRequest());

        // Validate the BomChild in the database
        List<BomChild> bomChildList = bomChildRepository.findAll();
        assertThat(bomChildList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBomChild() throws Exception {
        int databaseSizeBeforeUpdate = bomChildRepository.findAll().size();
        bomChild.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBomChildMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bomChild))
            )
            .andExpect(status().isBadRequest());

        // Validate the BomChild in the database
        List<BomChild> bomChildList = bomChildRepository.findAll();
        assertThat(bomChildList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBomChild() throws Exception {
        int databaseSizeBeforeUpdate = bomChildRepository.findAll().size();
        bomChild.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBomChildMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(bomChild)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BomChild in the database
        List<BomChild> bomChildList = bomChildRepository.findAll();
        assertThat(bomChildList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBomChild() throws Exception {
        // Initialize the database
        bomChildRepository.saveAndFlush(bomChild);

        int databaseSizeBeforeDelete = bomChildRepository.findAll().size();

        // Delete the bomChild
        restBomChildMockMvc
            .perform(delete(ENTITY_API_URL_ID, bomChild.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BomChild> bomChildList = bomChildRepository.findAll();
        assertThat(bomChildList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
