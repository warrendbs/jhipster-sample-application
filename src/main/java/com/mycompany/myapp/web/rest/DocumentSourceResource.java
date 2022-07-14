package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.DocumentSource;
import com.mycompany.myapp.repository.DocumentSourceRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.DocumentSource}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DocumentSourceResource {

    private final Logger log = LoggerFactory.getLogger(DocumentSourceResource.class);

    private static final String ENTITY_NAME = "jhipsterSampleApplicationDocumentSource";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocumentSourceRepository documentSourceRepository;

    public DocumentSourceResource(DocumentSourceRepository documentSourceRepository) {
        this.documentSourceRepository = documentSourceRepository;
    }

    /**
     * {@code POST  /document-sources} : Create a new documentSource.
     *
     * @param documentSource the documentSource to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new documentSource, or with status {@code 400 (Bad Request)} if the documentSource has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/document-sources")
    public ResponseEntity<DocumentSource> createDocumentSource(@RequestBody DocumentSource documentSource) throws URISyntaxException {
        log.debug("REST request to save DocumentSource : {}", documentSource);
        if (documentSource.getId() != null) {
            throw new BadRequestAlertException("A new documentSource cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DocumentSource result = documentSourceRepository.save(documentSource);
        return ResponseEntity
            .created(new URI("/api/document-sources/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /document-sources/:id} : Updates an existing documentSource.
     *
     * @param id the id of the documentSource to save.
     * @param documentSource the documentSource to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentSource,
     * or with status {@code 400 (Bad Request)} if the documentSource is not valid,
     * or with status {@code 500 (Internal Server Error)} if the documentSource couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/document-sources/{id}")
    public ResponseEntity<DocumentSource> updateDocumentSource(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DocumentSource documentSource
    ) throws URISyntaxException {
        log.debug("REST request to update DocumentSource : {}, {}", id, documentSource);
        if (documentSource.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentSource.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentSourceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DocumentSource result = documentSourceRepository.save(documentSource);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, documentSource.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /document-sources/:id} : Partial updates given fields of an existing documentSource, field will ignore if it is null
     *
     * @param id the id of the documentSource to save.
     * @param documentSource the documentSource to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentSource,
     * or with status {@code 400 (Bad Request)} if the documentSource is not valid,
     * or with status {@code 404 (Not Found)} if the documentSource is not found,
     * or with status {@code 500 (Internal Server Error)} if the documentSource couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/document-sources/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DocumentSource> partialUpdateDocumentSource(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DocumentSource documentSource
    ) throws URISyntaxException {
        log.debug("REST request to partial update DocumentSource partially : {}, {}", id, documentSource);
        if (documentSource.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentSource.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentSourceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DocumentSource> result = documentSourceRepository
            .findById(documentSource.getId())
            .map(existingDocumentSource -> {
                if (documentSource.getName() != null) {
                    existingDocumentSource.setName(documentSource.getName());
                }
                if (documentSource.getDescription() != null) {
                    existingDocumentSource.setDescription(documentSource.getDescription());
                }
                if (documentSource.getChangeIndicator() != null) {
                    existingDocumentSource.setChangeIndicator(documentSource.getChangeIndicator());
                }
                if (documentSource.getType() != null) {
                    existingDocumentSource.setType(documentSource.getType());
                }
                if (documentSource.getSubtype() != null) {
                    existingDocumentSource.setSubtype(documentSource.getSubtype());
                }
                if (documentSource.getGroup() != null) {
                    existingDocumentSource.setGroup(documentSource.getGroup());
                }
                if (documentSource.getSheet() != null) {
                    existingDocumentSource.setSheet(documentSource.getSheet());
                }

                return existingDocumentSource;
            })
            .map(documentSourceRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, documentSource.getId().toString())
        );
    }

    /**
     * {@code GET  /document-sources} : get all the documentSources.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of documentSources in body.
     */
    @GetMapping("/document-sources")
    public List<DocumentSource> getAllDocumentSources(@RequestParam(required = false) String filter) {
        if ("document-is-null".equals(filter)) {
            log.debug("REST request to get all DocumentSources where document is null");
            return StreamSupport
                .stream(documentSourceRepository.findAll().spliterator(), false)
                .filter(documentSource -> documentSource.getDocument() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all DocumentSources");
        return documentSourceRepository.findAll();
    }

    /**
     * {@code GET  /document-sources/:id} : get the "id" documentSource.
     *
     * @param id the id of the documentSource to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the documentSource, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/document-sources/{id}")
    public ResponseEntity<DocumentSource> getDocumentSource(@PathVariable Long id) {
        log.debug("REST request to get DocumentSource : {}", id);
        Optional<DocumentSource> documentSource = documentSourceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(documentSource);
    }

    /**
     * {@code DELETE  /document-sources/:id} : delete the "id" documentSource.
     *
     * @param id the id of the documentSource to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/document-sources/{id}")
    public ResponseEntity<Void> deleteDocumentSource(@PathVariable Long id) {
        log.debug("REST request to delete DocumentSource : {}", id);
        documentSourceRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
