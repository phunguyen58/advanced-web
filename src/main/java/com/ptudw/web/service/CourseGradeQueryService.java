package com.ptudw.web.service;

import com.ptudw.web.domain.*; // for static metamodels
import com.ptudw.web.domain.CourseGrade;
import com.ptudw.web.repository.CourseGradeRepository;
import com.ptudw.web.service.criteria.CourseGradeCriteria;
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
 * Service for executing complex queries for {@link CourseGrade} entities in the database.
 * The main input is a {@link CourseGradeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CourseGrade} or a {@link Page} of {@link CourseGrade} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CourseGradeQueryService extends QueryService<CourseGrade> {

    private final Logger log = LoggerFactory.getLogger(CourseGradeQueryService.class);

    private final CourseGradeRepository courseGradeRepository;

    public CourseGradeQueryService(CourseGradeRepository courseGradeRepository) {
        this.courseGradeRepository = courseGradeRepository;
    }

    /**
     * Return a {@link List} of {@link CourseGrade} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CourseGrade> findByCriteria(CourseGradeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CourseGrade> specification = createSpecification(criteria);
        return courseGradeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link CourseGrade} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CourseGrade> findByCriteria(CourseGradeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CourseGrade> specification = createSpecification(criteria);
        return courseGradeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CourseGradeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CourseGrade> specification = createSpecification(criteria);
        return courseGradeRepository.count(specification);
    }

    /**
     * Function to convert {@link CourseGradeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CourseGrade> createSpecification(CourseGradeCriteria criteria) {
        Specification<CourseGrade> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CourseGrade_.id));
            }
            if (criteria.getGradeCompositionId() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getGradeCompositionId(), CourseGrade_.gradeCompositionId));
            }
            if (criteria.getStudentId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStudentId(), CourseGrade_.studentId));
            }
            if (criteria.getIsMarked() != null) {
                specification = specification.and(buildSpecification(criteria.getIsMarked(), CourseGrade_.isMarked));
            }
            if (criteria.getIsDeleted() != null) {
                specification = specification.and(buildSpecification(criteria.getIsDeleted(), CourseGrade_.isDeleted));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), CourseGrade_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), CourseGrade_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), CourseGrade_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), CourseGrade_.lastModifiedDate));
            }
        }
        return specification;
    }
}
