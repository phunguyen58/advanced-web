package com.ptudw.web.repository;

import com.ptudw.web.domain.Course;
import com.ptudw.web.domain.GradeComposition;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the GradeComposition entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GradeCompositionRepository extends JpaRepository<GradeComposition, Long>, JpaSpecificationExecutor<GradeComposition> {
    @Query(value = "select * from grade_composition " + "where course_id = :course_id LIMIT 1", nativeQuery = true)
    GradeComposition findOneByCourseId(@Param("course_id") Long courseId);

    List<GradeComposition> findAllByCourse(Course course);
}
