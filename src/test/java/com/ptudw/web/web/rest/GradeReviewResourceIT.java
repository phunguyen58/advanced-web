package com.ptudw.web.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ptudw.web.IntegrationTest;
import com.ptudw.web.domain.GradeReview;
import com.ptudw.web.repository.GradeReviewRepository;
import com.ptudw.web.service.criteria.GradeReviewCriteria;
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
 * Integration tests for the {@link GradeReviewResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GradeReviewResourceIT {

    private static final Long DEFAULT_GRADE_COMPOSITION_ID = 1L;
    private static final Long UPDATED_GRADE_COMPOSITION_ID = 2L;
    private static final Long SMALLER_GRADE_COMPOSITION_ID = 1L - 1L;

    private static final String DEFAULT_STUDENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_STUDENT_ID = "BBBBBBBBBB";

    private static final Long DEFAULT_COURSE_ID = 1L;
    private static final Long UPDATED_COURSE_ID = 2L;
    private static final Long SMALLER_COURSE_ID = 1L - 1L;

    private static final Long DEFAULT_REVIEWER_ID = 1L;
    private static final Long UPDATED_REVIEWER_ID = 2L;
    private static final Long SMALLER_REVIEWER_ID = 1L - 1L;

    private static final Long DEFAULT_ASSIGMENT_ID = 1L;
    private static final Long UPDATED_ASSIGMENT_ID = 2L;
    private static final Long SMALLER_ASSIGMENT_ID = 1L - 1L;

    private static final Long DEFAULT_ASSIMENT_GRADE_ID = 1L;
    private static final Long UPDATED_ASSIMENT_GRADE_ID = 2L;
    private static final Long SMALLER_ASSIMENT_GRADE_ID = 1L - 1L;

    private static final Long DEFAULT_CURRENT_GRADE = 1L;
    private static final Long UPDATED_CURRENT_GRADE = 2L;
    private static final Long SMALLER_CURRENT_GRADE = 1L - 1L;

    private static final Long DEFAULT_EXPECTATION_GRADE = 1L;
    private static final Long UPDATED_EXPECTATION_GRADE = 2L;
    private static final Long SMALLER_EXPECTATION_GRADE = 1L - 1L;

    private static final String DEFAULT_STUDENT_EXPLANATION = "AAAAAAAAAA";
    private static final String UPDATED_STUDENT_EXPLANATION = "BBBBBBBBBB";

    private static final String DEFAULT_TEACHER_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_TEACHER_COMMENT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_FINAL = false;
    private static final Boolean UPDATED_IS_FINAL = true;

    private static final String ENTITY_API_URL = "/api/grade-reviews";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GradeReviewRepository gradeReviewRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGradeReviewMockMvc;

    private GradeReview gradeReview;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GradeReview createEntity(EntityManager em) {
        GradeReview gradeReview = new GradeReview()
            .gradeCompositionId(DEFAULT_GRADE_COMPOSITION_ID)
            .studentId(DEFAULT_STUDENT_ID)
            .courseId(DEFAULT_COURSE_ID)
            .reviewerId(DEFAULT_REVIEWER_ID)
            .assigmentId(DEFAULT_ASSIGMENT_ID)
            .assimentGradeId(DEFAULT_ASSIMENT_GRADE_ID)
            .currentGrade(DEFAULT_CURRENT_GRADE)
            .expectationGrade(DEFAULT_EXPECTATION_GRADE)
            .studentExplanation(DEFAULT_STUDENT_EXPLANATION)
            .teacherComment(DEFAULT_TEACHER_COMMENT)
            .isFinal(DEFAULT_IS_FINAL);
        return gradeReview;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GradeReview createUpdatedEntity(EntityManager em) {
        GradeReview gradeReview = new GradeReview()
            .gradeCompositionId(UPDATED_GRADE_COMPOSITION_ID)
            .studentId(UPDATED_STUDENT_ID)
            .courseId(UPDATED_COURSE_ID)
            .reviewerId(UPDATED_REVIEWER_ID)
            .assigmentId(UPDATED_ASSIGMENT_ID)
            .assimentGradeId(UPDATED_ASSIMENT_GRADE_ID)
            .currentGrade(UPDATED_CURRENT_GRADE)
            .expectationGrade(UPDATED_EXPECTATION_GRADE)
            .studentExplanation(UPDATED_STUDENT_EXPLANATION)
            .teacherComment(UPDATED_TEACHER_COMMENT)
            .isFinal(UPDATED_IS_FINAL);
        return gradeReview;
    }

    @BeforeEach
    public void initTest() {
        gradeReview = createEntity(em);
    }

    @Test
    @Transactional
    void createGradeReview() throws Exception {
        int databaseSizeBeforeCreate = gradeReviewRepository.findAll().size();
        // Create the GradeReview
        restGradeReviewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gradeReview)))
            .andExpect(status().isCreated());

        // Validate the GradeReview in the database
        List<GradeReview> gradeReviewList = gradeReviewRepository.findAll();
        assertThat(gradeReviewList).hasSize(databaseSizeBeforeCreate + 1);
        GradeReview testGradeReview = gradeReviewList.get(gradeReviewList.size() - 1);
        assertThat(testGradeReview.getGradeCompositionId()).isEqualTo(DEFAULT_GRADE_COMPOSITION_ID);
        assertThat(testGradeReview.getStudentId()).isEqualTo(DEFAULT_STUDENT_ID);
        assertThat(testGradeReview.getCourseId()).isEqualTo(DEFAULT_COURSE_ID);
        assertThat(testGradeReview.getReviewerId()).isEqualTo(DEFAULT_REVIEWER_ID);
        assertThat(testGradeReview.getAssigmentId()).isEqualTo(DEFAULT_ASSIGMENT_ID);
        assertThat(testGradeReview.getAssimentGradeId()).isEqualTo(DEFAULT_ASSIMENT_GRADE_ID);
        assertThat(testGradeReview.getCurrentGrade()).isEqualTo(DEFAULT_CURRENT_GRADE);
        assertThat(testGradeReview.getExpectationGrade()).isEqualTo(DEFAULT_EXPECTATION_GRADE);
        assertThat(testGradeReview.getStudentExplanation()).isEqualTo(DEFAULT_STUDENT_EXPLANATION);
        assertThat(testGradeReview.getTeacherComment()).isEqualTo(DEFAULT_TEACHER_COMMENT);
        assertThat(testGradeReview.getIsFinal()).isEqualTo(DEFAULT_IS_FINAL);
    }

    @Test
    @Transactional
    void createGradeReviewWithExistingId() throws Exception {
        // Create the GradeReview with an existing ID
        gradeReview.setId(1L);

        int databaseSizeBeforeCreate = gradeReviewRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGradeReviewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gradeReview)))
            .andExpect(status().isBadRequest());

        // Validate the GradeReview in the database
        List<GradeReview> gradeReviewList = gradeReviewRepository.findAll();
        assertThat(gradeReviewList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkGradeCompositionIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = gradeReviewRepository.findAll().size();
        // set the field null
        gradeReview.setGradeCompositionId(null);

        // Create the GradeReview, which fails.

        restGradeReviewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gradeReview)))
            .andExpect(status().isBadRequest());

        List<GradeReview> gradeReviewList = gradeReviewRepository.findAll();
        assertThat(gradeReviewList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStudentIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = gradeReviewRepository.findAll().size();
        // set the field null
        gradeReview.setStudentId(null);

        // Create the GradeReview, which fails.

        restGradeReviewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gradeReview)))
            .andExpect(status().isBadRequest());

        List<GradeReview> gradeReviewList = gradeReviewRepository.findAll();
        assertThat(gradeReviewList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCourseIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = gradeReviewRepository.findAll().size();
        // set the field null
        gradeReview.setCourseId(null);

        // Create the GradeReview, which fails.

        restGradeReviewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gradeReview)))
            .andExpect(status().isBadRequest());

        List<GradeReview> gradeReviewList = gradeReviewRepository.findAll();
        assertThat(gradeReviewList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGradeReviews() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList
        restGradeReviewMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gradeReview.getId().intValue())))
            .andExpect(jsonPath("$.[*].gradeCompositionId").value(hasItem(DEFAULT_GRADE_COMPOSITION_ID.intValue())))
            .andExpect(jsonPath("$.[*].studentId").value(hasItem(DEFAULT_STUDENT_ID)))
            .andExpect(jsonPath("$.[*].courseId").value(hasItem(DEFAULT_COURSE_ID.intValue())))
            .andExpect(jsonPath("$.[*].reviewerId").value(hasItem(DEFAULT_REVIEWER_ID.intValue())))
            .andExpect(jsonPath("$.[*].assigmentId").value(hasItem(DEFAULT_ASSIGMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].assimentGradeId").value(hasItem(DEFAULT_ASSIMENT_GRADE_ID.intValue())))
            .andExpect(jsonPath("$.[*].currentGrade").value(hasItem(DEFAULT_CURRENT_GRADE.intValue())))
            .andExpect(jsonPath("$.[*].expectationGrade").value(hasItem(DEFAULT_EXPECTATION_GRADE.intValue())))
            .andExpect(jsonPath("$.[*].studentExplanation").value(hasItem(DEFAULT_STUDENT_EXPLANATION)))
            .andExpect(jsonPath("$.[*].teacherComment").value(hasItem(DEFAULT_TEACHER_COMMENT)))
            .andExpect(jsonPath("$.[*].isFinal").value(hasItem(DEFAULT_IS_FINAL.booleanValue())));
    }

    @Test
    @Transactional
    void getGradeReview() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get the gradeReview
        restGradeReviewMockMvc
            .perform(get(ENTITY_API_URL_ID, gradeReview.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(gradeReview.getId().intValue()))
            .andExpect(jsonPath("$.gradeCompositionId").value(DEFAULT_GRADE_COMPOSITION_ID.intValue()))
            .andExpect(jsonPath("$.studentId").value(DEFAULT_STUDENT_ID))
            .andExpect(jsonPath("$.courseId").value(DEFAULT_COURSE_ID.intValue()))
            .andExpect(jsonPath("$.reviewerId").value(DEFAULT_REVIEWER_ID.intValue()))
            .andExpect(jsonPath("$.assigmentId").value(DEFAULT_ASSIGMENT_ID.intValue()))
            .andExpect(jsonPath("$.assimentGradeId").value(DEFAULT_ASSIMENT_GRADE_ID.intValue()))
            .andExpect(jsonPath("$.currentGrade").value(DEFAULT_CURRENT_GRADE.intValue()))
            .andExpect(jsonPath("$.expectationGrade").value(DEFAULT_EXPECTATION_GRADE.intValue()))
            .andExpect(jsonPath("$.studentExplanation").value(DEFAULT_STUDENT_EXPLANATION))
            .andExpect(jsonPath("$.teacherComment").value(DEFAULT_TEACHER_COMMENT))
            .andExpect(jsonPath("$.isFinal").value(DEFAULT_IS_FINAL.booleanValue()));
    }

    @Test
    @Transactional
    void getGradeReviewsByIdFiltering() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        Long id = gradeReview.getId();

        defaultGradeReviewShouldBeFound("id.equals=" + id);
        defaultGradeReviewShouldNotBeFound("id.notEquals=" + id);

        defaultGradeReviewShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultGradeReviewShouldNotBeFound("id.greaterThan=" + id);

        defaultGradeReviewShouldBeFound("id.lessThanOrEqual=" + id);
        defaultGradeReviewShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByGradeCompositionIdIsEqualToSomething() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where gradeCompositionId equals to DEFAULT_GRADE_COMPOSITION_ID
        defaultGradeReviewShouldBeFound("gradeCompositionId.equals=" + DEFAULT_GRADE_COMPOSITION_ID);

        // Get all the gradeReviewList where gradeCompositionId equals to UPDATED_GRADE_COMPOSITION_ID
        defaultGradeReviewShouldNotBeFound("gradeCompositionId.equals=" + UPDATED_GRADE_COMPOSITION_ID);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByGradeCompositionIdIsInShouldWork() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where gradeCompositionId in DEFAULT_GRADE_COMPOSITION_ID or UPDATED_GRADE_COMPOSITION_ID
        defaultGradeReviewShouldBeFound("gradeCompositionId.in=" + DEFAULT_GRADE_COMPOSITION_ID + "," + UPDATED_GRADE_COMPOSITION_ID);

        // Get all the gradeReviewList where gradeCompositionId equals to UPDATED_GRADE_COMPOSITION_ID
        defaultGradeReviewShouldNotBeFound("gradeCompositionId.in=" + UPDATED_GRADE_COMPOSITION_ID);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByGradeCompositionIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where gradeCompositionId is not null
        defaultGradeReviewShouldBeFound("gradeCompositionId.specified=true");

        // Get all the gradeReviewList where gradeCompositionId is null
        defaultGradeReviewShouldNotBeFound("gradeCompositionId.specified=false");
    }

    @Test
    @Transactional
    void getAllGradeReviewsByGradeCompositionIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where gradeCompositionId is greater than or equal to DEFAULT_GRADE_COMPOSITION_ID
        defaultGradeReviewShouldBeFound("gradeCompositionId.greaterThanOrEqual=" + DEFAULT_GRADE_COMPOSITION_ID);

        // Get all the gradeReviewList where gradeCompositionId is greater than or equal to UPDATED_GRADE_COMPOSITION_ID
        defaultGradeReviewShouldNotBeFound("gradeCompositionId.greaterThanOrEqual=" + UPDATED_GRADE_COMPOSITION_ID);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByGradeCompositionIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where gradeCompositionId is less than or equal to DEFAULT_GRADE_COMPOSITION_ID
        defaultGradeReviewShouldBeFound("gradeCompositionId.lessThanOrEqual=" + DEFAULT_GRADE_COMPOSITION_ID);

        // Get all the gradeReviewList where gradeCompositionId is less than or equal to SMALLER_GRADE_COMPOSITION_ID
        defaultGradeReviewShouldNotBeFound("gradeCompositionId.lessThanOrEqual=" + SMALLER_GRADE_COMPOSITION_ID);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByGradeCompositionIdIsLessThanSomething() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where gradeCompositionId is less than DEFAULT_GRADE_COMPOSITION_ID
        defaultGradeReviewShouldNotBeFound("gradeCompositionId.lessThan=" + DEFAULT_GRADE_COMPOSITION_ID);

        // Get all the gradeReviewList where gradeCompositionId is less than UPDATED_GRADE_COMPOSITION_ID
        defaultGradeReviewShouldBeFound("gradeCompositionId.lessThan=" + UPDATED_GRADE_COMPOSITION_ID);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByGradeCompositionIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where gradeCompositionId is greater than DEFAULT_GRADE_COMPOSITION_ID
        defaultGradeReviewShouldNotBeFound("gradeCompositionId.greaterThan=" + DEFAULT_GRADE_COMPOSITION_ID);

        // Get all the gradeReviewList where gradeCompositionId is greater than SMALLER_GRADE_COMPOSITION_ID
        defaultGradeReviewShouldBeFound("gradeCompositionId.greaterThan=" + SMALLER_GRADE_COMPOSITION_ID);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByStudentIdIsEqualToSomething() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where studentId equals to DEFAULT_STUDENT_ID
        defaultGradeReviewShouldBeFound("studentId.equals=" + DEFAULT_STUDENT_ID);

        // Get all the gradeReviewList where studentId equals to UPDATED_STUDENT_ID
        defaultGradeReviewShouldNotBeFound("studentId.equals=" + UPDATED_STUDENT_ID);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByStudentIdIsInShouldWork() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where studentId in DEFAULT_STUDENT_ID or UPDATED_STUDENT_ID
        defaultGradeReviewShouldBeFound("studentId.in=" + DEFAULT_STUDENT_ID + "," + UPDATED_STUDENT_ID);

        // Get all the gradeReviewList where studentId equals to UPDATED_STUDENT_ID
        defaultGradeReviewShouldNotBeFound("studentId.in=" + UPDATED_STUDENT_ID);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByStudentIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where studentId is not null
        defaultGradeReviewShouldBeFound("studentId.specified=true");

        // Get all the gradeReviewList where studentId is null
        defaultGradeReviewShouldNotBeFound("studentId.specified=false");
    }

    @Test
    @Transactional
    void getAllGradeReviewsByStudentIdContainsSomething() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where studentId contains DEFAULT_STUDENT_ID
        defaultGradeReviewShouldBeFound("studentId.contains=" + DEFAULT_STUDENT_ID);

        // Get all the gradeReviewList where studentId contains UPDATED_STUDENT_ID
        defaultGradeReviewShouldNotBeFound("studentId.contains=" + UPDATED_STUDENT_ID);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByStudentIdNotContainsSomething() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where studentId does not contain DEFAULT_STUDENT_ID
        defaultGradeReviewShouldNotBeFound("studentId.doesNotContain=" + DEFAULT_STUDENT_ID);

        // Get all the gradeReviewList where studentId does not contain UPDATED_STUDENT_ID
        defaultGradeReviewShouldBeFound("studentId.doesNotContain=" + UPDATED_STUDENT_ID);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByCourseIdIsEqualToSomething() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where courseId equals to DEFAULT_COURSE_ID
        defaultGradeReviewShouldBeFound("courseId.equals=" + DEFAULT_COURSE_ID);

        // Get all the gradeReviewList where courseId equals to UPDATED_COURSE_ID
        defaultGradeReviewShouldNotBeFound("courseId.equals=" + UPDATED_COURSE_ID);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByCourseIdIsInShouldWork() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where courseId in DEFAULT_COURSE_ID or UPDATED_COURSE_ID
        defaultGradeReviewShouldBeFound("courseId.in=" + DEFAULT_COURSE_ID + "," + UPDATED_COURSE_ID);

        // Get all the gradeReviewList where courseId equals to UPDATED_COURSE_ID
        defaultGradeReviewShouldNotBeFound("courseId.in=" + UPDATED_COURSE_ID);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByCourseIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where courseId is not null
        defaultGradeReviewShouldBeFound("courseId.specified=true");

        // Get all the gradeReviewList where courseId is null
        defaultGradeReviewShouldNotBeFound("courseId.specified=false");
    }

    @Test
    @Transactional
    void getAllGradeReviewsByCourseIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where courseId is greater than or equal to DEFAULT_COURSE_ID
        defaultGradeReviewShouldBeFound("courseId.greaterThanOrEqual=" + DEFAULT_COURSE_ID);

        // Get all the gradeReviewList where courseId is greater than or equal to UPDATED_COURSE_ID
        defaultGradeReviewShouldNotBeFound("courseId.greaterThanOrEqual=" + UPDATED_COURSE_ID);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByCourseIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where courseId is less than or equal to DEFAULT_COURSE_ID
        defaultGradeReviewShouldBeFound("courseId.lessThanOrEqual=" + DEFAULT_COURSE_ID);

        // Get all the gradeReviewList where courseId is less than or equal to SMALLER_COURSE_ID
        defaultGradeReviewShouldNotBeFound("courseId.lessThanOrEqual=" + SMALLER_COURSE_ID);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByCourseIdIsLessThanSomething() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where courseId is less than DEFAULT_COURSE_ID
        defaultGradeReviewShouldNotBeFound("courseId.lessThan=" + DEFAULT_COURSE_ID);

        // Get all the gradeReviewList where courseId is less than UPDATED_COURSE_ID
        defaultGradeReviewShouldBeFound("courseId.lessThan=" + UPDATED_COURSE_ID);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByCourseIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where courseId is greater than DEFAULT_COURSE_ID
        defaultGradeReviewShouldNotBeFound("courseId.greaterThan=" + DEFAULT_COURSE_ID);

        // Get all the gradeReviewList where courseId is greater than SMALLER_COURSE_ID
        defaultGradeReviewShouldBeFound("courseId.greaterThan=" + SMALLER_COURSE_ID);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByReviewerIdIsEqualToSomething() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where reviewerId equals to DEFAULT_REVIEWER_ID
        defaultGradeReviewShouldBeFound("reviewerId.equals=" + DEFAULT_REVIEWER_ID);

        // Get all the gradeReviewList where reviewerId equals to UPDATED_REVIEWER_ID
        defaultGradeReviewShouldNotBeFound("reviewerId.equals=" + UPDATED_REVIEWER_ID);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByReviewerIdIsInShouldWork() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where reviewerId in DEFAULT_REVIEWER_ID or UPDATED_REVIEWER_ID
        defaultGradeReviewShouldBeFound("reviewerId.in=" + DEFAULT_REVIEWER_ID + "," + UPDATED_REVIEWER_ID);

        // Get all the gradeReviewList where reviewerId equals to UPDATED_REVIEWER_ID
        defaultGradeReviewShouldNotBeFound("reviewerId.in=" + UPDATED_REVIEWER_ID);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByReviewerIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where reviewerId is not null
        defaultGradeReviewShouldBeFound("reviewerId.specified=true");

        // Get all the gradeReviewList where reviewerId is null
        defaultGradeReviewShouldNotBeFound("reviewerId.specified=false");
    }

    @Test
    @Transactional
    void getAllGradeReviewsByReviewerIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where reviewerId is greater than or equal to DEFAULT_REVIEWER_ID
        defaultGradeReviewShouldBeFound("reviewerId.greaterThanOrEqual=" + DEFAULT_REVIEWER_ID);

        // Get all the gradeReviewList where reviewerId is greater than or equal to UPDATED_REVIEWER_ID
        defaultGradeReviewShouldNotBeFound("reviewerId.greaterThanOrEqual=" + UPDATED_REVIEWER_ID);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByReviewerIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where reviewerId is less than or equal to DEFAULT_REVIEWER_ID
        defaultGradeReviewShouldBeFound("reviewerId.lessThanOrEqual=" + DEFAULT_REVIEWER_ID);

        // Get all the gradeReviewList where reviewerId is less than or equal to SMALLER_REVIEWER_ID
        defaultGradeReviewShouldNotBeFound("reviewerId.lessThanOrEqual=" + SMALLER_REVIEWER_ID);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByReviewerIdIsLessThanSomething() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where reviewerId is less than DEFAULT_REVIEWER_ID
        defaultGradeReviewShouldNotBeFound("reviewerId.lessThan=" + DEFAULT_REVIEWER_ID);

        // Get all the gradeReviewList where reviewerId is less than UPDATED_REVIEWER_ID
        defaultGradeReviewShouldBeFound("reviewerId.lessThan=" + UPDATED_REVIEWER_ID);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByReviewerIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where reviewerId is greater than DEFAULT_REVIEWER_ID
        defaultGradeReviewShouldNotBeFound("reviewerId.greaterThan=" + DEFAULT_REVIEWER_ID);

        // Get all the gradeReviewList where reviewerId is greater than SMALLER_REVIEWER_ID
        defaultGradeReviewShouldBeFound("reviewerId.greaterThan=" + SMALLER_REVIEWER_ID);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByAssigmentIdIsEqualToSomething() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where assigmentId equals to DEFAULT_ASSIGMENT_ID
        defaultGradeReviewShouldBeFound("assigmentId.equals=" + DEFAULT_ASSIGMENT_ID);

        // Get all the gradeReviewList where assigmentId equals to UPDATED_ASSIGMENT_ID
        defaultGradeReviewShouldNotBeFound("assigmentId.equals=" + UPDATED_ASSIGMENT_ID);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByAssigmentIdIsInShouldWork() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where assigmentId in DEFAULT_ASSIGMENT_ID or UPDATED_ASSIGMENT_ID
        defaultGradeReviewShouldBeFound("assigmentId.in=" + DEFAULT_ASSIGMENT_ID + "," + UPDATED_ASSIGMENT_ID);

        // Get all the gradeReviewList where assigmentId equals to UPDATED_ASSIGMENT_ID
        defaultGradeReviewShouldNotBeFound("assigmentId.in=" + UPDATED_ASSIGMENT_ID);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByAssigmentIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where assigmentId is not null
        defaultGradeReviewShouldBeFound("assigmentId.specified=true");

        // Get all the gradeReviewList where assigmentId is null
        defaultGradeReviewShouldNotBeFound("assigmentId.specified=false");
    }

    @Test
    @Transactional
    void getAllGradeReviewsByAssigmentIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where assigmentId is greater than or equal to DEFAULT_ASSIGMENT_ID
        defaultGradeReviewShouldBeFound("assigmentId.greaterThanOrEqual=" + DEFAULT_ASSIGMENT_ID);

        // Get all the gradeReviewList where assigmentId is greater than or equal to UPDATED_ASSIGMENT_ID
        defaultGradeReviewShouldNotBeFound("assigmentId.greaterThanOrEqual=" + UPDATED_ASSIGMENT_ID);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByAssigmentIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where assigmentId is less than or equal to DEFAULT_ASSIGMENT_ID
        defaultGradeReviewShouldBeFound("assigmentId.lessThanOrEqual=" + DEFAULT_ASSIGMENT_ID);

        // Get all the gradeReviewList where assigmentId is less than or equal to SMALLER_ASSIGMENT_ID
        defaultGradeReviewShouldNotBeFound("assigmentId.lessThanOrEqual=" + SMALLER_ASSIGMENT_ID);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByAssigmentIdIsLessThanSomething() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where assigmentId is less than DEFAULT_ASSIGMENT_ID
        defaultGradeReviewShouldNotBeFound("assigmentId.lessThan=" + DEFAULT_ASSIGMENT_ID);

        // Get all the gradeReviewList where assigmentId is less than UPDATED_ASSIGMENT_ID
        defaultGradeReviewShouldBeFound("assigmentId.lessThan=" + UPDATED_ASSIGMENT_ID);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByAssigmentIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where assigmentId is greater than DEFAULT_ASSIGMENT_ID
        defaultGradeReviewShouldNotBeFound("assigmentId.greaterThan=" + DEFAULT_ASSIGMENT_ID);

        // Get all the gradeReviewList where assigmentId is greater than SMALLER_ASSIGMENT_ID
        defaultGradeReviewShouldBeFound("assigmentId.greaterThan=" + SMALLER_ASSIGMENT_ID);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByAssimentGradeIdIsEqualToSomething() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where assimentGradeId equals to DEFAULT_ASSIMENT_GRADE_ID
        defaultGradeReviewShouldBeFound("assimentGradeId.equals=" + DEFAULT_ASSIMENT_GRADE_ID);

        // Get all the gradeReviewList where assimentGradeId equals to UPDATED_ASSIMENT_GRADE_ID
        defaultGradeReviewShouldNotBeFound("assimentGradeId.equals=" + UPDATED_ASSIMENT_GRADE_ID);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByAssimentGradeIdIsInShouldWork() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where assimentGradeId in DEFAULT_ASSIMENT_GRADE_ID or UPDATED_ASSIMENT_GRADE_ID
        defaultGradeReviewShouldBeFound("assimentGradeId.in=" + DEFAULT_ASSIMENT_GRADE_ID + "," + UPDATED_ASSIMENT_GRADE_ID);

        // Get all the gradeReviewList where assimentGradeId equals to UPDATED_ASSIMENT_GRADE_ID
        defaultGradeReviewShouldNotBeFound("assimentGradeId.in=" + UPDATED_ASSIMENT_GRADE_ID);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByAssimentGradeIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where assimentGradeId is not null
        defaultGradeReviewShouldBeFound("assimentGradeId.specified=true");

        // Get all the gradeReviewList where assimentGradeId is null
        defaultGradeReviewShouldNotBeFound("assimentGradeId.specified=false");
    }

    @Test
    @Transactional
    void getAllGradeReviewsByAssimentGradeIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where assimentGradeId is greater than or equal to DEFAULT_ASSIMENT_GRADE_ID
        defaultGradeReviewShouldBeFound("assimentGradeId.greaterThanOrEqual=" + DEFAULT_ASSIMENT_GRADE_ID);

        // Get all the gradeReviewList where assimentGradeId is greater than or equal to UPDATED_ASSIMENT_GRADE_ID
        defaultGradeReviewShouldNotBeFound("assimentGradeId.greaterThanOrEqual=" + UPDATED_ASSIMENT_GRADE_ID);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByAssimentGradeIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where assimentGradeId is less than or equal to DEFAULT_ASSIMENT_GRADE_ID
        defaultGradeReviewShouldBeFound("assimentGradeId.lessThanOrEqual=" + DEFAULT_ASSIMENT_GRADE_ID);

        // Get all the gradeReviewList where assimentGradeId is less than or equal to SMALLER_ASSIMENT_GRADE_ID
        defaultGradeReviewShouldNotBeFound("assimentGradeId.lessThanOrEqual=" + SMALLER_ASSIMENT_GRADE_ID);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByAssimentGradeIdIsLessThanSomething() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where assimentGradeId is less than DEFAULT_ASSIMENT_GRADE_ID
        defaultGradeReviewShouldNotBeFound("assimentGradeId.lessThan=" + DEFAULT_ASSIMENT_GRADE_ID);

        // Get all the gradeReviewList where assimentGradeId is less than UPDATED_ASSIMENT_GRADE_ID
        defaultGradeReviewShouldBeFound("assimentGradeId.lessThan=" + UPDATED_ASSIMENT_GRADE_ID);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByAssimentGradeIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where assimentGradeId is greater than DEFAULT_ASSIMENT_GRADE_ID
        defaultGradeReviewShouldNotBeFound("assimentGradeId.greaterThan=" + DEFAULT_ASSIMENT_GRADE_ID);

        // Get all the gradeReviewList where assimentGradeId is greater than SMALLER_ASSIMENT_GRADE_ID
        defaultGradeReviewShouldBeFound("assimentGradeId.greaterThan=" + SMALLER_ASSIMENT_GRADE_ID);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByCurrentGradeIsEqualToSomething() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where currentGrade equals to DEFAULT_CURRENT_GRADE
        defaultGradeReviewShouldBeFound("currentGrade.equals=" + DEFAULT_CURRENT_GRADE);

        // Get all the gradeReviewList where currentGrade equals to UPDATED_CURRENT_GRADE
        defaultGradeReviewShouldNotBeFound("currentGrade.equals=" + UPDATED_CURRENT_GRADE);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByCurrentGradeIsInShouldWork() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where currentGrade in DEFAULT_CURRENT_GRADE or UPDATED_CURRENT_GRADE
        defaultGradeReviewShouldBeFound("currentGrade.in=" + DEFAULT_CURRENT_GRADE + "," + UPDATED_CURRENT_GRADE);

        // Get all the gradeReviewList where currentGrade equals to UPDATED_CURRENT_GRADE
        defaultGradeReviewShouldNotBeFound("currentGrade.in=" + UPDATED_CURRENT_GRADE);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByCurrentGradeIsNullOrNotNull() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where currentGrade is not null
        defaultGradeReviewShouldBeFound("currentGrade.specified=true");

        // Get all the gradeReviewList where currentGrade is null
        defaultGradeReviewShouldNotBeFound("currentGrade.specified=false");
    }

    @Test
    @Transactional
    void getAllGradeReviewsByCurrentGradeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where currentGrade is greater than or equal to DEFAULT_CURRENT_GRADE
        defaultGradeReviewShouldBeFound("currentGrade.greaterThanOrEqual=" + DEFAULT_CURRENT_GRADE);

        // Get all the gradeReviewList where currentGrade is greater than or equal to UPDATED_CURRENT_GRADE
        defaultGradeReviewShouldNotBeFound("currentGrade.greaterThanOrEqual=" + UPDATED_CURRENT_GRADE);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByCurrentGradeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where currentGrade is less than or equal to DEFAULT_CURRENT_GRADE
        defaultGradeReviewShouldBeFound("currentGrade.lessThanOrEqual=" + DEFAULT_CURRENT_GRADE);

        // Get all the gradeReviewList where currentGrade is less than or equal to SMALLER_CURRENT_GRADE
        defaultGradeReviewShouldNotBeFound("currentGrade.lessThanOrEqual=" + SMALLER_CURRENT_GRADE);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByCurrentGradeIsLessThanSomething() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where currentGrade is less than DEFAULT_CURRENT_GRADE
        defaultGradeReviewShouldNotBeFound("currentGrade.lessThan=" + DEFAULT_CURRENT_GRADE);

        // Get all the gradeReviewList where currentGrade is less than UPDATED_CURRENT_GRADE
        defaultGradeReviewShouldBeFound("currentGrade.lessThan=" + UPDATED_CURRENT_GRADE);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByCurrentGradeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where currentGrade is greater than DEFAULT_CURRENT_GRADE
        defaultGradeReviewShouldNotBeFound("currentGrade.greaterThan=" + DEFAULT_CURRENT_GRADE);

        // Get all the gradeReviewList where currentGrade is greater than SMALLER_CURRENT_GRADE
        defaultGradeReviewShouldBeFound("currentGrade.greaterThan=" + SMALLER_CURRENT_GRADE);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByExpectationGradeIsEqualToSomething() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where expectationGrade equals to DEFAULT_EXPECTATION_GRADE
        defaultGradeReviewShouldBeFound("expectationGrade.equals=" + DEFAULT_EXPECTATION_GRADE);

        // Get all the gradeReviewList where expectationGrade equals to UPDATED_EXPECTATION_GRADE
        defaultGradeReviewShouldNotBeFound("expectationGrade.equals=" + UPDATED_EXPECTATION_GRADE);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByExpectationGradeIsInShouldWork() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where expectationGrade in DEFAULT_EXPECTATION_GRADE or UPDATED_EXPECTATION_GRADE
        defaultGradeReviewShouldBeFound("expectationGrade.in=" + DEFAULT_EXPECTATION_GRADE + "," + UPDATED_EXPECTATION_GRADE);

        // Get all the gradeReviewList where expectationGrade equals to UPDATED_EXPECTATION_GRADE
        defaultGradeReviewShouldNotBeFound("expectationGrade.in=" + UPDATED_EXPECTATION_GRADE);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByExpectationGradeIsNullOrNotNull() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where expectationGrade is not null
        defaultGradeReviewShouldBeFound("expectationGrade.specified=true");

        // Get all the gradeReviewList where expectationGrade is null
        defaultGradeReviewShouldNotBeFound("expectationGrade.specified=false");
    }

    @Test
    @Transactional
    void getAllGradeReviewsByExpectationGradeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where expectationGrade is greater than or equal to DEFAULT_EXPECTATION_GRADE
        defaultGradeReviewShouldBeFound("expectationGrade.greaterThanOrEqual=" + DEFAULT_EXPECTATION_GRADE);

        // Get all the gradeReviewList where expectationGrade is greater than or equal to UPDATED_EXPECTATION_GRADE
        defaultGradeReviewShouldNotBeFound("expectationGrade.greaterThanOrEqual=" + UPDATED_EXPECTATION_GRADE);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByExpectationGradeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where expectationGrade is less than or equal to DEFAULT_EXPECTATION_GRADE
        defaultGradeReviewShouldBeFound("expectationGrade.lessThanOrEqual=" + DEFAULT_EXPECTATION_GRADE);

        // Get all the gradeReviewList where expectationGrade is less than or equal to SMALLER_EXPECTATION_GRADE
        defaultGradeReviewShouldNotBeFound("expectationGrade.lessThanOrEqual=" + SMALLER_EXPECTATION_GRADE);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByExpectationGradeIsLessThanSomething() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where expectationGrade is less than DEFAULT_EXPECTATION_GRADE
        defaultGradeReviewShouldNotBeFound("expectationGrade.lessThan=" + DEFAULT_EXPECTATION_GRADE);

        // Get all the gradeReviewList where expectationGrade is less than UPDATED_EXPECTATION_GRADE
        defaultGradeReviewShouldBeFound("expectationGrade.lessThan=" + UPDATED_EXPECTATION_GRADE);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByExpectationGradeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where expectationGrade is greater than DEFAULT_EXPECTATION_GRADE
        defaultGradeReviewShouldNotBeFound("expectationGrade.greaterThan=" + DEFAULT_EXPECTATION_GRADE);

        // Get all the gradeReviewList where expectationGrade is greater than SMALLER_EXPECTATION_GRADE
        defaultGradeReviewShouldBeFound("expectationGrade.greaterThan=" + SMALLER_EXPECTATION_GRADE);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByStudentExplanationIsEqualToSomething() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where studentExplanation equals to DEFAULT_STUDENT_EXPLANATION
        defaultGradeReviewShouldBeFound("studentExplanation.equals=" + DEFAULT_STUDENT_EXPLANATION);

        // Get all the gradeReviewList where studentExplanation equals to UPDATED_STUDENT_EXPLANATION
        defaultGradeReviewShouldNotBeFound("studentExplanation.equals=" + UPDATED_STUDENT_EXPLANATION);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByStudentExplanationIsInShouldWork() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where studentExplanation in DEFAULT_STUDENT_EXPLANATION or UPDATED_STUDENT_EXPLANATION
        defaultGradeReviewShouldBeFound("studentExplanation.in=" + DEFAULT_STUDENT_EXPLANATION + "," + UPDATED_STUDENT_EXPLANATION);

        // Get all the gradeReviewList where studentExplanation equals to UPDATED_STUDENT_EXPLANATION
        defaultGradeReviewShouldNotBeFound("studentExplanation.in=" + UPDATED_STUDENT_EXPLANATION);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByStudentExplanationIsNullOrNotNull() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where studentExplanation is not null
        defaultGradeReviewShouldBeFound("studentExplanation.specified=true");

        // Get all the gradeReviewList where studentExplanation is null
        defaultGradeReviewShouldNotBeFound("studentExplanation.specified=false");
    }

    @Test
    @Transactional
    void getAllGradeReviewsByStudentExplanationContainsSomething() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where studentExplanation contains DEFAULT_STUDENT_EXPLANATION
        defaultGradeReviewShouldBeFound("studentExplanation.contains=" + DEFAULT_STUDENT_EXPLANATION);

        // Get all the gradeReviewList where studentExplanation contains UPDATED_STUDENT_EXPLANATION
        defaultGradeReviewShouldNotBeFound("studentExplanation.contains=" + UPDATED_STUDENT_EXPLANATION);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByStudentExplanationNotContainsSomething() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where studentExplanation does not contain DEFAULT_STUDENT_EXPLANATION
        defaultGradeReviewShouldNotBeFound("studentExplanation.doesNotContain=" + DEFAULT_STUDENT_EXPLANATION);

        // Get all the gradeReviewList where studentExplanation does not contain UPDATED_STUDENT_EXPLANATION
        defaultGradeReviewShouldBeFound("studentExplanation.doesNotContain=" + UPDATED_STUDENT_EXPLANATION);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByTeacherCommentIsEqualToSomething() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where teacherComment equals to DEFAULT_TEACHER_COMMENT
        defaultGradeReviewShouldBeFound("teacherComment.equals=" + DEFAULT_TEACHER_COMMENT);

        // Get all the gradeReviewList where teacherComment equals to UPDATED_TEACHER_COMMENT
        defaultGradeReviewShouldNotBeFound("teacherComment.equals=" + UPDATED_TEACHER_COMMENT);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByTeacherCommentIsInShouldWork() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where teacherComment in DEFAULT_TEACHER_COMMENT or UPDATED_TEACHER_COMMENT
        defaultGradeReviewShouldBeFound("teacherComment.in=" + DEFAULT_TEACHER_COMMENT + "," + UPDATED_TEACHER_COMMENT);

        // Get all the gradeReviewList where teacherComment equals to UPDATED_TEACHER_COMMENT
        defaultGradeReviewShouldNotBeFound("teacherComment.in=" + UPDATED_TEACHER_COMMENT);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByTeacherCommentIsNullOrNotNull() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where teacherComment is not null
        defaultGradeReviewShouldBeFound("teacherComment.specified=true");

        // Get all the gradeReviewList where teacherComment is null
        defaultGradeReviewShouldNotBeFound("teacherComment.specified=false");
    }

    @Test
    @Transactional
    void getAllGradeReviewsByTeacherCommentContainsSomething() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where teacherComment contains DEFAULT_TEACHER_COMMENT
        defaultGradeReviewShouldBeFound("teacherComment.contains=" + DEFAULT_TEACHER_COMMENT);

        // Get all the gradeReviewList where teacherComment contains UPDATED_TEACHER_COMMENT
        defaultGradeReviewShouldNotBeFound("teacherComment.contains=" + UPDATED_TEACHER_COMMENT);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByTeacherCommentNotContainsSomething() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where teacherComment does not contain DEFAULT_TEACHER_COMMENT
        defaultGradeReviewShouldNotBeFound("teacherComment.doesNotContain=" + DEFAULT_TEACHER_COMMENT);

        // Get all the gradeReviewList where teacherComment does not contain UPDATED_TEACHER_COMMENT
        defaultGradeReviewShouldBeFound("teacherComment.doesNotContain=" + UPDATED_TEACHER_COMMENT);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByIsFinalIsEqualToSomething() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where isFinal equals to DEFAULT_IS_FINAL
        defaultGradeReviewShouldBeFound("isFinal.equals=" + DEFAULT_IS_FINAL);

        // Get all the gradeReviewList where isFinal equals to UPDATED_IS_FINAL
        defaultGradeReviewShouldNotBeFound("isFinal.equals=" + UPDATED_IS_FINAL);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByIsFinalIsInShouldWork() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where isFinal in DEFAULT_IS_FINAL or UPDATED_IS_FINAL
        defaultGradeReviewShouldBeFound("isFinal.in=" + DEFAULT_IS_FINAL + "," + UPDATED_IS_FINAL);

        // Get all the gradeReviewList where isFinal equals to UPDATED_IS_FINAL
        defaultGradeReviewShouldNotBeFound("isFinal.in=" + UPDATED_IS_FINAL);
    }

    @Test
    @Transactional
    void getAllGradeReviewsByIsFinalIsNullOrNotNull() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        // Get all the gradeReviewList where isFinal is not null
        defaultGradeReviewShouldBeFound("isFinal.specified=true");

        // Get all the gradeReviewList where isFinal is null
        defaultGradeReviewShouldNotBeFound("isFinal.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGradeReviewShouldBeFound(String filter) throws Exception {
        restGradeReviewMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gradeReview.getId().intValue())))
            .andExpect(jsonPath("$.[*].gradeCompositionId").value(hasItem(DEFAULT_GRADE_COMPOSITION_ID.intValue())))
            .andExpect(jsonPath("$.[*].studentId").value(hasItem(DEFAULT_STUDENT_ID)))
            .andExpect(jsonPath("$.[*].courseId").value(hasItem(DEFAULT_COURSE_ID.intValue())))
            .andExpect(jsonPath("$.[*].reviewerId").value(hasItem(DEFAULT_REVIEWER_ID.intValue())))
            .andExpect(jsonPath("$.[*].assigmentId").value(hasItem(DEFAULT_ASSIGMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].assimentGradeId").value(hasItem(DEFAULT_ASSIMENT_GRADE_ID.intValue())))
            .andExpect(jsonPath("$.[*].currentGrade").value(hasItem(DEFAULT_CURRENT_GRADE.intValue())))
            .andExpect(jsonPath("$.[*].expectationGrade").value(hasItem(DEFAULT_EXPECTATION_GRADE.intValue())))
            .andExpect(jsonPath("$.[*].studentExplanation").value(hasItem(DEFAULT_STUDENT_EXPLANATION)))
            .andExpect(jsonPath("$.[*].teacherComment").value(hasItem(DEFAULT_TEACHER_COMMENT)))
            .andExpect(jsonPath("$.[*].isFinal").value(hasItem(DEFAULT_IS_FINAL.booleanValue())));

        // Check, that the count call also returns 1
        restGradeReviewMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGradeReviewShouldNotBeFound(String filter) throws Exception {
        restGradeReviewMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGradeReviewMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingGradeReview() throws Exception {
        // Get the gradeReview
        restGradeReviewMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGradeReview() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        int databaseSizeBeforeUpdate = gradeReviewRepository.findAll().size();

        // Update the gradeReview
        GradeReview updatedGradeReview = gradeReviewRepository.findById(gradeReview.getId()).get();
        // Disconnect from session so that the updates on updatedGradeReview are not directly saved in db
        em.detach(updatedGradeReview);
        updatedGradeReview
            .gradeCompositionId(UPDATED_GRADE_COMPOSITION_ID)
            .studentId(UPDATED_STUDENT_ID)
            .courseId(UPDATED_COURSE_ID)
            .reviewerId(UPDATED_REVIEWER_ID)
            .assigmentId(UPDATED_ASSIGMENT_ID)
            .assimentGradeId(UPDATED_ASSIMENT_GRADE_ID)
            .currentGrade(UPDATED_CURRENT_GRADE)
            .expectationGrade(UPDATED_EXPECTATION_GRADE)
            .studentExplanation(UPDATED_STUDENT_EXPLANATION)
            .teacherComment(UPDATED_TEACHER_COMMENT)
            .isFinal(UPDATED_IS_FINAL);

        restGradeReviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGradeReview.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedGradeReview))
            )
            .andExpect(status().isOk());

        // Validate the GradeReview in the database
        List<GradeReview> gradeReviewList = gradeReviewRepository.findAll();
        assertThat(gradeReviewList).hasSize(databaseSizeBeforeUpdate);
        GradeReview testGradeReview = gradeReviewList.get(gradeReviewList.size() - 1);
        assertThat(testGradeReview.getGradeCompositionId()).isEqualTo(UPDATED_GRADE_COMPOSITION_ID);
        assertThat(testGradeReview.getStudentId()).isEqualTo(UPDATED_STUDENT_ID);
        assertThat(testGradeReview.getCourseId()).isEqualTo(UPDATED_COURSE_ID);
        assertThat(testGradeReview.getReviewerId()).isEqualTo(UPDATED_REVIEWER_ID);
        assertThat(testGradeReview.getAssigmentId()).isEqualTo(UPDATED_ASSIGMENT_ID);
        assertThat(testGradeReview.getAssimentGradeId()).isEqualTo(UPDATED_ASSIMENT_GRADE_ID);
        assertThat(testGradeReview.getCurrentGrade()).isEqualTo(UPDATED_CURRENT_GRADE);
        assertThat(testGradeReview.getExpectationGrade()).isEqualTo(UPDATED_EXPECTATION_GRADE);
        assertThat(testGradeReview.getStudentExplanation()).isEqualTo(UPDATED_STUDENT_EXPLANATION);
        assertThat(testGradeReview.getTeacherComment()).isEqualTo(UPDATED_TEACHER_COMMENT);
        assertThat(testGradeReview.getIsFinal()).isEqualTo(UPDATED_IS_FINAL);
    }

    @Test
    @Transactional
    void putNonExistingGradeReview() throws Exception {
        int databaseSizeBeforeUpdate = gradeReviewRepository.findAll().size();
        gradeReview.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGradeReviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gradeReview.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gradeReview))
            )
            .andExpect(status().isBadRequest());

        // Validate the GradeReview in the database
        List<GradeReview> gradeReviewList = gradeReviewRepository.findAll();
        assertThat(gradeReviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGradeReview() throws Exception {
        int databaseSizeBeforeUpdate = gradeReviewRepository.findAll().size();
        gradeReview.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGradeReviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gradeReview))
            )
            .andExpect(status().isBadRequest());

        // Validate the GradeReview in the database
        List<GradeReview> gradeReviewList = gradeReviewRepository.findAll();
        assertThat(gradeReviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGradeReview() throws Exception {
        int databaseSizeBeforeUpdate = gradeReviewRepository.findAll().size();
        gradeReview.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGradeReviewMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gradeReview)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the GradeReview in the database
        List<GradeReview> gradeReviewList = gradeReviewRepository.findAll();
        assertThat(gradeReviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGradeReviewWithPatch() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        int databaseSizeBeforeUpdate = gradeReviewRepository.findAll().size();

        // Update the gradeReview using partial update
        GradeReview partialUpdatedGradeReview = new GradeReview();
        partialUpdatedGradeReview.setId(gradeReview.getId());

        partialUpdatedGradeReview
            .courseId(UPDATED_COURSE_ID)
            .reviewerId(UPDATED_REVIEWER_ID)
            .assimentGradeId(UPDATED_ASSIMENT_GRADE_ID)
            .expectationGrade(UPDATED_EXPECTATION_GRADE)
            .studentExplanation(UPDATED_STUDENT_EXPLANATION)
            .teacherComment(UPDATED_TEACHER_COMMENT);

        restGradeReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGradeReview.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGradeReview))
            )
            .andExpect(status().isOk());

        // Validate the GradeReview in the database
        List<GradeReview> gradeReviewList = gradeReviewRepository.findAll();
        assertThat(gradeReviewList).hasSize(databaseSizeBeforeUpdate);
        GradeReview testGradeReview = gradeReviewList.get(gradeReviewList.size() - 1);
        assertThat(testGradeReview.getGradeCompositionId()).isEqualTo(DEFAULT_GRADE_COMPOSITION_ID);
        assertThat(testGradeReview.getStudentId()).isEqualTo(DEFAULT_STUDENT_ID);
        assertThat(testGradeReview.getCourseId()).isEqualTo(UPDATED_COURSE_ID);
        assertThat(testGradeReview.getReviewerId()).isEqualTo(UPDATED_REVIEWER_ID);
        assertThat(testGradeReview.getAssigmentId()).isEqualTo(DEFAULT_ASSIGMENT_ID);
        assertThat(testGradeReview.getAssimentGradeId()).isEqualTo(UPDATED_ASSIMENT_GRADE_ID);
        assertThat(testGradeReview.getCurrentGrade()).isEqualTo(DEFAULT_CURRENT_GRADE);
        assertThat(testGradeReview.getExpectationGrade()).isEqualTo(UPDATED_EXPECTATION_GRADE);
        assertThat(testGradeReview.getStudentExplanation()).isEqualTo(UPDATED_STUDENT_EXPLANATION);
        assertThat(testGradeReview.getTeacherComment()).isEqualTo(UPDATED_TEACHER_COMMENT);
        assertThat(testGradeReview.getIsFinal()).isEqualTo(DEFAULT_IS_FINAL);
    }

    @Test
    @Transactional
    void fullUpdateGradeReviewWithPatch() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        int databaseSizeBeforeUpdate = gradeReviewRepository.findAll().size();

        // Update the gradeReview using partial update
        GradeReview partialUpdatedGradeReview = new GradeReview();
        partialUpdatedGradeReview.setId(gradeReview.getId());

        partialUpdatedGradeReview
            .gradeCompositionId(UPDATED_GRADE_COMPOSITION_ID)
            .studentId(UPDATED_STUDENT_ID)
            .courseId(UPDATED_COURSE_ID)
            .reviewerId(UPDATED_REVIEWER_ID)
            .assigmentId(UPDATED_ASSIGMENT_ID)
            .assimentGradeId(UPDATED_ASSIMENT_GRADE_ID)
            .currentGrade(UPDATED_CURRENT_GRADE)
            .expectationGrade(UPDATED_EXPECTATION_GRADE)
            .studentExplanation(UPDATED_STUDENT_EXPLANATION)
            .teacherComment(UPDATED_TEACHER_COMMENT)
            .isFinal(UPDATED_IS_FINAL);

        restGradeReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGradeReview.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGradeReview))
            )
            .andExpect(status().isOk());

        // Validate the GradeReview in the database
        List<GradeReview> gradeReviewList = gradeReviewRepository.findAll();
        assertThat(gradeReviewList).hasSize(databaseSizeBeforeUpdate);
        GradeReview testGradeReview = gradeReviewList.get(gradeReviewList.size() - 1);
        assertThat(testGradeReview.getGradeCompositionId()).isEqualTo(UPDATED_GRADE_COMPOSITION_ID);
        assertThat(testGradeReview.getStudentId()).isEqualTo(UPDATED_STUDENT_ID);
        assertThat(testGradeReview.getCourseId()).isEqualTo(UPDATED_COURSE_ID);
        assertThat(testGradeReview.getReviewerId()).isEqualTo(UPDATED_REVIEWER_ID);
        assertThat(testGradeReview.getAssigmentId()).isEqualTo(UPDATED_ASSIGMENT_ID);
        assertThat(testGradeReview.getAssimentGradeId()).isEqualTo(UPDATED_ASSIMENT_GRADE_ID);
        assertThat(testGradeReview.getCurrentGrade()).isEqualTo(UPDATED_CURRENT_GRADE);
        assertThat(testGradeReview.getExpectationGrade()).isEqualTo(UPDATED_EXPECTATION_GRADE);
        assertThat(testGradeReview.getStudentExplanation()).isEqualTo(UPDATED_STUDENT_EXPLANATION);
        assertThat(testGradeReview.getTeacherComment()).isEqualTo(UPDATED_TEACHER_COMMENT);
        assertThat(testGradeReview.getIsFinal()).isEqualTo(UPDATED_IS_FINAL);
    }

    @Test
    @Transactional
    void patchNonExistingGradeReview() throws Exception {
        int databaseSizeBeforeUpdate = gradeReviewRepository.findAll().size();
        gradeReview.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGradeReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, gradeReview.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gradeReview))
            )
            .andExpect(status().isBadRequest());

        // Validate the GradeReview in the database
        List<GradeReview> gradeReviewList = gradeReviewRepository.findAll();
        assertThat(gradeReviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGradeReview() throws Exception {
        int databaseSizeBeforeUpdate = gradeReviewRepository.findAll().size();
        gradeReview.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGradeReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gradeReview))
            )
            .andExpect(status().isBadRequest());

        // Validate the GradeReview in the database
        List<GradeReview> gradeReviewList = gradeReviewRepository.findAll();
        assertThat(gradeReviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGradeReview() throws Exception {
        int databaseSizeBeforeUpdate = gradeReviewRepository.findAll().size();
        gradeReview.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGradeReviewMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(gradeReview))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GradeReview in the database
        List<GradeReview> gradeReviewList = gradeReviewRepository.findAll();
        assertThat(gradeReviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGradeReview() throws Exception {
        // Initialize the database
        gradeReviewRepository.saveAndFlush(gradeReview);

        int databaseSizeBeforeDelete = gradeReviewRepository.findAll().size();

        // Delete the gradeReview
        restGradeReviewMockMvc
            .perform(delete(ENTITY_API_URL_ID, gradeReview.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GradeReview> gradeReviewList = gradeReviewRepository.findAll();
        assertThat(gradeReviewList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
