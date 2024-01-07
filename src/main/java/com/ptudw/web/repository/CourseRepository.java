package com.ptudw.web.repository;

import com.ptudw.web.domain.Course;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Course entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseRepository extends JpaRepository<Course, Long>, JpaSpecificationExecutor<Course> {
    Optional<Course> findOneByCode(String code);
    Page<Course> findAllByIdInAndIsDeleted(List<Long> courseId, Boolean isDeleted, Pageable pageable);
}
