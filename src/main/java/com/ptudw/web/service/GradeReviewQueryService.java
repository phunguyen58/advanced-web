package com.ptudw.web.service;

import com.ptudw.web.domain.*; // for static metamodels
import com.ptudw.web.domain.GradeReview;
import com.ptudw.web.repository.GradeReviewRepository;
import com.ptudw.web.service.criteria.GradeReviewCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link GradeReview} entities in the database.
 * The main input is a {@link GradeReviewCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GradeReview} or a {@link Page} of {@link GradeReview} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GradeReviewQueryService extends QueryService<GradeReview> {

    private final Logger log = LoggerFactory.getLogger(GradeReviewQueryService.class);

    private final GradeReviewRepository gradeReviewRepository;

    public GradeReviewQueryService(GradeReviewRepository gradeReviewRepository) {
        this.gradeReviewRepository = gradeReviewRepository;
    }

    /**
     * Return a {@link List} of {@link GradeReview} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GradeReview> findByCriteria(GradeReviewCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<GradeReview> specification = createSpecification(criteria);
        return gradeReviewRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link GradeReview} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GradeReview> findByCriteria(GradeReviewCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<GradeReview> specification = createSpecification(criteria);
        return gradeReviewRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GradeReviewCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<GradeReview> specification = createSpecification(criteria);
        return gradeReviewRepository.count(specification);
    }

    /**
     * Function to convert {@link GradeReviewCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<GradeReview> createSpecification(GradeReviewCriteria criteria) {
        Specification<GradeReview> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), GradeReview_.id));
            }
            if (criteria.getGradeCompositionId() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getGradeCompositionId(), GradeReview_.gradeCompositionId));
            }
            if (criteria.getStudentId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStudentId(), GradeReview_.studentId));
            }
            if (criteria.getCourseId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCourseId(), GradeReview_.courseId));
            }
            if (criteria.getReviewerId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReviewerId(), GradeReview_.reviewerId));
            }
            if (criteria.getAssigmentId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAssigmentId(), GradeReview_.assigmentId));
            }
            if (criteria.getAssimentGradeId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAssimentGradeId(), GradeReview_.assimentGradeId));
            }
            if (criteria.getCurrentGrade() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCurrentGrade(), GradeReview_.currentGrade));
            }
            if (criteria.getExpectationGrade() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExpectationGrade(), GradeReview_.expectationGrade));
            }
            if (criteria.getStudentExplanation() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getStudentExplanation(), GradeReview_.studentExplanation));
            }
            if (criteria.getTeacherComment() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTeacherComment(), GradeReview_.teacherComment));
            }
            if (criteria.getIsFinal() != null) {
                specification = specification.and(buildSpecification(criteria.getIsFinal(), GradeReview_.isFinal));
            }
            if (criteria.getFinalGrade() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFinalGrade(), GradeReview_.finalGrade));
            }
        }
        return specification;
    }
}
