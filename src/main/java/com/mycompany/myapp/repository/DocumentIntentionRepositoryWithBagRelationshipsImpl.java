package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.DocumentIntention;
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
public class DocumentIntentionRepositoryWithBagRelationshipsImpl implements DocumentIntentionRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<DocumentIntention> fetchBagRelationships(Optional<DocumentIntention> documentIntention) {
        return documentIntention.map(this::fetchReleasePackages);
    }

    @Override
    public Page<DocumentIntention> fetchBagRelationships(Page<DocumentIntention> documentIntentions) {
        return new PageImpl<>(
            fetchBagRelationships(documentIntentions.getContent()),
            documentIntentions.getPageable(),
            documentIntentions.getTotalElements()
        );
    }

    @Override
    public List<DocumentIntention> fetchBagRelationships(List<DocumentIntention> documentIntentions) {
        return Optional.of(documentIntentions).map(this::fetchReleasePackages).orElse(Collections.emptyList());
    }

    DocumentIntention fetchReleasePackages(DocumentIntention result) {
        return entityManager
            .createQuery(
                "select documentIntention from DocumentIntention documentIntention left join fetch documentIntention.releasePackages where documentIntention is :documentIntention",
                DocumentIntention.class
            )
            .setParameter("documentIntention", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<DocumentIntention> fetchReleasePackages(List<DocumentIntention> documentIntentions) {
        return entityManager
            .createQuery(
                "select distinct documentIntention from DocumentIntention documentIntention left join fetch documentIntention.releasePackages where documentIntention in :documentIntentions",
                DocumentIntention.class
            )
            .setParameter("documentIntentions", documentIntentions)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
