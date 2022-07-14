package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Context;
import com.mycompany.myapp.repository.ContextRepository;
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
 * Integration tests for the {@link ContextResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContextResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    private static final String ENTITY_API_URL = "/api/contexts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContextRepository contextRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContextMockMvc;

    private Context context;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Context createEntity(EntityManager em) {
        Context context = new Context().type(DEFAULT_TYPE).name(DEFAULT_NAME).status(DEFAULT_STATUS);
        return context;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Context createUpdatedEntity(EntityManager em) {
        Context context = new Context().type(UPDATED_TYPE).name(UPDATED_NAME).status(UPDATED_STATUS);
        return context;
    }

    @BeforeEach
    public void initTest() {
        context = createEntity(em);
    }

    @Test
    @Transactional
    void createContext() throws Exception {
        int databaseSizeBeforeCreate = contextRepository.findAll().size();
        // Create the Context
        restContextMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(context)))
            .andExpect(status().isCreated());

        // Validate the Context in the database
        List<Context> contextList = contextRepository.findAll();
        assertThat(contextList).hasSize(databaseSizeBeforeCreate + 1);
        Context testContext = contextList.get(contextList.size() - 1);
        assertThat(testContext.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testContext.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testContext.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createContextWithExistingId() throws Exception {
        // Create the Context with an existing ID
        context.setId(1L);

        int databaseSizeBeforeCreate = contextRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContextMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(context)))
            .andExpect(status().isBadRequest());

        // Validate the Context in the database
        List<Context> contextList = contextRepository.findAll();
        assertThat(contextList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllContexts() throws Exception {
        // Initialize the database
        contextRepository.saveAndFlush(context);

        // Get all the contextList
        restContextMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(context.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    void getContext() throws Exception {
        // Initialize the database
        contextRepository.saveAndFlush(context);

        // Get the context
        restContextMockMvc
            .perform(get(ENTITY_API_URL_ID, context.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(context.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    void getNonExistingContext() throws Exception {
        // Get the context
        restContextMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewContext() throws Exception {
        // Initialize the database
        contextRepository.saveAndFlush(context);

        int databaseSizeBeforeUpdate = contextRepository.findAll().size();

        // Update the context
        Context updatedContext = contextRepository.findById(context.getId()).get();
        // Disconnect from session so that the updates on updatedContext are not directly saved in db
        em.detach(updatedContext);
        updatedContext.type(UPDATED_TYPE).name(UPDATED_NAME).status(UPDATED_STATUS);

        restContextMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedContext.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedContext))
            )
            .andExpect(status().isOk());

        // Validate the Context in the database
        List<Context> contextList = contextRepository.findAll();
        assertThat(contextList).hasSize(databaseSizeBeforeUpdate);
        Context testContext = contextList.get(contextList.size() - 1);
        assertThat(testContext.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testContext.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testContext.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingContext() throws Exception {
        int databaseSizeBeforeUpdate = contextRepository.findAll().size();
        context.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContextMockMvc
            .perform(
                put(ENTITY_API_URL_ID, context.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(context))
            )
            .andExpect(status().isBadRequest());

        // Validate the Context in the database
        List<Context> contextList = contextRepository.findAll();
        assertThat(contextList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContext() throws Exception {
        int databaseSizeBeforeUpdate = contextRepository.findAll().size();
        context.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContextMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(context))
            )
            .andExpect(status().isBadRequest());

        // Validate the Context in the database
        List<Context> contextList = contextRepository.findAll();
        assertThat(contextList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContext() throws Exception {
        int databaseSizeBeforeUpdate = contextRepository.findAll().size();
        context.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContextMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(context)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Context in the database
        List<Context> contextList = contextRepository.findAll();
        assertThat(contextList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContextWithPatch() throws Exception {
        // Initialize the database
        contextRepository.saveAndFlush(context);

        int databaseSizeBeforeUpdate = contextRepository.findAll().size();

        // Update the context using partial update
        Context partialUpdatedContext = new Context();
        partialUpdatedContext.setId(context.getId());

        partialUpdatedContext.type(UPDATED_TYPE);

        restContextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContext.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContext))
            )
            .andExpect(status().isOk());

        // Validate the Context in the database
        List<Context> contextList = contextRepository.findAll();
        assertThat(contextList).hasSize(databaseSizeBeforeUpdate);
        Context testContext = contextList.get(contextList.size() - 1);
        assertThat(testContext.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testContext.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testContext.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateContextWithPatch() throws Exception {
        // Initialize the database
        contextRepository.saveAndFlush(context);

        int databaseSizeBeforeUpdate = contextRepository.findAll().size();

        // Update the context using partial update
        Context partialUpdatedContext = new Context();
        partialUpdatedContext.setId(context.getId());

        partialUpdatedContext.type(UPDATED_TYPE).name(UPDATED_NAME).status(UPDATED_STATUS);

        restContextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContext.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContext))
            )
            .andExpect(status().isOk());

        // Validate the Context in the database
        List<Context> contextList = contextRepository.findAll();
        assertThat(contextList).hasSize(databaseSizeBeforeUpdate);
        Context testContext = contextList.get(contextList.size() - 1);
        assertThat(testContext.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testContext.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testContext.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingContext() throws Exception {
        int databaseSizeBeforeUpdate = contextRepository.findAll().size();
        context.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, context.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(context))
            )
            .andExpect(status().isBadRequest());

        // Validate the Context in the database
        List<Context> contextList = contextRepository.findAll();
        assertThat(contextList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContext() throws Exception {
        int databaseSizeBeforeUpdate = contextRepository.findAll().size();
        context.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(context))
            )
            .andExpect(status().isBadRequest());

        // Validate the Context in the database
        List<Context> contextList = contextRepository.findAll();
        assertThat(contextList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContext() throws Exception {
        int databaseSizeBeforeUpdate = contextRepository.findAll().size();
        context.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContextMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(context)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Context in the database
        List<Context> contextList = contextRepository.findAll();
        assertThat(contextList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContext() throws Exception {
        // Initialize the database
        contextRepository.saveAndFlush(context);

        int databaseSizeBeforeDelete = contextRepository.findAll().size();

        // Delete the context
        restContextMockMvc
            .perform(delete(ENTITY_API_URL_ID, context.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Context> contextList = contextRepository.findAll();
        assertThat(contextList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
