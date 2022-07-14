package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ReleasePackage;
import com.mycompany.myapp.repository.ReleasePackageRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.ReleasePackage}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ReleasePackageResource {

    private final Logger log = LoggerFactory.getLogger(ReleasePackageResource.class);

    private static final String ENTITY_NAME = "jhipsterSampleApplicationReleasePackage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReleasePackageRepository releasePackageRepository;

    public ReleasePackageResource(ReleasePackageRepository releasePackageRepository) {
        this.releasePackageRepository = releasePackageRepository;
    }

    /**
     * {@code POST  /release-packages} : Create a new releasePackage.
     *
     * @param releasePackage the releasePackage to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new releasePackage, or with status {@code 400 (Bad Request)} if the releasePackage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/release-packages")
    public ResponseEntity<ReleasePackage> createReleasePackage(@RequestBody ReleasePackage releasePackage) throws URISyntaxException {
        log.debug("REST request to save ReleasePackage : {}", releasePackage);
        if (releasePackage.getId() != null) {
            throw new BadRequestAlertException("A new releasePackage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReleasePackage result = releasePackageRepository.save(releasePackage);
        return ResponseEntity
            .created(new URI("/api/release-packages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /release-packages/:id} : Updates an existing releasePackage.
     *
     * @param id the id of the releasePackage to save.
     * @param releasePackage the releasePackage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated releasePackage,
     * or with status {@code 400 (Bad Request)} if the releasePackage is not valid,
     * or with status {@code 500 (Internal Server Error)} if the releasePackage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/release-packages/{id}")
    public ResponseEntity<ReleasePackage> updateReleasePackage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReleasePackage releasePackage
    ) throws URISyntaxException {
        log.debug("REST request to update ReleasePackage : {}, {}", id, releasePackage);
        if (releasePackage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, releasePackage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!releasePackageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ReleasePackage result = releasePackageRepository.save(releasePackage);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, releasePackage.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /release-packages/:id} : Partial updates given fields of an existing releasePackage, field will ignore if it is null
     *
     * @param id the id of the releasePackage to save.
     * @param releasePackage the releasePackage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated releasePackage,
     * or with status {@code 400 (Bad Request)} if the releasePackage is not valid,
     * or with status {@code 404 (Not Found)} if the releasePackage is not found,
     * or with status {@code 500 (Internal Server Error)} if the releasePackage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/release-packages/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReleasePackage> partialUpdateReleasePackage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReleasePackage releasePackage
    ) throws URISyntaxException {
        log.debug("REST request to partial update ReleasePackage partially : {}, {}", id, releasePackage);
        if (releasePackage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, releasePackage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!releasePackageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReleasePackage> result = releasePackageRepository
            .findById(releasePackage.getId())
            .map(existingReleasePackage -> {
                if (releasePackage.getTitle() != null) {
                    existingReleasePackage.setTitle(releasePackage.getTitle());
                }
                if (releasePackage.getReleasePackageNumber() != null) {
                    existingReleasePackage.setReleasePackageNumber(releasePackage.getReleasePackageNumber());
                }
                if (releasePackage.getReleasePackageTitle() != null) {
                    existingReleasePackage.setReleasePackageTitle(releasePackage.getReleasePackageTitle());
                }
                if (releasePackage.getStatus() != null) {
                    existingReleasePackage.setStatus(releasePackage.getStatus());
                }
                if (releasePackage.getEcn() != null) {
                    existingReleasePackage.setEcn(releasePackage.getEcn());
                }

                return existingReleasePackage;
            })
            .map(releasePackageRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, releasePackage.getId().toString())
        );
    }

    /**
     * {@code GET  /release-packages} : get all the releasePackages.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of releasePackages in body.
     */
    @GetMapping("/release-packages")
    public List<ReleasePackage> getAllReleasePackages() {
        log.debug("REST request to get all ReleasePackages");
        return releasePackageRepository.findAll();
    }

    /**
     * {@code GET  /release-packages/:id} : get the "id" releasePackage.
     *
     * @param id the id of the releasePackage to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the releasePackage, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/release-packages/{id}")
    public ResponseEntity<ReleasePackage> getReleasePackage(@PathVariable Long id) {
        log.debug("REST request to get ReleasePackage : {}", id);
        Optional<ReleasePackage> releasePackage = releasePackageRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(releasePackage);
    }

    /**
     * {@code DELETE  /release-packages/:id} : delete the "id" releasePackage.
     *
     * @param id the id of the releasePackage to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/release-packages/{id}")
    public ResponseEntity<Void> deleteReleasePackage(@PathVariable Long id) {
        log.debug("REST request to delete ReleasePackage : {}", id);
        releasePackageRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
