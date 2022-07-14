package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.PlantSpecific;
import com.mycompany.myapp.repository.PlantSpecificRepository;
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
 * Integration tests for the {@link PlantSpecificResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PlantSpecificResourceIT {

    private static final String DEFAULT_OBJECT_DEPENDANCY = "AAAAAAAAAA";
    private static final String UPDATED_OBJECT_DEPENDANCY = "BBBBBBBBBB";

    private static final String DEFAULT_REF_MATERIAL = "AAAAAAAAAA";
    private static final String UPDATED_REF_MATERIAL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DISCONTINUED = false;
    private static final Boolean UPDATED_IS_DISCONTINUED = true;

    private static final String ENTITY_API_URL = "/api/plant-specifics";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PlantSpecificRepository plantSpecificRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlantSpecificMockMvc;

    private PlantSpecific plantSpecific;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlantSpecific createEntity(EntityManager em) {
        PlantSpecific plantSpecific = new PlantSpecific()
            .objectDependancy(DEFAULT_OBJECT_DEPENDANCY)
            .refMaterial(DEFAULT_REF_MATERIAL)
            .isDiscontinued(DEFAULT_IS_DISCONTINUED);
        return plantSpecific;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlantSpecific createUpdatedEntity(EntityManager em) {
        PlantSpecific plantSpecific = new PlantSpecific()
            .objectDependancy(UPDATED_OBJECT_DEPENDANCY)
            .refMaterial(UPDATED_REF_MATERIAL)
            .isDiscontinued(UPDATED_IS_DISCONTINUED);
        return plantSpecific;
    }

    @BeforeEach
    public void initTest() {
        plantSpecific = createEntity(em);
    }

    @Test
    @Transactional
    void createPlantSpecific() throws Exception {
        int databaseSizeBeforeCreate = plantSpecificRepository.findAll().size();
        // Create the PlantSpecific
        restPlantSpecificMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(plantSpecific)))
            .andExpect(status().isCreated());

        // Validate the PlantSpecific in the database
        List<PlantSpecific> plantSpecificList = plantSpecificRepository.findAll();
        assertThat(plantSpecificList).hasSize(databaseSizeBeforeCreate + 1);
        PlantSpecific testPlantSpecific = plantSpecificList.get(plantSpecificList.size() - 1);
        assertThat(testPlantSpecific.getObjectDependancy()).isEqualTo(DEFAULT_OBJECT_DEPENDANCY);
        assertThat(testPlantSpecific.getRefMaterial()).isEqualTo(DEFAULT_REF_MATERIAL);
        assertThat(testPlantSpecific.getIsDiscontinued()).isEqualTo(DEFAULT_IS_DISCONTINUED);
    }

    @Test
    @Transactional
    void createPlantSpecificWithExistingId() throws Exception {
        // Create the PlantSpecific with an existing ID
        plantSpecific.setId(1L);

        int databaseSizeBeforeCreate = plantSpecificRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlantSpecificMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(plantSpecific)))
            .andExpect(status().isBadRequest());

        // Validate the PlantSpecific in the database
        List<PlantSpecific> plantSpecificList = plantSpecificRepository.findAll();
        assertThat(plantSpecificList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPlantSpecifics() throws Exception {
        // Initialize the database
        plantSpecificRepository.saveAndFlush(plantSpecific);

        // Get all the plantSpecificList
        restPlantSpecificMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(plantSpecific.getId().intValue())))
            .andExpect(jsonPath("$.[*].objectDependancy").value(hasItem(DEFAULT_OBJECT_DEPENDANCY)))
            .andExpect(jsonPath("$.[*].refMaterial").value(hasItem(DEFAULT_REF_MATERIAL)))
            .andExpect(jsonPath("$.[*].isDiscontinued").value(hasItem(DEFAULT_IS_DISCONTINUED.booleanValue())));
    }

    @Test
    @Transactional
    void getPlantSpecific() throws Exception {
        // Initialize the database
        plantSpecificRepository.saveAndFlush(plantSpecific);

        // Get the plantSpecific
        restPlantSpecificMockMvc
            .perform(get(ENTITY_API_URL_ID, plantSpecific.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(plantSpecific.getId().intValue()))
            .andExpect(jsonPath("$.objectDependancy").value(DEFAULT_OBJECT_DEPENDANCY))
            .andExpect(jsonPath("$.refMaterial").value(DEFAULT_REF_MATERIAL))
            .andExpect(jsonPath("$.isDiscontinued").value(DEFAULT_IS_DISCONTINUED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingPlantSpecific() throws Exception {
        // Get the plantSpecific
        restPlantSpecificMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPlantSpecific() throws Exception {
        // Initialize the database
        plantSpecificRepository.saveAndFlush(plantSpecific);

        int databaseSizeBeforeUpdate = plantSpecificRepository.findAll().size();

        // Update the plantSpecific
        PlantSpecific updatedPlantSpecific = plantSpecificRepository.findById(plantSpecific.getId()).get();
        // Disconnect from session so that the updates on updatedPlantSpecific are not directly saved in db
        em.detach(updatedPlantSpecific);
        updatedPlantSpecific
            .objectDependancy(UPDATED_OBJECT_DEPENDANCY)
            .refMaterial(UPDATED_REF_MATERIAL)
            .isDiscontinued(UPDATED_IS_DISCONTINUED);

        restPlantSpecificMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPlantSpecific.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPlantSpecific))
            )
            .andExpect(status().isOk());

        // Validate the PlantSpecific in the database
        List<PlantSpecific> plantSpecificList = plantSpecificRepository.findAll();
        assertThat(plantSpecificList).hasSize(databaseSizeBeforeUpdate);
        PlantSpecific testPlantSpecific = plantSpecificList.get(plantSpecificList.size() - 1);
        assertThat(testPlantSpecific.getObjectDependancy()).isEqualTo(UPDATED_OBJECT_DEPENDANCY);
        assertThat(testPlantSpecific.getRefMaterial()).isEqualTo(UPDATED_REF_MATERIAL);
        assertThat(testPlantSpecific.getIsDiscontinued()).isEqualTo(UPDATED_IS_DISCONTINUED);
    }

    @Test
    @Transactional
    void putNonExistingPlantSpecific() throws Exception {
        int databaseSizeBeforeUpdate = plantSpecificRepository.findAll().size();
        plantSpecific.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlantSpecificMockMvc
            .perform(
                put(ENTITY_API_URL_ID, plantSpecific.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(plantSpecific))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlantSpecific in the database
        List<PlantSpecific> plantSpecificList = plantSpecificRepository.findAll();
        assertThat(plantSpecificList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPlantSpecific() throws Exception {
        int databaseSizeBeforeUpdate = plantSpecificRepository.findAll().size();
        plantSpecific.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlantSpecificMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(plantSpecific))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlantSpecific in the database
        List<PlantSpecific> plantSpecificList = plantSpecificRepository.findAll();
        assertThat(plantSpecificList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPlantSpecific() throws Exception {
        int databaseSizeBeforeUpdate = plantSpecificRepository.findAll().size();
        plantSpecific.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlantSpecificMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(plantSpecific)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlantSpecific in the database
        List<PlantSpecific> plantSpecificList = plantSpecificRepository.findAll();
        assertThat(plantSpecificList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePlantSpecificWithPatch() throws Exception {
        // Initialize the database
        plantSpecificRepository.saveAndFlush(plantSpecific);

        int databaseSizeBeforeUpdate = plantSpecificRepository.findAll().size();

        // Update the plantSpecific using partial update
        PlantSpecific partialUpdatedPlantSpecific = new PlantSpecific();
        partialUpdatedPlantSpecific.setId(plantSpecific.getId());

        partialUpdatedPlantSpecific.objectDependancy(UPDATED_OBJECT_DEPENDANCY).refMaterial(UPDATED_REF_MATERIAL);

        restPlantSpecificMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlantSpecific.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlantSpecific))
            )
            .andExpect(status().isOk());

        // Validate the PlantSpecific in the database
        List<PlantSpecific> plantSpecificList = plantSpecificRepository.findAll();
        assertThat(plantSpecificList).hasSize(databaseSizeBeforeUpdate);
        PlantSpecific testPlantSpecific = plantSpecificList.get(plantSpecificList.size() - 1);
        assertThat(testPlantSpecific.getObjectDependancy()).isEqualTo(UPDATED_OBJECT_DEPENDANCY);
        assertThat(testPlantSpecific.getRefMaterial()).isEqualTo(UPDATED_REF_MATERIAL);
        assertThat(testPlantSpecific.getIsDiscontinued()).isEqualTo(DEFAULT_IS_DISCONTINUED);
    }

    @Test
    @Transactional
    void fullUpdatePlantSpecificWithPatch() throws Exception {
        // Initialize the database
        plantSpecificRepository.saveAndFlush(plantSpecific);

        int databaseSizeBeforeUpdate = plantSpecificRepository.findAll().size();

        // Update the plantSpecific using partial update
        PlantSpecific partialUpdatedPlantSpecific = new PlantSpecific();
        partialUpdatedPlantSpecific.setId(plantSpecific.getId());

        partialUpdatedPlantSpecific
            .objectDependancy(UPDATED_OBJECT_DEPENDANCY)
            .refMaterial(UPDATED_REF_MATERIAL)
            .isDiscontinued(UPDATED_IS_DISCONTINUED);

        restPlantSpecificMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlantSpecific.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlantSpecific))
            )
            .andExpect(status().isOk());

        // Validate the PlantSpecific in the database
        List<PlantSpecific> plantSpecificList = plantSpecificRepository.findAll();
        assertThat(plantSpecificList).hasSize(databaseSizeBeforeUpdate);
        PlantSpecific testPlantSpecific = plantSpecificList.get(plantSpecificList.size() - 1);
        assertThat(testPlantSpecific.getObjectDependancy()).isEqualTo(UPDATED_OBJECT_DEPENDANCY);
        assertThat(testPlantSpecific.getRefMaterial()).isEqualTo(UPDATED_REF_MATERIAL);
        assertThat(testPlantSpecific.getIsDiscontinued()).isEqualTo(UPDATED_IS_DISCONTINUED);
    }

    @Test
    @Transactional
    void patchNonExistingPlantSpecific() throws Exception {
        int databaseSizeBeforeUpdate = plantSpecificRepository.findAll().size();
        plantSpecific.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlantSpecificMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, plantSpecific.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(plantSpecific))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlantSpecific in the database
        List<PlantSpecific> plantSpecificList = plantSpecificRepository.findAll();
        assertThat(plantSpecificList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPlantSpecific() throws Exception {
        int databaseSizeBeforeUpdate = plantSpecificRepository.findAll().size();
        plantSpecific.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlantSpecificMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(plantSpecific))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlantSpecific in the database
        List<PlantSpecific> plantSpecificList = plantSpecificRepository.findAll();
        assertThat(plantSpecificList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPlantSpecific() throws Exception {
        int databaseSizeBeforeUpdate = plantSpecificRepository.findAll().size();
        plantSpecific.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlantSpecificMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(plantSpecific))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlantSpecific in the database
        List<PlantSpecific> plantSpecificList = plantSpecificRepository.findAll();
        assertThat(plantSpecificList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePlantSpecific() throws Exception {
        // Initialize the database
        plantSpecificRepository.saveAndFlush(plantSpecific);

        int databaseSizeBeforeDelete = plantSpecificRepository.findAll().size();

        // Delete the plantSpecific
        restPlantSpecificMockMvc
            .perform(delete(ENTITY_API_URL_ID, plantSpecific.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PlantSpecific> plantSpecificList = plantSpecificRepository.findAll();
        assertThat(plantSpecificList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
