package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.PartIntention;
import com.mycompany.myapp.repository.PartIntentionRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.PartIntention}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PartIntentionResource {

    private final Logger log = LoggerFactory.getLogger(PartIntentionResource.class);

    private static final String ENTITY_NAME = "jhipsterSampleApplicationPartIntention";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PartIntentionRepository partIntentionRepository;

    public PartIntentionResource(PartIntentionRepository partIntentionRepository) {
        this.partIntentionRepository = partIntentionRepository;
    }

    /**
     * {@code POST  /part-intentions} : Create a new partIntention.
     *
     * @param partIntention the partIntention to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new partIntention, or with status {@code 400 (Bad Request)} if the partIntention has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/part-intentions")
    public ResponseEntity<PartIntention> createPartIntention(@RequestBody PartIntention partIntention) throws URISyntaxException {
        log.debug("REST request to save PartIntention : {}", partIntention);
        if (partIntention.getId() != null) {
            throw new BadRequestAlertException("A new partIntention cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PartIntention result = partIntentionRepository.save(partIntention);
        return ResponseEntity
            .created(new URI("/api/part-intentions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /part-intentions/:id} : Updates an existing partIntention.
     *
     * @param id the id of the partIntention to save.
     * @param partIntention the partIntention to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated partIntention,
     * or with status {@code 400 (Bad Request)} if the partIntention is not valid,
     * or with status {@code 500 (Internal Server Error)} if the partIntention couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/part-intentions/{id}")
    public ResponseEntity<PartIntention> updatePartIntention(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PartIntention partIntention
    ) throws URISyntaxException {
        log.debug("REST request to update PartIntention : {}, {}", id, partIntention);
        if (partIntention.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, partIntention.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!partIntentionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PartIntention result = partIntentionRepository.save(partIntention);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, partIntention.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /part-intentions/:id} : Partial updates given fields of an existing partIntention, field will ignore if it is null
     *
     * @param id the id of the partIntention to save.
     * @param partIntention the partIntention to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated partIntention,
     * or with status {@code 400 (Bad Request)} if the partIntention is not valid,
     * or with status {@code 404 (Not Found)} if the partIntention is not found,
     * or with status {@code 500 (Internal Server Error)} if the partIntention couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/part-intentions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PartIntention> partialUpdatePartIntention(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PartIntention partIntention
    ) throws URISyntaxException {
        log.debug("REST request to partial update PartIntention partially : {}, {}", id, partIntention);
        if (partIntention.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, partIntention.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!partIntentionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PartIntention> result = partIntentionRepository
            .findById(partIntention.getId())
            .map(existingPartIntention -> {
                if (partIntention.getProductId() != null) {
                    existingPartIntention.setProductId(partIntention.getProductId());
                }
                if (partIntention.getRevision() != null) {
                    existingPartIntention.setRevision(partIntention.getRevision());
                }
                if (partIntention.getName() != null) {
                    existingPartIntention.setName(partIntention.getName());
                }
                if (partIntention.getDescription() != null) {
                    existingPartIntention.setDescription(partIntention.getDescription());
                }
                if (partIntention.getVqi() != null) {
                    existingPartIntention.setVqi(partIntention.getVqi());
                }
                if (partIntention.getProcurementType() != null) {
                    existingPartIntention.setProcurementType(partIntention.getProcurementType());
                }
                if (partIntention.getMaterialType() != null) {
                    existingPartIntention.setMaterialType(partIntention.getMaterialType());
                }
                if (partIntention.getSerialNumberProfile() != null) {
                    existingPartIntention.setSerialNumberProfile(partIntention.getSerialNumberProfile());
                }
                if (partIntention.getCriticalConfigurationItemIndicator() != null) {
                    existingPartIntention.setCriticalConfigurationItemIndicator(partIntention.getCriticalConfigurationItemIndicator());
                }
                if (partIntention.getRegularPartIndicator() != null) {
                    existingPartIntention.setRegularPartIndicator(partIntention.getRegularPartIndicator());
                }
                if (partIntention.getHistoryIndicator() != null) {
                    existingPartIntention.setHistoryIndicator(partIntention.getHistoryIndicator());
                }
                if (partIntention.getCrossPlantStatus() != null) {
                    existingPartIntention.setCrossPlantStatus(partIntention.getCrossPlantStatus());
                }
                if (partIntention.getCrossPlantStatusToBe() != null) {
                    existingPartIntention.setCrossPlantStatusToBe(partIntention.getCrossPlantStatusToBe());
                }
                if (partIntention.getToolPackCategory() != null) {
                    existingPartIntention.setToolPackCategory(partIntention.getToolPackCategory());
                }
                if (partIntention.getTcChangeControl() != null) {
                    existingPartIntention.setTcChangeControl(partIntention.getTcChangeControl());
                }
                if (partIntention.getSapChangeControl() != null) {
                    existingPartIntention.setSapChangeControl(partIntention.getSapChangeControl());
                }
                if (partIntention.getAllowBomRestructuring() != null) {
                    existingPartIntention.setAllowBomRestructuring(partIntention.getAllowBomRestructuring());
                }
                if (partIntention.getUnitOfMeasure() != null) {
                    existingPartIntention.setUnitOfMeasure(partIntention.getUnitOfMeasure());
                }
                if (partIntention.getItemUsage() != null) {
                    existingPartIntention.setItemUsage(partIntention.getItemUsage());
                }
                if (partIntention.getIsPhantom() != null) {
                    existingPartIntention.setIsPhantom(partIntention.getIsPhantom());
                }
                if (partIntention.getFailureRate() != null) {
                    existingPartIntention.setFailureRate(partIntention.getFailureRate());
                }
                if (partIntention.getInHouseProductionTime() != null) {
                    existingPartIntention.setInHouseProductionTime(partIntention.getInHouseProductionTime());
                }
                if (partIntention.getSlAbcCode() != null) {
                    existingPartIntention.setSlAbcCode(partIntention.getSlAbcCode());
                }
                if (partIntention.getProductionPlant() != null) {
                    existingPartIntention.setProductionPlant(partIntention.getProductionPlant());
                }
                if (partIntention.getLimitedDriving12Nc() != null) {
                    existingPartIntention.setLimitedDriving12Nc(partIntention.getLimitedDriving12Nc());
                }
                if (partIntention.getLimitedDriving12Ncflag() != null) {
                    existingPartIntention.setLimitedDriving12Ncflag(partIntention.getLimitedDriving12Ncflag());
                }
                if (partIntention.getMultiPlant() != null) {
                    existingPartIntention.setMultiPlant(partIntention.getMultiPlant());
                }
                if (partIntention.getType() != null) {
                    existingPartIntention.setType(partIntention.getType());
                }
                if (partIntention.getSuccessorPartId() != null) {
                    existingPartIntention.setSuccessorPartId(partIntention.getSuccessorPartId());
                }

                return existingPartIntention;
            })
            .map(partIntentionRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, partIntention.getId().toString())
        );
    }

    /**
     * {@code GET  /part-intentions} : get all the partIntentions.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of partIntentions in body.
     */
    @GetMapping("/part-intentions")
    public List<PartIntention> getAllPartIntentions(
        @RequestParam(required = false) String filter,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        if ("part-is-null".equals(filter)) {
            log.debug("REST request to get all PartIntentions where part is null");
            return StreamSupport
                .stream(partIntentionRepository.findAll().spliterator(), false)
                .filter(partIntention -> partIntention.getPart() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all PartIntentions");
        return partIntentionRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /part-intentions/:id} : get the "id" partIntention.
     *
     * @param id the id of the partIntention to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the partIntention, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/part-intentions/{id}")
    public ResponseEntity<PartIntention> getPartIntention(@PathVariable Long id) {
        log.debug("REST request to get PartIntention : {}", id);
        Optional<PartIntention> partIntention = partIntentionRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(partIntention);
    }

    /**
     * {@code DELETE  /part-intentions/:id} : delete the "id" partIntention.
     *
     * @param id the id of the partIntention to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/part-intentions/{id}")
    public ResponseEntity<Void> deletePartIntention(@PathVariable Long id) {
        log.debug("REST request to delete PartIntention : {}", id);
        partIntentionRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
