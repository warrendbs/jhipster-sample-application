package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ReleasePackage;
import com.mycompany.myapp.repository.ReleasePackageRepository;
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
 * Integration tests for the {@link ReleasePackageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReleasePackageResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_RELEASE_PACKAGE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_RELEASE_PACKAGE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_RELEASE_PACKAGE_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_RELEASE_PACKAGE_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_ECN = "AAAAAAAAAA";
    private static final String UPDATED_ECN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/release-packages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReleasePackageRepository releasePackageRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReleasePackageMockMvc;

    private ReleasePackage releasePackage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReleasePackage createEntity(EntityManager em) {
        ReleasePackage releasePackage = new ReleasePackage()
            .title(DEFAULT_TITLE)
            .releasePackageNumber(DEFAULT_RELEASE_PACKAGE_NUMBER)
            .releasePackageTitle(DEFAULT_RELEASE_PACKAGE_TITLE)
            .status(DEFAULT_STATUS)
            .ecn(DEFAULT_ECN);
        return releasePackage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReleasePackage createUpdatedEntity(EntityManager em) {
        ReleasePackage releasePackage = new ReleasePackage()
            .title(UPDATED_TITLE)
            .releasePackageNumber(UPDATED_RELEASE_PACKAGE_NUMBER)
            .releasePackageTitle(UPDATED_RELEASE_PACKAGE_TITLE)
            .status(UPDATED_STATUS)
            .ecn(UPDATED_ECN);
        return releasePackage;
    }

    @BeforeEach
    public void initTest() {
        releasePackage = createEntity(em);
    }

    @Test
    @Transactional
    void createReleasePackage() throws Exception {
        int databaseSizeBeforeCreate = releasePackageRepository.findAll().size();
        // Create the ReleasePackage
        restReleasePackageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(releasePackage))
            )
            .andExpect(status().isCreated());

        // Validate the ReleasePackage in the database
        List<ReleasePackage> releasePackageList = releasePackageRepository.findAll();
        assertThat(releasePackageList).hasSize(databaseSizeBeforeCreate + 1);
        ReleasePackage testReleasePackage = releasePackageList.get(releasePackageList.size() - 1);
        assertThat(testReleasePackage.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testReleasePackage.getReleasePackageNumber()).isEqualTo(DEFAULT_RELEASE_PACKAGE_NUMBER);
        assertThat(testReleasePackage.getReleasePackageTitle()).isEqualTo(DEFAULT_RELEASE_PACKAGE_TITLE);
        assertThat(testReleasePackage.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testReleasePackage.getEcn()).isEqualTo(DEFAULT_ECN);
    }

    @Test
    @Transactional
    void createReleasePackageWithExistingId() throws Exception {
        // Create the ReleasePackage with an existing ID
        releasePackage.setId(1L);

        int databaseSizeBeforeCreate = releasePackageRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReleasePackageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(releasePackage))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReleasePackage in the database
        List<ReleasePackage> releasePackageList = releasePackageRepository.findAll();
        assertThat(releasePackageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllReleasePackages() throws Exception {
        // Initialize the database
        releasePackageRepository.saveAndFlush(releasePackage);

        // Get all the releasePackageList
        restReleasePackageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(releasePackage.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].releasePackageNumber").value(hasItem(DEFAULT_RELEASE_PACKAGE_NUMBER)))
            .andExpect(jsonPath("$.[*].releasePackageTitle").value(hasItem(DEFAULT_RELEASE_PACKAGE_TITLE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].ecn").value(hasItem(DEFAULT_ECN)));
    }

    @Test
    @Transactional
    void getReleasePackage() throws Exception {
        // Initialize the database
        releasePackageRepository.saveAndFlush(releasePackage);

        // Get the releasePackage
        restReleasePackageMockMvc
            .perform(get(ENTITY_API_URL_ID, releasePackage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(releasePackage.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.releasePackageNumber").value(DEFAULT_RELEASE_PACKAGE_NUMBER))
            .andExpect(jsonPath("$.releasePackageTitle").value(DEFAULT_RELEASE_PACKAGE_TITLE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.ecn").value(DEFAULT_ECN));
    }

    @Test
    @Transactional
    void getNonExistingReleasePackage() throws Exception {
        // Get the releasePackage
        restReleasePackageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewReleasePackage() throws Exception {
        // Initialize the database
        releasePackageRepository.saveAndFlush(releasePackage);

        int databaseSizeBeforeUpdate = releasePackageRepository.findAll().size();

        // Update the releasePackage
        ReleasePackage updatedReleasePackage = releasePackageRepository.findById(releasePackage.getId()).get();
        // Disconnect from session so that the updates on updatedReleasePackage are not directly saved in db
        em.detach(updatedReleasePackage);
        updatedReleasePackage
            .title(UPDATED_TITLE)
            .releasePackageNumber(UPDATED_RELEASE_PACKAGE_NUMBER)
            .releasePackageTitle(UPDATED_RELEASE_PACKAGE_TITLE)
            .status(UPDATED_STATUS)
            .ecn(UPDATED_ECN);

        restReleasePackageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedReleasePackage.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedReleasePackage))
            )
            .andExpect(status().isOk());

        // Validate the ReleasePackage in the database
        List<ReleasePackage> releasePackageList = releasePackageRepository.findAll();
        assertThat(releasePackageList).hasSize(databaseSizeBeforeUpdate);
        ReleasePackage testReleasePackage = releasePackageList.get(releasePackageList.size() - 1);
        assertThat(testReleasePackage.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testReleasePackage.getReleasePackageNumber()).isEqualTo(UPDATED_RELEASE_PACKAGE_NUMBER);
        assertThat(testReleasePackage.getReleasePackageTitle()).isEqualTo(UPDATED_RELEASE_PACKAGE_TITLE);
        assertThat(testReleasePackage.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testReleasePackage.getEcn()).isEqualTo(UPDATED_ECN);
    }

    @Test
    @Transactional
    void putNonExistingReleasePackage() throws Exception {
        int databaseSizeBeforeUpdate = releasePackageRepository.findAll().size();
        releasePackage.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReleasePackageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, releasePackage.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(releasePackage))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReleasePackage in the database
        List<ReleasePackage> releasePackageList = releasePackageRepository.findAll();
        assertThat(releasePackageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReleasePackage() throws Exception {
        int databaseSizeBeforeUpdate = releasePackageRepository.findAll().size();
        releasePackage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReleasePackageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(releasePackage))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReleasePackage in the database
        List<ReleasePackage> releasePackageList = releasePackageRepository.findAll();
        assertThat(releasePackageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReleasePackage() throws Exception {
        int databaseSizeBeforeUpdate = releasePackageRepository.findAll().size();
        releasePackage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReleasePackageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(releasePackage)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReleasePackage in the database
        List<ReleasePackage> releasePackageList = releasePackageRepository.findAll();
        assertThat(releasePackageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReleasePackageWithPatch() throws Exception {
        // Initialize the database
        releasePackageRepository.saveAndFlush(releasePackage);

        int databaseSizeBeforeUpdate = releasePackageRepository.findAll().size();

        // Update the releasePackage using partial update
        ReleasePackage partialUpdatedReleasePackage = new ReleasePackage();
        partialUpdatedReleasePackage.setId(releasePackage.getId());

        partialUpdatedReleasePackage.title(UPDATED_TITLE).status(UPDATED_STATUS).ecn(UPDATED_ECN);

        restReleasePackageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReleasePackage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReleasePackage))
            )
            .andExpect(status().isOk());

        // Validate the ReleasePackage in the database
        List<ReleasePackage> releasePackageList = releasePackageRepository.findAll();
        assertThat(releasePackageList).hasSize(databaseSizeBeforeUpdate);
        ReleasePackage testReleasePackage = releasePackageList.get(releasePackageList.size() - 1);
        assertThat(testReleasePackage.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testReleasePackage.getReleasePackageNumber()).isEqualTo(DEFAULT_RELEASE_PACKAGE_NUMBER);
        assertThat(testReleasePackage.getReleasePackageTitle()).isEqualTo(DEFAULT_RELEASE_PACKAGE_TITLE);
        assertThat(testReleasePackage.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testReleasePackage.getEcn()).isEqualTo(UPDATED_ECN);
    }

    @Test
    @Transactional
    void fullUpdateReleasePackageWithPatch() throws Exception {
        // Initialize the database
        releasePackageRepository.saveAndFlush(releasePackage);

        int databaseSizeBeforeUpdate = releasePackageRepository.findAll().size();

        // Update the releasePackage using partial update
        ReleasePackage partialUpdatedReleasePackage = new ReleasePackage();
        partialUpdatedReleasePackage.setId(releasePackage.getId());

        partialUpdatedReleasePackage
            .title(UPDATED_TITLE)
            .releasePackageNumber(UPDATED_RELEASE_PACKAGE_NUMBER)
            .releasePackageTitle(UPDATED_RELEASE_PACKAGE_TITLE)
            .status(UPDATED_STATUS)
            .ecn(UPDATED_ECN);

        restReleasePackageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReleasePackage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReleasePackage))
            )
            .andExpect(status().isOk());

        // Validate the ReleasePackage in the database
        List<ReleasePackage> releasePackageList = releasePackageRepository.findAll();
        assertThat(releasePackageList).hasSize(databaseSizeBeforeUpdate);
        ReleasePackage testReleasePackage = releasePackageList.get(releasePackageList.size() - 1);
        assertThat(testReleasePackage.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testReleasePackage.getReleasePackageNumber()).isEqualTo(UPDATED_RELEASE_PACKAGE_NUMBER);
        assertThat(testReleasePackage.getReleasePackageTitle()).isEqualTo(UPDATED_RELEASE_PACKAGE_TITLE);
        assertThat(testReleasePackage.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testReleasePackage.getEcn()).isEqualTo(UPDATED_ECN);
    }

    @Test
    @Transactional
    void patchNonExistingReleasePackage() throws Exception {
        int databaseSizeBeforeUpdate = releasePackageRepository.findAll().size();
        releasePackage.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReleasePackageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, releasePackage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(releasePackage))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReleasePackage in the database
        List<ReleasePackage> releasePackageList = releasePackageRepository.findAll();
        assertThat(releasePackageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReleasePackage() throws Exception {
        int databaseSizeBeforeUpdate = releasePackageRepository.findAll().size();
        releasePackage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReleasePackageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(releasePackage))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReleasePackage in the database
        List<ReleasePackage> releasePackageList = releasePackageRepository.findAll();
        assertThat(releasePackageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReleasePackage() throws Exception {
        int databaseSizeBeforeUpdate = releasePackageRepository.findAll().size();
        releasePackage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReleasePackageMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(releasePackage))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReleasePackage in the database
        List<ReleasePackage> releasePackageList = releasePackageRepository.findAll();
        assertThat(releasePackageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReleasePackage() throws Exception {
        // Initialize the database
        releasePackageRepository.saveAndFlush(releasePackage);

        int databaseSizeBeforeDelete = releasePackageRepository.findAll().size();

        // Delete the releasePackage
        restReleasePackageMockMvc
            .perform(delete(ENTITY_API_URL_ID, releasePackage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ReleasePackage> releasePackageList = releasePackageRepository.findAll();
        assertThat(releasePackageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
