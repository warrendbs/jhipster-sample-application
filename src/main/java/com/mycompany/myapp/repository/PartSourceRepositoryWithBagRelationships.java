package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.PartSource;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface PartSourceRepositoryWithBagRelationships {
    Optional<PartSource> fetchBagRelationships(Optional<PartSource> partSource);

    List<PartSource> fetchBagRelationships(List<PartSource> partSources);

    Page<PartSource> fetchBagRelationships(Page<PartSource> partSources);
}
