package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.DocumentIntention;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DocumentIntention entity.
 */
@Repository
public interface DocumentIntentionRepository
    extends DocumentIntentionRepositoryWithBagRelationships, JpaRepository<DocumentIntention, Long> {
    default Optional<DocumentIntention> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<DocumentIntention> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<DocumentIntention> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
