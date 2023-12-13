package com.ptudw.web.repository;

import com.ptudw.web.domain.AssignmentGrade;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AssignmentGrade entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AssignmentGradeRepository extends JpaRepository<AssignmentGrade, Long>, JpaSpecificationExecutor<AssignmentGrade> {}
