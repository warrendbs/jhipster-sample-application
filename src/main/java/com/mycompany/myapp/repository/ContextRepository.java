package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Context;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Context entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContextRepository extends JpaRepository<Context, Long> {}
