package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.BomIntention;
import com.mycompany.myapp.repository.BomIntentionRepository;
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
 * Integration tests for the {@link BomIntentionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BomIntentionResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/bom-intentions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BomIntentionRepository bomIntentionRepository;

    @Mock
    private BomIntentionRepository bomIntentionRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBomIntentionMockMvc;

    private BomIntention bomIntention;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BomIntention createEntity(EntityManager em) {
        BomIntention bomIntention = new BomIntention().type(DEFAULT_TYPE);
        return bomIntention;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BomIntention createUpdatedEntity(EntityManager em) {
        BomIntention bomIntention = new BomIntention().type(UPDATED_TYPE);
        return bomIntention;
    }

    @BeforeEach
    public void initTest() {
        bomIntention = createEntity(em);
    }

    @Test
    @Transactional
    void createBomIntention() throws Exception {
        int databaseSizeBeforeCreate = bomIntentionRepository.findAll().size();
        // Create the BomIntention
        restBomIntentionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bomIntention)))
            .andExpect(status().isCreated());

        // Validate the BomIntention in the database
        List<BomIntention> bomIntentionList = bomIntentionRepository.findAll();
        assertThat(bomIntentionList).hasSize(databaseSizeBeforeCreate + 1);
        BomIntention testBomIntention = bomIntentionList.get(bomIntentionList.size() - 1);
        assertThat(testBomIntention.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void createBomIntentionWithExistingId() throws Exception {
        // Create the BomIntention with an existing ID
        bomIntention.setId(1L);

        int databaseSizeBeforeCreate = bomIntentionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBomIntentionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bomIntention)))
            .andExpect(status().isBadRequest());

        // Validate the BomIntention in the database
        List<BomIntention> bomIntentionList = bomIntentionRepository.findAll();
        assertThat(bomIntentionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBomIntentions() throws Exception {
        // Initialize the database
        bomIntentionRepository.saveAndFlush(bomIntention);

        // Get all the bomIntentionList
        restBomIntentionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bomIntention.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBomIntentionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(bomIntentionRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBomIntentionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(bomIntentionRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBomIntentionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(bomIntentionRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBomIntentionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(bomIntentionRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getBomIntention() throws Exception {
        // Initialize the database
        bomIntentionRepository.saveAndFlush(bomIntention);

        // Get the bomIntention
        restBomIntentionMockMvc
            .perform(get(ENTITY_API_URL_ID, bomIntention.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bomIntention.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE));
    }

    @Test
    @Transactional
    void getNonExistingBomIntention() throws Exception {
        // Get the bomIntention
        restBomIntentionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBomIntention() throws Exception {
        // Initialize the database
        bomIntentionRepository.saveAndFlush(bomIntention);

        int databaseSizeBeforeUpdate = bomIntentionRepository.findAll().size();

        // Update the bomIntention
        BomIntention updatedBomIntention = bomIntentionRepository.findById(bomIntention.getId()).get();
        // Disconnect from session so that the updates on updatedBomIntention are not directly saved in db
        em.detach(updatedBomIntention);
        updatedBomIntention.type(UPDATED_TYPE);

        restBomIntentionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBomIntention.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBomIntention))
            )
            .andExpect(status().isOk());

        // Validate the BomIntention in the database
        List<BomIntention> bomIntentionList = bomIntentionRepository.findAll();
        assertThat(bomIntentionList).hasSize(databaseSizeBeforeUpdate);
        BomIntention testBomIntention = bomIntentionList.get(bomIntentionList.size() - 1);
        assertThat(testBomIntention.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingBomIntention() throws Exception {
        int databaseSizeBeforeUpdate = bomIntentionRepository.findAll().size();
        bomIntention.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBomIntentionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bomIntention.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bomIntention))
            )
            .andExpect(status().isBadRequest());

        // Validate the BomIntention in the database
        List<BomIntention> bomIntentionList = bomIntentionRepository.findAll();
        assertThat(bomIntentionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBomIntention() throws Exception {
        int databaseSizeBeforeUpdate = bomIntentionRepository.findAll().size();
        bomIntention.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBomIntentionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bomIntention))
            )
            .andExpect(status().isBadRequest());

        // Validate the BomIntention in the database
        List<BomIntention> bomIntentionList = bomIntentionRepository.findAll();
        assertThat(bomIntentionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBomIntention() throws Exception {
        int databaseSizeBeforeUpdate = bomIntentionRepository.findAll().size();
        bomIntention.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBomIntentionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bomIntention)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BomIntention in the database
        List<BomIntention> bomIntentionList = bomIntentionRepository.findAll();
        assertThat(bomIntentionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBomIntentionWithPatch() throws Exception {
        // Initialize the database
        bomIntentionRepository.saveAndFlush(bomIntention);

        int databaseSizeBeforeUpdate = bomIntentionRepository.findAll().size();

        // Update the bomIntention using partial update
        BomIntention partialUpdatedBomIntention = new BomIntention();
        partialUpdatedBomIntention.setId(bomIntention.getId());

        partialUpdatedBomIntention.type(UPDATED_TYPE);

        restBomIntentionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBomIntention.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBomIntention))
            )
            .andExpect(status().isOk());

        // Validate the BomIntention in the database
        List<BomIntention> bomIntentionList = bomIntentionRepository.findAll();
        assertThat(bomIntentionList).hasSize(databaseSizeBeforeUpdate);
        BomIntention testBomIntention = bomIntentionList.get(bomIntentionList.size() - 1);
        assertThat(testBomIntention.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateBomIntentionWithPatch() throws Exception {
        // Initialize the database
        bomIntentionRepository.saveAndFlush(bomIntention);

        int databaseSizeBeforeUpdate = bomIntentionRepository.findAll().size();

        // Update the bomIntention using partial update
        BomIntention partialUpdatedBomIntention = new BomIntention();
        partialUpdatedBomIntention.setId(bomIntention.getId());

        partialUpdatedBomIntention.type(UPDATED_TYPE);

        restBomIntentionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBomIntention.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBomIntention))
            )
            .andExpect(status().isOk());

        // Validate the BomIntention in the database
        List<BomIntention> bomIntentionList = bomIntentionRepository.findAll();
        assertThat(bomIntentionList).hasSize(databaseSizeBeforeUpdate);
        BomIntention testBomIntention = bomIntentionList.get(bomIntentionList.size() - 1);
        assertThat(testBomIntention.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingBomIntention() throws Exception {
        int databaseSizeBeforeUpdate = bomIntentionRepository.findAll().size();
        bomIntention.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBomIntentionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bomIntention.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bomIntention))
            )
            .andExpect(status().isBadRequest());

        // Validate the BomIntention in the database
        List<BomIntention> bomIntentionList = bomIntentionRepository.findAll();
        assertThat(bomIntentionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBomIntention() throws Exception {
        int databaseSizeBeforeUpdate = bomIntentionRepository.findAll().size();
        bomIntention.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBomIntentionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bomIntention))
            )
            .andExpect(status().isBadRequest());

        // Validate the BomIntention in the database
        List<BomIntention> bomIntentionList = bomIntentionRepository.findAll();
        assertThat(bomIntentionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBomIntention() throws Exception {
        int databaseSizeBeforeUpdate = bomIntentionRepository.findAll().size();
        bomIntention.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBomIntentionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(bomIntention))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BomIntention in the database
        List<BomIntention> bomIntentionList = bomIntentionRepository.findAll();
        assertThat(bomIntentionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBomIntention() throws Exception {
        // Initialize the database
        bomIntentionRepository.saveAndFlush(bomIntention);

        int databaseSizeBeforeDelete = bomIntentionRepository.findAll().size();

        // Delete the bomIntention
        restBomIntentionMockMvc
            .perform(delete(ENTITY_API_URL_ID, bomIntention.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BomIntention> bomIntentionList = bomIntentionRepository.findAll();
        assertThat(bomIntentionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
