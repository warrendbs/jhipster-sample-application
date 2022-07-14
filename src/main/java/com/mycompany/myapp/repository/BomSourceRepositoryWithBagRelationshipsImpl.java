package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.BomSource;
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
public class BomSourceRepositoryWithBagRelationshipsImpl implements BomSourceRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<BomSource> fetchBagRelationships(Optional<BomSource> bomSource) {
        return bomSource.map(this::fetchBomChildren);
    }

    @Override
    public Page<BomSource> fetchBagRelationships(Page<BomSource> bomSources) {
        return new PageImpl<>(fetchBagRelationships(bomSources.getContent()), bomSources.getPageable(), bomSources.getTotalElements());
    }

    @Override
    public List<BomSource> fetchBagRelationships(List<BomSource> bomSources) {
        return Optional.of(bomSources).map(this::fetchBomChildren).orElse(Collections.emptyList());
    }

    BomSource fetchBomChildren(BomSource result) {
        return entityManager
            .createQuery(
                "select bomSource from BomSource bomSource left join fetch bomSource.bomChildren where bomSource is :bomSource",
                BomSource.class
            )
            .setParameter("bomSource", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<BomSource> fetchBomChildren(List<BomSource> bomSources) {
        return entityManager
            .createQuery(
                "select distinct bomSource from BomSource bomSource left join fetch bomSource.bomChildren where bomSource in :bomSources",
                BomSource.class
            )
            .setParameter("bomSources", bomSources)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
