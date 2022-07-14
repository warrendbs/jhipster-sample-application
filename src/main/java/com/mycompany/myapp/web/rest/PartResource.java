package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Part;
import com.mycompany.myapp.repository.PartRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Part}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PartResource {

    private final Logger log = LoggerFactory.getLogger(PartResource.class);

    private static final String ENTITY_NAME = "jhipsterSampleApplicationPart";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PartRepository partRepository;

    public PartResource(PartRepository partRepository) {
        this.partRepository = partRepository;
    }

    /**
     * {@code POST  /parts} : Create a new part.
     *
     * @param part the part to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new part, or with status {@code 400 (Bad Request)} if the part has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/parts")
    public ResponseEntity<Part> createPart(@RequestBody Part part) throws URISyntaxException {
        log.debug("REST request to save Part : {}", part);
        if (part.getId() != null) {
            throw new BadRequestAlertException("A new part cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Part result = partRepository.save(part);
        return ResponseEntity
            .created(new URI("/api/parts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /parts/:id} : Updates an existing part.
     *
     * @param id the id of the part to save.
     * @param part the part to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated part,
     * or with status {@code 400 (Bad Request)} if the part is not valid,
     * or with status {@code 500 (Internal Server Error)} if the part couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/parts/{id}")
    public ResponseEntity<Part> updatePart(@PathVariable(value = "id", required = false) final Long id, @RequestBody Part part)
        throws URISyntaxException {
        log.debug("REST request to update Part : {}, {}", id, part);
        if (part.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, part.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!partRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Part result = partRepository.save(part);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, part.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /parts/:id} : Partial updates given fields of an existing part, field will ignore if it is null
     *
     * @param id the id of the part to save.
     * @param part the part to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated part,
     * or with status {@code 400 (Bad Request)} if the part is not valid,
     * or with status {@code 404 (Not Found)} if the part is not found,
     * or with status {@code 500 (Internal Server Error)} if the part couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/parts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Part> partialUpdatePart(@PathVariable(value = "id", required = false) final Long id, @RequestBody Part part)
        throws URISyntaxException {
        log.debug("REST request to partial update Part partially : {}, {}", id, part);
        if (part.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, part.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!partRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Part> result = partRepository
            .findById(part.getId())
            .map(existingPart -> {
                if (part.getStatus() != null) {
                    existingPart.setStatus(part.getStatus());
                }
                if (part.getChangeIndication() != null) {
                    existingPart.setChangeIndication(part.getChangeIndication());
                }
                if (part.getIsParentPartBomChanged() != null) {
                    existingPart.setIsParentPartBomChanged(part.getIsParentPartBomChanged());
                }

                return existingPart;
            })
            .map(partRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, part.getId().toString())
        );
    }

    /**
     * {@code GET  /parts} : get all the parts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of parts in body.
     */
    @GetMapping("/parts")
    public List<Part> getAllParts() {
        log.debug("REST request to get all Parts");
        return partRepository.findAll();
    }

    /**
     * {@code GET  /parts/:id} : get the "id" part.
     *
     * @param id the id of the part to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the part, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/parts/{id}")
    public ResponseEntity<Part> getPart(@PathVariable Long id) {
        log.debug("REST request to get Part : {}", id);
        Optional<Part> part = partRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(part);
    }

    /**
     * {@code DELETE  /parts/:id} : delete the "id" part.
     *
     * @param id the id of the part to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/parts/{id}")
    public ResponseEntity<Void> deletePart(@PathVariable Long id) {
        log.debug("REST request to delete Part : {}", id);
        partRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
