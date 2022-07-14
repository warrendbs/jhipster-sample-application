package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.BomSource;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface BomSourceRepositoryWithBagRelationships {
    Optional<BomSource> fetchBagRelationships(Optional<BomSource> bomSource);

    List<BomSource> fetchBagRelationships(List<BomSource> bomSources);

    Page<BomSource> fetchBagRelationships(Page<BomSource> bomSources);
}
