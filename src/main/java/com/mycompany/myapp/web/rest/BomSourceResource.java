package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.BomSource;
import com.mycompany.myapp.repository.BomSourceRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.BomSource}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class BomSourceResource {

    private final Logger log = LoggerFactory.getLogger(BomSourceResource.class);

    private static final String ENTITY_NAME = "jhipsterSampleApplicationBomSource";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BomSourceRepository bomSourceRepository;

    public BomSourceResource(BomSourceRepository bomSourceRepository) {
        this.bomSourceRepository = bomSourceRepository;
    }

    /**
     * {@code POST  /bom-sources} : Create a new bomSource.
     *
     * @param bomSource the bomSource to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bomSource, or with status {@code 400 (Bad Request)} if the bomSource has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bom-sources")
    public ResponseEntity<BomSource> createBomSource(@RequestBody BomSource bomSource) throws URISyntaxException {
        log.debug("REST request to save BomSource : {}", bomSource);
        if (bomSource.getId() != null) {
            throw new BadRequestAlertException("A new bomSource cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BomSource result = bomSourceRepository.save(bomSource);
        return ResponseEntity
            .created(new URI("/api/bom-sources/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bom-sources/:id} : Updates an existing bomSource.
     *
     * @param id the id of the bomSource to save.
     * @param bomSource the bomSource to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bomSource,
     * or with status {@code 400 (Bad Request)} if the bomSource is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bomSource couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bom-sources/{id}")
    public ResponseEntity<BomSource> updateBomSource(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BomSource bomSource
    ) throws URISyntaxException {
        log.debug("REST request to update BomSource : {}, {}", id, bomSource);
        if (bomSource.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bomSource.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bomSourceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BomSource result = bomSourceRepository.save(bomSource);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bomSource.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /bom-sources/:id} : Partial updates given fields of an existing bomSource, field will ignore if it is null
     *
     * @param id the id of the bomSource to save.
     * @param bomSource the bomSource to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bomSource,
     * or with status {@code 400 (Bad Request)} if the bomSource is not valid,
     * or with status {@code 404 (Not Found)} if the bomSource is not found,
     * or with status {@code 500 (Internal Server Error)} if the bomSource couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/bom-sources/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BomSource> partialUpdateBomSource(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BomSource bomSource
    ) throws URISyntaxException {
        log.debug("REST request to partial update BomSource partially : {}, {}", id, bomSource);
        if (bomSource.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bomSource.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bomSourceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BomSource> result = bomSourceRepository
            .findById(bomSource.getId())
            .map(existingBomSource -> {
                if (bomSource.getType() != null) {
                    existingBomSource.setType(bomSource.getType());
                }

                return existingBomSource;
            })
            .map(bomSourceRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bomSource.getId().toString())
        );
    }

    /**
     * {@code GET  /bom-sources} : get all the bomSources.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bomSources in body.
     */
    @GetMapping("/bom-sources")
    public List<BomSource> getAllBomSources(
        @RequestParam(required = false) String filter,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        if ("bom-is-null".equals(filter)) {
            log.debug("REST request to get all BomSources where bom is null");
            return StreamSupport
                .stream(bomSourceRepository.findAll().spliterator(), false)
                .filter(bomSource -> bomSource.getBom() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all BomSources");
        return bomSourceRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /bom-sources/:id} : get the "id" bomSource.
     *
     * @param id the id of the bomSource to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bomSource, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bom-sources/{id}")
    public ResponseEntity<BomSource> getBomSource(@PathVariable Long id) {
        log.debug("REST request to get BomSource : {}", id);
        Optional<BomSource> bomSource = bomSourceRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(bomSource);
    }

    /**
     * {@code DELETE  /bom-sources/:id} : delete the "id" bomSource.
     *
     * @param id the id of the bomSource to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bom-sources/{id}")
    public ResponseEntity<Void> deleteBomSource(@PathVariable Long id) {
        log.debug("REST request to delete BomSource : {}", id);
        bomSourceRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
