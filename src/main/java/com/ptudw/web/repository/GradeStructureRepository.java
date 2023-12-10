package com.ptudw.web.repository;

import com.ptudw.web.domain.GradeStructure;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the GradeStructure entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GradeStructureRepository extends JpaRepository<GradeStructure, Long>, JpaSpecificationExecutor<GradeStructure> {}
