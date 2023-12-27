package com.ptudw.web.service;

import com.ptudw.web.domain.GradeComposition;
import com.ptudw.web.repository.GradeCompositionRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link GradeComposition}.
 */
@Service
@Transactional
public class GradeCompositionService {

    private final Logger log = LoggerFactory.getLogger(GradeCompositionService.class);

    private final GradeCompositionRepository gradeCompositionRepository;

    public GradeCompositionService(GradeCompositionRepository gradeCompositionRepository) {
        this.gradeCompositionRepository = gradeCompositionRepository;
    }

    /**
     * Save a gradeComposition.
     *
     * @param gradeComposition the entity to save.
     * @return the persisted entity.
     */
    public GradeComposition save(GradeComposition gradeComposition) {
        log.debug("Request to save GradeComposition : {}", gradeComposition);
        return gradeCompositionRepository.save(gradeComposition);
    }

    /**
     * Update a gradeComposition.
     *
     * @param gradeComposition the entity to save.
     * @return the persisted entity.
     */
    public GradeComposition update(GradeComposition gradeComposition) {
        log.debug("Request to update GradeComposition : {}", gradeComposition);
        return gradeCompositionRepository.save(gradeComposition);
    }

    /**
     * Partially update a gradeComposition.
     *
     * @param gradeComposition the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<GradeComposition> partialUpdate(GradeComposition gradeComposition) {
        log.debug("Request to partially update GradeComposition : {}", gradeComposition);

        return gradeCompositionRepository
            .findById(gradeComposition.getId())
            .map(existingGradeComposition -> {
                if (gradeComposition.getName() != null) {
                    existingGradeComposition.setName(gradeComposition.getName());
                }
                if (gradeComposition.getScale() != null) {
                    existingGradeComposition.setScale(gradeComposition.getScale());
                }
                if (gradeComposition.getIsDeleted() != null) {
                    existingGradeComposition.setIsDeleted(gradeComposition.getIsDeleted());
                }
                if (gradeComposition.getCreatedBy() != null) {
                    existingGradeComposition.setCreatedBy(gradeComposition.getCreatedBy());
                }
                if (gradeComposition.getCreatedDate() != null) {
                    existingGradeComposition.setCreatedDate(gradeComposition.getCreatedDate());
                }
                if (gradeComposition.getLastModifiedBy() != null) {
                    existingGradeComposition.setLastModifiedBy(gradeComposition.getLastModifiedBy());
                }
                if (gradeComposition.getLastModifiedDate() != null) {
                    existingGradeComposition.setLastModifiedDate(gradeComposition.getLastModifiedDate());
                }
                if (gradeComposition.getType() != null) {
                    existingGradeComposition.setType(gradeComposition.getType());
                }

                return existingGradeComposition;
            })
            .map(gradeCompositionRepository::save);
    }

    /**
     * Get all the gradeCompositions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<GradeComposition> findAll(Pageable pageable) {
        log.debug("Request to get all GradeCompositions");
        return gradeCompositionRepository.findAll(pageable);
    }

    /**
     * Get one gradeComposition by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<GradeComposition> findOne(Long id) {
        log.debug("Request to get GradeComposition : {}", id);
        return gradeCompositionRepository.findById(id);
    }

    /**
     * Delete the gradeComposition by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete GradeComposition : {}", id);
        gradeCompositionRepository.deleteById(id);
    }
}
