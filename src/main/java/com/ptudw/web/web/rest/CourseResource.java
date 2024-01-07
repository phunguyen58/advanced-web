package com.ptudw.web.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ptudw.web.domain.Course;
import com.ptudw.web.domain.User;
import com.ptudw.web.domain.UserCourse;
import com.ptudw.web.repository.CourseRepository;
import com.ptudw.web.service.CourseQueryService;
import com.ptudw.web.service.CourseService;
import com.ptudw.web.service.MailService;
import com.ptudw.web.service.UserCourseQueryService;
import com.ptudw.web.service.UserService;
import com.ptudw.web.service.criteria.CourseCriteria;
import com.ptudw.web.service.criteria.UserCourseCriteria;
import com.ptudw.web.service.dto.AdminUserDTO;
import com.ptudw.web.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ptudw.web.domain.Course}.
 */
@RestController
@RequestMapping("/api")
public class CourseResource {

    private final Logger log = LoggerFactory.getLogger(CourseResource.class);

    private static final String ENTITY_NAME = "course";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CourseService courseService;

    private final CourseRepository courseRepository;

    private final CourseQueryService courseQueryService;

    private final UserService userService;

    private final UserCourseQueryService userCourseQueryService;

    private final MailService mailService;

    public CourseResource(
        CourseService courseService,
        CourseRepository courseRepository,
        CourseQueryService courseQueryService,
        UserService userService,
        UserCourseQueryService userCourseQueryService,
        MailService mailService
    ) {
        this.courseService = courseService;
        this.courseRepository = courseRepository;
        this.courseQueryService = courseQueryService;
        this.userService = userService;
        this.userCourseQueryService = userCourseQueryService;
        this.mailService = mailService;
    }

    /**
     * {@code POST  /courses} : Create a new course.
     *
     * @param course the course to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new course, or with status {@code 400 (Bad Request)} if the course has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/courses")
    public ResponseEntity<Course> createCourse(@RequestBody Course course) throws URISyntaxException {
        log.debug("REST request to save Course : {}", course);
        if (course.getId() != null) {
            throw new BadRequestAlertException("A new course cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (courseService.findOneByCode(course.getCode()).isPresent()) {
            throw new BadRequestAlertException("A new course cannot already have an code", ENTITY_NAME, "codeexists");
        }
        Course result = courseService.save(course);
        return ResponseEntity
            .created(new URI("/api/courses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /courses/:id} : Updates an existing course.
     *
     * @param id the id of the course to save.
     * @param course the course to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated course,
     * or with status {@code 400 (Bad Request)} if the course is not valid,
     * or with status {@code 500 (Internal Server Error)} if the course couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/courses/{id}")
    public ResponseEntity<Course> updateCourse(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Course course
    ) throws URISyntaxException {
        log.debug("REST request to update Course : {}, {}", id, course);
        if (course.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, course.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!courseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Course result = courseService.update(course);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, course.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /courses/:id} : Partial updates given fields of an existing course, field will ignore if it is null
     *
     * @param id the id of the course to save.
     * @param course the course to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated course,
     * or with status {@code 400 (Bad Request)} if the course is not valid,
     * or with status {@code 404 (Not Found)} if the course is not found,
     * or with status {@code 500 (Internal Server Error)} if the course couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/courses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Course> partialUpdateCourse(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Course course
    ) throws URISyntaxException {
        log.debug("REST request to partial update Course partially : {}, {}", id, course);
        if (course.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, course.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!courseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Course> result = courseService.partialUpdate(course);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, course.getId().toString())
        );
    }

    /**
     * {@code GET  /courses} : get all the courses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of courses in body.
     */
    @GetMapping("/courses")
    public ResponseEntity<List<Course>> getAllCourses(
        CourseCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Courses by criteria: {}", criteria);
        Page<Course> page = courseQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /courses/count} : count all the courses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/courses/count")
    public ResponseEntity<Long> countCourses(CourseCriteria criteria) {
        log.debug("REST request to count Courses by criteria: {}", criteria);
        return ResponseEntity.ok().body(courseQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /courses/:id} : get the "id" course.
     *
     * @param id the id of the course to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the course, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/courses/{id}")
    public ResponseEntity<Course> getCourse(@PathVariable Long id) {
        log.debug("REST request to get Course : {}", id);
        Optional<Course> course = courseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(course);
    }

    /**
     * {@code GET  /courses/:id} : get the "id" course.
     *
     * @param id the id of the course to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the course, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/courses/joined-course/{userId}")
    //TODO: get all joined courses by userId
    public ResponseEntity<Course> getJoinedCoursesByUserId(@PathVariable Long userId) {
        log.debug("REST request to get all joined courses by userId : {}", userId);
        Optional<Course> course = courseService.findOne(userId);
        return ResponseUtil.wrapOrNotFound(course);
    }

    @GetMapping("/courses/my-courses")
    public ResponseEntity<List<Course>> getJoinedCoursesOfCurrentUser(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        User user = userService
            .getUserWithAuthorities()
            .orElseThrow(() -> new BadRequestAlertException("Invalid user", ENTITY_NAME, "userinvalid"));

        Long userId = user.getId();
        log.debug("REST request to get all joined courses by userId : {}", userId);

        UserCourseCriteria userCourseCriteria = new UserCourseCriteria();
        LongFilter userIdFilter = new LongFilter();
        userIdFilter.setEquals(userId);
        userCourseCriteria.setUserId(userIdFilter);

        List<UserCourse> userCourses = userCourseQueryService.findByCriteria(userCourseCriteria);

        List<Long> joinedCourseIds = userCourses.stream().map(UserCourse::getCourseId).collect(Collectors.toList());

        Page<Course> joinedCoursesPage = courseService.findAllByIds(joinedCourseIds, pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
            ServletUriComponentsBuilder.fromCurrentRequest(),
            joinedCoursesPage
        );
        return ResponseEntity.ok().headers(headers).body(joinedCoursesPage.getContent());
    }

    /**
     * {@code DELETE  /courses/:id} : delete the "id" course.
     *
     * @param id the id of the course to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/courses/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        log.debug("REST request to delete Course : {}", id);
        courseService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/courses/invitation/{invitationCode}")
    public ResponseEntity<Course> joinCourseByInvitationCode(@PathVariable String invitationCode) throws URISyntaxException {
        log.debug("REST request to join Course by invitation code: {}", invitationCode);
        CourseCriteria criteria = new CourseCriteria();
        StringFilter invitationCodeFilter = new StringFilter();
        invitationCodeFilter.setEquals(invitationCode);
        criteria.setInvitationCode(invitationCodeFilter);

        Optional<User> user = userService.getUserWithAuthorities();
        if (!user.isPresent()) {
            throw new BadRequestAlertException("Invalid user", ENTITY_NAME, "userinvalid");
        }

        Course course = Optional
            .ofNullable(courseQueryService.findByCriteria(criteria))
            .orElse(Collections.emptyList())
            .stream()
            .findFirst()
            .orElse(null);
        if (course == null) {
            throw new BadRequestAlertException("Invalid invitation code", ENTITY_NAME, "invitationcodeinvalid");
        }

        boolean isExpiredInvitation = course.getExpirationDate().isBefore(ZonedDateTime.now());
        if (isExpiredInvitation) {
            throw new BadRequestAlertException("Invitation code is expired", ENTITY_NAME, "invitationcodeexpired");
        }

        UserCourseCriteria userCourseCriteria = new UserCourseCriteria();
        LongFilter userIdFilter = new LongFilter();
        userIdFilter.setEquals(user.get().getId());
        userCourseCriteria.setUserId(userIdFilter);
        LongFilter courseIdFilter = new LongFilter();
        courseIdFilter.setEquals(course.getId());
        userCourseCriteria.setCourseId(courseIdFilter);

        Optional<UserCourse> userCourse = userCourseQueryService.findByCriteria(userCourseCriteria).stream().findFirst();
        if (userCourse.isPresent()) {
            throw new BadRequestAlertException("User already joined this course", ENTITY_NAME, "useralreadyjoined");
        }

        this.courseService.joinCourseByInvitationCode(course, user.get());

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createAlert(applicationName, "webApp.course.coursejoinedsuccessfully", course.getId().toString()))
            .body(course);
    }

    @PostMapping("/courses/send-invitation/{invitationCode}")
    public ResponseEntity<Void> sendInvitation(@PathVariable String invitationCode, @RequestBody List<String> emails)
        throws URISyntaxException, JsonMappingException, JsonProcessingException {
        log.debug("REST request to send invitation: {}", invitationCode);

        Optional
            .ofNullable(emails)
            .orElse(Collections.emptyList())
            .forEach(email -> {
                mailService.sendInviteToClassMail(userService.getUserWithAuthorities().orElse(null), email, invitationCode);
            });

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createAlert(applicationName, "webApp.course.inviteSuccess", invitationCode))
            .body(null);
    }

    @GetMapping("/users/course/{courseId}")
    public ResponseEntity<List<AdminUserDTO>> getAllUsersByCourseId(
        @PathVariable Long courseId,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get all User in a course: {}", courseId);

        UserCourseCriteria userCourseCriteria = new UserCourseCriteria();
        LongFilter courseIdFilter = new LongFilter();
        courseIdFilter.setEquals(courseId);
        userCourseCriteria.setCourseId(courseIdFilter);
        List<UserCourse> userCourses = userCourseQueryService.findByCriteria(userCourseCriteria);

        List<Long> userIds = userCourses.stream().map(UserCourse::getUserId).collect(Collectors.toList());

        final Page<AdminUserDTO> page = userService.findAllByIds(userIds, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
}
