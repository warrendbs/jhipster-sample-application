package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Bom;
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
public class BomRepositoryWithBagRelationshipsImpl implements BomRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Bom> fetchBagRelationships(Optional<Bom> bom) {
        return bom.map(this::fetchItemReferences);
    }

    @Override
    public Page<Bom> fetchBagRelationships(Page<Bom> boms) {
        return new PageImpl<>(fetchBagRelationships(boms.getContent()), boms.getPageable(), boms.getTotalElements());
    }

    @Override
    public List<Bom> fetchBagRelationships(List<Bom> boms) {
        return Optional.of(boms).map(this::fetchItemReferences).orElse(Collections.emptyList());
    }

    Bom fetchItemReferences(Bom result) {
        return entityManager
            .createQuery("select bom from Bom bom left join fetch bom.itemReferences where bom is :bom", Bom.class)
            .setParameter("bom", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Bom> fetchItemReferences(List<Bom> boms) {
        return entityManager
            .createQuery("select distinct bom from Bom bom left join fetch bom.itemReferences where bom in :boms", Bom.class)
            .setParameter("boms", boms)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
