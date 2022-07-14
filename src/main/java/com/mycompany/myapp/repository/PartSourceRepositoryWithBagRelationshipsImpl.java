package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.PartSource;
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
public class PartSourceRepositoryWithBagRelationshipsImpl implements PartSourceRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<PartSource> fetchBagRelationships(Optional<PartSource> partSource) {
        return partSource.map(this::fetchPlantSpecifics);
    }

    @Override
    public Page<PartSource> fetchBagRelationships(Page<PartSource> partSources) {
        return new PageImpl<>(fetchBagRelationships(partSources.getContent()), partSources.getPageable(), partSources.getTotalElements());
    }

    @Override
    public List<PartSource> fetchBagRelationships(List<PartSource> partSources) {
        return Optional.of(partSources).map(this::fetchPlantSpecifics).orElse(Collections.emptyList());
    }

    PartSource fetchPlantSpecifics(PartSource result) {
        return entityManager
            .createQuery(
                "select partSource from PartSource partSource left join fetch partSource.plantSpecifics where partSource is :partSource",
                PartSource.class
            )
            .setParameter("partSource", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<PartSource> fetchPlantSpecifics(List<PartSource> partSources) {
        return entityManager
            .createQuery(
                "select distinct partSource from PartSource partSource left join fetch partSource.plantSpecifics where partSource in :partSources",
                PartSource.class
            )
            .setParameter("partSources", partSources)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
