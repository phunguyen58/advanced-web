package com.ptudw.web.web.rest;

import com.ptudw.web.domain.CourseGrade;
import com.ptudw.web.repository.CourseGradeRepository;
import com.ptudw.web.service.CourseGradeQueryService;
import com.ptudw.web.service.CourseGradeService;
import com.ptudw.web.service.criteria.CourseGradeCriteria;
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
 * REST controller for managing {@link com.ptudw.web.domain.CourseGrade}.
 */
@RestController
@RequestMapping("/api")
public class CourseGradeResource {

    private final Logger log = LoggerFactory.getLogger(CourseGradeResource.class);

    private static final String ENTITY_NAME = "courseGrade";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CourseGradeService courseGradeService;

    private final CourseGradeRepository courseGradeRepository;

    private final CourseGradeQueryService courseGradeQueryService;

    public CourseGradeResource(
        CourseGradeService courseGradeService,
        CourseGradeRepository courseGradeRepository,
        CourseGradeQueryService courseGradeQueryService
    ) {
        this.courseGradeService = courseGradeService;
        this.courseGradeRepository = courseGradeRepository;
        this.courseGradeQueryService = courseGradeQueryService;
    }

    /**
     * {@code POST  /course-grades} : Create a new courseGrade.
     *
     * @param courseGrade the courseGrade to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new courseGrade, or with status {@code 400 (Bad Request)} if the courseGrade has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/course-grades")
    public ResponseEntity<CourseGrade> createCourseGrade(@Valid @RequestBody CourseGrade courseGrade) throws URISyntaxException {
        log.debug("REST request to save CourseGrade : {}", courseGrade);
        if (courseGrade.getId() != null) {
            throw new BadRequestAlertException("A new courseGrade cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CourseGrade result = courseGradeService.save(courseGrade);
        return ResponseEntity
            .created(new URI("/api/course-grades/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /course-grades/:id} : Updates an existing courseGrade.
     *
     * @param id the id of the courseGrade to save.
     * @param courseGrade the courseGrade to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courseGrade,
     * or with status {@code 400 (Bad Request)} if the courseGrade is not valid,
     * or with status {@code 500 (Internal Server Error)} if the courseGrade couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/course-grades/{id}")
    public ResponseEntity<CourseGrade> updateCourseGrade(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CourseGrade courseGrade
    ) throws URISyntaxException {
        log.debug("REST request to update CourseGrade : {}, {}", id, courseGrade);
        if (courseGrade.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, courseGrade.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!courseGradeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CourseGrade result = courseGradeService.update(courseGrade);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, courseGrade.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /course-grades/:id} : Partial updates given fields of an existing courseGrade, field will ignore if it is null
     *
     * @param id the id of the courseGrade to save.
     * @param courseGrade the courseGrade to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courseGrade,
     * or with status {@code 400 (Bad Request)} if the courseGrade is not valid,
     * or with status {@code 404 (Not Found)} if the courseGrade is not found,
     * or with status {@code 500 (Internal Server Error)} if the courseGrade couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/course-grades/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CourseGrade> partialUpdateCourseGrade(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CourseGrade courseGrade
    ) throws URISyntaxException {
        log.debug("REST request to partial update CourseGrade partially : {}, {}", id, courseGrade);
        if (courseGrade.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, courseGrade.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!courseGradeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CourseGrade> result = courseGradeService.partialUpdate(courseGrade);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, courseGrade.getId().toString())
        );
    }

    /**
     * {@code GET  /course-grades} : get all the courseGrades.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of courseGrades in body.
     */
    @GetMapping("/course-grades")
    public ResponseEntity<List<CourseGrade>> getAllCourseGrades(
        CourseGradeCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get CourseGrades by criteria: {}", criteria);
        Page<CourseGrade> page = courseGradeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /course-grades/count} : count all the courseGrades.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/course-grades/count")
    public ResponseEntity<Long> countCourseGrades(CourseGradeCriteria criteria) {
        log.debug("REST request to count CourseGrades by criteria: {}", criteria);
        return ResponseEntity.ok().body(courseGradeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /course-grades/:id} : get the "id" courseGrade.
     *
     * @param id the id of the courseGrade to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the courseGrade, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/course-grades/{id}")
    public ResponseEntity<CourseGrade> getCourseGrade(@PathVariable Long id) {
        log.debug("REST request to get CourseGrade : {}", id);
        Optional<CourseGrade> courseGrade = courseGradeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(courseGrade);
    }

    /**
     * {@code DELETE  /course-grades/:id} : delete the "id" courseGrade.
     *
     * @param id the id of the courseGrade to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/course-grades/{id}")
    public ResponseEntity<Void> deleteCourseGrade(@PathVariable Long id) {
        log.debug("REST request to delete CourseGrade : {}", id);
        courseGradeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
