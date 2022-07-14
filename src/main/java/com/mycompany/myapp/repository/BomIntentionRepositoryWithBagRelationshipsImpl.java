package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.BomIntention;
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
public class BomIntentionRepositoryWithBagRelationshipsImpl implements BomIntentionRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<BomIntention> fetchBagRelationships(Optional<BomIntention> bomIntention) {
        return bomIntention.map(this::fetchBomChildren).map(this::fetchReleasePackages);
    }

    @Override
    public Page<BomIntention> fetchBagRelationships(Page<BomIntention> bomIntentions) {
        return new PageImpl<>(
            fetchBagRelationships(bomIntentions.getContent()),
            bomIntentions.getPageable(),
            bomIntentions.getTotalElements()
        );
    }

    @Override
    public List<BomIntention> fetchBagRelationships(List<BomIntention> bomIntentions) {
        return Optional.of(bomIntentions).map(this::fetchBomChildren).map(this::fetchReleasePackages).orElse(Collections.emptyList());
    }

    BomIntention fetchBomChildren(BomIntention result) {
        return entityManager
            .createQuery(
                "select bomIntention from BomIntention bomIntention left join fetch bomIntention.bomChildren where bomIntention is :bomIntention",
                BomIntention.class
            )
            .setParameter("bomIntention", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<BomIntention> fetchBomChildren(List<BomIntention> bomIntentions) {
        return entityManager
            .createQuery(
                "select distinct bomIntention from BomIntention bomIntention left join fetch bomIntention.bomChildren where bomIntention in :bomIntentions",
                BomIntention.class
            )
            .setParameter("bomIntentions", bomIntentions)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }

    BomIntention fetchReleasePackages(BomIntention result) {
        return entityManager
            .createQuery(
                "select bomIntention from BomIntention bomIntention left join fetch bomIntention.releasePackages where bomIntention is :bomIntention",
                BomIntention.class
            )
            .setParameter("bomIntention", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<BomIntention> fetchReleasePackages(List<BomIntention> bomIntentions) {
        return entityManager
            .createQuery(
                "select distinct bomIntention from BomIntention bomIntention left join fetch bomIntention.releasePackages where bomIntention in :bomIntentions",
                BomIntention.class
            )
            .setParameter("bomIntentions", bomIntentions)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
