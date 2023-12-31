package com.ptudw.web.service;

import com.ptudw.web.domain.Course;
import com.ptudw.web.repository.CourseRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Course}.
 */
@Service
@Transactional
public class CourseService {

    private final Logger log = LoggerFactory.getLogger(CourseService.class);

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    /**
     * Save a course.
     *
     * @param course the entity to save.
     * @return the persisted entity.
     */
    public Course save(Course course) {
        log.debug("Request to save Course : {}", course);
        return courseRepository.save(course);
    }

    /**
     * Update a course.
     *
     * @param course the entity to save.
     * @return the persisted entity.
     */
    public Course update(Course course) {
        log.debug("Request to update Course : {}", course);
        return courseRepository.save(course);
    }

    /**
     * Partially update a course.
     *
     * @param course the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Course> partialUpdate(Course course) {
        log.debug("Request to partially update Course : {}", course);

        return courseRepository
            .findById(course.getId())
            .map(existingCourse -> {
                if (course.getCode() != null) {
                    existingCourse.setCode(course.getCode());
                }
                if (course.getName() != null) {
                    existingCourse.setName(course.getName());
                }
                if (course.getInvitationCode() != null) {
                    existingCourse.setInvitationCode(course.getInvitationCode());
                }
                if (course.getExpirationDate() != null) {
                    existingCourse.setExpirationDate(course.getExpirationDate());
                }
                if (course.getGradeStructureId() != null) {
                    existingCourse.setGradeStructureId(course.getGradeStructureId());
                }
                if (course.getIsDeleted() != null) {
                    existingCourse.setIsDeleted(course.getIsDeleted());
                }
                if (course.getCreatedBy() != null) {
                    existingCourse.setCreatedBy(course.getCreatedBy());
                }
                if (course.getCreatedDate() != null) {
                    existingCourse.setCreatedDate(course.getCreatedDate());
                }
                if (course.getLastModifiedBy() != null) {
                    existingCourse.setLastModifiedBy(course.getLastModifiedBy());
                }
                if (course.getLastModifiedDate() != null) {
                    existingCourse.setLastModifiedDate(course.getLastModifiedDate());
                }

                return existingCourse;
            })
            .map(courseRepository::save);
    }

    /**
     * Get all the courses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Course> findAll(Pageable pageable) {
        log.debug("Request to get all Courses");
        return courseRepository.findAll(pageable);
    }

    /**
     * Get one course by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Course> findOne(Long id) {
        log.debug("Request to get Course : {}", id);
        return courseRepository.findById(id);
    }

    /**
     * Delete the course by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Course : {}", id);
        courseRepository.deleteById(id);
    }
}
