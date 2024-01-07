package com.ptudw.web.repository;

import com.ptudw.web.domain.Assignment;
import com.ptudw.web.domain.Course;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Assignment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long>, JpaSpecificationExecutor<Assignment> {
    @Query(
        value = "SELECT * FROM assignment join course on assignment.course_id =  course.id where assignment.course_id = :course_id",
        nativeQuery = true
    )
    List<Assignment> findAllByCourseId(@Param("course_id") Long courseId);
}
