package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Bom;
import com.mycompany.myapp.repository.BomRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Bom}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class BomResource {

    private final Logger log = LoggerFactory.getLogger(BomResource.class);

    private static final String ENTITY_NAME = "jhipsterSampleApplicationBom";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BomRepository bomRepository;

    public BomResource(BomRepository bomRepository) {
        this.bomRepository = bomRepository;
    }

    /**
     * {@code POST  /boms} : Create a new bom.
     *
     * @param bom the bom to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bom, or with status {@code 400 (Bad Request)} if the bom has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/boms")
    public ResponseEntity<Bom> createBom(@RequestBody Bom bom) throws URISyntaxException {
        log.debug("REST request to save Bom : {}", bom);
        if (bom.getId() != null) {
            throw new BadRequestAlertException("A new bom cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Bom result = bomRepository.save(bom);
        return ResponseEntity
            .created(new URI("/api/boms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /boms/:id} : Updates an existing bom.
     *
     * @param id the id of the bom to save.
     * @param bom the bom to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bom,
     * or with status {@code 400 (Bad Request)} if the bom is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bom couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/boms/{id}")
    public ResponseEntity<Bom> updateBom(@PathVariable(value = "id", required = false) final Long id, @RequestBody Bom bom)
        throws URISyntaxException {
        log.debug("REST request to update Bom : {}, {}", id, bom);
        if (bom.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bom.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bomRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Bom result = bomRepository.save(bom);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bom.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /boms/:id} : Partial updates given fields of an existing bom, field will ignore if it is null
     *
     * @param id the id of the bom to save.
     * @param bom the bom to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bom,
     * or with status {@code 400 (Bad Request)} if the bom is not valid,
     * or with status {@code 404 (Not Found)} if the bom is not found,
     * or with status {@code 500 (Internal Server Error)} if the bom couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/boms/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Bom> partialUpdateBom(@PathVariable(value = "id", required = false) final Long id, @RequestBody Bom bom)
        throws URISyntaxException {
        log.debug("REST request to partial update Bom partially : {}, {}", id, bom);
        if (bom.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bom.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bomRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Bom> result = bomRepository
            .findById(bom.getId())
            .map(existingBom -> {
                if (bom.getStatus() != null) {
                    existingBom.setStatus(bom.getStatus());
                }

                return existingBom;
            })
            .map(bomRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bom.getId().toString())
        );
    }

    /**
     * {@code GET  /boms} : get all the boms.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of boms in body.
     */
    @GetMapping("/boms")
    public List<Bom> getAllBoms(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Boms");
        return bomRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /boms/:id} : get the "id" bom.
     *
     * @param id the id of the bom to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bom, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/boms/{id}")
    public ResponseEntity<Bom> getBom(@PathVariable Long id) {
        log.debug("REST request to get Bom : {}", id);
        Optional<Bom> bom = bomRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(bom);
    }

    /**
     * {@code DELETE  /boms/:id} : delete the "id" bom.
     *
     * @param id the id of the bom to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/boms/{id}")
    public ResponseEntity<Void> deleteBom(@PathVariable Long id) {
        log.debug("REST request to delete Bom : {}", id);
        bomRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
