package com.ptudw.web.repository;

import com.ptudw.web.domain.UserCourse;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserCourse entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserCourseRepository extends JpaRepository<UserCourse, Long>, JpaSpecificationExecutor<UserCourse> {
    List<UserCourse> findAllByCourseId(Long id);
}
