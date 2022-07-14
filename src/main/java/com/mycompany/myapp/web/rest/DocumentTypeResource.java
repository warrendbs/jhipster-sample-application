package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.DocumentType;
import com.mycompany.myapp.repository.DocumentTypeRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.DocumentType}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DocumentTypeResource {

    private final Logger log = LoggerFactory.getLogger(DocumentTypeResource.class);

    private static final String ENTITY_NAME = "jhipsterSampleApplicationDocumentType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocumentTypeRepository documentTypeRepository;

    public DocumentTypeResource(DocumentTypeRepository documentTypeRepository) {
        this.documentTypeRepository = documentTypeRepository;
    }

    /**
     * {@code POST  /document-types} : Create a new documentType.
     *
     * @param documentType the documentType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new documentType, or with status {@code 400 (Bad Request)} if the documentType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/document-types")
    public ResponseEntity<DocumentType> createDocumentType(@RequestBody DocumentType documentType) throws URISyntaxException {
        log.debug("REST request to save DocumentType : {}", documentType);
        if (documentType.getId() != null) {
            throw new BadRequestAlertException("A new documentType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DocumentType result = documentTypeRepository.save(documentType);
        return ResponseEntity
            .created(new URI("/api/document-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /document-types/:id} : Updates an existing documentType.
     *
     * @param id the id of the documentType to save.
     * @param documentType the documentType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentType,
     * or with status {@code 400 (Bad Request)} if the documentType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the documentType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/document-types/{id}")
    public ResponseEntity<DocumentType> updateDocumentType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DocumentType documentType
    ) throws URISyntaxException {
        log.debug("REST request to update DocumentType : {}, {}", id, documentType);
        if (documentType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DocumentType result = documentTypeRepository.save(documentType);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, documentType.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /document-types/:id} : Partial updates given fields of an existing documentType, field will ignore if it is null
     *
     * @param id the id of the documentType to save.
     * @param documentType the documentType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentType,
     * or with status {@code 400 (Bad Request)} if the documentType is not valid,
     * or with status {@code 404 (Not Found)} if the documentType is not found,
     * or with status {@code 500 (Internal Server Error)} if the documentType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/document-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DocumentType> partialUpdateDocumentType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DocumentType documentType
    ) throws URISyntaxException {
        log.debug("REST request to partial update DocumentType partially : {}, {}", id, documentType);
        if (documentType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DocumentType> result = documentTypeRepository
            .findById(documentType.getId())
            .map(existingDocumentType -> {
                if (documentType.getDesctiption() != null) {
                    existingDocumentType.setDesctiption(documentType.getDesctiption());
                }

                return existingDocumentType;
            })
            .map(documentTypeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, documentType.getId().toString())
        );
    }

    /**
     * {@code GET  /document-types} : get all the documentTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of documentTypes in body.
     */
    @GetMapping("/document-types")
    public List<DocumentType> getAllDocumentTypes() {
        log.debug("REST request to get all DocumentTypes");
        return documentTypeRepository.findAll();
    }

    /**
     * {@code GET  /document-types/:id} : get the "id" documentType.
     *
     * @param id the id of the documentType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the documentType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/document-types/{id}")
    public ResponseEntity<DocumentType> getDocumentType(@PathVariable Long id) {
        log.debug("REST request to get DocumentType : {}", id);
        Optional<DocumentType> documentType = documentTypeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(documentType);
    }

    /**
     * {@code DELETE  /document-types/:id} : delete the "id" documentType.
     *
     * @param id the id of the documentType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/document-types/{id}")
    public ResponseEntity<Void> deleteDocumentType(@PathVariable Long id) {
        log.debug("REST request to delete DocumentType : {}", id);
        documentTypeRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
