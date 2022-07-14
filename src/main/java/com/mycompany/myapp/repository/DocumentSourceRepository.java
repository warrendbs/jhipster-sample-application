package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.DocumentSource;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DocumentSource entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentSourceRepository extends JpaRepository<DocumentSource, Long> {}
