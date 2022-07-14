package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.PlantSpecific;
import com.mycompany.myapp.repository.PlantSpecificRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.PlantSpecific}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PlantSpecificResource {

    private final Logger log = LoggerFactory.getLogger(PlantSpecificResource.class);

    private static final String ENTITY_NAME = "jhipsterSampleApplicationPlantSpecific";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlantSpecificRepository plantSpecificRepository;

    public PlantSpecificResource(PlantSpecificRepository plantSpecificRepository) {
        this.plantSpecificRepository = plantSpecificRepository;
    }

    /**
     * {@code POST  /plant-specifics} : Create a new plantSpecific.
     *
     * @param plantSpecific the plantSpecific to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new plantSpecific, or with status {@code 400 (Bad Request)} if the plantSpecific has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/plant-specifics")
    public ResponseEntity<PlantSpecific> createPlantSpecific(@RequestBody PlantSpecific plantSpecific) throws URISyntaxException {
        log.debug("REST request to save PlantSpecific : {}", plantSpecific);
        if (plantSpecific.getId() != null) {
            throw new BadRequestAlertException("A new plantSpecific cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PlantSpecific result = plantSpecificRepository.save(plantSpecific);
        return ResponseEntity
            .created(new URI("/api/plant-specifics/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /plant-specifics/:id} : Updates an existing plantSpecific.
     *
     * @param id the id of the plantSpecific to save.
     * @param plantSpecific the plantSpecific to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated plantSpecific,
     * or with status {@code 400 (Bad Request)} if the plantSpecific is not valid,
     * or with status {@code 500 (Internal Server Error)} if the plantSpecific couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/plant-specifics/{id}")
    public ResponseEntity<PlantSpecific> updatePlantSpecific(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PlantSpecific plantSpecific
    ) throws URISyntaxException {
        log.debug("REST request to update PlantSpecific : {}, {}", id, plantSpecific);
        if (plantSpecific.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, plantSpecific.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!plantSpecificRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PlantSpecific result = plantSpecificRepository.save(plantSpecific);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, plantSpecific.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /plant-specifics/:id} : Partial updates given fields of an existing plantSpecific, field will ignore if it is null
     *
     * @param id the id of the plantSpecific to save.
     * @param plantSpecific the plantSpecific to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated plantSpecific,
     * or with status {@code 400 (Bad Request)} if the plantSpecific is not valid,
     * or with status {@code 404 (Not Found)} if the plantSpecific is not found,
     * or with status {@code 500 (Internal Server Error)} if the plantSpecific couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/plant-specifics/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PlantSpecific> partialUpdatePlantSpecific(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PlantSpecific plantSpecific
    ) throws URISyntaxException {
        log.debug("REST request to partial update PlantSpecific partially : {}, {}", id, plantSpecific);
        if (plantSpecific.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, plantSpecific.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!plantSpecificRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PlantSpecific> result = plantSpecificRepository
            .findById(plantSpecific.getId())
            .map(existingPlantSpecific -> {
                if (plantSpecific.getObjectDependancy() != null) {
                    existingPlantSpecific.setObjectDependancy(plantSpecific.getObjectDependancy());
                }
                if (plantSpecific.getRefMaterial() != null) {
                    existingPlantSpecific.setRefMaterial(plantSpecific.getRefMaterial());
                }
                if (plantSpecific.getIsDiscontinued() != null) {
                    existingPlantSpecific.setIsDiscontinued(plantSpecific.getIsDiscontinued());
                }

                return existingPlantSpecific;
            })
            .map(plantSpecificRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, plantSpecific.getId().toString())
        );
    }

    /**
     * {@code GET  /plant-specifics} : get all the plantSpecifics.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of plantSpecifics in body.
     */
    @GetMapping("/plant-specifics")
    public List<PlantSpecific> getAllPlantSpecifics() {
        log.debug("REST request to get all PlantSpecifics");
        return plantSpecificRepository.findAll();
    }

    /**
     * {@code GET  /plant-specifics/:id} : get the "id" plantSpecific.
     *
     * @param id the id of the plantSpecific to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the plantSpecific, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/plant-specifics/{id}")
    public ResponseEntity<PlantSpecific> getPlantSpecific(@PathVariable Long id) {
        log.debug("REST request to get PlantSpecific : {}", id);
        Optional<PlantSpecific> plantSpecific = plantSpecificRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(plantSpecific);
    }

    /**
     * {@code DELETE  /plant-specifics/:id} : delete the "id" plantSpecific.
     *
     * @param id the id of the plantSpecific to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/plant-specifics/{id}")
    public ResponseEntity<Void> deletePlantSpecific(@PathVariable Long id) {
        log.debug("REST request to delete PlantSpecific : {}", id);
        plantSpecificRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
