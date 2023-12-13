package com.ptudw.web.service;

import com.ptudw.web.domain.*; // for static metamodels
import com.ptudw.web.domain.GradeStructure;
import com.ptudw.web.repository.GradeStructureRepository;
import com.ptudw.web.service.criteria.GradeStructureCriteria;
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
 * Service for executing complex queries for {@link GradeStructure} entities in the database.
 * The main input is a {@link GradeStructureCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GradeStructure} or a {@link Page} of {@link GradeStructure} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GradeStructureQueryService extends QueryService<GradeStructure> {

    private final Logger log = LoggerFactory.getLogger(GradeStructureQueryService.class);

    private final GradeStructureRepository gradeStructureRepository;

    public GradeStructureQueryService(GradeStructureRepository gradeStructureRepository) {
        this.gradeStructureRepository = gradeStructureRepository;
    }

    /**
     * Return a {@link List} of {@link GradeStructure} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GradeStructure> findByCriteria(GradeStructureCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<GradeStructure> specification = createSpecification(criteria);
        return gradeStructureRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link GradeStructure} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GradeStructure> findByCriteria(GradeStructureCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<GradeStructure> specification = createSpecification(criteria);
        return gradeStructureRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GradeStructureCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<GradeStructure> specification = createSpecification(criteria);
        return gradeStructureRepository.count(specification);
    }

    /**
     * Function to convert {@link GradeStructureCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<GradeStructure> createSpecification(GradeStructureCriteria criteria) {
        Specification<GradeStructure> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), GradeStructure_.id));
            }
            if (criteria.getCourseId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCourseId(), GradeStructure_.courseId));
            }
            if (criteria.getIsDeleted() != null) {
                specification = specification.and(buildSpecification(criteria.getIsDeleted(), GradeStructure_.isDeleted));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), GradeStructure_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), GradeStructure_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), GradeStructure_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), GradeStructure_.lastModifiedDate));
            }
            if (criteria.getGradeCompositionsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getGradeCompositionsId(),
                            root -> root.join(GradeStructure_.gradeCompositions, JoinType.LEFT).get(GradeComposition_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
