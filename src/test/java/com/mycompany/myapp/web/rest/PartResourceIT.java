package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Part;
import com.mycompany.myapp.repository.PartRepository;
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
 * Integration tests for the {@link PartResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PartResourceIT {

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_CHANGE_INDICATION = false;
    private static final Boolean UPDATED_CHANGE_INDICATION = true;

    private static final Boolean DEFAULT_IS_PARENT_PART_BOM_CHANGED = false;
    private static final Boolean UPDATED_IS_PARENT_PART_BOM_CHANGED = true;

    private static final String ENTITY_API_URL = "/api/parts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PartRepository partRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPartMockMvc;

    private Part part;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Part createEntity(EntityManager em) {
        Part part = new Part()
            .status(DEFAULT_STATUS)
            .changeIndication(DEFAULT_CHANGE_INDICATION)
            .isParentPartBomChanged(DEFAULT_IS_PARENT_PART_BOM_CHANGED);
        return part;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Part createUpdatedEntity(EntityManager em) {
        Part part = new Part()
            .status(UPDATED_STATUS)
            .changeIndication(UPDATED_CHANGE_INDICATION)
            .isParentPartBomChanged(UPDATED_IS_PARENT_PART_BOM_CHANGED);
        return part;
    }

    @BeforeEach
    public void initTest() {
        part = createEntity(em);
    }

    @Test
    @Transactional
    void createPart() throws Exception {
        int databaseSizeBeforeCreate = partRepository.findAll().size();
        // Create the Part
        restPartMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(part)))
            .andExpect(status().isCreated());

        // Validate the Part in the database
        List<Part> partList = partRepository.findAll();
        assertThat(partList).hasSize(databaseSizeBeforeCreate + 1);
        Part testPart = partList.get(partList.size() - 1);
        assertThat(testPart.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testPart.getChangeIndication()).isEqualTo(DEFAULT_CHANGE_INDICATION);
        assertThat(testPart.getIsParentPartBomChanged()).isEqualTo(DEFAULT_IS_PARENT_PART_BOM_CHANGED);
    }

    @Test
    @Transactional
    void createPartWithExistingId() throws Exception {
        // Create the Part with an existing ID
        part.setId(1L);

        int databaseSizeBeforeCreate = partRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPartMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(part)))
            .andExpect(status().isBadRequest());

        // Validate the Part in the database
        List<Part> partList = partRepository.findAll();
        assertThat(partList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllParts() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        // Get all the partList
        restPartMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(part.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].changeIndication").value(hasItem(DEFAULT_CHANGE_INDICATION.booleanValue())))
            .andExpect(jsonPath("$.[*].isParentPartBomChanged").value(hasItem(DEFAULT_IS_PARENT_PART_BOM_CHANGED.booleanValue())));
    }

    @Test
    @Transactional
    void getPart() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        // Get the part
        restPartMockMvc
            .perform(get(ENTITY_API_URL_ID, part.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(part.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.changeIndication").value(DEFAULT_CHANGE_INDICATION.booleanValue()))
            .andExpect(jsonPath("$.isParentPartBomChanged").value(DEFAULT_IS_PARENT_PART_BOM_CHANGED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingPart() throws Exception {
        // Get the part
        restPartMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPart() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        int databaseSizeBeforeUpdate = partRepository.findAll().size();

        // Update the part
        Part updatedPart = partRepository.findById(part.getId()).get();
        // Disconnect from session so that the updates on updatedPart are not directly saved in db
        em.detach(updatedPart);
        updatedPart
            .status(UPDATED_STATUS)
            .changeIndication(UPDATED_CHANGE_INDICATION)
            .isParentPartBomChanged(UPDATED_IS_PARENT_PART_BOM_CHANGED);

        restPartMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPart.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPart))
            )
            .andExpect(status().isOk());

        // Validate the Part in the database
        List<Part> partList = partRepository.findAll();
        assertThat(partList).hasSize(databaseSizeBeforeUpdate);
        Part testPart = partList.get(partList.size() - 1);
        assertThat(testPart.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testPart.getChangeIndication()).isEqualTo(UPDATED_CHANGE_INDICATION);
        assertThat(testPart.getIsParentPartBomChanged()).isEqualTo(UPDATED_IS_PARENT_PART_BOM_CHANGED);
    }

    @Test
    @Transactional
    void putNonExistingPart() throws Exception {
        int databaseSizeBeforeUpdate = partRepository.findAll().size();
        part.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartMockMvc
            .perform(
                put(ENTITY_API_URL_ID, part.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(part))
            )
            .andExpect(status().isBadRequest());

        // Validate the Part in the database
        List<Part> partList = partRepository.findAll();
        assertThat(partList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPart() throws Exception {
        int databaseSizeBeforeUpdate = partRepository.findAll().size();
        part.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(part))
            )
            .andExpect(status().isBadRequest());

        // Validate the Part in the database
        List<Part> partList = partRepository.findAll();
        assertThat(partList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPart() throws Exception {
        int databaseSizeBeforeUpdate = partRepository.findAll().size();
        part.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(part)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Part in the database
        List<Part> partList = partRepository.findAll();
        assertThat(partList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePartWithPatch() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        int databaseSizeBeforeUpdate = partRepository.findAll().size();

        // Update the part using partial update
        Part partialUpdatedPart = new Part();
        partialUpdatedPart.setId(part.getId());

        restPartMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPart.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPart))
            )
            .andExpect(status().isOk());

        // Validate the Part in the database
        List<Part> partList = partRepository.findAll();
        assertThat(partList).hasSize(databaseSizeBeforeUpdate);
        Part testPart = partList.get(partList.size() - 1);
        assertThat(testPart.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testPart.getChangeIndication()).isEqualTo(DEFAULT_CHANGE_INDICATION);
        assertThat(testPart.getIsParentPartBomChanged()).isEqualTo(DEFAULT_IS_PARENT_PART_BOM_CHANGED);
    }

    @Test
    @Transactional
    void fullUpdatePartWithPatch() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        int databaseSizeBeforeUpdate = partRepository.findAll().size();

        // Update the part using partial update
        Part partialUpdatedPart = new Part();
        partialUpdatedPart.setId(part.getId());

        partialUpdatedPart
            .status(UPDATED_STATUS)
            .changeIndication(UPDATED_CHANGE_INDICATION)
            .isParentPartBomChanged(UPDATED_IS_PARENT_PART_BOM_CHANGED);

        restPartMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPart.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPart))
            )
            .andExpect(status().isOk());

        // Validate the Part in the database
        List<Part> partList = partRepository.findAll();
        assertThat(partList).hasSize(databaseSizeBeforeUpdate);
        Part testPart = partList.get(partList.size() - 1);
        assertThat(testPart.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testPart.getChangeIndication()).isEqualTo(UPDATED_CHANGE_INDICATION);
        assertThat(testPart.getIsParentPartBomChanged()).isEqualTo(UPDATED_IS_PARENT_PART_BOM_CHANGED);
    }

    @Test
    @Transactional
    void patchNonExistingPart() throws Exception {
        int databaseSizeBeforeUpdate = partRepository.findAll().size();
        part.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, part.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(part))
            )
            .andExpect(status().isBadRequest());

        // Validate the Part in the database
        List<Part> partList = partRepository.findAll();
        assertThat(partList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPart() throws Exception {
        int databaseSizeBeforeUpdate = partRepository.findAll().size();
        part.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(part))
            )
            .andExpect(status().isBadRequest());

        // Validate the Part in the database
        List<Part> partList = partRepository.findAll();
        assertThat(partList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPart() throws Exception {
        int databaseSizeBeforeUpdate = partRepository.findAll().size();
        part.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(part)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Part in the database
        List<Part> partList = partRepository.findAll();
        assertThat(partList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePart() throws Exception {
        // Initialize the database
        partRepository.saveAndFlush(part);

        int databaseSizeBeforeDelete = partRepository.findAll().size();

        // Delete the part
        restPartMockMvc
            .perform(delete(ENTITY_API_URL_ID, part.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Part> partList = partRepository.findAll();
        assertThat(partList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
