package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.BomChild;
import com.mycompany.myapp.repository.BomChildRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.BomChild}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class BomChildResource {

    private final Logger log = LoggerFactory.getLogger(BomChildResource.class);

    private static final String ENTITY_NAME = "jhipsterSampleApplicationBomChild";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BomChildRepository bomChildRepository;

    public BomChildResource(BomChildRepository bomChildRepository) {
        this.bomChildRepository = bomChildRepository;
    }

    /**
     * {@code POST  /bom-children} : Create a new bomChild.
     *
     * @param bomChild the bomChild to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bomChild, or with status {@code 400 (Bad Request)} if the bomChild has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bom-children")
    public ResponseEntity<BomChild> createBomChild(@RequestBody BomChild bomChild) throws URISyntaxException {
        log.debug("REST request to save BomChild : {}", bomChild);
        if (bomChild.getId() != null) {
            throw new BadRequestAlertException("A new bomChild cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BomChild result = bomChildRepository.save(bomChild);
        return ResponseEntity
            .created(new URI("/api/bom-children/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bom-children/:id} : Updates an existing bomChild.
     *
     * @param id the id of the bomChild to save.
     * @param bomChild the bomChild to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bomChild,
     * or with status {@code 400 (Bad Request)} if the bomChild is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bomChild couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bom-children/{id}")
    public ResponseEntity<BomChild> updateBomChild(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BomChild bomChild
    ) throws URISyntaxException {
        log.debug("REST request to update BomChild : {}, {}", id, bomChild);
        if (bomChild.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bomChild.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bomChildRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BomChild result = bomChildRepository.save(bomChild);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bomChild.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /bom-children/:id} : Partial updates given fields of an existing bomChild, field will ignore if it is null
     *
     * @param id the id of the bomChild to save.
     * @param bomChild the bomChild to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bomChild,
     * or with status {@code 400 (Bad Request)} if the bomChild is not valid,
     * or with status {@code 404 (Not Found)} if the bomChild is not found,
     * or with status {@code 500 (Internal Server Error)} if the bomChild couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/bom-children/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BomChild> partialUpdateBomChild(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BomChild bomChild
    ) throws URISyntaxException {
        log.debug("REST request to partial update BomChild partially : {}, {}", id, bomChild);
        if (bomChild.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bomChild.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bomChildRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BomChild> result = bomChildRepository
            .findById(bomChild.getId())
            .map(existingBomChild -> {
                if (bomChild.getProductId() != null) {
                    existingBomChild.setProductId(bomChild.getProductId());
                }
                if (bomChild.getRevision() != null) {
                    existingBomChild.setRevision(bomChild.getRevision());
                }
                if (bomChild.getQuantity() != null) {
                    existingBomChild.setQuantity(bomChild.getQuantity());
                }
                if (bomChild.getRelationType() != null) {
                    existingBomChild.setRelationType(bomChild.getRelationType());
                }

                return existingBomChild;
            })
            .map(bomChildRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bomChild.getId().toString())
        );
    }

    /**
     * {@code GET  /bom-children} : get all the bomChildren.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bomChildren in body.
     */
    @GetMapping("/bom-children")
    public List<BomChild> getAllBomChildren() {
        log.debug("REST request to get all BomChildren");
        return bomChildRepository.findAll();
    }

    /**
     * {@code GET  /bom-children/:id} : get the "id" bomChild.
     *
     * @param id the id of the bomChild to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bomChild, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bom-children/{id}")
    public ResponseEntity<BomChild> getBomChild(@PathVariable Long id) {
        log.debug("REST request to get BomChild : {}", id);
        Optional<BomChild> bomChild = bomChildRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(bomChild);
    }

    /**
     * {@code DELETE  /bom-children/:id} : delete the "id" bomChild.
     *
     * @param id the id of the bomChild to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bom-children/{id}")
    public ResponseEntity<Void> deleteBomChild(@PathVariable Long id) {
        log.debug("REST request to delete BomChild : {}", id);
        bomChildRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
