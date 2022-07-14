package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.BomSource;
import com.mycompany.myapp.repository.BomSourceRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link BomSourceResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BomSourceResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/bom-sources";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BomSourceRepository bomSourceRepository;

    @Mock
    private BomSourceRepository bomSourceRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBomSourceMockMvc;

    private BomSource bomSource;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BomSource createEntity(EntityManager em) {
        BomSource bomSource = new BomSource().type(DEFAULT_TYPE);
        return bomSource;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BomSource createUpdatedEntity(EntityManager em) {
        BomSource bomSource = new BomSource().type(UPDATED_TYPE);
        return bomSource;
    }

    @BeforeEach
    public void initTest() {
        bomSource = createEntity(em);
    }

    @Test
    @Transactional
    void createBomSource() throws Exception {
        int databaseSizeBeforeCreate = bomSourceRepository.findAll().size();
        // Create the BomSource
        restBomSourceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bomSource)))
            .andExpect(status().isCreated());

        // Validate the BomSource in the database
        List<BomSource> bomSourceList = bomSourceRepository.findAll();
        assertThat(bomSourceList).hasSize(databaseSizeBeforeCreate + 1);
        BomSource testBomSource = bomSourceList.get(bomSourceList.size() - 1);
        assertThat(testBomSource.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void createBomSourceWithExistingId() throws Exception {
        // Create the BomSource with an existing ID
        bomSource.setId(1L);

        int databaseSizeBeforeCreate = bomSourceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBomSourceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bomSource)))
            .andExpect(status().isBadRequest());

        // Validate the BomSource in the database
        List<BomSource> bomSourceList = bomSourceRepository.findAll();
        assertThat(bomSourceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBomSources() throws Exception {
        // Initialize the database
        bomSourceRepository.saveAndFlush(bomSource);

        // Get all the bomSourceList
        restBomSourceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bomSource.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBomSourcesWithEagerRelationshipsIsEnabled() throws Exception {
        when(bomSourceRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBomSourceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(bomSourceRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBomSourcesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(bomSourceRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBomSourceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(bomSourceRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getBomSource() throws Exception {
        // Initialize the database
        bomSourceRepository.saveAndFlush(bomSource);

        // Get the bomSource
        restBomSourceMockMvc
            .perform(get(ENTITY_API_URL_ID, bomSource.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bomSource.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE));
    }

    @Test
    @Transactional
    void getNonExistingBomSource() throws Exception {
        // Get the bomSource
        restBomSourceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBomSource() throws Exception {
        // Initialize the database
        bomSourceRepository.saveAndFlush(bomSource);

        int databaseSizeBeforeUpdate = bomSourceRepository.findAll().size();

        // Update the bomSource
        BomSource updatedBomSource = bomSourceRepository.findById(bomSource.getId()).get();
        // Disconnect from session so that the updates on updatedBomSource are not directly saved in db
        em.detach(updatedBomSource);
        updatedBomSource.type(UPDATED_TYPE);

        restBomSourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBomSource.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBomSource))
            )
            .andExpect(status().isOk());

        // Validate the BomSource in the database
        List<BomSource> bomSourceList = bomSourceRepository.findAll();
        assertThat(bomSourceList).hasSize(databaseSizeBeforeUpdate);
        BomSource testBomSource = bomSourceList.get(bomSourceList.size() - 1);
        assertThat(testBomSource.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingBomSource() throws Exception {
        int databaseSizeBeforeUpdate = bomSourceRepository.findAll().size();
        bomSource.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBomSourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bomSource.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bomSource))
            )
            .andExpect(status().isBadRequest());

        // Validate the BomSource in the database
        List<BomSource> bomSourceList = bomSourceRepository.findAll();
        assertThat(bomSourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBomSource() throws Exception {
        int databaseSizeBeforeUpdate = bomSourceRepository.findAll().size();
        bomSource.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBomSourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bomSource))
            )
            .andExpect(status().isBadRequest());

        // Validate the BomSource in the database
        List<BomSource> bomSourceList = bomSourceRepository.findAll();
        assertThat(bomSourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBomSource() throws Exception {
        int databaseSizeBeforeUpdate = bomSourceRepository.findAll().size();
        bomSource.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBomSourceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bomSource)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BomSource in the database
        List<BomSource> bomSourceList = bomSourceRepository.findAll();
        assertThat(bomSourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBomSourceWithPatch() throws Exception {
        // Initialize the database
        bomSourceRepository.saveAndFlush(bomSource);

        int databaseSizeBeforeUpdate = bomSourceRepository.findAll().size();

        // Update the bomSource using partial update
        BomSource partialUpdatedBomSource = new BomSource();
        partialUpdatedBomSource.setId(bomSource.getId());

        partialUpdatedBomSource.type(UPDATED_TYPE);

        restBomSourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBomSource.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBomSource))
            )
            .andExpect(status().isOk());

        // Validate the BomSource in the database
        List<BomSource> bomSourceList = bomSourceRepository.findAll();
        assertThat(bomSourceList).hasSize(databaseSizeBeforeUpdate);
        BomSource testBomSource = bomSourceList.get(bomSourceList.size() - 1);
        assertThat(testBomSource.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateBomSourceWithPatch() throws Exception {
        // Initialize the database
        bomSourceRepository.saveAndFlush(bomSource);

        int databaseSizeBeforeUpdate = bomSourceRepository.findAll().size();

        // Update the bomSource using partial update
        BomSource partialUpdatedBomSource = new BomSource();
        partialUpdatedBomSource.setId(bomSource.getId());

        partialUpdatedBomSource.type(UPDATED_TYPE);

        restBomSourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBomSource.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBomSource))
            )
            .andExpect(status().isOk());

        // Validate the BomSource in the database
        List<BomSource> bomSourceList = bomSourceRepository.findAll();
        assertThat(bomSourceList).hasSize(databaseSizeBeforeUpdate);
        BomSource testBomSource = bomSourceList.get(bomSourceList.size() - 1);
        assertThat(testBomSource.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingBomSource() throws Exception {
        int databaseSizeBeforeUpdate = bomSourceRepository.findAll().size();
        bomSource.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBomSourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bomSource.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bomSource))
            )
            .andExpect(status().isBadRequest());

        // Validate the BomSource in the database
        List<BomSource> bomSourceList = bomSourceRepository.findAll();
        assertThat(bomSourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBomSource() throws Exception {
        int databaseSizeBeforeUpdate = bomSourceRepository.findAll().size();
        bomSource.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBomSourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bomSource))
            )
            .andExpect(status().isBadRequest());

        // Validate the BomSource in the database
        List<BomSource> bomSourceList = bomSourceRepository.findAll();
        assertThat(bomSourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBomSource() throws Exception {
        int databaseSizeBeforeUpdate = bomSourceRepository.findAll().size();
        bomSource.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBomSourceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(bomSource))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BomSource in the database
        List<BomSource> bomSourceList = bomSourceRepository.findAll();
        assertThat(bomSourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBomSource() throws Exception {
        // Initialize the database
        bomSourceRepository.saveAndFlush(bomSource);

        int databaseSizeBeforeDelete = bomSourceRepository.findAll().size();

        // Delete the bomSource
        restBomSourceMockMvc
            .perform(delete(ENTITY_API_URL_ID, bomSource.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BomSource> bomSourceList = bomSourceRepository.findAll();
        assertThat(bomSourceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
