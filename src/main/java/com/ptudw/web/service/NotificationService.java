package com.ptudw.web.service;

import com.ptudw.web.domain.Notification;
import com.ptudw.web.repository.NotificationRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Notification}.
 */
@Service
@Transactional
public class NotificationService {

    private final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    /**
     * Save a notification.
     *
     * @param notification the entity to save.
     * @return the persisted entity.
     */
    public Notification save(Notification notification) {
        log.debug("Request to save Notification : {}", notification);
        return notificationRepository.save(notification);
    }

    /**
     * Update a notification.
     *
     * @param notification the entity to save.
     * @return the persisted entity.
     */
    public Notification update(Notification notification) {
        log.debug("Request to update Notification : {}", notification);
        return notificationRepository.save(notification);
    }

    /**
     * Partially update a notification.
     *
     * @param notification the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Notification> partialUpdate(Notification notification) {
        log.debug("Request to partially update Notification : {}", notification);

        return notificationRepository
            .findById(notification.getId())
            .map(existingNotification -> {
                if (notification.getMessage() != null) {
                    existingNotification.setMessage(notification.getMessage());
                }
                if (notification.getTopic() != null) {
                    existingNotification.setTopic(notification.getTopic());
                }
                if (notification.getReceivers() != null) {
                    existingNotification.setReceivers(notification.getReceivers());
                }
                if (notification.getSender() != null) {
                    existingNotification.setSender(notification.getSender());
                }
                if (notification.getRole() != null) {
                    existingNotification.setRole(notification.getRole());
                }
                if (notification.getLink() != null) {
                    existingNotification.setLink(notification.getLink());
                }
                if (notification.getGradeReviewId() != null) {
                    existingNotification.setGradeReviewId(notification.getGradeReviewId());
                }
                if (notification.getIsRead() != null) {
                    existingNotification.setIsRead(notification.getIsRead());
                }

                return existingNotification;
            })
            .map(notificationRepository::save);
    }

    /**
     * Get all the notifications.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Notification> findAll(Pageable pageable) {
        log.debug("Request to get all Notifications");
        return notificationRepository.findAll(pageable);
    }

    /**
     * Get one notification by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Notification> findOne(Long id) {
        log.debug("Request to get Notification : {}", id);
        return notificationRepository.findById(id);
    }

    /**
     * Delete the notification by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Notification : {}", id);
        notificationRepository.deleteById(id);
    }
}
