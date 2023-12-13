package com.ptudw.web.service;

import com.ptudw.web.domain.CourseGrade;
import com.ptudw.web.repository.CourseGradeRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CourseGrade}.
 */
@Service
@Transactional
public class CourseGradeService {

    private final Logger log = LoggerFactory.getLogger(CourseGradeService.class);

    private final CourseGradeRepository courseGradeRepository;

    public CourseGradeService(CourseGradeRepository courseGradeRepository) {
        this.courseGradeRepository = courseGradeRepository;
    }

    /**
     * Save a courseGrade.
     *
     * @param courseGrade the entity to save.
     * @return the persisted entity.
     */
    public CourseGrade save(CourseGrade courseGrade) {
        log.debug("Request to save CourseGrade : {}", courseGrade);
        return courseGradeRepository.save(courseGrade);
    }

    /**
     * Update a courseGrade.
     *
     * @param courseGrade the entity to save.
     * @return the persisted entity.
     */
    public CourseGrade update(CourseGrade courseGrade) {
        log.debug("Request to update CourseGrade : {}", courseGrade);
        return courseGradeRepository.save(courseGrade);
    }

    /**
     * Partially update a courseGrade.
     *
     * @param courseGrade the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CourseGrade> partialUpdate(CourseGrade courseGrade) {
        log.debug("Request to partially update CourseGrade : {}", courseGrade);

        return courseGradeRepository
            .findById(courseGrade.getId())
            .map(existingCourseGrade -> {
                if (courseGrade.getGradeCompositionId() != null) {
                    existingCourseGrade.setGradeCompositionId(courseGrade.getGradeCompositionId());
                }
                if (courseGrade.getStudentId() != null) {
                    existingCourseGrade.setStudentId(courseGrade.getStudentId());
                }
                if (courseGrade.getIsMarked() != null) {
                    existingCourseGrade.setIsMarked(courseGrade.getIsMarked());
                }
                if (courseGrade.getIsDeleted() != null) {
                    existingCourseGrade.setIsDeleted(courseGrade.getIsDeleted());
                }
                if (courseGrade.getCreatedBy() != null) {
                    existingCourseGrade.setCreatedBy(courseGrade.getCreatedBy());
                }
                if (courseGrade.getCreatedDate() != null) {
                    existingCourseGrade.setCreatedDate(courseGrade.getCreatedDate());
                }
                if (courseGrade.getLastModifiedBy() != null) {
                    existingCourseGrade.setLastModifiedBy(courseGrade.getLastModifiedBy());
                }
                if (courseGrade.getLastModifiedDate() != null) {
                    existingCourseGrade.setLastModifiedDate(courseGrade.getLastModifiedDate());
                }

                return existingCourseGrade;
            })
            .map(courseGradeRepository::save);
    }

    /**
     * Get all the courseGrades.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CourseGrade> findAll(Pageable pageable) {
        log.debug("Request to get all CourseGrades");
        return courseGradeRepository.findAll(pageable);
    }

    /**
     * Get one courseGrade by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CourseGrade> findOne(Long id) {
        log.debug("Request to get CourseGrade : {}", id);
        return courseGradeRepository.findById(id);
    }

    /**
     * Delete the courseGrade by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CourseGrade : {}", id);
        courseGradeRepository.deleteById(id);
    }
}
