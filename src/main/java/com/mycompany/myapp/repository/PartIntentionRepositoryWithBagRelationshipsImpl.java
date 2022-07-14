package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.PartIntention;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class PartIntentionRepositoryWithBagRelationshipsImpl implements PartIntentionRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<PartIntention> fetchBagRelationships(Optional<PartIntention> partIntention) {
        return partIntention.map(this::fetchPlantSpecifics).map(this::fetchItemReferences).map(this::fetchReleasePackages);
    }

    @Override
    public Page<PartIntention> fetchBagRelationships(Page<PartIntention> partIntentions) {
        return new PageImpl<>(
            fetchBagRelationships(partIntentions.getContent()),
            partIntentions.getPageable(),
            partIntentions.getTotalElements()
        );
    }

    @Override
    public List<PartIntention> fetchBagRelationships(List<PartIntention> partIntentions) {
        return Optional
            .of(partIntentions)
            .map(this::fetchPlantSpecifics)
            .map(this::fetchItemReferences)
            .map(this::fetchReleasePackages)
            .orElse(Collections.emptyList());
    }

    PartIntention fetchPlantSpecifics(PartIntention result) {
        return entityManager
            .createQuery(
                "select partIntention from PartIntention partIntention left join fetch partIntention.plantSpecifics where partIntention is :partIntention",
                PartIntention.class
            )
            .setParameter("partIntention", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<PartIntention> fetchPlantSpecifics(List<PartIntention> partIntentions) {
        return entityManager
            .createQuery(
                "select distinct partIntention from PartIntention partIntention left join fetch partIntention.plantSpecifics where partIntention in :partIntentions",
                PartIntention.class
            )
            .setParameter("partIntentions", partIntentions)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }

    PartIntention fetchItemReferences(PartIntention result) {
        return entityManager
            .createQuery(
                "select partIntention from PartIntention partIntention left join fetch partIntention.itemReferences where partIntention is :partIntention",
                PartIntention.class
            )
            .setParameter("partIntention", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<PartIntention> fetchItemReferences(List<PartIntention> partIntentions) {
        return entityManager
            .createQuery(
                "select distinct partIntention from PartIntention partIntention left join fetch partIntention.itemReferences where partIntention in :partIntentions",
                PartIntention.class
            )
            .setParameter("partIntentions", partIntentions)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }

    PartIntention fetchReleasePackages(PartIntention result) {
        return entityManager
            .createQuery(
                "select partIntention from PartIntention partIntention left join fetch partIntention.releasePackages where partIntention is :partIntention",
                PartIntention.class
            )
            .setParameter("partIntention", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<PartIntention> fetchReleasePackages(List<PartIntention> partIntentions) {
        return entityManager
            .createQuery(
                "select distinct partIntention from PartIntention partIntention left join fetch partIntention.releasePackages where partIntention in :partIntentions",
                PartIntention.class
            )
            .setParameter("partIntentions", partIntentions)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
