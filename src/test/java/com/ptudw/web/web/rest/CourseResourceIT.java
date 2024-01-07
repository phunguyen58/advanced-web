package com.ptudw.web.web.rest;

import static com.ptudw.web.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ptudw.web.IntegrationTest;
import com.ptudw.web.domain.Assignment;
import com.ptudw.web.domain.Course;
import com.ptudw.web.domain.GradeComposition;
import com.ptudw.web.repository.CourseRepository;
import com.ptudw.web.service.criteria.CourseCriteria;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CourseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CourseResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_OWNER_ID = 1L;
    private static final Long UPDATED_OWNER_ID = 2L;
    private static final Long SMALLER_OWNER_ID = 1L - 1L;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_INVITATION_CODE = "AAAAAAAAAA";
    private static final String UPDATED_INVITATION_CODE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_EXPIRATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_EXPIRATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_EXPIRATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_LAST_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_MODIFIED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_LAST_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/courses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCourseMockMvc;

    private Course course;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Course createEntity(EntityManager em) {
        Course course = new Course()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .ownerId(DEFAULT_OWNER_ID)
            .description(DEFAULT_DESCRIPTION)
            .invitationCode(DEFAULT_INVITATION_CODE)
            .expirationDate(DEFAULT_EXPIRATION_DATE)
            .isDeleted(DEFAULT_IS_DELETED)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return course;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Course createUpdatedEntity(EntityManager em) {
        Course course = new Course()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .ownerId(UPDATED_OWNER_ID)
            .description(UPDATED_DESCRIPTION)
            .invitationCode(UPDATED_INVITATION_CODE)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .isDeleted(UPDATED_IS_DELETED)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return course;
    }

    @BeforeEach
    public void initTest() {
        course = createEntity(em);
    }

    @Test
    @Transactional
    void createCourse() throws Exception {
        int databaseSizeBeforeCreate = courseRepository.findAll().size();
        // Create the Course
        restCourseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isCreated());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeCreate + 1);
        Course testCourse = courseList.get(courseList.size() - 1);
        assertThat(testCourse.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testCourse.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCourse.getOwnerId()).isEqualTo(DEFAULT_OWNER_ID);
        assertThat(testCourse.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCourse.getInvitationCode()).isEqualTo(DEFAULT_INVITATION_CODE);
        assertThat(testCourse.getExpirationDate()).isEqualTo(DEFAULT_EXPIRATION_DATE);
        assertThat(testCourse.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testCourse.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testCourse.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testCourse.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testCourse.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createCourseWithExistingId() throws Exception {
        // Create the Course with an existing ID
        course.setId(1L);

        int databaseSizeBeforeCreate = courseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isBadRequest());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().size();
        // set the field null
        course.setCode(null);

        // Create the Course, which fails.

        restCourseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isBadRequest());

        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().size();
        // set the field null
        course.setName(null);

        // Create the Course, which fails.

        restCourseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isBadRequest());

        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOwnerIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().size();
        // set the field null
        course.setOwnerId(null);

        // Create the Course, which fails.

        restCourseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isBadRequest());

        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkInvitationCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().size();
        // set the field null
        course.setInvitationCode(null);

        // Create the Course, which fails.

        restCourseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isBadRequest());

        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().size();
        // set the field null
        course.setCreatedBy(null);

        // Create the Course, which fails.

        restCourseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isBadRequest());

        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().size();
        // set the field null
        course.setCreatedDate(null);

        // Create the Course, which fails.

        restCourseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isBadRequest());

        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastModifiedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().size();
        // set the field null
        course.setLastModifiedBy(null);

        // Create the Course, which fails.

        restCourseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isBadRequest());

        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastModifiedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().size();
        // set the field null
        course.setLastModifiedDate(null);

        // Create the Course, which fails.

        restCourseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isBadRequest());

        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCourses() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList
        restCourseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(course.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].ownerId").value(hasItem(DEFAULT_OWNER_ID.intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].invitationCode").value(hasItem(DEFAULT_INVITATION_CODE)))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(sameInstant(DEFAULT_EXPIRATION_DATE))))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(sameInstant(DEFAULT_LAST_MODIFIED_DATE))));
    }

    @Test
    @Transactional
    void getCourse() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get the course
        restCourseMockMvc
            .perform(get(ENTITY_API_URL_ID, course.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(course.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.ownerId").value(DEFAULT_OWNER_ID.intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.invitationCode").value(DEFAULT_INVITATION_CODE))
            .andExpect(jsonPath("$.expirationDate").value(sameInstant(DEFAULT_EXPIRATION_DATE)))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(sameInstant(DEFAULT_LAST_MODIFIED_DATE)));
    }

    @Test
    @Transactional
    void getCoursesByIdFiltering() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        Long id = course.getId();

        defaultCourseShouldBeFound("id.equals=" + id);
        defaultCourseShouldNotBeFound("id.notEquals=" + id);

        defaultCourseShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCourseShouldNotBeFound("id.greaterThan=" + id);

        defaultCourseShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCourseShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCoursesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where code equals to DEFAULT_CODE
        defaultCourseShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the courseList where code equals to UPDATED_CODE
        defaultCourseShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCoursesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where code in DEFAULT_CODE or UPDATED_CODE
        defaultCourseShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the courseList where code equals to UPDATED_CODE
        defaultCourseShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCoursesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where code is not null
        defaultCourseShouldBeFound("code.specified=true");

        // Get all the courseList where code is null
        defaultCourseShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllCoursesByCodeContainsSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where code contains DEFAULT_CODE
        defaultCourseShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the courseList where code contains UPDATED_CODE
        defaultCourseShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCoursesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where code does not contain DEFAULT_CODE
        defaultCourseShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the courseList where code does not contain UPDATED_CODE
        defaultCourseShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCoursesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where name equals to DEFAULT_NAME
        defaultCourseShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the courseList where name equals to UPDATED_NAME
        defaultCourseShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCoursesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCourseShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the courseList where name equals to UPDATED_NAME
        defaultCourseShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCoursesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where name is not null
        defaultCourseShouldBeFound("name.specified=true");

        // Get all the courseList where name is null
        defaultCourseShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllCoursesByNameContainsSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where name contains DEFAULT_NAME
        defaultCourseShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the courseList where name contains UPDATED_NAME
        defaultCourseShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCoursesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where name does not contain DEFAULT_NAME
        defaultCourseShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the courseList where name does not contain UPDATED_NAME
        defaultCourseShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCoursesByOwnerIdIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where ownerId equals to DEFAULT_OWNER_ID
        defaultCourseShouldBeFound("ownerId.equals=" + DEFAULT_OWNER_ID);

        // Get all the courseList where ownerId equals to UPDATED_OWNER_ID
        defaultCourseShouldNotBeFound("ownerId.equals=" + UPDATED_OWNER_ID);
    }

    @Test
    @Transactional
    void getAllCoursesByOwnerIdIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where ownerId in DEFAULT_OWNER_ID or UPDATED_OWNER_ID
        defaultCourseShouldBeFound("ownerId.in=" + DEFAULT_OWNER_ID + "," + UPDATED_OWNER_ID);

        // Get all the courseList where ownerId equals to UPDATED_OWNER_ID
        defaultCourseShouldNotBeFound("ownerId.in=" + UPDATED_OWNER_ID);
    }

    @Test
    @Transactional
    void getAllCoursesByOwnerIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where ownerId is not null
        defaultCourseShouldBeFound("ownerId.specified=true");

        // Get all the courseList where ownerId is null
        defaultCourseShouldNotBeFound("ownerId.specified=false");
    }

    @Test
    @Transactional
    void getAllCoursesByOwnerIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where ownerId is greater than or equal to DEFAULT_OWNER_ID
        defaultCourseShouldBeFound("ownerId.greaterThanOrEqual=" + DEFAULT_OWNER_ID);

        // Get all the courseList where ownerId is greater than or equal to UPDATED_OWNER_ID
        defaultCourseShouldNotBeFound("ownerId.greaterThanOrEqual=" + UPDATED_OWNER_ID);
    }

    @Test
    @Transactional
    void getAllCoursesByOwnerIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where ownerId is less than or equal to DEFAULT_OWNER_ID
        defaultCourseShouldBeFound("ownerId.lessThanOrEqual=" + DEFAULT_OWNER_ID);

        // Get all the courseList where ownerId is less than or equal to SMALLER_OWNER_ID
        defaultCourseShouldNotBeFound("ownerId.lessThanOrEqual=" + SMALLER_OWNER_ID);
    }

    @Test
    @Transactional
    void getAllCoursesByOwnerIdIsLessThanSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where ownerId is less than DEFAULT_OWNER_ID
        defaultCourseShouldNotBeFound("ownerId.lessThan=" + DEFAULT_OWNER_ID);

        // Get all the courseList where ownerId is less than UPDATED_OWNER_ID
        defaultCourseShouldBeFound("ownerId.lessThan=" + UPDATED_OWNER_ID);
    }

    @Test
    @Transactional
    void getAllCoursesByOwnerIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where ownerId is greater than DEFAULT_OWNER_ID
        defaultCourseShouldNotBeFound("ownerId.greaterThan=" + DEFAULT_OWNER_ID);

        // Get all the courseList where ownerId is greater than SMALLER_OWNER_ID
        defaultCourseShouldBeFound("ownerId.greaterThan=" + SMALLER_OWNER_ID);
    }

    @Test
    @Transactional
    void getAllCoursesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where description equals to DEFAULT_DESCRIPTION
        defaultCourseShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the courseList where description equals to UPDATED_DESCRIPTION
        defaultCourseShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCoursesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultCourseShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the courseList where description equals to UPDATED_DESCRIPTION
        defaultCourseShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCoursesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where description is not null
        defaultCourseShouldBeFound("description.specified=true");

        // Get all the courseList where description is null
        defaultCourseShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllCoursesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where description contains DEFAULT_DESCRIPTION
        defaultCourseShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the courseList where description contains UPDATED_DESCRIPTION
        defaultCourseShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCoursesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where description does not contain DEFAULT_DESCRIPTION
        defaultCourseShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the courseList where description does not contain UPDATED_DESCRIPTION
        defaultCourseShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCoursesByInvitationCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where invitationCode equals to DEFAULT_INVITATION_CODE
        defaultCourseShouldBeFound("invitationCode.equals=" + DEFAULT_INVITATION_CODE);

        // Get all the courseList where invitationCode equals to UPDATED_INVITATION_CODE
        defaultCourseShouldNotBeFound("invitationCode.equals=" + UPDATED_INVITATION_CODE);
    }

    @Test
    @Transactional
    void getAllCoursesByInvitationCodeIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where invitationCode in DEFAULT_INVITATION_CODE or UPDATED_INVITATION_CODE
        defaultCourseShouldBeFound("invitationCode.in=" + DEFAULT_INVITATION_CODE + "," + UPDATED_INVITATION_CODE);

        // Get all the courseList where invitationCode equals to UPDATED_INVITATION_CODE
        defaultCourseShouldNotBeFound("invitationCode.in=" + UPDATED_INVITATION_CODE);
    }

    @Test
    @Transactional
    void getAllCoursesByInvitationCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where invitationCode is not null
        defaultCourseShouldBeFound("invitationCode.specified=true");

        // Get all the courseList where invitationCode is null
        defaultCourseShouldNotBeFound("invitationCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCoursesByInvitationCodeContainsSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where invitationCode contains DEFAULT_INVITATION_CODE
        defaultCourseShouldBeFound("invitationCode.contains=" + DEFAULT_INVITATION_CODE);

        // Get all the courseList where invitationCode contains UPDATED_INVITATION_CODE
        defaultCourseShouldNotBeFound("invitationCode.contains=" + UPDATED_INVITATION_CODE);
    }

    @Test
    @Transactional
    void getAllCoursesByInvitationCodeNotContainsSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where invitationCode does not contain DEFAULT_INVITATION_CODE
        defaultCourseShouldNotBeFound("invitationCode.doesNotContain=" + DEFAULT_INVITATION_CODE);

        // Get all the courseList where invitationCode does not contain UPDATED_INVITATION_CODE
        defaultCourseShouldBeFound("invitationCode.doesNotContain=" + UPDATED_INVITATION_CODE);
    }

    @Test
    @Transactional
    void getAllCoursesByExpirationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where expirationDate equals to DEFAULT_EXPIRATION_DATE
        defaultCourseShouldBeFound("expirationDate.equals=" + DEFAULT_EXPIRATION_DATE);

        // Get all the courseList where expirationDate equals to UPDATED_EXPIRATION_DATE
        defaultCourseShouldNotBeFound("expirationDate.equals=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllCoursesByExpirationDateIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where expirationDate in DEFAULT_EXPIRATION_DATE or UPDATED_EXPIRATION_DATE
        defaultCourseShouldBeFound("expirationDate.in=" + DEFAULT_EXPIRATION_DATE + "," + UPDATED_EXPIRATION_DATE);

        // Get all the courseList where expirationDate equals to UPDATED_EXPIRATION_DATE
        defaultCourseShouldNotBeFound("expirationDate.in=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllCoursesByExpirationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where expirationDate is not null
        defaultCourseShouldBeFound("expirationDate.specified=true");

        // Get all the courseList where expirationDate is null
        defaultCourseShouldNotBeFound("expirationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCoursesByExpirationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where expirationDate is greater than or equal to DEFAULT_EXPIRATION_DATE
        defaultCourseShouldBeFound("expirationDate.greaterThanOrEqual=" + DEFAULT_EXPIRATION_DATE);

        // Get all the courseList where expirationDate is greater than or equal to UPDATED_EXPIRATION_DATE
        defaultCourseShouldNotBeFound("expirationDate.greaterThanOrEqual=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllCoursesByExpirationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where expirationDate is less than or equal to DEFAULT_EXPIRATION_DATE
        defaultCourseShouldBeFound("expirationDate.lessThanOrEqual=" + DEFAULT_EXPIRATION_DATE);

        // Get all the courseList where expirationDate is less than or equal to SMALLER_EXPIRATION_DATE
        defaultCourseShouldNotBeFound("expirationDate.lessThanOrEqual=" + SMALLER_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllCoursesByExpirationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where expirationDate is less than DEFAULT_EXPIRATION_DATE
        defaultCourseShouldNotBeFound("expirationDate.lessThan=" + DEFAULT_EXPIRATION_DATE);

        // Get all the courseList where expirationDate is less than UPDATED_EXPIRATION_DATE
        defaultCourseShouldBeFound("expirationDate.lessThan=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllCoursesByExpirationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where expirationDate is greater than DEFAULT_EXPIRATION_DATE
        defaultCourseShouldNotBeFound("expirationDate.greaterThan=" + DEFAULT_EXPIRATION_DATE);

        // Get all the courseList where expirationDate is greater than SMALLER_EXPIRATION_DATE
        defaultCourseShouldBeFound("expirationDate.greaterThan=" + SMALLER_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllCoursesByIsDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where isDeleted equals to DEFAULT_IS_DELETED
        defaultCourseShouldBeFound("isDeleted.equals=" + DEFAULT_IS_DELETED);

        // Get all the courseList where isDeleted equals to UPDATED_IS_DELETED
        defaultCourseShouldNotBeFound("isDeleted.equals=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllCoursesByIsDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where isDeleted in DEFAULT_IS_DELETED or UPDATED_IS_DELETED
        defaultCourseShouldBeFound("isDeleted.in=" + DEFAULT_IS_DELETED + "," + UPDATED_IS_DELETED);

        // Get all the courseList where isDeleted equals to UPDATED_IS_DELETED
        defaultCourseShouldNotBeFound("isDeleted.in=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllCoursesByIsDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where isDeleted is not null
        defaultCourseShouldBeFound("isDeleted.specified=true");

        // Get all the courseList where isDeleted is null
        defaultCourseShouldNotBeFound("isDeleted.specified=false");
    }

    @Test
    @Transactional
    void getAllCoursesByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where createdBy equals to DEFAULT_CREATED_BY
        defaultCourseShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the courseList where createdBy equals to UPDATED_CREATED_BY
        defaultCourseShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllCoursesByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultCourseShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the courseList where createdBy equals to UPDATED_CREATED_BY
        defaultCourseShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllCoursesByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where createdBy is not null
        defaultCourseShouldBeFound("createdBy.specified=true");

        // Get all the courseList where createdBy is null
        defaultCourseShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllCoursesByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where createdBy contains DEFAULT_CREATED_BY
        defaultCourseShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the courseList where createdBy contains UPDATED_CREATED_BY
        defaultCourseShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllCoursesByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where createdBy does not contain DEFAULT_CREATED_BY
        defaultCourseShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the courseList where createdBy does not contain UPDATED_CREATED_BY
        defaultCourseShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllCoursesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where createdDate equals to DEFAULT_CREATED_DATE
        defaultCourseShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the courseList where createdDate equals to UPDATED_CREATED_DATE
        defaultCourseShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllCoursesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultCourseShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the courseList where createdDate equals to UPDATED_CREATED_DATE
        defaultCourseShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllCoursesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where createdDate is not null
        defaultCourseShouldBeFound("createdDate.specified=true");

        // Get all the courseList where createdDate is null
        defaultCourseShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCoursesByCreatedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where createdDate is greater than or equal to DEFAULT_CREATED_DATE
        defaultCourseShouldBeFound("createdDate.greaterThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the courseList where createdDate is greater than or equal to UPDATED_CREATED_DATE
        defaultCourseShouldNotBeFound("createdDate.greaterThanOrEqual=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllCoursesByCreatedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where createdDate is less than or equal to DEFAULT_CREATED_DATE
        defaultCourseShouldBeFound("createdDate.lessThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the courseList where createdDate is less than or equal to SMALLER_CREATED_DATE
        defaultCourseShouldNotBeFound("createdDate.lessThanOrEqual=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllCoursesByCreatedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where createdDate is less than DEFAULT_CREATED_DATE
        defaultCourseShouldNotBeFound("createdDate.lessThan=" + DEFAULT_CREATED_DATE);

        // Get all the courseList where createdDate is less than UPDATED_CREATED_DATE
        defaultCourseShouldBeFound("createdDate.lessThan=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllCoursesByCreatedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where createdDate is greater than DEFAULT_CREATED_DATE
        defaultCourseShouldNotBeFound("createdDate.greaterThan=" + DEFAULT_CREATED_DATE);

        // Get all the courseList where createdDate is greater than SMALLER_CREATED_DATE
        defaultCourseShouldBeFound("createdDate.greaterThan=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllCoursesByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultCourseShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the courseList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultCourseShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllCoursesByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultCourseShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the courseList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultCourseShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllCoursesByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where lastModifiedBy is not null
        defaultCourseShouldBeFound("lastModifiedBy.specified=true");

        // Get all the courseList where lastModifiedBy is null
        defaultCourseShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllCoursesByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultCourseShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the courseList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultCourseShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllCoursesByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultCourseShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the courseList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultCourseShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllCoursesByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultCourseShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the courseList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultCourseShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllCoursesByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultCourseShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the courseList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultCourseShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllCoursesByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where lastModifiedDate is not null
        defaultCourseShouldBeFound("lastModifiedDate.specified=true");

        // Get all the courseList where lastModifiedDate is null
        defaultCourseShouldNotBeFound("lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCoursesByLastModifiedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where lastModifiedDate is greater than or equal to DEFAULT_LAST_MODIFIED_DATE
        defaultCourseShouldBeFound("lastModifiedDate.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the courseList where lastModifiedDate is greater than or equal to UPDATED_LAST_MODIFIED_DATE
        defaultCourseShouldNotBeFound("lastModifiedDate.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllCoursesByLastModifiedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where lastModifiedDate is less than or equal to DEFAULT_LAST_MODIFIED_DATE
        defaultCourseShouldBeFound("lastModifiedDate.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the courseList where lastModifiedDate is less than or equal to SMALLER_LAST_MODIFIED_DATE
        defaultCourseShouldNotBeFound("lastModifiedDate.lessThanOrEqual=" + SMALLER_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllCoursesByLastModifiedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where lastModifiedDate is less than DEFAULT_LAST_MODIFIED_DATE
        defaultCourseShouldNotBeFound("lastModifiedDate.lessThan=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the courseList where lastModifiedDate is less than UPDATED_LAST_MODIFIED_DATE
        defaultCourseShouldBeFound("lastModifiedDate.lessThan=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllCoursesByLastModifiedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where lastModifiedDate is greater than DEFAULT_LAST_MODIFIED_DATE
        defaultCourseShouldNotBeFound("lastModifiedDate.greaterThan=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the courseList where lastModifiedDate is greater than SMALLER_LAST_MODIFIED_DATE
        defaultCourseShouldBeFound("lastModifiedDate.greaterThan=" + SMALLER_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllCoursesByAssignmentsIsEqualToSomething() throws Exception {
        Assignment assignments;
        if (TestUtil.findAll(em, Assignment.class).isEmpty()) {
            courseRepository.saveAndFlush(course);
            assignments = AssignmentResourceIT.createEntity(em);
        } else {
            assignments = TestUtil.findAll(em, Assignment.class).get(0);
        }
        em.persist(assignments);
        em.flush();
        course.addAssignments(assignments);
        courseRepository.saveAndFlush(course);
        Long assignmentsId = assignments.getId();

        // Get all the courseList where assignments equals to assignmentsId
        defaultCourseShouldBeFound("assignmentsId.equals=" + assignmentsId);

        // Get all the courseList where assignments equals to (assignmentsId + 1)
        defaultCourseShouldNotBeFound("assignmentsId.equals=" + (assignmentsId + 1));
    }

    @Test
    @Transactional
    void getAllCoursesByGradeCompositionsIsEqualToSomething() throws Exception {
        GradeComposition gradeCompositions;
        if (TestUtil.findAll(em, GradeComposition.class).isEmpty()) {
            courseRepository.saveAndFlush(course);
            gradeCompositions = GradeCompositionResourceIT.createEntity(em);
        } else {
            gradeCompositions = TestUtil.findAll(em, GradeComposition.class).get(0);
        }
        em.persist(gradeCompositions);
        em.flush();
        course.addGradeCompositions(gradeCompositions);
        courseRepository.saveAndFlush(course);
        Long gradeCompositionsId = gradeCompositions.getId();

        // Get all the courseList where gradeCompositions equals to gradeCompositionsId
        defaultCourseShouldBeFound("gradeCompositionsId.equals=" + gradeCompositionsId);

        // Get all the courseList where gradeCompositions equals to (gradeCompositionsId + 1)
        defaultCourseShouldNotBeFound("gradeCompositionsId.equals=" + (gradeCompositionsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCourseShouldBeFound(String filter) throws Exception {
        restCourseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(course.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].ownerId").value(hasItem(DEFAULT_OWNER_ID.intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].invitationCode").value(hasItem(DEFAULT_INVITATION_CODE)))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(sameInstant(DEFAULT_EXPIRATION_DATE))))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(sameInstant(DEFAULT_LAST_MODIFIED_DATE))));

        // Check, that the count call also returns 1
        restCourseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCourseShouldNotBeFound(String filter) throws Exception {
        restCourseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCourseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCourse() throws Exception {
        // Get the course
        restCourseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCourse() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        int databaseSizeBeforeUpdate = courseRepository.findAll().size();

        // Update the course
        Course updatedCourse = courseRepository.findById(course.getId()).get();
        // Disconnect from session so that the updates on updatedCourse are not directly saved in db
        em.detach(updatedCourse);
        updatedCourse
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .ownerId(UPDATED_OWNER_ID)
            .description(UPDATED_DESCRIPTION)
            .invitationCode(UPDATED_INVITATION_CODE)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .isDeleted(UPDATED_IS_DELETED)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restCourseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCourse.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCourse))
            )
            .andExpect(status().isOk());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
        Course testCourse = courseList.get(courseList.size() - 1);
        assertThat(testCourse.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCourse.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCourse.getOwnerId()).isEqualTo(UPDATED_OWNER_ID);
        assertThat(testCourse.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCourse.getInvitationCode()).isEqualTo(UPDATED_INVITATION_CODE);
        assertThat(testCourse.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testCourse.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testCourse.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testCourse.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testCourse.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testCourse.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingCourse() throws Exception {
        int databaseSizeBeforeUpdate = courseRepository.findAll().size();
        course.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, course.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(course))
            )
            .andExpect(status().isBadRequest());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCourse() throws Exception {
        int databaseSizeBeforeUpdate = courseRepository.findAll().size();
        course.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(course))
            )
            .andExpect(status().isBadRequest());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCourse() throws Exception {
        int databaseSizeBeforeUpdate = courseRepository.findAll().size();
        course.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCourseWithPatch() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        int databaseSizeBeforeUpdate = courseRepository.findAll().size();

        // Update the course using partial update
        Course partialUpdatedCourse = new Course();
        partialUpdatedCourse.setId(course.getId());

        partialUpdatedCourse
            .ownerId(UPDATED_OWNER_ID)
            .invitationCode(UPDATED_INVITATION_CODE)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restCourseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCourse))
            )
            .andExpect(status().isOk());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
        Course testCourse = courseList.get(courseList.size() - 1);
        assertThat(testCourse.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testCourse.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCourse.getOwnerId()).isEqualTo(UPDATED_OWNER_ID);
        assertThat(testCourse.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCourse.getInvitationCode()).isEqualTo(UPDATED_INVITATION_CODE);
        assertThat(testCourse.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testCourse.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testCourse.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testCourse.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testCourse.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testCourse.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateCourseWithPatch() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        int databaseSizeBeforeUpdate = courseRepository.findAll().size();

        // Update the course using partial update
        Course partialUpdatedCourse = new Course();
        partialUpdatedCourse.setId(course.getId());

        partialUpdatedCourse
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .ownerId(UPDATED_OWNER_ID)
            .description(UPDATED_DESCRIPTION)
            .invitationCode(UPDATED_INVITATION_CODE)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .isDeleted(UPDATED_IS_DELETED)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restCourseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCourse))
            )
            .andExpect(status().isOk());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
        Course testCourse = courseList.get(courseList.size() - 1);
        assertThat(testCourse.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCourse.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCourse.getOwnerId()).isEqualTo(UPDATED_OWNER_ID);
        assertThat(testCourse.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCourse.getInvitationCode()).isEqualTo(UPDATED_INVITATION_CODE);
        assertThat(testCourse.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testCourse.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testCourse.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testCourse.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testCourse.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testCourse.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingCourse() throws Exception {
        int databaseSizeBeforeUpdate = courseRepository.findAll().size();
        course.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, course.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(course))
            )
            .andExpect(status().isBadRequest());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCourse() throws Exception {
        int databaseSizeBeforeUpdate = courseRepository.findAll().size();
        course.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(course))
            )
            .andExpect(status().isBadRequest());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCourse() throws Exception {
        int databaseSizeBeforeUpdate = courseRepository.findAll().size();
        course.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCourse() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        int databaseSizeBeforeDelete = courseRepository.findAll().size();

        // Delete the course
        restCourseMockMvc
            .perform(delete(ENTITY_API_URL_ID, course.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
