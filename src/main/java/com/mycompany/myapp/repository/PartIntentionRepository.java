package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.PartIntention;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PartIntention entity.
 */
@Repository
public interface PartIntentionRepository extends PartIntentionRepositoryWithBagRelationships, JpaRepository<PartIntention, Long> {
    default Optional<PartIntention> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<PartIntention> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<PartIntention> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
