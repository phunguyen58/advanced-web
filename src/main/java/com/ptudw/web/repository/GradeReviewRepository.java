package com.ptudw.web.repository;

import com.ptudw.web.domain.GradeReview;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the GradeReview entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GradeReviewRepository extends JpaRepository<GradeReview, Long>, JpaSpecificationExecutor<GradeReview> {}
