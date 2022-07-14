package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ImpactMatrix;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ImpactMatrix entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ImpactMatrixRepository extends JpaRepository<ImpactMatrix, Long> {}
