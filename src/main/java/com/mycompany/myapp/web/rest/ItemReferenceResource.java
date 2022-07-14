package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ItemReference;
import com.mycompany.myapp.repository.ItemReferenceRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.ItemReference}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ItemReferenceResource {

    private final Logger log = LoggerFactory.getLogger(ItemReferenceResource.class);

    private static final String ENTITY_NAME = "jhipsterSampleApplicationItemReference";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ItemReferenceRepository itemReferenceRepository;

    public ItemReferenceResource(ItemReferenceRepository itemReferenceRepository) {
        this.itemReferenceRepository = itemReferenceRepository;
    }

    /**
     * {@code POST  /item-references} : Create a new itemReference.
     *
     * @param itemReference the itemReference to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new itemReference, or with status {@code 400 (Bad Request)} if the itemReference has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/item-references")
    public ResponseEntity<ItemReference> createItemReference(@RequestBody ItemReference itemReference) throws URISyntaxException {
        log.debug("REST request to save ItemReference : {}", itemReference);
        if (itemReference.getId() != null) {
            throw new BadRequestAlertException("A new itemReference cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ItemReference result = itemReferenceRepository.save(itemReference);
        return ResponseEntity
            .created(new URI("/api/item-references/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /item-references/:id} : Updates an existing itemReference.
     *
     * @param id the id of the itemReference to save.
     * @param itemReference the itemReference to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemReference,
     * or with status {@code 400 (Bad Request)} if the itemReference is not valid,
     * or with status {@code 500 (Internal Server Error)} if the itemReference couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/item-references/{id}")
    public ResponseEntity<ItemReference> updateItemReference(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ItemReference itemReference
    ) throws URISyntaxException {
        log.debug("REST request to update ItemReference : {}, {}", id, itemReference);
        if (itemReference.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemReference.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!itemReferenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ItemReference result = itemReferenceRepository.save(itemReference);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, itemReference.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /item-references/:id} : Partial updates given fields of an existing itemReference, field will ignore if it is null
     *
     * @param id the id of the itemReference to save.
     * @param itemReference the itemReference to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemReference,
     * or with status {@code 400 (Bad Request)} if the itemReference is not valid,
     * or with status {@code 404 (Not Found)} if the itemReference is not found,
     * or with status {@code 500 (Internal Server Error)} if the itemReference couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/item-references/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ItemReference> partialUpdateItemReference(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ItemReference itemReference
    ) throws URISyntaxException {
        log.debug("REST request to partial update ItemReference partially : {}, {}", id, itemReference);
        if (itemReference.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemReference.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!itemReferenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ItemReference> result = itemReferenceRepository
            .findById(itemReference.getId())
            .map(existingItemReference -> {
                if (itemReference.getReferenceId() != null) {
                    existingItemReference.setReferenceId(itemReference.getReferenceId());
                }
                if (itemReference.getType() != null) {
                    existingItemReference.setType(itemReference.getType());
                }

                return existingItemReference;
            })
            .map(itemReferenceRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, itemReference.getId().toString())
        );
    }

    /**
     * {@code GET  /item-references} : get all the itemReferences.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of itemReferences in body.
     */
    @GetMapping("/item-references")
    public List<ItemReference> getAllItemReferences() {
        log.debug("REST request to get all ItemReferences");
        return itemReferenceRepository.findAll();
    }

    /**
     * {@code GET  /item-references/:id} : get the "id" itemReference.
     *
     * @param id the id of the itemReference to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the itemReference, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/item-references/{id}")
    public ResponseEntity<ItemReference> getItemReference(@PathVariable Long id) {
        log.debug("REST request to get ItemReference : {}", id);
        Optional<ItemReference> itemReference = itemReferenceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(itemReference);
    }

    /**
     * {@code DELETE  /item-references/:id} : delete the "id" itemReference.
     *
     * @param id the id of the itemReference to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/item-references/{id}")
    public ResponseEntity<Void> deleteItemReference(@PathVariable Long id) {
        log.debug("REST request to delete ItemReference : {}", id);
        itemReferenceRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
