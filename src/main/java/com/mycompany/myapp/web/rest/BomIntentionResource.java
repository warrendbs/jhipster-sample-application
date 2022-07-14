package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.BomIntention;
import com.mycompany.myapp.repository.BomIntentionRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.BomIntention}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class BomIntentionResource {

    private final Logger log = LoggerFactory.getLogger(BomIntentionResource.class);

    private static final String ENTITY_NAME = "jhipsterSampleApplicationBomIntention";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BomIntentionRepository bomIntentionRepository;

    public BomIntentionResource(BomIntentionRepository bomIntentionRepository) {
        this.bomIntentionRepository = bomIntentionRepository;
    }

    /**
     * {@code POST  /bom-intentions} : Create a new bomIntention.
     *
     * @param bomIntention the bomIntention to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bomIntention, or with status {@code 400 (Bad Request)} if the bomIntention has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bom-intentions")
    public ResponseEntity<BomIntention> createBomIntention(@RequestBody BomIntention bomIntention) throws URISyntaxException {
        log.debug("REST request to save BomIntention : {}", bomIntention);
        if (bomIntention.getId() != null) {
            throw new BadRequestAlertException("A new bomIntention cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BomIntention result = bomIntentionRepository.save(bomIntention);
        return ResponseEntity
            .created(new URI("/api/bom-intentions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bom-intentions/:id} : Updates an existing bomIntention.
     *
     * @param id the id of the bomIntention to save.
     * @param bomIntention the bomIntention to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bomIntention,
     * or with status {@code 400 (Bad Request)} if the bomIntention is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bomIntention couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bom-intentions/{id}")
    public ResponseEntity<BomIntention> updateBomIntention(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BomIntention bomIntention
    ) throws URISyntaxException {
        log.debug("REST request to update BomIntention : {}, {}", id, bomIntention);
        if (bomIntention.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bomIntention.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bomIntentionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BomIntention result = bomIntentionRepository.save(bomIntention);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bomIntention.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /bom-intentions/:id} : Partial updates given fields of an existing bomIntention, field will ignore if it is null
     *
     * @param id the id of the bomIntention to save.
     * @param bomIntention the bomIntention to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bomIntention,
     * or with status {@code 400 (Bad Request)} if the bomIntention is not valid,
     * or with status {@code 404 (Not Found)} if the bomIntention is not found,
     * or with status {@code 500 (Internal Server Error)} if the bomIntention couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/bom-intentions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BomIntention> partialUpdateBomIntention(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BomIntention bomIntention
    ) throws URISyntaxException {
        log.debug("REST request to partial update BomIntention partially : {}, {}", id, bomIntention);
        if (bomIntention.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bomIntention.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bomIntentionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BomIntention> result = bomIntentionRepository
            .findById(bomIntention.getId())
            .map(existingBomIntention -> {
                if (bomIntention.getType() != null) {
                    existingBomIntention.setType(bomIntention.getType());
                }

                return existingBomIntention;
            })
            .map(bomIntentionRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bomIntention.getId().toString())
        );
    }

    /**
     * {@code GET  /bom-intentions} : get all the bomIntentions.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bomIntentions in body.
     */
    @GetMapping("/bom-intentions")
    public List<BomIntention> getAllBomIntentions(
        @RequestParam(required = false) String filter,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        if ("bom-is-null".equals(filter)) {
            log.debug("REST request to get all BomIntentions where bom is null");
            return StreamSupport
                .stream(bomIntentionRepository.findAll().spliterator(), false)
                .filter(bomIntention -> bomIntention.getBom() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all BomIntentions");
        return bomIntentionRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /bom-intentions/:id} : get the "id" bomIntention.
     *
     * @param id the id of the bomIntention to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bomIntention, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bom-intentions/{id}")
    public ResponseEntity<BomIntention> getBomIntention(@PathVariable Long id) {
        log.debug("REST request to get BomIntention : {}", id);
        Optional<BomIntention> bomIntention = bomIntentionRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(bomIntention);
    }

    /**
     * {@code DELETE  /bom-intentions/:id} : delete the "id" bomIntention.
     *
     * @param id the id of the bomIntention to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bom-intentions/{id}")
    public ResponseEntity<Void> deleteBomIntention(@PathVariable Long id) {
        log.debug("REST request to delete BomIntention : {}", id);
        bomIntentionRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
