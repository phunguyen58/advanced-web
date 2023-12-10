package com.ptudw.web.web.rest;

import com.ptudw.web.domain.GradeStructure;
import com.ptudw.web.repository.GradeStructureRepository;
import com.ptudw.web.service.GradeStructureQueryService;
import com.ptudw.web.service.GradeStructureService;
import com.ptudw.web.service.criteria.GradeStructureCriteria;
import com.ptudw.web.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ptudw.web.domain.GradeStructure}.
 */
@RestController
@RequestMapping("/api")
public class GradeStructureResource {

    private final Logger log = LoggerFactory.getLogger(GradeStructureResource.class);

    private static final String ENTITY_NAME = "gradeStructure";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GradeStructureService gradeStructureService;

    private final GradeStructureRepository gradeStructureRepository;

    private final GradeStructureQueryService gradeStructureQueryService;

    public GradeStructureResource(
        GradeStructureService gradeStructureService,
        GradeStructureRepository gradeStructureRepository,
        GradeStructureQueryService gradeStructureQueryService
    ) {
        this.gradeStructureService = gradeStructureService;
        this.gradeStructureRepository = gradeStructureRepository;
        this.gradeStructureQueryService = gradeStructureQueryService;
    }

    /**
     * {@code POST  /grade-structures} : Create a new gradeStructure.
     *
     * @param gradeStructure the gradeStructure to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new gradeStructure, or with status {@code 400 (Bad Request)} if the gradeStructure has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/grade-structures")
    public ResponseEntity<GradeStructure> createGradeStructure(@Valid @RequestBody GradeStructure gradeStructure)
        throws URISyntaxException {
        log.debug("REST request to save GradeStructure : {}", gradeStructure);
        if (gradeStructure.getId() != null) {
            throw new BadRequestAlertException("A new gradeStructure cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GradeStructure result = gradeStructureService.save(gradeStructure);
        return ResponseEntity
            .created(new URI("/api/grade-structures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /grade-structures/:id} : Updates an existing gradeStructure.
     *
     * @param id the id of the gradeStructure to save.
     * @param gradeStructure the gradeStructure to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gradeStructure,
     * or with status {@code 400 (Bad Request)} if the gradeStructure is not valid,
     * or with status {@code 500 (Internal Server Error)} if the gradeStructure couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/grade-structures/{id}")
    public ResponseEntity<GradeStructure> updateGradeStructure(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody GradeStructure gradeStructure
    ) throws URISyntaxException {
        log.debug("REST request to update GradeStructure : {}, {}", id, gradeStructure);
        if (gradeStructure.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gradeStructure.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gradeStructureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GradeStructure result = gradeStructureService.update(gradeStructure);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gradeStructure.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /grade-structures/:id} : Partial updates given fields of an existing gradeStructure, field will ignore if it is null
     *
     * @param id the id of the gradeStructure to save.
     * @param gradeStructure the gradeStructure to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gradeStructure,
     * or with status {@code 400 (Bad Request)} if the gradeStructure is not valid,
     * or with status {@code 404 (Not Found)} if the gradeStructure is not found,
     * or with status {@code 500 (Internal Server Error)} if the gradeStructure couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/grade-structures/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GradeStructure> partialUpdateGradeStructure(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GradeStructure gradeStructure
    ) throws URISyntaxException {
        log.debug("REST request to partial update GradeStructure partially : {}, {}", id, gradeStructure);
        if (gradeStructure.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gradeStructure.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gradeStructureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GradeStructure> result = gradeStructureService.partialUpdate(gradeStructure);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gradeStructure.getId().toString())
        );
    }

    /**
     * {@code GET  /grade-structures} : get all the gradeStructures.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of gradeStructures in body.
     */
    @GetMapping("/grade-structures")
    public ResponseEntity<List<GradeStructure>> getAllGradeStructures(
        GradeStructureCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get GradeStructures by criteria: {}", criteria);
        Page<GradeStructure> page = gradeStructureQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /grade-structures/count} : count all the gradeStructures.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/grade-structures/count")
    public ResponseEntity<Long> countGradeStructures(GradeStructureCriteria criteria) {
        log.debug("REST request to count GradeStructures by criteria: {}", criteria);
        return ResponseEntity.ok().body(gradeStructureQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /grade-structures/:id} : get the "id" gradeStructure.
     *
     * @param id the id of the gradeStructure to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the gradeStructure, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/grade-structures/{id}")
    public ResponseEntity<GradeStructure> getGradeStructure(@PathVariable Long id) {
        log.debug("REST request to get GradeStructure : {}", id);
        Optional<GradeStructure> gradeStructure = gradeStructureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(gradeStructure);
    }

    /**
     * {@code DELETE  /grade-structures/:id} : delete the "id" gradeStructure.
     *
     * @param id the id of the gradeStructure to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/grade-structures/{id}")
    public ResponseEntity<Void> deleteGradeStructure(@PathVariable Long id) {
        log.debug("REST request to delete GradeStructure : {}", id);
        gradeStructureService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
