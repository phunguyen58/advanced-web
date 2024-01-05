package com.ptudw.web.web.rest;

import com.ptudw.web.domain.GradeReview;
import com.ptudw.web.repository.GradeReviewRepository;
import com.ptudw.web.service.GradeReviewQueryService;
import com.ptudw.web.service.GradeReviewService;
import com.ptudw.web.service.criteria.GradeReviewCriteria;
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
 * REST controller for managing {@link com.ptudw.web.domain.GradeReview}.
 */
@RestController
@RequestMapping("/api")
public class GradeReviewResource {

    private final Logger log = LoggerFactory.getLogger(GradeReviewResource.class);

    private static final String ENTITY_NAME = "gradeReview";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GradeReviewService gradeReviewService;

    private final GradeReviewRepository gradeReviewRepository;

    private final GradeReviewQueryService gradeReviewQueryService;

    public GradeReviewResource(
        GradeReviewService gradeReviewService,
        GradeReviewRepository gradeReviewRepository,
        GradeReviewQueryService gradeReviewQueryService
    ) {
        this.gradeReviewService = gradeReviewService;
        this.gradeReviewRepository = gradeReviewRepository;
        this.gradeReviewQueryService = gradeReviewQueryService;
    }

    /**
     * {@code POST  /grade-reviews} : Create a new gradeReview.
     *
     * @param gradeReview the gradeReview to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new gradeReview, or with status {@code 400 (Bad Request)} if the gradeReview has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/grade-reviews")
    public ResponseEntity<GradeReview> createGradeReview(@Valid @RequestBody GradeReview gradeReview) throws URISyntaxException {
        log.debug("REST request to save GradeReview : {}", gradeReview);
        if (gradeReview.getId() != null) {
            throw new BadRequestAlertException("A new gradeReview cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GradeReview result = gradeReviewService.save(gradeReview);
        return ResponseEntity
            .created(new URI("/api/grade-reviews/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /grade-reviews/:id} : Updates an existing gradeReview.
     *
     * @param id the id of the gradeReview to save.
     * @param gradeReview the gradeReview to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gradeReview,
     * or with status {@code 400 (Bad Request)} if the gradeReview is not valid,
     * or with status {@code 500 (Internal Server Error)} if the gradeReview couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/grade-reviews/{id}")
    public ResponseEntity<GradeReview> updateGradeReview(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody GradeReview gradeReview
    ) throws URISyntaxException {
        log.debug("REST request to update GradeReview : {}, {}", id, gradeReview);
        if (gradeReview.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gradeReview.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gradeReviewRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GradeReview result = gradeReviewService.update(gradeReview);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gradeReview.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /grade-reviews/:id} : Partial updates given fields of an existing gradeReview, field will ignore if it is null
     *
     * @param id the id of the gradeReview to save.
     * @param gradeReview the gradeReview to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gradeReview,
     * or with status {@code 400 (Bad Request)} if the gradeReview is not valid,
     * or with status {@code 404 (Not Found)} if the gradeReview is not found,
     * or with status {@code 500 (Internal Server Error)} if the gradeReview couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/grade-reviews/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GradeReview> partialUpdateGradeReview(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GradeReview gradeReview
    ) throws URISyntaxException {
        log.debug("REST request to partial update GradeReview partially : {}, {}", id, gradeReview);
        if (gradeReview.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gradeReview.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gradeReviewRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GradeReview> result = gradeReviewService.partialUpdate(gradeReview);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gradeReview.getId().toString())
        );
    }

    /**
     * {@code GET  /grade-reviews} : get all the gradeReviews.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of gradeReviews in body.
     */
    @GetMapping("/grade-reviews")
    public ResponseEntity<List<GradeReview>> getAllGradeReviews(
        GradeReviewCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get GradeReviews by criteria: {}", criteria);
        Page<GradeReview> page = gradeReviewQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /grade-reviews/count} : count all the gradeReviews.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/grade-reviews/count")
    public ResponseEntity<Long> countGradeReviews(GradeReviewCriteria criteria) {
        log.debug("REST request to count GradeReviews by criteria: {}", criteria);
        return ResponseEntity.ok().body(gradeReviewQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /grade-reviews/:id} : get the "id" gradeReview.
     *
     * @param id the id of the gradeReview to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the gradeReview, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/grade-reviews/{id}")
    public ResponseEntity<GradeReview> getGradeReview(@PathVariable Long id) {
        log.debug("REST request to get GradeReview : {}", id);
        Optional<GradeReview> gradeReview = gradeReviewService.findOne(id);
        return ResponseUtil.wrapOrNotFound(gradeReview);
    }

    /**
     * {@code DELETE  /grade-reviews/:id} : delete the "id" gradeReview.
     *
     * @param id the id of the gradeReview to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/grade-reviews/{id}")
    public ResponseEntity<Void> deleteGradeReview(@PathVariable Long id) {
        log.debug("REST request to delete GradeReview : {}", id);
        gradeReviewService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
