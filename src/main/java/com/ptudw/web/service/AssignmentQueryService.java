package com.ptudw.web.service;

import com.ptudw.web.domain.*; // for static metamodels
import com.ptudw.web.domain.Assignment;
import com.ptudw.web.repository.AssignmentRepository;
import com.ptudw.web.service.criteria.AssignmentCriteria;
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
 * Service for executing complex queries for {@link Assignment} entities in the database.
 * The main input is a {@link AssignmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Assignment} or a {@link Page} of {@link Assignment} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AssignmentQueryService extends QueryService<Assignment> {

    private final Logger log = LoggerFactory.getLogger(AssignmentQueryService.class);

    private final AssignmentRepository assignmentRepository;

    public AssignmentQueryService(AssignmentRepository assignmentRepository) {
        this.assignmentRepository = assignmentRepository;
    }

    /**
     * Return a {@link List} of {@link Assignment} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Assignment> findByCriteria(AssignmentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Assignment> specification = createSpecification(criteria);
        return assignmentRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Assignment} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Assignment> findByCriteria(AssignmentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Assignment> specification = createSpecification(criteria);
        return assignmentRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AssignmentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Assignment> specification = createSpecification(criteria);
        return assignmentRepository.count(specification);
    }

    /**
     * Function to convert {@link AssignmentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Assignment> createSpecification(AssignmentCriteria criteria) {
        Specification<Assignment> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Assignment_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Assignment_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Assignment_.description));
            }
            if (criteria.getWeight() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWeight(), Assignment_.weight));
            }
            if (criteria.getIsDeleted() != null) {
                specification = specification.and(buildSpecification(criteria.getIsDeleted(), Assignment_.isDeleted));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), Assignment_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Assignment_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), Assignment_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), Assignment_.lastModifiedDate));
            }
            if (criteria.getAssignmentGradesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAssignmentGradesId(),
                            root -> root.join(Assignment_.assignmentGrades, JoinType.LEFT).get(AssignmentGrade_.id)
                        )
                    );
            }
            if (criteria.getCourseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCourseId(), root -> root.join(Assignment_.course, JoinType.LEFT).get(Course_.id))
                    );
            }
            if (criteria.getGradeCompositionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getGradeCompositionId(),
                            root -> root.join(Assignment_.gradeComposition, JoinType.LEFT).get(GradeComposition_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
