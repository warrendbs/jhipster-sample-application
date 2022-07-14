package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Bom;
import com.mycompany.myapp.repository.BomRepository;
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
 * Integration tests for the {@link BomResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BomResourceIT {

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    private static final String ENTITY_API_URL = "/api/boms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BomRepository bomRepository;

    @Mock
    private BomRepository bomRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBomMockMvc;

    private Bom bom;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bom createEntity(EntityManager em) {
        Bom bom = new Bom().status(DEFAULT_STATUS);
        return bom;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bom createUpdatedEntity(EntityManager em) {
        Bom bom = new Bom().status(UPDATED_STATUS);
        return bom;
    }

    @BeforeEach
    public void initTest() {
        bom = createEntity(em);
    }

    @Test
    @Transactional
    void createBom() throws Exception {
        int databaseSizeBeforeCreate = bomRepository.findAll().size();
        // Create the Bom
        restBomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bom)))
            .andExpect(status().isCreated());

        // Validate the Bom in the database
        List<Bom> bomList = bomRepository.findAll();
        assertThat(bomList).hasSize(databaseSizeBeforeCreate + 1);
        Bom testBom = bomList.get(bomList.size() - 1);
        assertThat(testBom.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createBomWithExistingId() throws Exception {
        // Create the Bom with an existing ID
        bom.setId(1L);

        int databaseSizeBeforeCreate = bomRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bom)))
            .andExpect(status().isBadRequest());

        // Validate the Bom in the database
        List<Bom> bomList = bomRepository.findAll();
        assertThat(bomList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBoms() throws Exception {
        // Initialize the database
        bomRepository.saveAndFlush(bom);

        // Get all the bomList
        restBomMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bom.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBomsWithEagerRelationshipsIsEnabled() throws Exception {
        when(bomRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBomMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(bomRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBomsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(bomRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBomMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(bomRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getBom() throws Exception {
        // Initialize the database
        bomRepository.saveAndFlush(bom);

        // Get the bom
        restBomMockMvc
            .perform(get(ENTITY_API_URL_ID, bom.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bom.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    void getNonExistingBom() throws Exception {
        // Get the bom
        restBomMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBom() throws Exception {
        // Initialize the database
        bomRepository.saveAndFlush(bom);

        int databaseSizeBeforeUpdate = bomRepository.findAll().size();

        // Update the bom
        Bom updatedBom = bomRepository.findById(bom.getId()).get();
        // Disconnect from session so that the updates on updatedBom are not directly saved in db
        em.detach(updatedBom);
        updatedBom.status(UPDATED_STATUS);

        restBomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBom.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBom))
            )
            .andExpect(status().isOk());

        // Validate the Bom in the database
        List<Bom> bomList = bomRepository.findAll();
        assertThat(bomList).hasSize(databaseSizeBeforeUpdate);
        Bom testBom = bomList.get(bomList.size() - 1);
        assertThat(testBom.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingBom() throws Exception {
        int databaseSizeBeforeUpdate = bomRepository.findAll().size();
        bom.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bom.getId()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bom))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bom in the database
        List<Bom> bomList = bomRepository.findAll();
        assertThat(bomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBom() throws Exception {
        int databaseSizeBeforeUpdate = bomRepository.findAll().size();
        bom.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bom))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bom in the database
        List<Bom> bomList = bomRepository.findAll();
        assertThat(bomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBom() throws Exception {
        int databaseSizeBeforeUpdate = bomRepository.findAll().size();
        bom.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBomMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bom)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bom in the database
        List<Bom> bomList = bomRepository.findAll();
        assertThat(bomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBomWithPatch() throws Exception {
        // Initialize the database
        bomRepository.saveAndFlush(bom);

        int databaseSizeBeforeUpdate = bomRepository.findAll().size();

        // Update the bom using partial update
        Bom partialUpdatedBom = new Bom();
        partialUpdatedBom.setId(bom.getId());

        restBomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBom.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBom))
            )
            .andExpect(status().isOk());

        // Validate the Bom in the database
        List<Bom> bomList = bomRepository.findAll();
        assertThat(bomList).hasSize(databaseSizeBeforeUpdate);
        Bom testBom = bomList.get(bomList.size() - 1);
        assertThat(testBom.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateBomWithPatch() throws Exception {
        // Initialize the database
        bomRepository.saveAndFlush(bom);

        int databaseSizeBeforeUpdate = bomRepository.findAll().size();

        // Update the bom using partial update
        Bom partialUpdatedBom = new Bom();
        partialUpdatedBom.setId(bom.getId());

        partialUpdatedBom.status(UPDATED_STATUS);

        restBomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBom.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBom))
            )
            .andExpect(status().isOk());

        // Validate the Bom in the database
        List<Bom> bomList = bomRepository.findAll();
        assertThat(bomList).hasSize(databaseSizeBeforeUpdate);
        Bom testBom = bomList.get(bomList.size() - 1);
        assertThat(testBom.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingBom() throws Exception {
        int databaseSizeBeforeUpdate = bomRepository.findAll().size();
        bom.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bom.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bom))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bom in the database
        List<Bom> bomList = bomRepository.findAll();
        assertThat(bomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBom() throws Exception {
        int databaseSizeBeforeUpdate = bomRepository.findAll().size();
        bom.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bom))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bom in the database
        List<Bom> bomList = bomRepository.findAll();
        assertThat(bomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBom() throws Exception {
        int databaseSizeBeforeUpdate = bomRepository.findAll().size();
        bom.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBomMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(bom)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bom in the database
        List<Bom> bomList = bomRepository.findAll();
        assertThat(bomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBom() throws Exception {
        // Initialize the database
        bomRepository.saveAndFlush(bom);

        int databaseSizeBeforeDelete = bomRepository.findAll().size();

        // Delete the bom
        restBomMockMvc.perform(delete(ENTITY_API_URL_ID, bom.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Bom> bomList = bomRepository.findAll();
        assertThat(bomList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
