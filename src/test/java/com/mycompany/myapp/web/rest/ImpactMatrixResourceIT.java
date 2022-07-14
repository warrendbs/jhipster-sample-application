package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ImpactMatrix;
import com.mycompany.myapp.repository.ImpactMatrixRepository;
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
 * Integration tests for the {@link ImpactMatrixResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ImpactMatrixResourceIT {

    private static final Long DEFAULT_IMPACT_MATRIX_NUMBER = 1L;
    private static final Long UPDATED_IMPACT_MATRIX_NUMBER = 2L;

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    private static final String DEFAULT_REVISION = "AAAAAAAAAA";
    private static final String UPDATED_REVISION = "BBBBBBBBBB";

    private static final String DEFAULT_REVISER = "AAAAAAAAAA";
    private static final String UPDATED_REVISER = "BBBBBBBBBB";

    private static final String DEFAULT_REVISION_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_REVISION_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_DATE_REVISED = "AAAAAAAAAA";
    private static final String UPDATED_DATE_REVISED = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_AUTO_LAYOUT_ENABLED = false;
    private static final Boolean UPDATED_IS_AUTO_LAYOUT_ENABLED = true;

    private static final String ENTITY_API_URL = "/api/impact-matrices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ImpactMatrixRepository impactMatrixRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restImpactMatrixMockMvc;

    private ImpactMatrix impactMatrix;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ImpactMatrix createEntity(EntityManager em) {
        ImpactMatrix impactMatrix = new ImpactMatrix()
            .impactMatrixNumber(DEFAULT_IMPACT_MATRIX_NUMBER)
            .status(DEFAULT_STATUS)
            .revision(DEFAULT_REVISION)
            .reviser(DEFAULT_REVISER)
            .revisionDescription(DEFAULT_REVISION_DESCRIPTION)
            .dateRevised(DEFAULT_DATE_REVISED)
            .title(DEFAULT_TITLE)
            .isAutoLayoutEnabled(DEFAULT_IS_AUTO_LAYOUT_ENABLED);
        return impactMatrix;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ImpactMatrix createUpdatedEntity(EntityManager em) {
        ImpactMatrix impactMatrix = new ImpactMatrix()
            .impactMatrixNumber(UPDATED_IMPACT_MATRIX_NUMBER)
            .status(UPDATED_STATUS)
            .revision(UPDATED_REVISION)
            .reviser(UPDATED_REVISER)
            .revisionDescription(UPDATED_REVISION_DESCRIPTION)
            .dateRevised(UPDATED_DATE_REVISED)
            .title(UPDATED_TITLE)
            .isAutoLayoutEnabled(UPDATED_IS_AUTO_LAYOUT_ENABLED);
        return impactMatrix;
    }

    @BeforeEach
    public void initTest() {
        impactMatrix = createEntity(em);
    }

    @Test
    @Transactional
    void createImpactMatrix() throws Exception {
        int databaseSizeBeforeCreate = impactMatrixRepository.findAll().size();
        // Create the ImpactMatrix
        restImpactMatrixMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(impactMatrix)))
            .andExpect(status().isCreated());

        // Validate the ImpactMatrix in the database
        List<ImpactMatrix> impactMatrixList = impactMatrixRepository.findAll();
        assertThat(impactMatrixList).hasSize(databaseSizeBeforeCreate + 1);
        ImpactMatrix testImpactMatrix = impactMatrixList.get(impactMatrixList.size() - 1);
        assertThat(testImpactMatrix.getImpactMatrixNumber()).isEqualTo(DEFAULT_IMPACT_MATRIX_NUMBER);
        assertThat(testImpactMatrix.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testImpactMatrix.getRevision()).isEqualTo(DEFAULT_REVISION);
        assertThat(testImpactMatrix.getReviser()).isEqualTo(DEFAULT_REVISER);
        assertThat(testImpactMatrix.getRevisionDescription()).isEqualTo(DEFAULT_REVISION_DESCRIPTION);
        assertThat(testImpactMatrix.getDateRevised()).isEqualTo(DEFAULT_DATE_REVISED);
        assertThat(testImpactMatrix.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testImpactMatrix.getIsAutoLayoutEnabled()).isEqualTo(DEFAULT_IS_AUTO_LAYOUT_ENABLED);
    }

    @Test
    @Transactional
    void createImpactMatrixWithExistingId() throws Exception {
        // Create the ImpactMatrix with an existing ID
        impactMatrix.setId(1L);

        int databaseSizeBeforeCreate = impactMatrixRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restImpactMatrixMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(impactMatrix)))
            .andExpect(status().isBadRequest());

        // Validate the ImpactMatrix in the database
        List<ImpactMatrix> impactMatrixList = impactMatrixRepository.findAll();
        assertThat(impactMatrixList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllImpactMatrices() throws Exception {
        // Initialize the database
        impactMatrixRepository.saveAndFlush(impactMatrix);

        // Get all the impactMatrixList
        restImpactMatrixMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(impactMatrix.getId().intValue())))
            .andExpect(jsonPath("$.[*].impactMatrixNumber").value(hasItem(DEFAULT_IMPACT_MATRIX_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].revision").value(hasItem(DEFAULT_REVISION)))
            .andExpect(jsonPath("$.[*].reviser").value(hasItem(DEFAULT_REVISER)))
            .andExpect(jsonPath("$.[*].revisionDescription").value(hasItem(DEFAULT_REVISION_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].dateRevised").value(hasItem(DEFAULT_DATE_REVISED)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].isAutoLayoutEnabled").value(hasItem(DEFAULT_IS_AUTO_LAYOUT_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    void getImpactMatrix() throws Exception {
        // Initialize the database
        impactMatrixRepository.saveAndFlush(impactMatrix);

        // Get the impactMatrix
        restImpactMatrixMockMvc
            .perform(get(ENTITY_API_URL_ID, impactMatrix.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(impactMatrix.getId().intValue()))
            .andExpect(jsonPath("$.impactMatrixNumber").value(DEFAULT_IMPACT_MATRIX_NUMBER.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.revision").value(DEFAULT_REVISION))
            .andExpect(jsonPath("$.reviser").value(DEFAULT_REVISER))
            .andExpect(jsonPath("$.revisionDescription").value(DEFAULT_REVISION_DESCRIPTION))
            .andExpect(jsonPath("$.dateRevised").value(DEFAULT_DATE_REVISED))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.isAutoLayoutEnabled").value(DEFAULT_IS_AUTO_LAYOUT_ENABLED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingImpactMatrix() throws Exception {
        // Get the impactMatrix
        restImpactMatrixMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewImpactMatrix() throws Exception {
        // Initialize the database
        impactMatrixRepository.saveAndFlush(impactMatrix);

        int databaseSizeBeforeUpdate = impactMatrixRepository.findAll().size();

        // Update the impactMatrix
        ImpactMatrix updatedImpactMatrix = impactMatrixRepository.findById(impactMatrix.getId()).get();
        // Disconnect from session so that the updates on updatedImpactMatrix are not directly saved in db
        em.detach(updatedImpactMatrix);
        updatedImpactMatrix
            .impactMatrixNumber(UPDATED_IMPACT_MATRIX_NUMBER)
            .status(UPDATED_STATUS)
            .revision(UPDATED_REVISION)
            .reviser(UPDATED_REVISER)
            .revisionDescription(UPDATED_REVISION_DESCRIPTION)
            .dateRevised(UPDATED_DATE_REVISED)
            .title(UPDATED_TITLE)
            .isAutoLayoutEnabled(UPDATED_IS_AUTO_LAYOUT_ENABLED);

        restImpactMatrixMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedImpactMatrix.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedImpactMatrix))
            )
            .andExpect(status().isOk());

        // Validate the ImpactMatrix in the database
        List<ImpactMatrix> impactMatrixList = impactMatrixRepository.findAll();
        assertThat(impactMatrixList).hasSize(databaseSizeBeforeUpdate);
        ImpactMatrix testImpactMatrix = impactMatrixList.get(impactMatrixList.size() - 1);
        assertThat(testImpactMatrix.getImpactMatrixNumber()).isEqualTo(UPDATED_IMPACT_MATRIX_NUMBER);
        assertThat(testImpactMatrix.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testImpactMatrix.getRevision()).isEqualTo(UPDATED_REVISION);
        assertThat(testImpactMatrix.getReviser()).isEqualTo(UPDATED_REVISER);
        assertThat(testImpactMatrix.getRevisionDescription()).isEqualTo(UPDATED_REVISION_DESCRIPTION);
        assertThat(testImpactMatrix.getDateRevised()).isEqualTo(UPDATED_DATE_REVISED);
        assertThat(testImpactMatrix.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testImpactMatrix.getIsAutoLayoutEnabled()).isEqualTo(UPDATED_IS_AUTO_LAYOUT_ENABLED);
    }

    @Test
    @Transactional
    void putNonExistingImpactMatrix() throws Exception {
        int databaseSizeBeforeUpdate = impactMatrixRepository.findAll().size();
        impactMatrix.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImpactMatrixMockMvc
            .perform(
                put(ENTITY_API_URL_ID, impactMatrix.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(impactMatrix))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImpactMatrix in the database
        List<ImpactMatrix> impactMatrixList = impactMatrixRepository.findAll();
        assertThat(impactMatrixList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchImpactMatrix() throws Exception {
        int databaseSizeBeforeUpdate = impactMatrixRepository.findAll().size();
        impactMatrix.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImpactMatrixMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(impactMatrix))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImpactMatrix in the database
        List<ImpactMatrix> impactMatrixList = impactMatrixRepository.findAll();
        assertThat(impactMatrixList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamImpactMatrix() throws Exception {
        int databaseSizeBeforeUpdate = impactMatrixRepository.findAll().size();
        impactMatrix.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImpactMatrixMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(impactMatrix)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ImpactMatrix in the database
        List<ImpactMatrix> impactMatrixList = impactMatrixRepository.findAll();
        assertThat(impactMatrixList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateImpactMatrixWithPatch() throws Exception {
        // Initialize the database
        impactMatrixRepository.saveAndFlush(impactMatrix);

        int databaseSizeBeforeUpdate = impactMatrixRepository.findAll().size();

        // Update the impactMatrix using partial update
        ImpactMatrix partialUpdatedImpactMatrix = new ImpactMatrix();
        partialUpdatedImpactMatrix.setId(impactMatrix.getId());

        partialUpdatedImpactMatrix
            .impactMatrixNumber(UPDATED_IMPACT_MATRIX_NUMBER)
            .revision(UPDATED_REVISION)
            .reviser(UPDATED_REVISER)
            .revisionDescription(UPDATED_REVISION_DESCRIPTION)
            .title(UPDATED_TITLE)
            .isAutoLayoutEnabled(UPDATED_IS_AUTO_LAYOUT_ENABLED);

        restImpactMatrixMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImpactMatrix.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedImpactMatrix))
            )
            .andExpect(status().isOk());

        // Validate the ImpactMatrix in the database
        List<ImpactMatrix> impactMatrixList = impactMatrixRepository.findAll();
        assertThat(impactMatrixList).hasSize(databaseSizeBeforeUpdate);
        ImpactMatrix testImpactMatrix = impactMatrixList.get(impactMatrixList.size() - 1);
        assertThat(testImpactMatrix.getImpactMatrixNumber()).isEqualTo(UPDATED_IMPACT_MATRIX_NUMBER);
        assertThat(testImpactMatrix.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testImpactMatrix.getRevision()).isEqualTo(UPDATED_REVISION);
        assertThat(testImpactMatrix.getReviser()).isEqualTo(UPDATED_REVISER);
        assertThat(testImpactMatrix.getRevisionDescription()).isEqualTo(UPDATED_REVISION_DESCRIPTION);
        assertThat(testImpactMatrix.getDateRevised()).isEqualTo(DEFAULT_DATE_REVISED);
        assertThat(testImpactMatrix.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testImpactMatrix.getIsAutoLayoutEnabled()).isEqualTo(UPDATED_IS_AUTO_LAYOUT_ENABLED);
    }

    @Test
    @Transactional
    void fullUpdateImpactMatrixWithPatch() throws Exception {
        // Initialize the database
        impactMatrixRepository.saveAndFlush(impactMatrix);

        int databaseSizeBeforeUpdate = impactMatrixRepository.findAll().size();

        // Update the impactMatrix using partial update
        ImpactMatrix partialUpdatedImpactMatrix = new ImpactMatrix();
        partialUpdatedImpactMatrix.setId(impactMatrix.getId());

        partialUpdatedImpactMatrix
            .impactMatrixNumber(UPDATED_IMPACT_MATRIX_NUMBER)
            .status(UPDATED_STATUS)
            .revision(UPDATED_REVISION)
            .reviser(UPDATED_REVISER)
            .revisionDescription(UPDATED_REVISION_DESCRIPTION)
            .dateRevised(UPDATED_DATE_REVISED)
            .title(UPDATED_TITLE)
            .isAutoLayoutEnabled(UPDATED_IS_AUTO_LAYOUT_ENABLED);

        restImpactMatrixMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImpactMatrix.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedImpactMatrix))
            )
            .andExpect(status().isOk());

        // Validate the ImpactMatrix in the database
        List<ImpactMatrix> impactMatrixList = impactMatrixRepository.findAll();
        assertThat(impactMatrixList).hasSize(databaseSizeBeforeUpdate);
        ImpactMatrix testImpactMatrix = impactMatrixList.get(impactMatrixList.size() - 1);
        assertThat(testImpactMatrix.getImpactMatrixNumber()).isEqualTo(UPDATED_IMPACT_MATRIX_NUMBER);
        assertThat(testImpactMatrix.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testImpactMatrix.getRevision()).isEqualTo(UPDATED_REVISION);
        assertThat(testImpactMatrix.getReviser()).isEqualTo(UPDATED_REVISER);
        assertThat(testImpactMatrix.getRevisionDescription()).isEqualTo(UPDATED_REVISION_DESCRIPTION);
        assertThat(testImpactMatrix.getDateRevised()).isEqualTo(UPDATED_DATE_REVISED);
        assertThat(testImpactMatrix.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testImpactMatrix.getIsAutoLayoutEnabled()).isEqualTo(UPDATED_IS_AUTO_LAYOUT_ENABLED);
    }

    @Test
    @Transactional
    void patchNonExistingImpactMatrix() throws Exception {
        int databaseSizeBeforeUpdate = impactMatrixRepository.findAll().size();
        impactMatrix.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImpactMatrixMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, impactMatrix.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(impactMatrix))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImpactMatrix in the database
        List<ImpactMatrix> impactMatrixList = impactMatrixRepository.findAll();
        assertThat(impactMatrixList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchImpactMatrix() throws Exception {
        int databaseSizeBeforeUpdate = impactMatrixRepository.findAll().size();
        impactMatrix.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImpactMatrixMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(impactMatrix))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImpactMatrix in the database
        List<ImpactMatrix> impactMatrixList = impactMatrixRepository.findAll();
        assertThat(impactMatrixList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamImpactMatrix() throws Exception {
        int databaseSizeBeforeUpdate = impactMatrixRepository.findAll().size();
        impactMatrix.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImpactMatrixMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(impactMatrix))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ImpactMatrix in the database
        List<ImpactMatrix> impactMatrixList = impactMatrixRepository.findAll();
        assertThat(impactMatrixList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteImpactMatrix() throws Exception {
        // Initialize the database
        impactMatrixRepository.saveAndFlush(impactMatrix);

        int databaseSizeBeforeDelete = impactMatrixRepository.findAll().size();

        // Delete the impactMatrix
        restImpactMatrixMockMvc
            .perform(delete(ENTITY_API_URL_ID, impactMatrix.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ImpactMatrix> impactMatrixList = impactMatrixRepository.findAll();
        assertThat(impactMatrixList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
