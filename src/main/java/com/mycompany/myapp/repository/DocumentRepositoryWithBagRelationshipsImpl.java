package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Document;
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
public class DocumentRepositoryWithBagRelationshipsImpl implements DocumentRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Document> fetchBagRelationships(Optional<Document> document) {
        return document.map(this::fetchItemReferences);
    }

    @Override
    public Page<Document> fetchBagRelationships(Page<Document> documents) {
        return new PageImpl<>(fetchBagRelationships(documents.getContent()), documents.getPageable(), documents.getTotalElements());
    }

    @Override
    public List<Document> fetchBagRelationships(List<Document> documents) {
        return Optional.of(documents).map(this::fetchItemReferences).orElse(Collections.emptyList());
    }

    Document fetchItemReferences(Document result) {
        return entityManager
            .createQuery(
                "select document from Document document left join fetch document.itemReferences where document is :document",
                Document.class
            )
            .setParameter("document", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Document> fetchItemReferences(List<Document> documents) {
        return entityManager
            .createQuery(
                "select distinct document from Document document left join fetch document.itemReferences where document in :documents",
                Document.class
            )
            .setParameter("documents", documents)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
