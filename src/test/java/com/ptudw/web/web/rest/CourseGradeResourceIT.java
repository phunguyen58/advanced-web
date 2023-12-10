package com.ptudw.web.web.rest;

import static com.ptudw.web.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ptudw.web.IntegrationTest;
import com.ptudw.web.domain.CourseGrade;
import com.ptudw.web.repository.CourseGradeRepository;
import com.ptudw.web.service.criteria.CourseGradeCriteria;
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
 * Integration tests for the {@link CourseGradeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CourseGradeResourceIT {

    private static final Long DEFAULT_GRADE_COMPOSITION_ID = 1L;
    private static final Long UPDATED_GRADE_COMPOSITION_ID = 2L;
    private static final Long SMALLER_GRADE_COMPOSITION_ID = 1L - 1L;

    private static final String DEFAULT_STUDENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_STUDENT_ID = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_MARKED = false;
    private static final Boolean UPDATED_IS_MARKED = true;

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

    private static final String ENTITY_API_URL = "/api/course-grades";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CourseGradeRepository courseGradeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCourseGradeMockMvc;

    private CourseGrade courseGrade;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CourseGrade createEntity(EntityManager em) {
        CourseGrade courseGrade = new CourseGrade()
            .gradeCompositionId(DEFAULT_GRADE_COMPOSITION_ID)
            .studentId(DEFAULT_STUDENT_ID)
            .isMarked(DEFAULT_IS_MARKED)
            .isDeleted(DEFAULT_IS_DELETED)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return courseGrade;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CourseGrade createUpdatedEntity(EntityManager em) {
        CourseGrade courseGrade = new CourseGrade()
            .gradeCompositionId(UPDATED_GRADE_COMPOSITION_ID)
            .studentId(UPDATED_STUDENT_ID)
            .isMarked(UPDATED_IS_MARKED)
            .isDeleted(UPDATED_IS_DELETED)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return courseGrade;
    }

    @BeforeEach
    public void initTest() {
        courseGrade = createEntity(em);
    }

    @Test
    @Transactional
    void createCourseGrade() throws Exception {
        int databaseSizeBeforeCreate = courseGradeRepository.findAll().size();
        // Create the CourseGrade
        restCourseGradeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseGrade)))
            .andExpect(status().isCreated());

        // Validate the CourseGrade in the database
        List<CourseGrade> courseGradeList = courseGradeRepository.findAll();
        assertThat(courseGradeList).hasSize(databaseSizeBeforeCreate + 1);
        CourseGrade testCourseGrade = courseGradeList.get(courseGradeList.size() - 1);
        assertThat(testCourseGrade.getGradeCompositionId()).isEqualTo(DEFAULT_GRADE_COMPOSITION_ID);
        assertThat(testCourseGrade.getStudentId()).isEqualTo(DEFAULT_STUDENT_ID);
        assertThat(testCourseGrade.getIsMarked()).isEqualTo(DEFAULT_IS_MARKED);
        assertThat(testCourseGrade.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testCourseGrade.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testCourseGrade.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testCourseGrade.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testCourseGrade.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createCourseGradeWithExistingId() throws Exception {
        // Create the CourseGrade with an existing ID
        courseGrade.setId(1L);

        int databaseSizeBeforeCreate = courseGradeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourseGradeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseGrade)))
            .andExpect(status().isBadRequest());

        // Validate the CourseGrade in the database
        List<CourseGrade> courseGradeList = courseGradeRepository.findAll();
        assertThat(courseGradeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStudentIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseGradeRepository.findAll().size();
        // set the field null
        courseGrade.setStudentId(null);

        // Create the CourseGrade, which fails.

        restCourseGradeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseGrade)))
            .andExpect(status().isBadRequest());

        List<CourseGrade> courseGradeList = courseGradeRepository.findAll();
        assertThat(courseGradeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseGradeRepository.findAll().size();
        // set the field null
        courseGrade.setCreatedBy(null);

        // Create the CourseGrade, which fails.

        restCourseGradeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseGrade)))
            .andExpect(status().isBadRequest());

        List<CourseGrade> courseGradeList = courseGradeRepository.findAll();
        assertThat(courseGradeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseGradeRepository.findAll().size();
        // set the field null
        courseGrade.setCreatedDate(null);

        // Create the CourseGrade, which fails.

        restCourseGradeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseGrade)))
            .andExpect(status().isBadRequest());

        List<CourseGrade> courseGradeList = courseGradeRepository.findAll();
        assertThat(courseGradeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastModifiedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseGradeRepository.findAll().size();
        // set the field null
        courseGrade.setLastModifiedBy(null);

        // Create the CourseGrade, which fails.

        restCourseGradeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseGrade)))
            .andExpect(status().isBadRequest());

        List<CourseGrade> courseGradeList = courseGradeRepository.findAll();
        assertThat(courseGradeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastModifiedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseGradeRepository.findAll().size();
        // set the field null
        courseGrade.setLastModifiedDate(null);

        // Create the CourseGrade, which fails.

        restCourseGradeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseGrade)))
            .andExpect(status().isBadRequest());

        List<CourseGrade> courseGradeList = courseGradeRepository.findAll();
        assertThat(courseGradeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCourseGrades() throws Exception {
        // Initialize the database
        courseGradeRepository.saveAndFlush(courseGrade);

        // Get all the courseGradeList
        restCourseGradeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(courseGrade.getId().intValue())))
            .andExpect(jsonPath("$.[*].gradeCompositionId").value(hasItem(DEFAULT_GRADE_COMPOSITION_ID.intValue())))
            .andExpect(jsonPath("$.[*].studentId").value(hasItem(DEFAULT_STUDENT_ID)))
            .andExpect(jsonPath("$.[*].isMarked").value(hasItem(DEFAULT_IS_MARKED.booleanValue())))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(sameInstant(DEFAULT_LAST_MODIFIED_DATE))));
    }

    @Test
    @Transactional
    void getCourseGrade() throws Exception {
        // Initialize the database
        courseGradeRepository.saveAndFlush(courseGrade);

        // Get the courseGrade
        restCourseGradeMockMvc
            .perform(get(ENTITY_API_URL_ID, courseGrade.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(courseGrade.getId().intValue()))
            .andExpect(jsonPath("$.gradeCompositionId").value(DEFAULT_GRADE_COMPOSITION_ID.intValue()))
            .andExpect(jsonPath("$.studentId").value(DEFAULT_STUDENT_ID))
            .andExpect(jsonPath("$.isMarked").value(DEFAULT_IS_MARKED.booleanValue()))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(sameInstant(DEFAULT_LAST_MODIFIED_DATE)));
    }

    @Test
    @Transactional
    void getCourseGradesByIdFiltering() throws Exception {
        // Initialize the database
        courseGradeRepository.saveAndFlush(courseGrade);

        Long id = courseGrade.getId();

        defaultCourseGradeShouldBeFound("id.equals=" + id);
        defaultCourseGradeShouldNotBeFound("id.notEquals=" + id);

        defaultCourseGradeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCourseGradeShouldNotBeFound("id.greaterThan=" + id);

        defaultCourseGradeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCourseGradeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCourseGradesByGradeCompositionIdIsEqualToSomething() throws Exception {
        // Initialize the database
        courseGradeRepository.saveAndFlush(courseGrade);

        // Get all the courseGradeList where gradeCompositionId equals to DEFAULT_GRADE_COMPOSITION_ID
        defaultCourseGradeShouldBeFound("gradeCompositionId.equals=" + DEFAULT_GRADE_COMPOSITION_ID);

        // Get all the courseGradeList where gradeCompositionId equals to UPDATED_GRADE_COMPOSITION_ID
        defaultCourseGradeShouldNotBeFound("gradeCompositionId.equals=" + UPDATED_GRADE_COMPOSITION_ID);
    }

    @Test
    @Transactional
    void getAllCourseGradesByGradeCompositionIdIsInShouldWork() throws Exception {
        // Initialize the database
        courseGradeRepository.saveAndFlush(courseGrade);

        // Get all the courseGradeList where gradeCompositionId in DEFAULT_GRADE_COMPOSITION_ID or UPDATED_GRADE_COMPOSITION_ID
        defaultCourseGradeShouldBeFound("gradeCompositionId.in=" + DEFAULT_GRADE_COMPOSITION_ID + "," + UPDATED_GRADE_COMPOSITION_ID);

        // Get all the courseGradeList where gradeCompositionId equals to UPDATED_GRADE_COMPOSITION_ID
        defaultCourseGradeShouldNotBeFound("gradeCompositionId.in=" + UPDATED_GRADE_COMPOSITION_ID);
    }

    @Test
    @Transactional
    void getAllCourseGradesByGradeCompositionIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseGradeRepository.saveAndFlush(courseGrade);

        // Get all the courseGradeList where gradeCompositionId is not null
        defaultCourseGradeShouldBeFound("gradeCompositionId.specified=true");

        // Get all the courseGradeList where gradeCompositionId is null
        defaultCourseGradeShouldNotBeFound("gradeCompositionId.specified=false");
    }

    @Test
    @Transactional
    void getAllCourseGradesByGradeCompositionIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        courseGradeRepository.saveAndFlush(courseGrade);

        // Get all the courseGradeList where gradeCompositionId is greater than or equal to DEFAULT_GRADE_COMPOSITION_ID
        defaultCourseGradeShouldBeFound("gradeCompositionId.greaterThanOrEqual=" + DEFAULT_GRADE_COMPOSITION_ID);

        // Get all the courseGradeList where gradeCompositionId is greater than or equal to UPDATED_GRADE_COMPOSITION_ID
        defaultCourseGradeShouldNotBeFound("gradeCompositionId.greaterThanOrEqual=" + UPDATED_GRADE_COMPOSITION_ID);
    }

    @Test
    @Transactional
    void getAllCourseGradesByGradeCompositionIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        courseGradeRepository.saveAndFlush(courseGrade);

        // Get all the courseGradeList where gradeCompositionId is less than or equal to DEFAULT_GRADE_COMPOSITION_ID
        defaultCourseGradeShouldBeFound("gradeCompositionId.lessThanOrEqual=" + DEFAULT_GRADE_COMPOSITION_ID);

        // Get all the courseGradeList where gradeCompositionId is less than or equal to SMALLER_GRADE_COMPOSITION_ID
        defaultCourseGradeShouldNotBeFound("gradeCompositionId.lessThanOrEqual=" + SMALLER_GRADE_COMPOSITION_ID);
    }

    @Test
    @Transactional
    void getAllCourseGradesByGradeCompositionIdIsLessThanSomething() throws Exception {
        // Initialize the database
        courseGradeRepository.saveAndFlush(courseGrade);

        // Get all the courseGradeList where gradeCompositionId is less than DEFAULT_GRADE_COMPOSITION_ID
        defaultCourseGradeShouldNotBeFound("gradeCompositionId.lessThan=" + DEFAULT_GRADE_COMPOSITION_ID);

        // Get all the courseGradeList where gradeCompositionId is less than UPDATED_GRADE_COMPOSITION_ID
        defaultCourseGradeShouldBeFound("gradeCompositionId.lessThan=" + UPDATED_GRADE_COMPOSITION_ID);
    }

    @Test
    @Transactional
    void getAllCourseGradesByGradeCompositionIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        courseGradeRepository.saveAndFlush(courseGrade);

        // Get all the courseGradeList where gradeCompositionId is greater than DEFAULT_GRADE_COMPOSITION_ID
        defaultCourseGradeShouldNotBeFound("gradeCompositionId.greaterThan=" + DEFAULT_GRADE_COMPOSITION_ID);

        // Get all the courseGradeList where gradeCompositionId is greater than SMALLER_GRADE_COMPOSITION_ID
        defaultCourseGradeShouldBeFound("gradeCompositionId.greaterThan=" + SMALLER_GRADE_COMPOSITION_ID);
    }

    @Test
    @Transactional
    void getAllCourseGradesByStudentIdIsEqualToSomething() throws Exception {
        // Initialize the database
        courseGradeRepository.saveAndFlush(courseGrade);

        // Get all the courseGradeList where studentId equals to DEFAULT_STUDENT_ID
        defaultCourseGradeShouldBeFound("studentId.equals=" + DEFAULT_STUDENT_ID);

        // Get all the courseGradeList where studentId equals to UPDATED_STUDENT_ID
        defaultCourseGradeShouldNotBeFound("studentId.equals=" + UPDATED_STUDENT_ID);
    }

    @Test
    @Transactional
    void getAllCourseGradesByStudentIdIsInShouldWork() throws Exception {
        // Initialize the database
        courseGradeRepository.saveAndFlush(courseGrade);

        // Get all the courseGradeList where studentId in DEFAULT_STUDENT_ID or UPDATED_STUDENT_ID
        defaultCourseGradeShouldBeFound("studentId.in=" + DEFAULT_STUDENT_ID + "," + UPDATED_STUDENT_ID);

        // Get all the courseGradeList where studentId equals to UPDATED_STUDENT_ID
        defaultCourseGradeShouldNotBeFound("studentId.in=" + UPDATED_STUDENT_ID);
    }

    @Test
    @Transactional
    void getAllCourseGradesByStudentIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseGradeRepository.saveAndFlush(courseGrade);

        // Get all the courseGradeList where studentId is not null
        defaultCourseGradeShouldBeFound("studentId.specified=true");

        // Get all the courseGradeList where studentId is null
        defaultCourseGradeShouldNotBeFound("studentId.specified=false");
    }

    @Test
    @Transactional
    void getAllCourseGradesByStudentIdContainsSomething() throws Exception {
        // Initialize the database
        courseGradeRepository.saveAndFlush(courseGrade);

        // Get all the courseGradeList where studentId contains DEFAULT_STUDENT_ID
        defaultCourseGradeShouldBeFound("studentId.contains=" + DEFAULT_STUDENT_ID);

        // Get all the courseGradeList where studentId contains UPDATED_STUDENT_ID
        defaultCourseGradeShouldNotBeFound("studentId.contains=" + UPDATED_STUDENT_ID);
    }

    @Test
    @Transactional
    void getAllCourseGradesByStudentIdNotContainsSomething() throws Exception {
        // Initialize the database
        courseGradeRepository.saveAndFlush(courseGrade);

        // Get all the courseGradeList where studentId does not contain DEFAULT_STUDENT_ID
        defaultCourseGradeShouldNotBeFound("studentId.doesNotContain=" + DEFAULT_STUDENT_ID);

        // Get all the courseGradeList where studentId does not contain UPDATED_STUDENT_ID
        defaultCourseGradeShouldBeFound("studentId.doesNotContain=" + UPDATED_STUDENT_ID);
    }

    @Test
    @Transactional
    void getAllCourseGradesByIsMarkedIsEqualToSomething() throws Exception {
        // Initialize the database
        courseGradeRepository.saveAndFlush(courseGrade);

        // Get all the courseGradeList where isMarked equals to DEFAULT_IS_MARKED
        defaultCourseGradeShouldBeFound("isMarked.equals=" + DEFAULT_IS_MARKED);

        // Get all the courseGradeList where isMarked equals to UPDATED_IS_MARKED
        defaultCourseGradeShouldNotBeFound("isMarked.equals=" + UPDATED_IS_MARKED);
    }

    @Test
    @Transactional
    void getAllCourseGradesByIsMarkedIsInShouldWork() throws Exception {
        // Initialize the database
        courseGradeRepository.saveAndFlush(courseGrade);

        // Get all the courseGradeList where isMarked in DEFAULT_IS_MARKED or UPDATED_IS_MARKED
        defaultCourseGradeShouldBeFound("isMarked.in=" + DEFAULT_IS_MARKED + "," + UPDATED_IS_MARKED);

        // Get all the courseGradeList where isMarked equals to UPDATED_IS_MARKED
        defaultCourseGradeShouldNotBeFound("isMarked.in=" + UPDATED_IS_MARKED);
    }

    @Test
    @Transactional
    void getAllCourseGradesByIsMarkedIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseGradeRepository.saveAndFlush(courseGrade);

        // Get all the courseGradeList where isMarked is not null
        defaultCourseGradeShouldBeFound("isMarked.specified=true");

        // Get all the courseGradeList where isMarked is null
        defaultCourseGradeShouldNotBeFound("isMarked.specified=false");
    }

    @Test
    @Transactional
    void getAllCourseGradesByIsDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        courseGradeRepository.saveAndFlush(courseGrade);

        // Get all the courseGradeList where isDeleted equals to DEFAULT_IS_DELETED
        defaultCourseGradeShouldBeFound("isDeleted.equals=" + DEFAULT_IS_DELETED);

        // Get all the courseGradeList where isDeleted equals to UPDATED_IS_DELETED
        defaultCourseGradeShouldNotBeFound("isDeleted.equals=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllCourseGradesByIsDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        courseGradeRepository.saveAndFlush(courseGrade);

        // Get all the courseGradeList where isDeleted in DEFAULT_IS_DELETED or UPDATED_IS_DELETED
        defaultCourseGradeShouldBeFound("isDeleted.in=" + DEFAULT_IS_DELETED + "," + UPDATED_IS_DELETED);

        // Get all the courseGradeList where isDeleted equals to UPDATED_IS_DELETED
        defaultCourseGradeShouldNotBeFound("isDeleted.in=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllCourseGradesByIsDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseGradeRepository.saveAndFlush(courseGrade);

        // Get all the courseGradeList where isDeleted is not null
        defaultCourseGradeShouldBeFound("isDeleted.specified=true");

        // Get all the courseGradeList where isDeleted is null
        defaultCourseGradeShouldNotBeFound("isDeleted.specified=false");
    }

    @Test
    @Transactional
    void getAllCourseGradesByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        courseGradeRepository.saveAndFlush(courseGrade);

        // Get all the courseGradeList where createdBy equals to DEFAULT_CREATED_BY
        defaultCourseGradeShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the courseGradeList where createdBy equals to UPDATED_CREATED_BY
        defaultCourseGradeShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllCourseGradesByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        courseGradeRepository.saveAndFlush(courseGrade);

        // Get all the courseGradeList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultCourseGradeShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the courseGradeList where createdBy equals to UPDATED_CREATED_BY
        defaultCourseGradeShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllCourseGradesByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseGradeRepository.saveAndFlush(courseGrade);

        // Get all the courseGradeList where createdBy is not null
        defaultCourseGradeShouldBeFound("createdBy.specified=true");

        // Get all the courseGradeList where createdBy is null
        defaultCourseGradeShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllCourseGradesByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        courseGradeRepository.saveAndFlush(courseGrade);

        // Get all the courseGradeList where createdBy contains DEFAULT_CREATED_BY
        defaultCourseGradeShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the courseGradeList where createdBy contains UPDATED_CREATED_BY
        defaultCourseGradeShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllCourseGradesByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        courseGradeRepository.saveAndFlush(courseGrade);

        // Get all the courseGradeList where createdBy does not contain DEFAULT_CREATED_BY
        defaultCourseGradeShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the courseGradeList where createdBy does not contain UPDATED_CREATED_BY
        defaultCourseGradeShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllCourseGradesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        courseGradeRepository.saveAndFlush(courseGrade);

        // Get all the courseGradeList where createdDate equals to DEFAULT_CREATED_DATE
        defaultCourseGradeShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the courseGradeList where createdDate equals to UPDATED_CREATED_DATE
        defaultCourseGradeShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllCourseGradesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        courseGradeRepository.saveAndFlush(courseGrade);

        // Get all the courseGradeList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultCourseGradeShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the courseGradeList where createdDate equals to UPDATED_CREATED_DATE
        defaultCourseGradeShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllCourseGradesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseGradeRepository.saveAndFlush(courseGrade);

        // Get all the courseGradeList where createdDate is not null
        defaultCourseGradeShouldBeFound("createdDate.specified=true");

        // Get all the courseGradeList where createdDate is null
        defaultCourseGradeShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCourseGradesByCreatedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        courseGradeRepository.saveAndFlush(courseGrade);

        // Get all the courseGradeList where createdDate is greater than or equal to DEFAULT_CREATED_DATE
        defaultCourseGradeShouldBeFound("createdDate.greaterThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the courseGradeList where createdDate is greater than or equal to UPDATED_CREATED_DATE
        defaultCourseGradeShouldNotBeFound("createdDate.greaterThanOrEqual=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllCourseGradesByCreatedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        courseGradeRepository.saveAndFlush(courseGrade);

        // Get all the courseGradeList where createdDate is less than or equal to DEFAULT_CREATED_DATE
        defaultCourseGradeShouldBeFound("createdDate.lessThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the courseGradeList where createdDate is less than or equal to SMALLER_CREATED_DATE
        defaultCourseGradeShouldNotBeFound("createdDate.lessThanOrEqual=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllCourseGradesByCreatedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        courseGradeRepository.saveAndFlush(courseGrade);

        // Get all the courseGradeList where createdDate is less than DEFAULT_CREATED_DATE
        defaultCourseGradeShouldNotBeFound("createdDate.lessThan=" + DEFAULT_CREATED_DATE);

        // Get all the courseGradeList where createdDate is less than UPDATED_CREATED_DATE
        defaultCourseGradeShouldBeFound("createdDate.lessThan=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllCourseGradesByCreatedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        courseGradeRepository.saveAndFlush(courseGrade);

        // Get all the courseGradeList where createdDate is greater than DEFAULT_CREATED_DATE
        defaultCourseGradeShouldNotBeFound("createdDate.greaterThan=" + DEFAULT_CREATED_DATE);

        // Get all the courseGradeList where createdDate is greater than SMALLER_CREATED_DATE
        defaultCourseGradeShouldBeFound("createdDate.greaterThan=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllCourseGradesByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        courseGradeRepository.saveAndFlush(courseGrade);

        // Get all the courseGradeList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultCourseGradeShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the courseGradeList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultCourseGradeShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllCourseGradesByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        courseGradeRepository.saveAndFlush(courseGrade);

        // Get all the courseGradeList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultCourseGradeShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the courseGradeList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultCourseGradeShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllCourseGradesByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseGradeRepository.saveAndFlush(courseGrade);

        // Get all the courseGradeList where lastModifiedBy is not null
        defaultCourseGradeShouldBeFound("lastModifiedBy.specified=true");

        // Get all the courseGradeList where lastModifiedBy is null
        defaultCourseGradeShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllCourseGradesByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        courseGradeRepository.saveAndFlush(courseGrade);

        // Get all the courseGradeList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultCourseGradeShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the courseGradeList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultCourseGradeShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllCourseGradesByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        courseGradeRepository.saveAndFlush(courseGrade);

        // Get all the courseGradeList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultCourseGradeShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the courseGradeList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultCourseGradeShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllCourseGradesByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        courseGradeRepository.saveAndFlush(courseGrade);

        // Get all the courseGradeList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultCourseGradeShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the courseGradeList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultCourseGradeShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllCourseGradesByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        courseGradeRepository.saveAndFlush(courseGrade);

        // Get all the courseGradeList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultCourseGradeShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the courseGradeList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultCourseGradeShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllCourseGradesByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseGradeRepository.saveAndFlush(courseGrade);

        // Get all the courseGradeList where lastModifiedDate is not null
        defaultCourseGradeShouldBeFound("lastModifiedDate.specified=true");

        // Get all the courseGradeList where lastModifiedDate is null
        defaultCourseGradeShouldNotBeFound("lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCourseGradesByLastModifiedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        courseGradeRepository.saveAndFlush(courseGrade);

        // Get all the courseGradeList where lastModifiedDate is greater than or equal to DEFAULT_LAST_MODIFIED_DATE
        defaultCourseGradeShouldBeFound("lastModifiedDate.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the courseGradeList where lastModifiedDate is greater than or equal to UPDATED_LAST_MODIFIED_DATE
        defaultCourseGradeShouldNotBeFound("lastModifiedDate.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllCourseGradesByLastModifiedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        courseGradeRepository.saveAndFlush(courseGrade);

        // Get all the courseGradeList where lastModifiedDate is less than or equal to DEFAULT_LAST_MODIFIED_DATE
        defaultCourseGradeShouldBeFound("lastModifiedDate.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the courseGradeList where lastModifiedDate is less than or equal to SMALLER_LAST_MODIFIED_DATE
        defaultCourseGradeShouldNotBeFound("lastModifiedDate.lessThanOrEqual=" + SMALLER_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllCourseGradesByLastModifiedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        courseGradeRepository.saveAndFlush(courseGrade);

        // Get all the courseGradeList where lastModifiedDate is less than DEFAULT_LAST_MODIFIED_DATE
        defaultCourseGradeShouldNotBeFound("lastModifiedDate.lessThan=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the courseGradeList where lastModifiedDate is less than UPDATED_LAST_MODIFIED_DATE
        defaultCourseGradeShouldBeFound("lastModifiedDate.lessThan=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllCourseGradesByLastModifiedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        courseGradeRepository.saveAndFlush(courseGrade);

        // Get all the courseGradeList where lastModifiedDate is greater than DEFAULT_LAST_MODIFIED_DATE
        defaultCourseGradeShouldNotBeFound("lastModifiedDate.greaterThan=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the courseGradeList where lastModifiedDate is greater than SMALLER_LAST_MODIFIED_DATE
        defaultCourseGradeShouldBeFound("lastModifiedDate.greaterThan=" + SMALLER_LAST_MODIFIED_DATE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCourseGradeShouldBeFound(String filter) throws Exception {
        restCourseGradeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(courseGrade.getId().intValue())))
            .andExpect(jsonPath("$.[*].gradeCompositionId").value(hasItem(DEFAULT_GRADE_COMPOSITION_ID.intValue())))
            .andExpect(jsonPath("$.[*].studentId").value(hasItem(DEFAULT_STUDENT_ID)))
            .andExpect(jsonPath("$.[*].isMarked").value(hasItem(DEFAULT_IS_MARKED.booleanValue())))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(sameInstant(DEFAULT_LAST_MODIFIED_DATE))));

        // Check, that the count call also returns 1
        restCourseGradeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCourseGradeShouldNotBeFound(String filter) throws Exception {
        restCourseGradeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCourseGradeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCourseGrade() throws Exception {
        // Get the courseGrade
        restCourseGradeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCourseGrade() throws Exception {
        // Initialize the database
        courseGradeRepository.saveAndFlush(courseGrade);

        int databaseSizeBeforeUpdate = courseGradeRepository.findAll().size();

        // Update the courseGrade
        CourseGrade updatedCourseGrade = courseGradeRepository.findById(courseGrade.getId()).get();
        // Disconnect from session so that the updates on updatedCourseGrade are not directly saved in db
        em.detach(updatedCourseGrade);
        updatedCourseGrade
            .gradeCompositionId(UPDATED_GRADE_COMPOSITION_ID)
            .studentId(UPDATED_STUDENT_ID)
            .isMarked(UPDATED_IS_MARKED)
            .isDeleted(UPDATED_IS_DELETED)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restCourseGradeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCourseGrade.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCourseGrade))
            )
            .andExpect(status().isOk());

        // Validate the CourseGrade in the database
        List<CourseGrade> courseGradeList = courseGradeRepository.findAll();
        assertThat(courseGradeList).hasSize(databaseSizeBeforeUpdate);
        CourseGrade testCourseGrade = courseGradeList.get(courseGradeList.size() - 1);
        assertThat(testCourseGrade.getGradeCompositionId()).isEqualTo(UPDATED_GRADE_COMPOSITION_ID);
        assertThat(testCourseGrade.getStudentId()).isEqualTo(UPDATED_STUDENT_ID);
        assertThat(testCourseGrade.getIsMarked()).isEqualTo(UPDATED_IS_MARKED);
        assertThat(testCourseGrade.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testCourseGrade.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testCourseGrade.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testCourseGrade.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testCourseGrade.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingCourseGrade() throws Exception {
        int databaseSizeBeforeUpdate = courseGradeRepository.findAll().size();
        courseGrade.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseGradeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, courseGrade.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courseGrade))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseGrade in the database
        List<CourseGrade> courseGradeList = courseGradeRepository.findAll();
        assertThat(courseGradeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCourseGrade() throws Exception {
        int databaseSizeBeforeUpdate = courseGradeRepository.findAll().size();
        courseGrade.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseGradeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courseGrade))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseGrade in the database
        List<CourseGrade> courseGradeList = courseGradeRepository.findAll();
        assertThat(courseGradeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCourseGrade() throws Exception {
        int databaseSizeBeforeUpdate = courseGradeRepository.findAll().size();
        courseGrade.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseGradeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseGrade)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CourseGrade in the database
        List<CourseGrade> courseGradeList = courseGradeRepository.findAll();
        assertThat(courseGradeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCourseGradeWithPatch() throws Exception {
        // Initialize the database
        courseGradeRepository.saveAndFlush(courseGrade);

        int databaseSizeBeforeUpdate = courseGradeRepository.findAll().size();

        // Update the courseGrade using partial update
        CourseGrade partialUpdatedCourseGrade = new CourseGrade();
        partialUpdatedCourseGrade.setId(courseGrade.getId());

        partialUpdatedCourseGrade.isMarked(UPDATED_IS_MARKED).lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restCourseGradeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourseGrade.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCourseGrade))
            )
            .andExpect(status().isOk());

        // Validate the CourseGrade in the database
        List<CourseGrade> courseGradeList = courseGradeRepository.findAll();
        assertThat(courseGradeList).hasSize(databaseSizeBeforeUpdate);
        CourseGrade testCourseGrade = courseGradeList.get(courseGradeList.size() - 1);
        assertThat(testCourseGrade.getGradeCompositionId()).isEqualTo(DEFAULT_GRADE_COMPOSITION_ID);
        assertThat(testCourseGrade.getStudentId()).isEqualTo(DEFAULT_STUDENT_ID);
        assertThat(testCourseGrade.getIsMarked()).isEqualTo(UPDATED_IS_MARKED);
        assertThat(testCourseGrade.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testCourseGrade.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testCourseGrade.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testCourseGrade.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testCourseGrade.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateCourseGradeWithPatch() throws Exception {
        // Initialize the database
        courseGradeRepository.saveAndFlush(courseGrade);

        int databaseSizeBeforeUpdate = courseGradeRepository.findAll().size();

        // Update the courseGrade using partial update
        CourseGrade partialUpdatedCourseGrade = new CourseGrade();
        partialUpdatedCourseGrade.setId(courseGrade.getId());

        partialUpdatedCourseGrade
            .gradeCompositionId(UPDATED_GRADE_COMPOSITION_ID)
            .studentId(UPDATED_STUDENT_ID)
            .isMarked(UPDATED_IS_MARKED)
            .isDeleted(UPDATED_IS_DELETED)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restCourseGradeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourseGrade.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCourseGrade))
            )
            .andExpect(status().isOk());

        // Validate the CourseGrade in the database
        List<CourseGrade> courseGradeList = courseGradeRepository.findAll();
        assertThat(courseGradeList).hasSize(databaseSizeBeforeUpdate);
        CourseGrade testCourseGrade = courseGradeList.get(courseGradeList.size() - 1);
        assertThat(testCourseGrade.getGradeCompositionId()).isEqualTo(UPDATED_GRADE_COMPOSITION_ID);
        assertThat(testCourseGrade.getStudentId()).isEqualTo(UPDATED_STUDENT_ID);
        assertThat(testCourseGrade.getIsMarked()).isEqualTo(UPDATED_IS_MARKED);
        assertThat(testCourseGrade.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testCourseGrade.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testCourseGrade.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testCourseGrade.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testCourseGrade.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingCourseGrade() throws Exception {
        int databaseSizeBeforeUpdate = courseGradeRepository.findAll().size();
        courseGrade.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseGradeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, courseGrade.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(courseGrade))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseGrade in the database
        List<CourseGrade> courseGradeList = courseGradeRepository.findAll();
        assertThat(courseGradeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCourseGrade() throws Exception {
        int databaseSizeBeforeUpdate = courseGradeRepository.findAll().size();
        courseGrade.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseGradeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(courseGrade))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseGrade in the database
        List<CourseGrade> courseGradeList = courseGradeRepository.findAll();
        assertThat(courseGradeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCourseGrade() throws Exception {
        int databaseSizeBeforeUpdate = courseGradeRepository.findAll().size();
        courseGrade.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseGradeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(courseGrade))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CourseGrade in the database
        List<CourseGrade> courseGradeList = courseGradeRepository.findAll();
        assertThat(courseGradeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCourseGrade() throws Exception {
        // Initialize the database
        courseGradeRepository.saveAndFlush(courseGrade);

        int databaseSizeBeforeDelete = courseGradeRepository.findAll().size();

        // Delete the courseGrade
        restCourseGradeMockMvc
            .perform(delete(ENTITY_API_URL_ID, courseGrade.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CourseGrade> courseGradeList = courseGradeRepository.findAll();
        assertThat(courseGradeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
