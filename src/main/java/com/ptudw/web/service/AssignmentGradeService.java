package com.ptudw.web.service;

import com.ptudw.web.domain.AssignmentGrade;
import com.ptudw.web.repository.AssignmentGradeRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AssignmentGrade}.
 */
@Service
@Transactional
public class AssignmentGradeService {

    private final Logger log = LoggerFactory.getLogger(AssignmentGradeService.class);

    private final AssignmentGradeRepository assignmentGradeRepository;

    public AssignmentGradeService(AssignmentGradeRepository assignmentGradeRepository) {
        this.assignmentGradeRepository = assignmentGradeRepository;
    }

    /**
     * Save a assignmentGrade.
     *
     * @param assignmentGrade the entity to save.
     * @return the persisted entity.
     */
    public AssignmentGrade save(AssignmentGrade assignmentGrade) {
        log.debug("Request to save AssignmentGrade : {}", assignmentGrade);
        return assignmentGradeRepository.save(assignmentGrade);
    }

    /**
     * Update a assignmentGrade.
     *
     * @param assignmentGrade the entity to save.
     * @return the persisted entity.
     */
    public AssignmentGrade update(AssignmentGrade assignmentGrade) {
        log.debug("Request to update AssignmentGrade : {}", assignmentGrade);
        return assignmentGradeRepository.save(assignmentGrade);
    }

    /**
     * Partially update a assignmentGrade.
     *
     * @param assignmentGrade the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AssignmentGrade> partialUpdate(AssignmentGrade assignmentGrade) {
        log.debug("Request to partially update AssignmentGrade : {}", assignmentGrade);

        return assignmentGradeRepository
            .findById(assignmentGrade.getId())
            .map(existingAssignmentGrade -> {
                if (assignmentGrade.getStudentId() != null) {
                    existingAssignmentGrade.setStudentId(assignmentGrade.getStudentId());
                }
                if (assignmentGrade.getGrade() != null) {
                    existingAssignmentGrade.setGrade(assignmentGrade.getGrade());
                }
                if (assignmentGrade.getIsDeleted() != null) {
                    existingAssignmentGrade.setIsDeleted(assignmentGrade.getIsDeleted());
                }
                if (assignmentGrade.getCreatedBy() != null) {
                    existingAssignmentGrade.setCreatedBy(assignmentGrade.getCreatedBy());
                }
                if (assignmentGrade.getCreatedDate() != null) {
                    existingAssignmentGrade.setCreatedDate(assignmentGrade.getCreatedDate());
                }
                if (assignmentGrade.getLastModifiedBy() != null) {
                    existingAssignmentGrade.setLastModifiedBy(assignmentGrade.getLastModifiedBy());
                }
                if (assignmentGrade.getLastModifiedDate() != null) {
                    existingAssignmentGrade.setLastModifiedDate(assignmentGrade.getLastModifiedDate());
                }

                return existingAssignmentGrade;
            })
            .map(assignmentGradeRepository::save);
    }

    /**
     * Get all the assignmentGrades.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AssignmentGrade> findAll(Pageable pageable) {
        log.debug("Request to get all AssignmentGrades");
        return assignmentGradeRepository.findAll(pageable);
    }

    /**
     * Get one assignmentGrade by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AssignmentGrade> findOne(Long id) {
        log.debug("Request to get AssignmentGrade : {}", id);
        return assignmentGradeRepository.findById(id);
    }

    /**
     * Delete the assignmentGrade by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AssignmentGrade : {}", id);
        assignmentGradeRepository.deleteById(id);
    }

    public List<AssignmentGrade> saveAll(List<AssignmentGrade> assignmentGrades) {
        log.debug("Request to save a list of AssignmentGrades");
        return assignmentGradeRepository.saveAll(assignmentGrades);
    }
}
