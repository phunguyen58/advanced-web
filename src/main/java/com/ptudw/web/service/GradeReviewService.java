package com.ptudw.web.service;

import com.ptudw.web.domain.GradeReview;
import com.ptudw.web.repository.GradeReviewRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link GradeReview}.
 */
@Service
@Transactional
public class GradeReviewService {

    private final Logger log = LoggerFactory.getLogger(GradeReviewService.class);

    private final GradeReviewRepository gradeReviewRepository;

    public GradeReviewService(GradeReviewRepository gradeReviewRepository) {
        this.gradeReviewRepository = gradeReviewRepository;
    }

    /**
     * Save a gradeReview.
     *
     * @param gradeReview the entity to save.
     * @return the persisted entity.
     */
    public GradeReview save(GradeReview gradeReview) {
        log.debug("Request to save GradeReview : {}", gradeReview);
        return gradeReviewRepository.save(gradeReview);
    }

    /**
     * Update a gradeReview.
     *
     * @param gradeReview the entity to save.
     * @return the persisted entity.
     */
    public GradeReview update(GradeReview gradeReview) {
        log.debug("Request to update GradeReview : {}", gradeReview);
        return gradeReviewRepository.save(gradeReview);
    }

    /**
     * Partially update a gradeReview.
     *
     * @param gradeReview the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<GradeReview> partialUpdate(GradeReview gradeReview) {
        log.debug("Request to partially update GradeReview : {}", gradeReview);

        return gradeReviewRepository
            .findById(gradeReview.getId())
            .map(existingGradeReview -> {
                if (gradeReview.getGradeCompositionId() != null) {
                    existingGradeReview.setGradeCompositionId(gradeReview.getGradeCompositionId());
                }
                if (gradeReview.getStudentId() != null) {
                    existingGradeReview.setStudentId(gradeReview.getStudentId());
                }
                if (gradeReview.getCourseId() != null) {
                    existingGradeReview.setCourseId(gradeReview.getCourseId());
                }
                if (gradeReview.getReviewerId() != null) {
                    existingGradeReview.setReviewerId(gradeReview.getReviewerId());
                }
                if (gradeReview.getAssigmentId() != null) {
                    existingGradeReview.setAssigmentId(gradeReview.getAssigmentId());
                }
                if (gradeReview.getAssimentGradeId() != null) {
                    existingGradeReview.setAssimentGradeId(gradeReview.getAssimentGradeId());
                }
                if (gradeReview.getCurrentGrade() != null) {
                    existingGradeReview.setCurrentGrade(gradeReview.getCurrentGrade());
                }
                if (gradeReview.getExpectationGrade() != null) {
                    existingGradeReview.setExpectationGrade(gradeReview.getExpectationGrade());
                }
                if (gradeReview.getStudentExplanation() != null) {
                    existingGradeReview.setStudentExplanation(gradeReview.getStudentExplanation());
                }
                if (gradeReview.getTeacherComment() != null) {
                    existingGradeReview.setTeacherComment(gradeReview.getTeacherComment());
                }
                if (gradeReview.getIsFinal() != null) {
                    existingGradeReview.setIsFinal(gradeReview.getIsFinal());
                }

                return existingGradeReview;
            })
            .map(gradeReviewRepository::save);
    }

    /**
     * Get all the gradeReviews.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<GradeReview> findAll(Pageable pageable) {
        log.debug("Request to get all GradeReviews");
        return gradeReviewRepository.findAll(pageable);
    }

    /**
     * Get one gradeReview by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<GradeReview> findOne(Long id) {
        log.debug("Request to get GradeReview : {}", id);
        return gradeReviewRepository.findById(id);
    }

    /**
     * Delete the gradeReview by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete GradeReview : {}", id);
        gradeReviewRepository.deleteById(id);
    }
}
