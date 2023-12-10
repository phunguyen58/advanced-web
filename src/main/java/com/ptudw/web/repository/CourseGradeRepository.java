package com.ptudw.web.repository;

import com.ptudw.web.domain.CourseGrade;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CourseGrade entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseGradeRepository extends JpaRepository<CourseGrade, Long>, JpaSpecificationExecutor<CourseGrade> {}
