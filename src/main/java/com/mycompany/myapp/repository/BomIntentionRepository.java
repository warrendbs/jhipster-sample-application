package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.BomIntention;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BomIntention entity.
 */
@Repository
public interface BomIntentionRepository extends BomIntentionRepositoryWithBagRelationships, JpaRepository<BomIntention, Long> {
    default Optional<BomIntention> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<BomIntention> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<BomIntention> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
