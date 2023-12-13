package com.ptudw.web.web.rest;

import com.ptudw.web.domain.GradeComposition;
import com.ptudw.web.repository.GradeCompositionRepository;
import com.ptudw.web.service.GradeCompositionQueryService;
import com.ptudw.web.service.GradeCompositionService;
import com.ptudw.web.service.criteria.GradeCompositionCriteria;
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
 * REST controller for managing {@link com.ptudw.web.domain.GradeComposition}.
 */
@RestController
@RequestMapping("/api")
public class GradeCompositionResource {

    private final Logger log = LoggerFactory.getLogger(GradeCompositionResource.class);

    private static final String ENTITY_NAME = "gradeComposition";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GradeCompositionService gradeCompositionService;

    private final GradeCompositionRepository gradeCompositionRepository;

    private final GradeCompositionQueryService gradeCompositionQueryService;

    public GradeCompositionResource(
        GradeCompositionService gradeCompositionService,
        GradeCompositionRepository gradeCompositionRepository,
        GradeCompositionQueryService gradeCompositionQueryService
    ) {
        this.gradeCompositionService = gradeCompositionService;
        this.gradeCompositionRepository = gradeCompositionRepository;
        this.gradeCompositionQueryService = gradeCompositionQueryService;
    }

    /**
     * {@code POST  /grade-compositions} : Create a new gradeComposition.
     *
     * @param gradeComposition the gradeComposition to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new gradeComposition, or with status {@code 400 (Bad Request)} if the gradeComposition has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/grade-compositions")
    public ResponseEntity<GradeComposition> createGradeComposition(@Valid @RequestBody GradeComposition gradeComposition)
        throws URISyntaxException {
        log.debug("REST request to save GradeComposition : {}", gradeComposition);
        if (gradeComposition.getId() != null) {
            throw new BadRequestAlertException("A new gradeComposition cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GradeComposition result = gradeCompositionService.save(gradeComposition);
        return ResponseEntity
            .created(new URI("/api/grade-compositions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /grade-compositions/:id} : Updates an existing gradeComposition.
     *
     * @param id the id of the gradeComposition to save.
     * @param gradeComposition the gradeComposition to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gradeComposition,
     * or with status {@code 400 (Bad Request)} if the gradeComposition is not valid,
     * or with status {@code 500 (Internal Server Error)} if the gradeComposition couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/grade-compositions/{id}")
    public ResponseEntity<GradeComposition> updateGradeComposition(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody GradeComposition gradeComposition
    ) throws URISyntaxException {
        log.debug("REST request to update GradeComposition : {}, {}", id, gradeComposition);
        if (gradeComposition.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gradeComposition.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gradeCompositionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GradeComposition result = gradeCompositionService.update(gradeComposition);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gradeComposition.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /grade-compositions/:id} : Partial updates given fields of an existing gradeComposition, field will ignore if it is null
     *
     * @param id the id of the gradeComposition to save.
     * @param gradeComposition the gradeComposition to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gradeComposition,
     * or with status {@code 400 (Bad Request)} if the gradeComposition is not valid,
     * or with status {@code 404 (Not Found)} if the gradeComposition is not found,
     * or with status {@code 500 (Internal Server Error)} if the gradeComposition couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/grade-compositions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GradeComposition> partialUpdateGradeComposition(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GradeComposition gradeComposition
    ) throws URISyntaxException {
        log.debug("REST request to partial update GradeComposition partially : {}, {}", id, gradeComposition);
        if (gradeComposition.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gradeComposition.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gradeCompositionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GradeComposition> result = gradeCompositionService.partialUpdate(gradeComposition);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gradeComposition.getId().toString())
        );
    }

    /**
     * {@code GET  /grade-compositions} : get all the gradeCompositions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of gradeCompositions in body.
     */
    @GetMapping("/grade-compositions")
    public ResponseEntity<List<GradeComposition>> getAllGradeCompositions(
        GradeCompositionCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get GradeCompositions by criteria: {}", criteria);
        Page<GradeComposition> page = gradeCompositionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /grade-compositions/count} : count all the gradeCompositions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/grade-compositions/count")
    public ResponseEntity<Long> countGradeCompositions(GradeCompositionCriteria criteria) {
        log.debug("REST request to count GradeCompositions by criteria: {}", criteria);
        return ResponseEntity.ok().body(gradeCompositionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /grade-compositions/:id} : get the "id" gradeComposition.
     *
     * @param id the id of the gradeComposition to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the gradeComposition, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/grade-compositions/{id}")
    public ResponseEntity<GradeComposition> getGradeComposition(@PathVariable Long id) {
        log.debug("REST request to get GradeComposition : {}", id);
        Optional<GradeComposition> gradeComposition = gradeCompositionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(gradeComposition);
    }

    /**
     * {@code DELETE  /grade-compositions/:id} : delete the "id" gradeComposition.
     *
     * @param id the id of the gradeComposition to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/grade-compositions/{id}")
    public ResponseEntity<Void> deleteGradeComposition(@PathVariable Long id) {
        log.debug("REST request to delete GradeComposition : {}", id);
        gradeCompositionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
