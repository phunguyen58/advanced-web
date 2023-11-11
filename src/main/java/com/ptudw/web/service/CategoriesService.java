package com.ptudw.web.service;

import com.ptudw.web.domain.Categories;
import com.ptudw.web.repository.CategoriesRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Categories}.
 */
@Service
@Transactional
public class CategoriesService {

    private final Logger log = LoggerFactory.getLogger(CategoriesService.class);

    private final CategoriesRepository categoriesRepository;

    public CategoriesService(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    /**
     * Save a categories.
     *
     * @param categories the entity to save.
     * @return the persisted entity.
     */
    public Categories save(Categories categories) {
        log.debug("Request to save Categories : {}", categories);
        return categoriesRepository.save(categories);
    }

    /**
     * Update a categories.
     *
     * @param categories the entity to save.
     * @return the persisted entity.
     */
    public Categories update(Categories categories) {
        log.debug("Request to update Categories : {}", categories);
        return categoriesRepository.save(categories);
    }

    /**
     * Partially update a categories.
     *
     * @param categories the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Categories> partialUpdate(Categories categories) {
        log.debug("Request to partially update Categories : {}", categories);

        return categoriesRepository
            .findById(categories.getId())
            .map(existingCategories -> {
                if (categories.getCategoryName() != null) {
                    existingCategories.setCategoryName(categories.getCategoryName());
                }
                if (categories.getCategoryDescription() != null) {
                    existingCategories.setCategoryDescription(categories.getCategoryDescription());
                }
                if (categories.getCategoryUrl() != null) {
                    existingCategories.setCategoryUrl(categories.getCategoryUrl());
                }
                if (categories.getCreatedBy() != null) {
                    existingCategories.setCreatedBy(categories.getCreatedBy());
                }
                if (categories.getCreatedTime() != null) {
                    existingCategories.setCreatedTime(categories.getCreatedTime());
                }

                return existingCategories;
            })
            .map(categoriesRepository::save);
    }

    /**
     * Get all the categories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Categories> findAll(Pageable pageable) {
        log.debug("Request to get all Categories");
        return categoriesRepository.findAll(pageable);
    }

    /**
     * Get one categories by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Categories> findOne(Long id) {
        log.debug("Request to get Categories : {}", id);
        return categoriesRepository.findById(id);
    }

    /**
     * Delete the categories by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Categories : {}", id);
        categoriesRepository.deleteById(id);
    }
}
