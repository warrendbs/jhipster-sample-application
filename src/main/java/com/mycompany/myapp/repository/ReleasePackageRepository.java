package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ReleasePackage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ReleasePackage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReleasePackageRepository extends JpaRepository<ReleasePackage, Long> {}
