package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.DocumentIntention;
import com.mycompany.myapp.repository.DocumentIntentionRepository;
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
 * Integration tests for the {@link DocumentIntentionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DocumentIntentionResourceIT {

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

    private static final String ENTITY_API_URL = "/api/document-intentions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DocumentIntentionRepository documentIntentionRepository;

    @Mock
    private DocumentIntentionRepository documentIntentionRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentIntentionMockMvc;

    private DocumentIntention documentIntention;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentIntention createEntity(EntityManager em) {
        DocumentIntention documentIntention = new DocumentIntention()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .changeIndicator(DEFAULT_CHANGE_INDICATOR)
            .type(DEFAULT_TYPE)
            .subtype(DEFAULT_SUBTYPE)
            .group(DEFAULT_GROUP)
            .sheet(DEFAULT_SHEET);
        return documentIntention;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentIntention createUpdatedEntity(EntityManager em) {
        DocumentIntention documentIntention = new DocumentIntention()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .changeIndicator(UPDATED_CHANGE_INDICATOR)
            .type(UPDATED_TYPE)
            .subtype(UPDATED_SUBTYPE)
            .group(UPDATED_GROUP)
            .sheet(UPDATED_SHEET);
        return documentIntention;
    }

    @BeforeEach
    public void initTest() {
        documentIntention = createEntity(em);
    }

    @Test
    @Transactional
    void createDocumentIntention() throws Exception {
        int databaseSizeBeforeCreate = documentIntentionRepository.findAll().size();
        // Create the DocumentIntention
        restDocumentIntentionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentIntention))
            )
            .andExpect(status().isCreated());

        // Validate the DocumentIntention in the database
        List<DocumentIntention> documentIntentionList = documentIntentionRepository.findAll();
        assertThat(documentIntentionList).hasSize(databaseSizeBeforeCreate + 1);
        DocumentIntention testDocumentIntention = documentIntentionList.get(documentIntentionList.size() - 1);
        assertThat(testDocumentIntention.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDocumentIntention.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDocumentIntention.getChangeIndicator()).isEqualTo(DEFAULT_CHANGE_INDICATOR);
        assertThat(testDocumentIntention.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testDocumentIntention.getSubtype()).isEqualTo(DEFAULT_SUBTYPE);
        assertThat(testDocumentIntention.getGroup()).isEqualTo(DEFAULT_GROUP);
        assertThat(testDocumentIntention.getSheet()).isEqualTo(DEFAULT_SHEET);
    }

    @Test
    @Transactional
    void createDocumentIntentionWithExistingId() throws Exception {
        // Create the DocumentIntention with an existing ID
        documentIntention.setId(1L);

        int databaseSizeBeforeCreate = documentIntentionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentIntentionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentIntention))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentIntention in the database
        List<DocumentIntention> documentIntentionList = documentIntentionRepository.findAll();
        assertThat(documentIntentionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDocumentIntentions() throws Exception {
        // Initialize the database
        documentIntentionRepository.saveAndFlush(documentIntention);

        // Get all the documentIntentionList
        restDocumentIntentionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentIntention.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].changeIndicator").value(hasItem(DEFAULT_CHANGE_INDICATOR.booleanValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].subtype").value(hasItem(DEFAULT_SUBTYPE)))
            .andExpect(jsonPath("$.[*].group").value(hasItem(DEFAULT_GROUP)))
            .andExpect(jsonPath("$.[*].sheet").value(hasItem(DEFAULT_SHEET)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDocumentIntentionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(documentIntentionRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDocumentIntentionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(documentIntentionRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDocumentIntentionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(documentIntentionRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDocumentIntentionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(documentIntentionRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getDocumentIntention() throws Exception {
        // Initialize the database
        documentIntentionRepository.saveAndFlush(documentIntention);

        // Get the documentIntention
        restDocumentIntentionMockMvc
            .perform(get(ENTITY_API_URL_ID, documentIntention.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(documentIntention.getId().intValue()))
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
    void getNonExistingDocumentIntention() throws Exception {
        // Get the documentIntention
        restDocumentIntentionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDocumentIntention() throws Exception {
        // Initialize the database
        documentIntentionRepository.saveAndFlush(documentIntention);

        int databaseSizeBeforeUpdate = documentIntentionRepository.findAll().size();

        // Update the documentIntention
        DocumentIntention updatedDocumentIntention = documentIntentionRepository.findById(documentIntention.getId()).get();
        // Disconnect from session so that the updates on updatedDocumentIntention are not directly saved in db
        em.detach(updatedDocumentIntention);
        updatedDocumentIntention
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .changeIndicator(UPDATED_CHANGE_INDICATOR)
            .type(UPDATED_TYPE)
            .subtype(UPDATED_SUBTYPE)
            .group(UPDATED_GROUP)
            .sheet(UPDATED_SHEET);

        restDocumentIntentionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDocumentIntention.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDocumentIntention))
            )
            .andExpect(status().isOk());

        // Validate the DocumentIntention in the database
        List<DocumentIntention> documentIntentionList = documentIntentionRepository.findAll();
        assertThat(documentIntentionList).hasSize(databaseSizeBeforeUpdate);
        DocumentIntention testDocumentIntention = documentIntentionList.get(documentIntentionList.size() - 1);
        assertThat(testDocumentIntention.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDocumentIntention.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDocumentIntention.getChangeIndicator()).isEqualTo(UPDATED_CHANGE_INDICATOR);
        assertThat(testDocumentIntention.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testDocumentIntention.getSubtype()).isEqualTo(UPDATED_SUBTYPE);
        assertThat(testDocumentIntention.getGroup()).isEqualTo(UPDATED_GROUP);
        assertThat(testDocumentIntention.getSheet()).isEqualTo(UPDATED_SHEET);
    }

    @Test
    @Transactional
    void putNonExistingDocumentIntention() throws Exception {
        int databaseSizeBeforeUpdate = documentIntentionRepository.findAll().size();
        documentIntention.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentIntentionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentIntention.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentIntention))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentIntention in the database
        List<DocumentIntention> documentIntentionList = documentIntentionRepository.findAll();
        assertThat(documentIntentionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocumentIntention() throws Exception {
        int databaseSizeBeforeUpdate = documentIntentionRepository.findAll().size();
        documentIntention.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentIntentionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentIntention))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentIntention in the database
        List<DocumentIntention> documentIntentionList = documentIntentionRepository.findAll();
        assertThat(documentIntentionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocumentIntention() throws Exception {
        int databaseSizeBeforeUpdate = documentIntentionRepository.findAll().size();
        documentIntention.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentIntentionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentIntention))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentIntention in the database
        List<DocumentIntention> documentIntentionList = documentIntentionRepository.findAll();
        assertThat(documentIntentionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDocumentIntentionWithPatch() throws Exception {
        // Initialize the database
        documentIntentionRepository.saveAndFlush(documentIntention);

        int databaseSizeBeforeUpdate = documentIntentionRepository.findAll().size();

        // Update the documentIntention using partial update
        DocumentIntention partialUpdatedDocumentIntention = new DocumentIntention();
        partialUpdatedDocumentIntention.setId(documentIntention.getId());

        partialUpdatedDocumentIntention.description(UPDATED_DESCRIPTION).subtype(UPDATED_SUBTYPE).group(UPDATED_GROUP).sheet(UPDATED_SHEET);

        restDocumentIntentionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentIntention.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocumentIntention))
            )
            .andExpect(status().isOk());

        // Validate the DocumentIntention in the database
        List<DocumentIntention> documentIntentionList = documentIntentionRepository.findAll();
        assertThat(documentIntentionList).hasSize(databaseSizeBeforeUpdate);
        DocumentIntention testDocumentIntention = documentIntentionList.get(documentIntentionList.size() - 1);
        assertThat(testDocumentIntention.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDocumentIntention.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDocumentIntention.getChangeIndicator()).isEqualTo(DEFAULT_CHANGE_INDICATOR);
        assertThat(testDocumentIntention.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testDocumentIntention.getSubtype()).isEqualTo(UPDATED_SUBTYPE);
        assertThat(testDocumentIntention.getGroup()).isEqualTo(UPDATED_GROUP);
        assertThat(testDocumentIntention.getSheet()).isEqualTo(UPDATED_SHEET);
    }

    @Test
    @Transactional
    void fullUpdateDocumentIntentionWithPatch() throws Exception {
        // Initialize the database
        documentIntentionRepository.saveAndFlush(documentIntention);

        int databaseSizeBeforeUpdate = documentIntentionRepository.findAll().size();

        // Update the documentIntention using partial update
        DocumentIntention partialUpdatedDocumentIntention = new DocumentIntention();
        partialUpdatedDocumentIntention.setId(documentIntention.getId());

        partialUpdatedDocumentIntention
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .changeIndicator(UPDATED_CHANGE_INDICATOR)
            .type(UPDATED_TYPE)
            .subtype(UPDATED_SUBTYPE)
            .group(UPDATED_GROUP)
            .sheet(UPDATED_SHEET);

        restDocumentIntentionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentIntention.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocumentIntention))
            )
            .andExpect(status().isOk());

        // Validate the DocumentIntention in the database
        List<DocumentIntention> documentIntentionList = documentIntentionRepository.findAll();
        assertThat(documentIntentionList).hasSize(databaseSizeBeforeUpdate);
        DocumentIntention testDocumentIntention = documentIntentionList.get(documentIntentionList.size() - 1);
        assertThat(testDocumentIntention.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDocumentIntention.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDocumentIntention.getChangeIndicator()).isEqualTo(UPDATED_CHANGE_INDICATOR);
        assertThat(testDocumentIntention.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testDocumentIntention.getSubtype()).isEqualTo(UPDATED_SUBTYPE);
        assertThat(testDocumentIntention.getGroup()).isEqualTo(UPDATED_GROUP);
        assertThat(testDocumentIntention.getSheet()).isEqualTo(UPDATED_SHEET);
    }

    @Test
    @Transactional
    void patchNonExistingDocumentIntention() throws Exception {
        int databaseSizeBeforeUpdate = documentIntentionRepository.findAll().size();
        documentIntention.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentIntentionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentIntention.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentIntention))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentIntention in the database
        List<DocumentIntention> documentIntentionList = documentIntentionRepository.findAll();
        assertThat(documentIntentionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocumentIntention() throws Exception {
        int databaseSizeBeforeUpdate = documentIntentionRepository.findAll().size();
        documentIntention.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentIntentionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentIntention))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentIntention in the database
        List<DocumentIntention> documentIntentionList = documentIntentionRepository.findAll();
        assertThat(documentIntentionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocumentIntention() throws Exception {
        int databaseSizeBeforeUpdate = documentIntentionRepository.findAll().size();
        documentIntention.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentIntentionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentIntention))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentIntention in the database
        List<DocumentIntention> documentIntentionList = documentIntentionRepository.findAll();
        assertThat(documentIntentionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocumentIntention() throws Exception {
        // Initialize the database
        documentIntentionRepository.saveAndFlush(documentIntention);

        int databaseSizeBeforeDelete = documentIntentionRepository.findAll().size();

        // Delete the documentIntention
        restDocumentIntentionMockMvc
            .perform(delete(ENTITY_API_URL_ID, documentIntention.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DocumentIntention> documentIntentionList = documentIntentionRepository.findAll();
        assertThat(documentIntentionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
