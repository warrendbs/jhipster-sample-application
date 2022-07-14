package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.PartSource;
import com.mycompany.myapp.repository.PartSourceRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.PartSource}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PartSourceResource {

    private final Logger log = LoggerFactory.getLogger(PartSourceResource.class);

    private static final String ENTITY_NAME = "jhipsterSampleApplicationPartSource";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PartSourceRepository partSourceRepository;

    public PartSourceResource(PartSourceRepository partSourceRepository) {
        this.partSourceRepository = partSourceRepository;
    }

    /**
     * {@code POST  /part-sources} : Create a new partSource.
     *
     * @param partSource the partSource to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new partSource, or with status {@code 400 (Bad Request)} if the partSource has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/part-sources")
    public ResponseEntity<PartSource> createPartSource(@RequestBody PartSource partSource) throws URISyntaxException {
        log.debug("REST request to save PartSource : {}", partSource);
        if (partSource.getId() != null) {
            throw new BadRequestAlertException("A new partSource cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PartSource result = partSourceRepository.save(partSource);
        return ResponseEntity
            .created(new URI("/api/part-sources/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /part-sources/:id} : Updates an existing partSource.
     *
     * @param id the id of the partSource to save.
     * @param partSource the partSource to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated partSource,
     * or with status {@code 400 (Bad Request)} if the partSource is not valid,
     * or with status {@code 500 (Internal Server Error)} if the partSource couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/part-sources/{id}")
    public ResponseEntity<PartSource> updatePartSource(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PartSource partSource
    ) throws URISyntaxException {
        log.debug("REST request to update PartSource : {}, {}", id, partSource);
        if (partSource.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, partSource.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!partSourceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PartSource result = partSourceRepository.save(partSource);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, partSource.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /part-sources/:id} : Partial updates given fields of an existing partSource, field will ignore if it is null
     *
     * @param id the id of the partSource to save.
     * @param partSource the partSource to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated partSource,
     * or with status {@code 400 (Bad Request)} if the partSource is not valid,
     * or with status {@code 404 (Not Found)} if the partSource is not found,
     * or with status {@code 500 (Internal Server Error)} if the partSource couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/part-sources/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PartSource> partialUpdatePartSource(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PartSource partSource
    ) throws URISyntaxException {
        log.debug("REST request to partial update PartSource partially : {}, {}", id, partSource);
        if (partSource.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, partSource.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!partSourceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PartSource> result = partSourceRepository
            .findById(partSource.getId())
            .map(existingPartSource -> {
                if (partSource.getProductId() != null) {
                    existingPartSource.setProductId(partSource.getProductId());
                }
                if (partSource.getRevision() != null) {
                    existingPartSource.setRevision(partSource.getRevision());
                }
                if (partSource.getName() != null) {
                    existingPartSource.setName(partSource.getName());
                }
                if (partSource.getDescription() != null) {
                    existingPartSource.setDescription(partSource.getDescription());
                }
                if (partSource.getVqi() != null) {
                    existingPartSource.setVqi(partSource.getVqi());
                }
                if (partSource.getProcurementType() != null) {
                    existingPartSource.setProcurementType(partSource.getProcurementType());
                }
                if (partSource.getMaterialType() != null) {
                    existingPartSource.setMaterialType(partSource.getMaterialType());
                }
                if (partSource.getSerialNumberProfile() != null) {
                    existingPartSource.setSerialNumberProfile(partSource.getSerialNumberProfile());
                }
                if (partSource.getCriticalConfigurationItemIndicator() != null) {
                    existingPartSource.setCriticalConfigurationItemIndicator(partSource.getCriticalConfigurationItemIndicator());
                }
                if (partSource.getRegularPartIndicator() != null) {
                    existingPartSource.setRegularPartIndicator(partSource.getRegularPartIndicator());
                }
                if (partSource.getHistoryIndicator() != null) {
                    existingPartSource.setHistoryIndicator(partSource.getHistoryIndicator());
                }
                if (partSource.getCrossPlantStatus() != null) {
                    existingPartSource.setCrossPlantStatus(partSource.getCrossPlantStatus());
                }
                if (partSource.getCrossPlantStatusToBe() != null) {
                    existingPartSource.setCrossPlantStatusToBe(partSource.getCrossPlantStatusToBe());
                }
                if (partSource.getToolPackCategory() != null) {
                    existingPartSource.setToolPackCategory(partSource.getToolPackCategory());
                }
                if (partSource.getTcChangeControl() != null) {
                    existingPartSource.setTcChangeControl(partSource.getTcChangeControl());
                }
                if (partSource.getSapChangeControl() != null) {
                    existingPartSource.setSapChangeControl(partSource.getSapChangeControl());
                }
                if (partSource.getAllowBomRestructuring() != null) {
                    existingPartSource.setAllowBomRestructuring(partSource.getAllowBomRestructuring());
                }
                if (partSource.getUnitOfMeasure() != null) {
                    existingPartSource.setUnitOfMeasure(partSource.getUnitOfMeasure());
                }
                if (partSource.getItemUsage() != null) {
                    existingPartSource.setItemUsage(partSource.getItemUsage());
                }
                if (partSource.getIsPhantom() != null) {
                    existingPartSource.setIsPhantom(partSource.getIsPhantom());
                }
                if (partSource.getFailureRate() != null) {
                    existingPartSource.setFailureRate(partSource.getFailureRate());
                }
                if (partSource.getInHouseProductionTime() != null) {
                    existingPartSource.setInHouseProductionTime(partSource.getInHouseProductionTime());
                }
                if (partSource.getSlAbcCode() != null) {
                    existingPartSource.setSlAbcCode(partSource.getSlAbcCode());
                }
                if (partSource.getProductionPlant() != null) {
                    existingPartSource.setProductionPlant(partSource.getProductionPlant());
                }
                if (partSource.getLimitedDriving12Nc() != null) {
                    existingPartSource.setLimitedDriving12Nc(partSource.getLimitedDriving12Nc());
                }
                if (partSource.getLimitedDriving12Ncflag() != null) {
                    existingPartSource.setLimitedDriving12Ncflag(partSource.getLimitedDriving12Ncflag());
                }
                if (partSource.getMultiPlant() != null) {
                    existingPartSource.setMultiPlant(partSource.getMultiPlant());
                }
                if (partSource.getType() != null) {
                    existingPartSource.setType(partSource.getType());
                }
                if (partSource.getSuccessorPartId() != null) {
                    existingPartSource.setSuccessorPartId(partSource.getSuccessorPartId());
                }

                return existingPartSource;
            })
            .map(partSourceRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, partSource.getId().toString())
        );
    }

    /**
     * {@code GET  /part-sources} : get all the partSources.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of partSources in body.
     */
    @GetMapping("/part-sources")
    public List<PartSource> getAllPartSources(
        @RequestParam(required = false) String filter,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        if ("part-is-null".equals(filter)) {
            log.debug("REST request to get all PartSources where part is null");
            return StreamSupport
                .stream(partSourceRepository.findAll().spliterator(), false)
                .filter(partSource -> partSource.getPart() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all PartSources");
        return partSourceRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /part-sources/:id} : get the "id" partSource.
     *
     * @param id the id of the partSource to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the partSource, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/part-sources/{id}")
    public ResponseEntity<PartSource> getPartSource(@PathVariable Long id) {
        log.debug("REST request to get PartSource : {}", id);
        Optional<PartSource> partSource = partSourceRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(partSource);
    }

    /**
     * {@code DELETE  /part-sources/:id} : delete the "id" partSource.
     *
     * @param id the id of the partSource to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/part-sources/{id}")
    public ResponseEntity<Void> deletePartSource(@PathVariable Long id) {
        log.debug("REST request to delete PartSource : {}", id);
        partSourceRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
