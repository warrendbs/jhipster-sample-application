package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.PartIntention;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface PartIntentionRepositoryWithBagRelationships {
    Optional<PartIntention> fetchBagRelationships(Optional<PartIntention> partIntention);

    List<PartIntention> fetchBagRelationships(List<PartIntention> partIntentions);

    Page<PartIntention> fetchBagRelationships(Page<PartIntention> partIntentions);
}
