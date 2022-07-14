package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.DocumentIntention;
import com.mycompany.myapp.repository.DocumentIntentionRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.DocumentIntention}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DocumentIntentionResource {

    private final Logger log = LoggerFactory.getLogger(DocumentIntentionResource.class);

    private static final String ENTITY_NAME = "jhipsterSampleApplicationDocumentIntention";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocumentIntentionRepository documentIntentionRepository;

    public DocumentIntentionResource(DocumentIntentionRepository documentIntentionRepository) {
        this.documentIntentionRepository = documentIntentionRepository;
    }

    /**
     * {@code POST  /document-intentions} : Create a new documentIntention.
     *
     * @param documentIntention the documentIntention to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new documentIntention, or with status {@code 400 (Bad Request)} if the documentIntention has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/document-intentions")
    public ResponseEntity<DocumentIntention> createDocumentIntention(@RequestBody DocumentIntention documentIntention)
        throws URISyntaxException {
        log.debug("REST request to save DocumentIntention : {}", documentIntention);
        if (documentIntention.getId() != null) {
            throw new BadRequestAlertException("A new documentIntention cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DocumentIntention result = documentIntentionRepository.save(documentIntention);
        return ResponseEntity
            .created(new URI("/api/document-intentions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /document-intentions/:id} : Updates an existing documentIntention.
     *
     * @param id the id of the documentIntention to save.
     * @param documentIntention the documentIntention to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentIntention,
     * or with status {@code 400 (Bad Request)} if the documentIntention is not valid,
     * or with status {@code 500 (Internal Server Error)} if the documentIntention couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/document-intentions/{id}")
    public ResponseEntity<DocumentIntention> updateDocumentIntention(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DocumentIntention documentIntention
    ) throws URISyntaxException {
        log.debug("REST request to update DocumentIntention : {}, {}", id, documentIntention);
        if (documentIntention.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentIntention.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentIntentionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DocumentIntention result = documentIntentionRepository.save(documentIntention);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, documentIntention.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /document-intentions/:id} : Partial updates given fields of an existing documentIntention, field will ignore if it is null
     *
     * @param id the id of the documentIntention to save.
     * @param documentIntention the documentIntention to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentIntention,
     * or with status {@code 400 (Bad Request)} if the documentIntention is not valid,
     * or with status {@code 404 (Not Found)} if the documentIntention is not found,
     * or with status {@code 500 (Internal Server Error)} if the documentIntention couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/document-intentions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DocumentIntention> partialUpdateDocumentIntention(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DocumentIntention documentIntention
    ) throws URISyntaxException {
        log.debug("REST request to partial update DocumentIntention partially : {}, {}", id, documentIntention);
        if (documentIntention.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentIntention.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentIntentionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DocumentIntention> result = documentIntentionRepository
            .findById(documentIntention.getId())
            .map(existingDocumentIntention -> {
                if (documentIntention.getName() != null) {
                    existingDocumentIntention.setName(documentIntention.getName());
                }
                if (documentIntention.getDescription() != null) {
                    existingDocumentIntention.setDescription(documentIntention.getDescription());
                }
                if (documentIntention.getChangeIndicator() != null) {
                    existingDocumentIntention.setChangeIndicator(documentIntention.getChangeIndicator());
                }
                if (documentIntention.getType() != null) {
                    existingDocumentIntention.setType(documentIntention.getType());
                }
                if (documentIntention.getSubtype() != null) {
                    existingDocumentIntention.setSubtype(documentIntention.getSubtype());
                }
                if (documentIntention.getGroup() != null) {
                    existingDocumentIntention.setGroup(documentIntention.getGroup());
                }
                if (documentIntention.getSheet() != null) {
                    existingDocumentIntention.setSheet(documentIntention.getSheet());
                }

                return existingDocumentIntention;
            })
            .map(documentIntentionRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, documentIntention.getId().toString())
        );
    }

    /**
     * {@code GET  /document-intentions} : get all the documentIntentions.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of documentIntentions in body.
     */
    @GetMapping("/document-intentions")
    public List<DocumentIntention> getAllDocumentIntentions(
        @RequestParam(required = false) String filter,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        if ("document-is-null".equals(filter)) {
            log.debug("REST request to get all DocumentIntentions where document is null");
            return StreamSupport
                .stream(documentIntentionRepository.findAll().spliterator(), false)
                .filter(documentIntention -> documentIntention.getDocument() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all DocumentIntentions");
        return documentIntentionRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /document-intentions/:id} : get the "id" documentIntention.
     *
     * @param id the id of the documentIntention to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the documentIntention, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/document-intentions/{id}")
    public ResponseEntity<DocumentIntention> getDocumentIntention(@PathVariable Long id) {
        log.debug("REST request to get DocumentIntention : {}", id);
        Optional<DocumentIntention> documentIntention = documentIntentionRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(documentIntention);
    }

    /**
     * {@code DELETE  /document-intentions/:id} : delete the "id" documentIntention.
     *
     * @param id the id of the documentIntention to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/document-intentions/{id}")
    public ResponseEntity<Void> deleteDocumentIntention(@PathVariable Long id) {
        log.debug("REST request to delete DocumentIntention : {}", id);
        documentIntentionRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
