package com.ptudw.web.service;

import com.ptudw.web.domain.*; // for static metamodels
import com.ptudw.web.domain.Notification;
import com.ptudw.web.repository.NotificationRepository;
import com.ptudw.web.service.criteria.NotificationCriteria;
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
 * Service for executing complex queries for {@link Notification} entities in the database.
 * The main input is a {@link NotificationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Notification} or a {@link Page} of {@link Notification} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NotificationQueryService extends QueryService<Notification> {

    private final Logger log = LoggerFactory.getLogger(NotificationQueryService.class);

    private final NotificationRepository notificationRepository;

    public NotificationQueryService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    /**
     * Return a {@link List} of {@link Notification} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Notification> findByCriteria(NotificationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Notification> specification = createSpecification(criteria);
        return notificationRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Notification} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Notification> findByCriteria(NotificationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Notification> specification = createSpecification(criteria);
        return notificationRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NotificationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Notification> specification = createSpecification(criteria);
        return notificationRepository.count(specification);
    }

    /**
     * Function to convert {@link NotificationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Notification> createSpecification(NotificationCriteria criteria) {
        Specification<Notification> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Notification_.id));
            }
            if (criteria.getMessage() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMessage(), Notification_.message));
            }
            if (criteria.getTopic() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTopic(), Notification_.topic));
            }
            if (criteria.getReceivers() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReceivers(), Notification_.receivers));
            }
            if (criteria.getSender() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSender(), Notification_.sender));
            }
            if (criteria.getRole() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRole(), Notification_.role));
            }
            if (criteria.getLink() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLink(), Notification_.link));
            }
            if (criteria.getGradeReviewId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGradeReviewId(), Notification_.gradeReviewId));
            }
            if (criteria.getIsRead() != null) {
                specification = specification.and(buildSpecification(criteria.getIsRead(), Notification_.isRead));
            }
        }
        return specification;
    }
}
