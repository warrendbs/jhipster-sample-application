package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.DocumentSource;
import com.mycompany.myapp.repository.DocumentSourceRepository;
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
 * Integration tests for the {@link DocumentSourceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocumentSourceResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_CHANGE_INDICATOR = false;
    private static final Boolean UPDATED_CHANGE_INDICATOR = true;

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_SUBTYPE = "AAAAAAAAAA";
    private static final String UPDATED_SUBTYPE = "BBBBBBBBBB";

    private static final String DEFAULT_GROUP = "AAAAAAAAAA";
    private static final String UPDATED_GROUP = "BBBBBBBBBB";

    private static final String DEFAULT_SHEET = "AAAAAAAAAA";
    private static final String UPDATED_SHEET = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/document-sources";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DocumentSourceRepository documentSourceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentSourceMockMvc;

    private DocumentSource documentSource;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentSource createEntity(EntityManager em) {
        DocumentSource documentSource = new DocumentSource()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .changeIndicator(DEFAULT_CHANGE_INDICATOR)
            .type(DEFAULT_TYPE)
            .subtype(DEFAULT_SUBTYPE)
            .group(DEFAULT_GROUP)
            .sheet(DEFAULT_SHEET);
        return documentSource;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentSource createUpdatedEntity(EntityManager em) {
        DocumentSource documentSource = new DocumentSource()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .changeIndicator(UPDATED_CHANGE_INDICATOR)
            .type(UPDATED_TYPE)
            .subtype(UPDATED_SUBTYPE)
            .group(UPDATED_GROUP)
            .sheet(UPDATED_SHEET);
        return documentSource;
    }

    @BeforeEach
    public void initTest() {
        documentSource = createEntity(em);
    }

    @Test
    @Transactional
    void createDocumentSource() throws Exception {
        int databaseSizeBeforeCreate = documentSourceRepository.findAll().size();
        // Create the DocumentSource
        restDocumentSourceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentSource))
            )
            .andExpect(status().isCreated());

        // Validate the DocumentSource in the database
        List<DocumentSource> documentSourceList = documentSourceRepository.findAll();
        assertThat(documentSourceList).hasSize(databaseSizeBeforeCreate + 1);
        DocumentSource testDocumentSource = documentSourceList.get(documentSourceList.size() - 1);
        assertThat(testDocumentSource.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDocumentSource.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDocumentSource.getChangeIndicator()).isEqualTo(DEFAULT_CHANGE_INDICATOR);
        assertThat(testDocumentSource.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testDocumentSource.getSubtype()).isEqualTo(DEFAULT_SUBTYPE);
        assertThat(testDocumentSource.getGroup()).isEqualTo(DEFAULT_GROUP);
        assertThat(testDocumentSource.getSheet()).isEqualTo(DEFAULT_SHEET);
    }

    @Test
    @Transactional
    void createDocumentSourceWithExistingId() throws Exception {
        // Create the DocumentSource with an existing ID
        documentSource.setId(1L);

        int databaseSizeBeforeCreate = documentSourceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentSourceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentSource))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentSource in the database
        List<DocumentSource> documentSourceList = documentSourceRepository.findAll();
        assertThat(documentSourceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDocumentSources() throws Exception {
        // Initialize the database
        documentSourceRepository.saveAndFlush(documentSource);

        // Get all the documentSourceList
        restDocumentSourceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentSource.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].changeIndicator").value(hasItem(DEFAULT_CHANGE_INDICATOR.booleanValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].subtype").value(hasItem(DEFAULT_SUBTYPE)))
            .andExpect(jsonPath("$.[*].group").value(hasItem(DEFAULT_GROUP)))
            .andExpect(jsonPath("$.[*].sheet").value(hasItem(DEFAULT_SHEET)));
    }

    @Test
    @Transactional
    void getDocumentSource() throws Exception {
        // Initialize the database
        documentSourceRepository.saveAndFlush(documentSource);

        // Get the documentSource
        restDocumentSourceMockMvc
            .perform(get(ENTITY_API_URL_ID, documentSource.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(documentSource.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.changeIndicator").value(DEFAULT_CHANGE_INDICATOR.booleanValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.subtype").value(DEFAULT_SUBTYPE))
            .andExpect(jsonPath("$.group").value(DEFAULT_GROUP))
            .andExpect(jsonPath("$.sheet").value(DEFAULT_SHEET));
    }

    @Test
    @Transactional
    void getNonExistingDocumentSource() throws Exception {
        // Get the documentSource
        restDocumentSourceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDocumentSource() throws Exception {
        // Initialize the database
        documentSourceRepository.saveAndFlush(documentSource);

        int databaseSizeBeforeUpdate = documentSourceRepository.findAll().size();

        // Update the documentSource
        DocumentSource updatedDocumentSource = documentSourceRepository.findById(documentSource.getId()).get();
        // Disconnect from session so that the updates on updatedDocumentSource are not directly saved in db
        em.detach(updatedDocumentSource);
        updatedDocumentSource
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .changeIndicator(UPDATED_CHANGE_INDICATOR)
            .type(UPDATED_TYPE)
            .subtype(UPDATED_SUBTYPE)
            .group(UPDATED_GROUP)
            .sheet(UPDATED_SHEET);

        restDocumentSourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDocumentSource.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDocumentSource))
            )
            .andExpect(status().isOk());

        // Validate the DocumentSource in the database
        List<DocumentSource> documentSourceList = documentSourceRepository.findAll();
        assertThat(documentSourceList).hasSize(databaseSizeBeforeUpdate);
        DocumentSource testDocumentSource = documentSourceList.get(documentSourceList.size() - 1);
        assertThat(testDocumentSource.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDocumentSource.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDocumentSource.getChangeIndicator()).isEqualTo(UPDATED_CHANGE_INDICATOR);
        assertThat(testDocumentSource.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testDocumentSource.getSubtype()).isEqualTo(UPDATED_SUBTYPE);
        assertThat(testDocumentSource.getGroup()).isEqualTo(UPDATED_GROUP);
        assertThat(testDocumentSource.getSheet()).isEqualTo(UPDATED_SHEET);
    }

    @Test
    @Transactional
    void putNonExistingDocumentSource() throws Exception {
        int databaseSizeBeforeUpdate = documentSourceRepository.findAll().size();
        documentSource.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentSourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentSource.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentSource))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentSource in the database
        List<DocumentSource> documentSourceList = documentSourceRepository.findAll();
        assertThat(documentSourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocumentSource() throws Exception {
        int databaseSizeBeforeUpdate = documentSourceRepository.findAll().size();
        documentSource.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentSourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentSource))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentSource in the database
        List<DocumentSource> documentSourceList = documentSourceRepository.findAll();
        assertThat(documentSourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocumentSource() throws Exception {
        int databaseSizeBeforeUpdate = documentSourceRepository.findAll().size();
        documentSource.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentSourceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentSource)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentSource in the database
        List<DocumentSource> documentSourceList = documentSourceRepository.findAll();
        assertThat(documentSourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDocumentSourceWithPatch() throws Exception {
        // Initialize the database
        documentSourceRepository.saveAndFlush(documentSource);

        int databaseSizeBeforeUpdate = documentSourceRepository.findAll().size();

        // Update the documentSource using partial update
        DocumentSource partialUpdatedDocumentSource = new DocumentSource();
        partialUpdatedDocumentSource.setId(documentSource.getId());

        partialUpdatedDocumentSource.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restDocumentSourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentSource.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocumentSource))
            )
            .andExpect(status().isOk());

        // Validate the DocumentSource in the database
        List<DocumentSource> documentSourceList = documentSourceRepository.findAll();
        assertThat(documentSourceList).hasSize(databaseSizeBeforeUpdate);
        DocumentSource testDocumentSource = documentSourceList.get(documentSourceList.size() - 1);
        assertThat(testDocumentSource.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDocumentSource.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDocumentSource.getChangeIndicator()).isEqualTo(DEFAULT_CHANGE_INDICATOR);
        assertThat(testDocumentSource.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testDocumentSource.getSubtype()).isEqualTo(DEFAULT_SUBTYPE);
        assertThat(testDocumentSource.getGroup()).isEqualTo(DEFAULT_GROUP);
        assertThat(testDocumentSource.getSheet()).isEqualTo(DEFAULT_SHEET);
    }

    @Test
    @Transactional
    void fullUpdateDocumentSourceWithPatch() throws Exception {
        // Initialize the database
        documentSourceRepository.saveAndFlush(documentSource);

        int databaseSizeBeforeUpdate = documentSourceRepository.findAll().size();

        // Update the documentSource using partial update
        DocumentSource partialUpdatedDocumentSource = new DocumentSource();
        partialUpdatedDocumentSource.setId(documentSource.getId());

        partialUpdatedDocumentSource
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .changeIndicator(UPDATED_CHANGE_INDICATOR)
            .type(UPDATED_TYPE)
            .subtype(UPDATED_SUBTYPE)
            .group(UPDATED_GROUP)
            .sheet(UPDATED_SHEET);

        restDocumentSourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentSource.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocumentSource))
            )
            .andExpect(status().isOk());

        // Validate the DocumentSource in the database
        List<DocumentSource> documentSourceList = documentSourceRepository.findAll();
        assertThat(documentSourceList).hasSize(databaseSizeBeforeUpdate);
        DocumentSource testDocumentSource = documentSourceList.get(documentSourceList.size() - 1);
        assertThat(testDocumentSource.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDocumentSource.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDocumentSource.getChangeIndicator()).isEqualTo(UPDATED_CHANGE_INDICATOR);
        assertThat(testDocumentSource.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testDocumentSource.getSubtype()).isEqualTo(UPDATED_SUBTYPE);
        assertThat(testDocumentSource.getGroup()).isEqualTo(UPDATED_GROUP);
        assertThat(testDocumentSource.getSheet()).isEqualTo(UPDATED_SHEET);
    }

    @Test
    @Transactional
    void patchNonExistingDocumentSource() throws Exception {
        int databaseSizeBeforeUpdate = documentSourceRepository.findAll().size();
        documentSource.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentSourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentSource.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentSource))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentSource in the database
        List<DocumentSource> documentSourceList = documentSourceRepository.findAll();
        assertThat(documentSourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocumentSource() throws Exception {
        int databaseSizeBeforeUpdate = documentSourceRepository.findAll().size();
        documentSource.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentSourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentSource))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentSource in the database
        List<DocumentSource> documentSourceList = documentSourceRepository.findAll();
        assertThat(documentSourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocumentSource() throws Exception {
        int databaseSizeBeforeUpdate = documentSourceRepository.findAll().size();
        documentSource.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentSourceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(documentSource))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentSource in the database
        List<DocumentSource> documentSourceList = documentSourceRepository.findAll();
        assertThat(documentSourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocumentSource() throws Exception {
        // Initialize the database
        documentSourceRepository.saveAndFlush(documentSource);

        int databaseSizeBeforeDelete = documentSourceRepository.findAll().size();

        // Delete the documentSource
        restDocumentSourceMockMvc
            .perform(delete(ENTITY_API_URL_ID, documentSource.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DocumentSource> documentSourceList = documentSourceRepository.findAll();
        assertThat(documentSourceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
