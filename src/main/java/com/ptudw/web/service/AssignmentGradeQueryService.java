package com.ptudw.web.service;

import com.ptudw.web.domain.*; // for static metamodels
import com.ptudw.web.domain.AssignmentGrade;
import com.ptudw.web.repository.AssignmentGradeRepository;
import com.ptudw.web.service.criteria.AssignmentGradeCriteria;
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
 * Service for executing complex queries for {@link AssignmentGrade} entities in the database.
 * The main input is a {@link AssignmentGradeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AssignmentGrade} or a {@link Page} of {@link AssignmentGrade} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AssignmentGradeQueryService extends QueryService<AssignmentGrade> {

    private final Logger log = LoggerFactory.getLogger(AssignmentGradeQueryService.class);

    private final AssignmentGradeRepository assignmentGradeRepository;

    public AssignmentGradeQueryService(AssignmentGradeRepository assignmentGradeRepository) {
        this.assignmentGradeRepository = assignmentGradeRepository;
    }

    /**
     * Return a {@link List} of {@link AssignmentGrade} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AssignmentGrade> findByCriteria(AssignmentGradeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AssignmentGrade> specification = createSpecification(criteria);
        return assignmentGradeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link AssignmentGrade} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AssignmentGrade> findByCriteria(AssignmentGradeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AssignmentGrade> specification = createSpecification(criteria);
        return assignmentGradeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AssignmentGradeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AssignmentGrade> specification = createSpecification(criteria);
        return assignmentGradeRepository.count(specification);
    }

    /**
     * Function to convert {@link AssignmentGradeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AssignmentGrade> createSpecification(AssignmentGradeCriteria criteria) {
        Specification<AssignmentGrade> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AssignmentGrade_.id));
            }
            if (criteria.getStudentId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStudentId(), AssignmentGrade_.studentId));
            }
            if (criteria.getGrade() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGrade(), AssignmentGrade_.grade));
            }
            if (criteria.getIsDeleted() != null) {
                specification = specification.and(buildSpecification(criteria.getIsDeleted(), AssignmentGrade_.isDeleted));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), AssignmentGrade_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), AssignmentGrade_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), AssignmentGrade_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), AssignmentGrade_.lastModifiedDate));
            }
            if (criteria.getAssignmentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAssignmentId(),
                            root -> root.join(AssignmentGrade_.assignment, JoinType.LEFT).get(Assignment_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
