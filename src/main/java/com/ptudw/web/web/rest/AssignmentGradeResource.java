package com.ptudw.web.web.rest;

import com.ptudw.web.domain.AssignmentGrade;
import com.ptudw.web.repository.AssignmentGradeRepository;
import com.ptudw.web.service.AssignmentGradeQueryService;
import com.ptudw.web.service.AssignmentGradeService;
import com.ptudw.web.service.criteria.AssignmentGradeCriteria;
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
 * REST controller for managing {@link com.ptudw.web.domain.AssignmentGrade}.
 */
@RestController
@RequestMapping("/api")
public class AssignmentGradeResource {

    private final Logger log = LoggerFactory.getLogger(AssignmentGradeResource.class);

    private static final String ENTITY_NAME = "assignmentGrade";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AssignmentGradeService assignmentGradeService;

    private final AssignmentGradeRepository assignmentGradeRepository;

    private final AssignmentGradeQueryService assignmentGradeQueryService;

    public AssignmentGradeResource(
        AssignmentGradeService assignmentGradeService,
        AssignmentGradeRepository assignmentGradeRepository,
        AssignmentGradeQueryService assignmentGradeQueryService
    ) {
        this.assignmentGradeService = assignmentGradeService;
        this.assignmentGradeRepository = assignmentGradeRepository;
        this.assignmentGradeQueryService = assignmentGradeQueryService;
    }

    /**
     * {@code POST  /assignment-grades} : Create a new assignmentGrade.
     *
     * @param assignmentGrade the assignmentGrade to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new assignmentGrade, or with status {@code 400 (Bad Request)} if the assignmentGrade has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/assignment-grades")
    public ResponseEntity<AssignmentGrade> createAssignmentGrade(@Valid @RequestBody AssignmentGrade assignmentGrade)
        throws URISyntaxException {
        log.debug("REST request to save AssignmentGrade : {}", assignmentGrade);
        if (assignmentGrade.getId() != null) {
            throw new BadRequestAlertException("A new assignmentGrade cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AssignmentGrade result = assignmentGradeService.save(assignmentGrade);
        return ResponseEntity
            .created(new URI("/api/assignment-grades/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /assignment-grades/:id} : Updates an existing assignmentGrade.
     *
     * @param id the id of the assignmentGrade to save.
     * @param assignmentGrade the assignmentGrade to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assignmentGrade,
     * or with status {@code 400 (Bad Request)} if the assignmentGrade is not valid,
     * or with status {@code 500 (Internal Server Error)} if the assignmentGrade couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/assignment-grades/{id}")
    public ResponseEntity<AssignmentGrade> updateAssignmentGrade(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AssignmentGrade assignmentGrade
    ) throws URISyntaxException {
        log.debug("REST request to update AssignmentGrade : {}, {}", id, assignmentGrade);
        if (assignmentGrade.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assignmentGrade.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assignmentGradeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AssignmentGrade result = assignmentGradeService.update(assignmentGrade);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assignmentGrade.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /assignment-grades/:id} : Partial updates given fields of an existing assignmentGrade, field will ignore if it is null
     *
     * @param id the id of the assignmentGrade to save.
     * @param assignmentGrade the assignmentGrade to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assignmentGrade,
     * or with status {@code 400 (Bad Request)} if the assignmentGrade is not valid,
     * or with status {@code 404 (Not Found)} if the assignmentGrade is not found,
     * or with status {@code 500 (Internal Server Error)} if the assignmentGrade couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/assignment-grades/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AssignmentGrade> partialUpdateAssignmentGrade(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AssignmentGrade assignmentGrade
    ) throws URISyntaxException {
        log.debug("REST request to partial update AssignmentGrade partially : {}, {}", id, assignmentGrade);
        if (assignmentGrade.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assignmentGrade.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assignmentGradeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AssignmentGrade> result = assignmentGradeService.partialUpdate(assignmentGrade);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assignmentGrade.getId().toString())
        );
    }

    /**
     * {@code GET  /assignment-grades} : get all the assignmentGrades.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assignmentGrades in body.
     */
    @GetMapping("/assignment-grades")
    public ResponseEntity<List<AssignmentGrade>> getAllAssignmentGrades(
        AssignmentGradeCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get AssignmentGrades by criteria: {}", criteria);
        Page<AssignmentGrade> page = assignmentGradeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /assignment-grades/count} : count all the assignmentGrades.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/assignment-grades/count")
    public ResponseEntity<Long> countAssignmentGrades(AssignmentGradeCriteria criteria) {
        log.debug("REST request to count AssignmentGrades by criteria: {}", criteria);
        return ResponseEntity.ok().body(assignmentGradeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /assignment-grades/:id} : get the "id" assignmentGrade.
     *
     * @param id the id of the assignmentGrade to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the assignmentGrade, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/assignment-grades/{id}")
    public ResponseEntity<AssignmentGrade> getAssignmentGrade(@PathVariable Long id) {
        log.debug("REST request to get AssignmentGrade : {}", id);
        Optional<AssignmentGrade> assignmentGrade = assignmentGradeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(assignmentGrade);
    }

    /**
     * {@code DELETE  /assignment-grades/:id} : delete the "id" assignmentGrade.
     *
     * @param id the id of the assignmentGrade to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/assignment-grades/{id}")
    public ResponseEntity<Void> deleteAssignmentGrade(@PathVariable Long id) {
        log.debug("REST request to delete AssignmentGrade : {}", id);
        assignmentGradeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
