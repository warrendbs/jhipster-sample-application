package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.DocumentIntention;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface DocumentIntentionRepositoryWithBagRelationships {
    Optional<DocumentIntention> fetchBagRelationships(Optional<DocumentIntention> documentIntention);

    List<DocumentIntention> fetchBagRelationships(List<DocumentIntention> documentIntentions);

    Page<DocumentIntention> fetchBagRelationships(Page<DocumentIntention> documentIntentions);
}
