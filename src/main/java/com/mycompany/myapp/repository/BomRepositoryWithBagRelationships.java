package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Bom;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface BomRepositoryWithBagRelationships {
    Optional<Bom> fetchBagRelationships(Optional<Bom> bom);

    List<Bom> fetchBagRelationships(List<Bom> boms);

    Page<Bom> fetchBagRelationships(Page<Bom> boms);
}
