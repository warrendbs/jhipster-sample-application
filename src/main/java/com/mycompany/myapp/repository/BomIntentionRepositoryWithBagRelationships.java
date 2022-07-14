package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.BomIntention;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface BomIntentionRepositoryWithBagRelationships {
    Optional<BomIntention> fetchBagRelationships(Optional<BomIntention> bomIntention);

    List<BomIntention> fetchBagRelationships(List<BomIntention> bomIntentions);

    Page<BomIntention> fetchBagRelationships(Page<BomIntention> bomIntentions);
}
