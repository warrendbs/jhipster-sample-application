package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.PlantSpecific;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PlantSpecific entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlantSpecificRepository extends JpaRepository<PlantSpecific, Long> {}
