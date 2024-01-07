package com.ptudw.web.service;

import com.ptudw.web.domain.Assignment;
import com.ptudw.web.repository.AssignmentRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Assignment}.
 */
@Service
@Transactional
public class AssignmentService {

    private final Logger log = LoggerFactory.getLogger(AssignmentService.class);

    private final AssignmentRepository assignmentRepository;

    public AssignmentService(AssignmentRepository assignmentRepository) {
        this.assignmentRepository = assignmentRepository;
    }

    /**
     * Save a assignment.
     *
     * @param assignment the entity to save.
     * @return the persisted entity.
     */
    public Assignment save(Assignment assignment) {
        log.debug("Request to save Assignment : {}", assignment);
        return assignmentRepository.save(assignment);
    }

    /**
     * Update a assignment.
     *
     * @param assignment the entity to save.
     * @return the persisted entity.
     */
    public Assignment update(Assignment assignment) {
        log.debug("Request to update Assignment : {}", assignment);
        return assignmentRepository.save(assignment);
    }

    /**
     * Partially update a assignment.
     *
     * @param assignment the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Assignment> partialUpdate(Assignment assignment) {
        log.debug("Request to partially update Assignment : {}", assignment);

        return assignmentRepository
            .findById(assignment.getId())
            .map(existingAssignment -> {
                if (assignment.getName() != null) {
                    existingAssignment.setName(assignment.getName());
                }
                if (assignment.getDescription() != null) {
                    existingAssignment.setDescription(assignment.getDescription());
                }
                if (assignment.getWeight() != null) {
                    existingAssignment.setWeight(assignment.getWeight());
                }
                if (assignment.getIsDeleted() != null) {
                    existingAssignment.setIsDeleted(assignment.getIsDeleted());
                }
                if (assignment.getCreatedBy() != null) {
                    existingAssignment.setCreatedBy(assignment.getCreatedBy());
                }
                if (assignment.getCreatedDate() != null) {
                    existingAssignment.setCreatedDate(assignment.getCreatedDate());
                }
                if (assignment.getLastModifiedBy() != null) {
                    existingAssignment.setLastModifiedBy(assignment.getLastModifiedBy());
                }
                if (assignment.getLastModifiedDate() != null) {
                    existingAssignment.setLastModifiedDate(assignment.getLastModifiedDate());
                }

                return existingAssignment;
            })
            .map(assignmentRepository::save);
    }

    /**
     * Get all the assignments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Assignment> findAll(Pageable pageable) {
        log.debug("Request to get all Assignments");
        return assignmentRepository.findAll(pageable);
    }

    /**
     * Get all the assignments.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Assignment> findAllByCourseId(Long courseId) {
        log.debug("Request to get all Assignments by courseId");
        return assignmentRepository.findAllByCourseId(courseId);
    }

    /**
     * Get one assignment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Assignment> findOne(Long id) {
        log.debug("Request to get Assignment : {}", id);
        return assignmentRepository.findById(id);
    }

    /**
     * Delete the assignment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Assignment : {}", id);
        assignmentRepository.deleteById(id);
    }
}
