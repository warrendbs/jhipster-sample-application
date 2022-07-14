package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ImpactMatrix;
import com.mycompany.myapp.repository.ImpactMatrixRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.ImpactMatrix}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ImpactMatrixResource {

    private final Logger log = LoggerFactory.getLogger(ImpactMatrixResource.class);

    private static final String ENTITY_NAME = "jhipsterSampleApplicationImpactMatrix";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ImpactMatrixRepository impactMatrixRepository;

    public ImpactMatrixResource(ImpactMatrixRepository impactMatrixRepository) {
        this.impactMatrixRepository = impactMatrixRepository;
    }

    /**
     * {@code POST  /impact-matrices} : Create a new impactMatrix.
     *
     * @param impactMatrix the impactMatrix to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new impactMatrix, or with status {@code 400 (Bad Request)} if the impactMatrix has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/impact-matrices")
    public ResponseEntity<ImpactMatrix> createImpactMatrix(@RequestBody ImpactMatrix impactMatrix) throws URISyntaxException {
        log.debug("REST request to save ImpactMatrix : {}", impactMatrix);
        if (impactMatrix.getId() != null) {
            throw new BadRequestAlertException("A new impactMatrix cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ImpactMatrix result = impactMatrixRepository.save(impactMatrix);
        return ResponseEntity
            .created(new URI("/api/impact-matrices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /impact-matrices/:id} : Updates an existing impactMatrix.
     *
     * @param id the id of the impactMatrix to save.
     * @param impactMatrix the impactMatrix to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated impactMatrix,
     * or with status {@code 400 (Bad Request)} if the impactMatrix is not valid,
     * or with status {@code 500 (Internal Server Error)} if the impactMatrix couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/impact-matrices/{id}")
    public ResponseEntity<ImpactMatrix> updateImpactMatrix(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ImpactMatrix impactMatrix
    ) throws URISyntaxException {
        log.debug("REST request to update ImpactMatrix : {}, {}", id, impactMatrix);
        if (impactMatrix.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, impactMatrix.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!impactMatrixRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ImpactMatrix result = impactMatrixRepository.save(impactMatrix);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, impactMatrix.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /impact-matrices/:id} : Partial updates given fields of an existing impactMatrix, field will ignore if it is null
     *
     * @param id the id of the impactMatrix to save.
     * @param impactMatrix the impactMatrix to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated impactMatrix,
     * or with status {@code 400 (Bad Request)} if the impactMatrix is not valid,
     * or with status {@code 404 (Not Found)} if the impactMatrix is not found,
     * or with status {@code 500 (Internal Server Error)} if the impactMatrix couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/impact-matrices/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ImpactMatrix> partialUpdateImpactMatrix(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ImpactMatrix impactMatrix
    ) throws URISyntaxException {
        log.debug("REST request to partial update ImpactMatrix partially : {}, {}", id, impactMatrix);
        if (impactMatrix.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, impactMatrix.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!impactMatrixRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ImpactMatrix> result = impactMatrixRepository
            .findById(impactMatrix.getId())
            .map(existingImpactMatrix -> {
                if (impactMatrix.getImpactMatrixNumber() != null) {
                    existingImpactMatrix.setImpactMatrixNumber(impactMatrix.getImpactMatrixNumber());
                }
                if (impactMatrix.getStatus() != null) {
                    existingImpactMatrix.setStatus(impactMatrix.getStatus());
                }
                if (impactMatrix.getRevision() != null) {
                    existingImpactMatrix.setRevision(impactMatrix.getRevision());
                }
                if (impactMatrix.getReviser() != null) {
                    existingImpactMatrix.setReviser(impactMatrix.getReviser());
                }
                if (impactMatrix.getRevisionDescription() != null) {
                    existingImpactMatrix.setRevisionDescription(impactMatrix.getRevisionDescription());
                }
                if (impactMatrix.getDateRevised() != null) {
                    existingImpactMatrix.setDateRevised(impactMatrix.getDateRevised());
                }
                if (impactMatrix.getTitle() != null) {
                    existingImpactMatrix.setTitle(impactMatrix.getTitle());
                }
                if (impactMatrix.getIsAutoLayoutEnabled() != null) {
                    existingImpactMatrix.setIsAutoLayoutEnabled(impactMatrix.getIsAutoLayoutEnabled());
                }

                return existingImpactMatrix;
            })
            .map(impactMatrixRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, impactMatrix.getId().toString())
        );
    }

    /**
     * {@code GET  /impact-matrices} : get all the impactMatrices.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of impactMatrices in body.
     */
    @GetMapping("/impact-matrices")
    public List<ImpactMatrix> getAllImpactMatrices() {
        log.debug("REST request to get all ImpactMatrices");
        return impactMatrixRepository.findAll();
    }

    /**
     * {@code GET  /impact-matrices/:id} : get the "id" impactMatrix.
     *
     * @param id the id of the impactMatrix to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the impactMatrix, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/impact-matrices/{id}")
    public ResponseEntity<ImpactMatrix> getImpactMatrix(@PathVariable Long id) {
        log.debug("REST request to get ImpactMatrix : {}", id);
        Optional<ImpactMatrix> impactMatrix = impactMatrixRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(impactMatrix);
    }

    /**
     * {@code DELETE  /impact-matrices/:id} : delete the "id" impactMatrix.
     *
     * @param id the id of the impactMatrix to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/impact-matrices/{id}")
    public ResponseEntity<Void> deleteImpactMatrix(@PathVariable Long id) {
        log.debug("REST request to delete ImpactMatrix : {}", id);
        impactMatrixRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
