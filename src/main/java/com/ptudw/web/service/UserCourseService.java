package com.ptudw.web.service;

import com.ptudw.web.domain.UserCourse;
import com.ptudw.web.repository.UserCourseRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.ptudw.web.domain.UserCourse}.
 */
@Service
@Transactional
public class UserCourseService {

    private final Logger log = LoggerFactory.getLogger(UserCourseService.class);

    private final UserCourseRepository userCourseRepository;

    public UserCourseService(UserCourseRepository userCourseRepository) {
        this.userCourseRepository = userCourseRepository;
    }

    /**
     * Save a userCourse.
     *
     * @param userCourse the entity to save.
     * @return the persisted entity.
     */
    public UserCourse save(UserCourse userCourse) {
        log.debug("Request to save UserCourse : {}", userCourse);
        return userCourseRepository.save(userCourse);
    }

    /**
     * Update a userCourse.
     *
     * @param userCourse the entity to save.
     * @return the persisted entity.
     */
    public UserCourse update(UserCourse userCourse) {
        log.debug("Request to update UserCourse : {}", userCourse);
        return userCourseRepository.save(userCourse);
    }

    /**
     * Partially update a userCourse.
     *
     * @param userCourse the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UserCourse> partialUpdate(UserCourse userCourse) {
        log.debug("Request to partially update UserCourse : {}", userCourse);

        return userCourseRepository
            .findById(userCourse.getId())
            .map(existingUserCourse -> {
                if (userCourse.getCourseId() != null) {
                    existingUserCourse.setCourseId(userCourse.getCourseId());
                }
                if (userCourse.getUserId() != null) {
                    existingUserCourse.setUserId(userCourse.getUserId());
                }

                return existingUserCourse;
            })
            .map(userCourseRepository::save);
    }

    /**
     * Get all the userCourses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UserCourse> findAll(Pageable pageable) {
        log.debug("Request to get all UserCourses");
        return userCourseRepository.findAll(pageable);
    }

    /**
     * Get one userCourse by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserCourse> findOne(Long id) {
        log.debug("Request to get UserCourse : {}", id);
        return userCourseRepository.findById(id);
    }

    /**
     * Delete the userCourse by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UserCourse : {}", id);
        userCourseRepository.deleteById(id);
    }
}
