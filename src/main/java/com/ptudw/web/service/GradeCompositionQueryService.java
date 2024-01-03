package com.ptudw.web.service;

import com.ptudw.web.domain.*; // for static metamodels
import com.ptudw.web.domain.GradeComposition;
import com.ptudw.web.repository.GradeCompositionRepository;
import com.ptudw.web.service.criteria.GradeCompositionCriteria;
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
 * Service for executing complex queries for {@link GradeComposition} entities in the database.
 * The main input is a {@link GradeCompositionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GradeComposition} or a {@link Page} of {@link GradeComposition} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GradeCompositionQueryService extends QueryService<GradeComposition> {

    private final Logger log = LoggerFactory.getLogger(GradeCompositionQueryService.class);

    private final GradeCompositionRepository gradeCompositionRepository;

    public GradeCompositionQueryService(GradeCompositionRepository gradeCompositionRepository) {
        this.gradeCompositionRepository = gradeCompositionRepository;
    }

    /**
     * Return a {@link List} of {@link GradeComposition} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GradeComposition> findByCriteria(GradeCompositionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<GradeComposition> specification = createSpecification(criteria);
        return gradeCompositionRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link GradeComposition} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GradeComposition> findByCriteria(GradeCompositionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<GradeComposition> specification = createSpecification(criteria);
        return gradeCompositionRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GradeCompositionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<GradeComposition> specification = createSpecification(criteria);
        return gradeCompositionRepository.count(specification);
    }

    /**
     * Function to convert {@link GradeCompositionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<GradeComposition> createSpecification(GradeCompositionCriteria criteria) {
        Specification<GradeComposition> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), GradeComposition_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), GradeComposition_.name));
            }
            if (criteria.getScale() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getScale(), GradeComposition_.scale));
            }
            if (criteria.getIsDeleted() != null) {
                specification = specification.and(buildSpecification(criteria.getIsDeleted(), GradeComposition_.isDeleted));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), GradeComposition_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), GradeComposition_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), GradeComposition_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), GradeComposition_.lastModifiedDate));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), GradeComposition_.type));
            }
            if (criteria.getIsPublic() != null) {
                specification = specification.and(buildSpecification(criteria.getIsPublic(), GradeComposition_.isPublic));
            }
            if (criteria.getAssignmentsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAssignmentsId(),
                            root -> root.join(GradeComposition_.assignments, JoinType.LEFT).get(Assignment_.id)
                        )
                    );
            }
            if (criteria.getCourseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCourseId(),
                            root -> root.join(GradeComposition_.course, JoinType.LEFT).get(Course_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
