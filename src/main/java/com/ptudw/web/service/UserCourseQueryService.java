package com.ptudw.web.service;

import com.ptudw.web.domain.*; // for static metamodels
import com.ptudw.web.domain.UserCourse;
import com.ptudw.web.repository.UserCourseRepository;
import com.ptudw.web.service.criteria.UserCourseCriteria;
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
 * Service for executing complex queries for {@link UserCourse} entities in the database.
 * The main input is a {@link UserCourseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UserCourse} or a {@link Page} of {@link UserCourse} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserCourseQueryService extends QueryService<UserCourse> {

    private final Logger log = LoggerFactory.getLogger(UserCourseQueryService.class);

    private final UserCourseRepository userCourseRepository;

    public UserCourseQueryService(UserCourseRepository userCourseRepository) {
        this.userCourseRepository = userCourseRepository;
    }

    /**
     * Return a {@link List} of {@link UserCourse} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UserCourse> findByCriteria(UserCourseCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<UserCourse> specification = createSpecification(criteria);
        return userCourseRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link UserCourse} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserCourse> findByCriteria(UserCourseCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UserCourse> specification = createSpecification(criteria);
        return userCourseRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserCourseCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<UserCourse> specification = createSpecification(criteria);
        return userCourseRepository.count(specification);
    }

    /**
     * Function to convert {@link UserCourseCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UserCourse> createSpecification(UserCourseCriteria criteria) {
        Specification<UserCourse> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), UserCourse_.id));
            }
            if (criteria.getCourseId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCourseId(), UserCourse_.courseId));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUserId(), UserCourse_.userId));
            }
        }
        return specification;
    }
}
