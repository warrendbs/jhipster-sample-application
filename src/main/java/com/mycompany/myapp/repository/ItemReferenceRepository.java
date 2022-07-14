package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ItemReference;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ItemReference entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemReferenceRepository extends JpaRepository<ItemReference, Long> {}
