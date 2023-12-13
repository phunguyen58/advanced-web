package com.ptudw.web.service;

import com.ptudw.web.domain.GradeStructure;
import com.ptudw.web.repository.GradeStructureRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link GradeStructure}.
 */
@Service
@Transactional
public class GradeStructureService {

    private final Logger log = LoggerFactory.getLogger(GradeStructureService.class);

    private final GradeStructureRepository gradeStructureRepository;

    public GradeStructureService(GradeStructureRepository gradeStructureRepository) {
        this.gradeStructureRepository = gradeStructureRepository;
    }

    /**
     * Save a gradeStructure.
     *
     * @param gradeStructure the entity to save.
     * @return the persisted entity.
     */
    public GradeStructure save(GradeStructure gradeStructure) {
        log.debug("Request to save GradeStructure : {}", gradeStructure);
        return gradeStructureRepository.save(gradeStructure);
    }

    /**
     * Update a gradeStructure.
     *
     * @param gradeStructure the entity to save.
     * @return the persisted entity.
     */
    public GradeStructure update(GradeStructure gradeStructure) {
        log.debug("Request to update GradeStructure : {}", gradeStructure);
        return gradeStructureRepository.save(gradeStructure);
    }

    /**
     * Partially update a gradeStructure.
     *
     * @param gradeStructure the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<GradeStructure> partialUpdate(GradeStructure gradeStructure) {
        log.debug("Request to partially update GradeStructure : {}", gradeStructure);

        return gradeStructureRepository
            .findById(gradeStructure.getId())
            .map(existingGradeStructure -> {
                if (gradeStructure.getCourseId() != null) {
                    existingGradeStructure.setCourseId(gradeStructure.getCourseId());
                }
                if (gradeStructure.getIsDeleted() != null) {
                    existingGradeStructure.setIsDeleted(gradeStructure.getIsDeleted());
                }
                if (gradeStructure.getCreatedBy() != null) {
                    existingGradeStructure.setCreatedBy(gradeStructure.getCreatedBy());
                }
                if (gradeStructure.getCreatedDate() != null) {
                    existingGradeStructure.setCreatedDate(gradeStructure.getCreatedDate());
                }
                if (gradeStructure.getLastModifiedBy() != null) {
                    existingGradeStructure.setLastModifiedBy(gradeStructure.getLastModifiedBy());
                }
                if (gradeStructure.getLastModifiedDate() != null) {
                    existingGradeStructure.setLastModifiedDate(gradeStructure.getLastModifiedDate());
                }

                return existingGradeStructure;
            })
            .map(gradeStructureRepository::save);
    }

    /**
     * Get all the gradeStructures.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<GradeStructure> findAll(Pageable pageable) {
        log.debug("Request to get all GradeStructures");
        return gradeStructureRepository.findAll(pageable);
    }

    /**
     * Get one gradeStructure by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<GradeStructure> findOne(Long id) {
        log.debug("Request to get GradeStructure : {}", id);
        return gradeStructureRepository.findById(id);
    }

    /**
     * Delete the gradeStructure by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete GradeStructure : {}", id);
        gradeStructureRepository.deleteById(id);
    }
}
