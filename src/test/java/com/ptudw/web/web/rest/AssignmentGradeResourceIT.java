package com.ptudw.web.web.rest;

import static com.ptudw.web.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ptudw.web.IntegrationTest;
import com.ptudw.web.domain.Assignment;
import com.ptudw.web.domain.AssignmentGrade;
import com.ptudw.web.repository.AssignmentGradeRepository;
import com.ptudw.web.service.criteria.AssignmentGradeCriteria;
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
 * Integration tests for the {@link AssignmentGradeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AssignmentGradeResourceIT {

    private static final String DEFAULT_STUDENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_STUDENT_ID = "BBBBBBBBBB";

    private static final Long DEFAULT_GRADE = 1L;
    private static final Long UPDATED_GRADE = 2L;
    private static final Long SMALLER_GRADE = 1L - 1L;

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

    private static final String ENTITY_API_URL = "/api/assignment-grades";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AssignmentGradeRepository assignmentGradeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAssignmentGradeMockMvc;

    private AssignmentGrade assignmentGrade;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssignmentGrade createEntity(EntityManager em) {
        AssignmentGrade assignmentGrade = new AssignmentGrade()
            .studentId(DEFAULT_STUDENT_ID)
            .grade(DEFAULT_GRADE)
            .isDeleted(DEFAULT_IS_DELETED)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return assignmentGrade;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssignmentGrade createUpdatedEntity(EntityManager em) {
        AssignmentGrade assignmentGrade = new AssignmentGrade()
            .studentId(UPDATED_STUDENT_ID)
            .grade(UPDATED_GRADE)
            .isDeleted(UPDATED_IS_DELETED)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return assignmentGrade;
    }

    @BeforeEach
    public void initTest() {
        assignmentGrade = createEntity(em);
    }

    @Test
    @Transactional
    void createAssignmentGrade() throws Exception {
        int databaseSizeBeforeCreate = assignmentGradeRepository.findAll().size();
        // Create the AssignmentGrade
        restAssignmentGradeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assignmentGrade))
            )
            .andExpect(status().isCreated());

        // Validate the AssignmentGrade in the database
        List<AssignmentGrade> assignmentGradeList = assignmentGradeRepository.findAll();
        assertThat(assignmentGradeList).hasSize(databaseSizeBeforeCreate + 1);
        AssignmentGrade testAssignmentGrade = assignmentGradeList.get(assignmentGradeList.size() - 1);
        assertThat(testAssignmentGrade.getStudentId()).isEqualTo(DEFAULT_STUDENT_ID);
        assertThat(testAssignmentGrade.getGrade()).isEqualTo(DEFAULT_GRADE);
        assertThat(testAssignmentGrade.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testAssignmentGrade.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testAssignmentGrade.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testAssignmentGrade.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testAssignmentGrade.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createAssignmentGradeWithExistingId() throws Exception {
        // Create the AssignmentGrade with an existing ID
        assignmentGrade.setId(1L);

        int databaseSizeBeforeCreate = assignmentGradeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssignmentGradeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assignmentGrade))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssignmentGrade in the database
        List<AssignmentGrade> assignmentGradeList = assignmentGradeRepository.findAll();
        assertThat(assignmentGradeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStudentIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = assignmentGradeRepository.findAll().size();
        // set the field null
        assignmentGrade.setStudentId(null);

        // Create the AssignmentGrade, which fails.

        restAssignmentGradeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assignmentGrade))
            )
            .andExpect(status().isBadRequest());

        List<AssignmentGrade> assignmentGradeList = assignmentGradeRepository.findAll();
        assertThat(assignmentGradeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGradeIsRequired() throws Exception {
        int databaseSizeBeforeTest = assignmentGradeRepository.findAll().size();
        // set the field null
        assignmentGrade.setGrade(null);

        // Create the AssignmentGrade, which fails.

        restAssignmentGradeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assignmentGrade))
            )
            .andExpect(status().isBadRequest());

        List<AssignmentGrade> assignmentGradeList = assignmentGradeRepository.findAll();
        assertThat(assignmentGradeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = assignmentGradeRepository.findAll().size();
        // set the field null
        assignmentGrade.setCreatedBy(null);

        // Create the AssignmentGrade, which fails.

        restAssignmentGradeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assignmentGrade))
            )
            .andExpect(status().isBadRequest());

        List<AssignmentGrade> assignmentGradeList = assignmentGradeRepository.findAll();
        assertThat(assignmentGradeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = assignmentGradeRepository.findAll().size();
        // set the field null
        assignmentGrade.setCreatedDate(null);

        // Create the AssignmentGrade, which fails.

        restAssignmentGradeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assignmentGrade))
            )
            .andExpect(status().isBadRequest());

        List<AssignmentGrade> assignmentGradeList = assignmentGradeRepository.findAll();
        assertThat(assignmentGradeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastModifiedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = assignmentGradeRepository.findAll().size();
        // set the field null
        assignmentGrade.setLastModifiedBy(null);

        // Create the AssignmentGrade, which fails.

        restAssignmentGradeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assignmentGrade))
            )
            .andExpect(status().isBadRequest());

        List<AssignmentGrade> assignmentGradeList = assignmentGradeRepository.findAll();
        assertThat(assignmentGradeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastModifiedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = assignmentGradeRepository.findAll().size();
        // set the field null
        assignmentGrade.setLastModifiedDate(null);

        // Create the AssignmentGrade, which fails.

        restAssignmentGradeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assignmentGrade))
            )
            .andExpect(status().isBadRequest());

        List<AssignmentGrade> assignmentGradeList = assignmentGradeRepository.findAll();
        assertThat(assignmentGradeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAssignmentGrades() throws Exception {
        // Initialize the database
        assignmentGradeRepository.saveAndFlush(assignmentGrade);

        // Get all the assignmentGradeList
        restAssignmentGradeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assignmentGrade.getId().intValue())))
            .andExpect(jsonPath("$.[*].studentId").value(hasItem(DEFAULT_STUDENT_ID)))
            .andExpect(jsonPath("$.[*].grade").value(hasItem(DEFAULT_GRADE.intValue())))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(sameInstant(DEFAULT_LAST_MODIFIED_DATE))));
    }

    @Test
    @Transactional
    void getAssignmentGrade() throws Exception {
        // Initialize the database
        assignmentGradeRepository.saveAndFlush(assignmentGrade);

        // Get the assignmentGrade
        restAssignmentGradeMockMvc
            .perform(get(ENTITY_API_URL_ID, assignmentGrade.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(assignmentGrade.getId().intValue()))
            .andExpect(jsonPath("$.studentId").value(DEFAULT_STUDENT_ID))
            .andExpect(jsonPath("$.grade").value(DEFAULT_GRADE.intValue()))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(sameInstant(DEFAULT_LAST_MODIFIED_DATE)));
    }

    @Test
    @Transactional
    void getAssignmentGradesByIdFiltering() throws Exception {
        // Initialize the database
        assignmentGradeRepository.saveAndFlush(assignmentGrade);

        Long id = assignmentGrade.getId();

        defaultAssignmentGradeShouldBeFound("id.equals=" + id);
        defaultAssignmentGradeShouldNotBeFound("id.notEquals=" + id);

        defaultAssignmentGradeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAssignmentGradeShouldNotBeFound("id.greaterThan=" + id);

        defaultAssignmentGradeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAssignmentGradeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAssignmentGradesByStudentIdIsEqualToSomething() throws Exception {
        // Initialize the database
        assignmentGradeRepository.saveAndFlush(assignmentGrade);

        // Get all the assignmentGradeList where studentId equals to DEFAULT_STUDENT_ID
        defaultAssignmentGradeShouldBeFound("studentId.equals=" + DEFAULT_STUDENT_ID);

        // Get all the assignmentGradeList where studentId equals to UPDATED_STUDENT_ID
        defaultAssignmentGradeShouldNotBeFound("studentId.equals=" + UPDATED_STUDENT_ID);
    }

    @Test
    @Transactional
    void getAllAssignmentGradesByStudentIdIsInShouldWork() throws Exception {
        // Initialize the database
        assignmentGradeRepository.saveAndFlush(assignmentGrade);

        // Get all the assignmentGradeList where studentId in DEFAULT_STUDENT_ID or UPDATED_STUDENT_ID
        defaultAssignmentGradeShouldBeFound("studentId.in=" + DEFAULT_STUDENT_ID + "," + UPDATED_STUDENT_ID);

        // Get all the assignmentGradeList where studentId equals to UPDATED_STUDENT_ID
        defaultAssignmentGradeShouldNotBeFound("studentId.in=" + UPDATED_STUDENT_ID);
    }

    @Test
    @Transactional
    void getAllAssignmentGradesByStudentIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        assignmentGradeRepository.saveAndFlush(assignmentGrade);

        // Get all the assignmentGradeList where studentId is not null
        defaultAssignmentGradeShouldBeFound("studentId.specified=true");

        // Get all the assignmentGradeList where studentId is null
        defaultAssignmentGradeShouldNotBeFound("studentId.specified=false");
    }

    @Test
    @Transactional
    void getAllAssignmentGradesByStudentIdContainsSomething() throws Exception {
        // Initialize the database
        assignmentGradeRepository.saveAndFlush(assignmentGrade);

        // Get all the assignmentGradeList where studentId contains DEFAULT_STUDENT_ID
        defaultAssignmentGradeShouldBeFound("studentId.contains=" + DEFAULT_STUDENT_ID);

        // Get all the assignmentGradeList where studentId contains UPDATED_STUDENT_ID
        defaultAssignmentGradeShouldNotBeFound("studentId.contains=" + UPDATED_STUDENT_ID);
    }

    @Test
    @Transactional
    void getAllAssignmentGradesByStudentIdNotContainsSomething() throws Exception {
        // Initialize the database
        assignmentGradeRepository.saveAndFlush(assignmentGrade);

        // Get all the assignmentGradeList where studentId does not contain DEFAULT_STUDENT_ID
        defaultAssignmentGradeShouldNotBeFound("studentId.doesNotContain=" + DEFAULT_STUDENT_ID);

        // Get all the assignmentGradeList where studentId does not contain UPDATED_STUDENT_ID
        defaultAssignmentGradeShouldBeFound("studentId.doesNotContain=" + UPDATED_STUDENT_ID);
    }

    @Test
    @Transactional
    void getAllAssignmentGradesByGradeIsEqualToSomething() throws Exception {
        // Initialize the database
        assignmentGradeRepository.saveAndFlush(assignmentGrade);

        // Get all the assignmentGradeList where grade equals to DEFAULT_GRADE
        defaultAssignmentGradeShouldBeFound("grade.equals=" + DEFAULT_GRADE);

        // Get all the assignmentGradeList where grade equals to UPDATED_GRADE
        defaultAssignmentGradeShouldNotBeFound("grade.equals=" + UPDATED_GRADE);
    }

    @Test
    @Transactional
    void getAllAssignmentGradesByGradeIsInShouldWork() throws Exception {
        // Initialize the database
        assignmentGradeRepository.saveAndFlush(assignmentGrade);

        // Get all the assignmentGradeList where grade in DEFAULT_GRADE or UPDATED_GRADE
        defaultAssignmentGradeShouldBeFound("grade.in=" + DEFAULT_GRADE + "," + UPDATED_GRADE);

        // Get all the assignmentGradeList where grade equals to UPDATED_GRADE
        defaultAssignmentGradeShouldNotBeFound("grade.in=" + UPDATED_GRADE);
    }

    @Test
    @Transactional
    void getAllAssignmentGradesByGradeIsNullOrNotNull() throws Exception {
        // Initialize the database
        assignmentGradeRepository.saveAndFlush(assignmentGrade);

        // Get all the assignmentGradeList where grade is not null
        defaultAssignmentGradeShouldBeFound("grade.specified=true");

        // Get all the assignmentGradeList where grade is null
        defaultAssignmentGradeShouldNotBeFound("grade.specified=false");
    }

    @Test
    @Transactional
    void getAllAssignmentGradesByGradeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assignmentGradeRepository.saveAndFlush(assignmentGrade);

        // Get all the assignmentGradeList where grade is greater than or equal to DEFAULT_GRADE
        defaultAssignmentGradeShouldBeFound("grade.greaterThanOrEqual=" + DEFAULT_GRADE);

        // Get all the assignmentGradeList where grade is greater than or equal to UPDATED_GRADE
        defaultAssignmentGradeShouldNotBeFound("grade.greaterThanOrEqual=" + UPDATED_GRADE);
    }

    @Test
    @Transactional
    void getAllAssignmentGradesByGradeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assignmentGradeRepository.saveAndFlush(assignmentGrade);

        // Get all the assignmentGradeList where grade is less than or equal to DEFAULT_GRADE
        defaultAssignmentGradeShouldBeFound("grade.lessThanOrEqual=" + DEFAULT_GRADE);

        // Get all the assignmentGradeList where grade is less than or equal to SMALLER_GRADE
        defaultAssignmentGradeShouldNotBeFound("grade.lessThanOrEqual=" + SMALLER_GRADE);
    }

    @Test
    @Transactional
    void getAllAssignmentGradesByGradeIsLessThanSomething() throws Exception {
        // Initialize the database
        assignmentGradeRepository.saveAndFlush(assignmentGrade);

        // Get all the assignmentGradeList where grade is less than DEFAULT_GRADE
        defaultAssignmentGradeShouldNotBeFound("grade.lessThan=" + DEFAULT_GRADE);

        // Get all the assignmentGradeList where grade is less than UPDATED_GRADE
        defaultAssignmentGradeShouldBeFound("grade.lessThan=" + UPDATED_GRADE);
    }

    @Test
    @Transactional
    void getAllAssignmentGradesByGradeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assignmentGradeRepository.saveAndFlush(assignmentGrade);

        // Get all the assignmentGradeList where grade is greater than DEFAULT_GRADE
        defaultAssignmentGradeShouldNotBeFound("grade.greaterThan=" + DEFAULT_GRADE);

        // Get all the assignmentGradeList where grade is greater than SMALLER_GRADE
        defaultAssignmentGradeShouldBeFound("grade.greaterThan=" + SMALLER_GRADE);
    }

    @Test
    @Transactional
    void getAllAssignmentGradesByIsDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        assignmentGradeRepository.saveAndFlush(assignmentGrade);

        // Get all the assignmentGradeList where isDeleted equals to DEFAULT_IS_DELETED
        defaultAssignmentGradeShouldBeFound("isDeleted.equals=" + DEFAULT_IS_DELETED);

        // Get all the assignmentGradeList where isDeleted equals to UPDATED_IS_DELETED
        defaultAssignmentGradeShouldNotBeFound("isDeleted.equals=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllAssignmentGradesByIsDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        assignmentGradeRepository.saveAndFlush(assignmentGrade);

        // Get all the assignmentGradeList where isDeleted in DEFAULT_IS_DELETED or UPDATED_IS_DELETED
        defaultAssignmentGradeShouldBeFound("isDeleted.in=" + DEFAULT_IS_DELETED + "," + UPDATED_IS_DELETED);

        // Get all the assignmentGradeList where isDeleted equals to UPDATED_IS_DELETED
        defaultAssignmentGradeShouldNotBeFound("isDeleted.in=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllAssignmentGradesByIsDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        assignmentGradeRepository.saveAndFlush(assignmentGrade);

        // Get all the assignmentGradeList where isDeleted is not null
        defaultAssignmentGradeShouldBeFound("isDeleted.specified=true");

        // Get all the assignmentGradeList where isDeleted is null
        defaultAssignmentGradeShouldNotBeFound("isDeleted.specified=false");
    }

    @Test
    @Transactional
    void getAllAssignmentGradesByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        assignmentGradeRepository.saveAndFlush(assignmentGrade);

        // Get all the assignmentGradeList where createdBy equals to DEFAULT_CREATED_BY
        defaultAssignmentGradeShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the assignmentGradeList where createdBy equals to UPDATED_CREATED_BY
        defaultAssignmentGradeShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllAssignmentGradesByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        assignmentGradeRepository.saveAndFlush(assignmentGrade);

        // Get all the assignmentGradeList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultAssignmentGradeShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the assignmentGradeList where createdBy equals to UPDATED_CREATED_BY
        defaultAssignmentGradeShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllAssignmentGradesByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        assignmentGradeRepository.saveAndFlush(assignmentGrade);

        // Get all the assignmentGradeList where createdBy is not null
        defaultAssignmentGradeShouldBeFound("createdBy.specified=true");

        // Get all the assignmentGradeList where createdBy is null
        defaultAssignmentGradeShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllAssignmentGradesByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        assignmentGradeRepository.saveAndFlush(assignmentGrade);

        // Get all the assignmentGradeList where createdBy contains DEFAULT_CREATED_BY
        defaultAssignmentGradeShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the assignmentGradeList where createdBy contains UPDATED_CREATED_BY
        defaultAssignmentGradeShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllAssignmentGradesByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        assignmentGradeRepository.saveAndFlush(assignmentGrade);

        // Get all the assignmentGradeList where createdBy does not contain DEFAULT_CREATED_BY
        defaultAssignmentGradeShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the assignmentGradeList where createdBy does not contain UPDATED_CREATED_BY
        defaultAssignmentGradeShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllAssignmentGradesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        assignmentGradeRepository.saveAndFlush(assignmentGrade);

        // Get all the assignmentGradeList where createdDate equals to DEFAULT_CREATED_DATE
        defaultAssignmentGradeShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the assignmentGradeList where createdDate equals to UPDATED_CREATED_DATE
        defaultAssignmentGradeShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllAssignmentGradesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        assignmentGradeRepository.saveAndFlush(assignmentGrade);

        // Get all the assignmentGradeList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultAssignmentGradeShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the assignmentGradeList where createdDate equals to UPDATED_CREATED_DATE
        defaultAssignmentGradeShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllAssignmentGradesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        assignmentGradeRepository.saveAndFlush(assignmentGrade);

        // Get all the assignmentGradeList where createdDate is not null
        defaultAssignmentGradeShouldBeFound("createdDate.specified=true");

        // Get all the assignmentGradeList where createdDate is null
        defaultAssignmentGradeShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAssignmentGradesByCreatedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assignmentGradeRepository.saveAndFlush(assignmentGrade);

        // Get all the assignmentGradeList where createdDate is greater than or equal to DEFAULT_CREATED_DATE
        defaultAssignmentGradeShouldBeFound("createdDate.greaterThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the assignmentGradeList where createdDate is greater than or equal to UPDATED_CREATED_DATE
        defaultAssignmentGradeShouldNotBeFound("createdDate.greaterThanOrEqual=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllAssignmentGradesByCreatedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assignmentGradeRepository.saveAndFlush(assignmentGrade);

        // Get all the assignmentGradeList where createdDate is less than or equal to DEFAULT_CREATED_DATE
        defaultAssignmentGradeShouldBeFound("createdDate.lessThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the assignmentGradeList where createdDate is less than or equal to SMALLER_CREATED_DATE
        defaultAssignmentGradeShouldNotBeFound("createdDate.lessThanOrEqual=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllAssignmentGradesByCreatedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        assignmentGradeRepository.saveAndFlush(assignmentGrade);

        // Get all the assignmentGradeList where createdDate is less than DEFAULT_CREATED_DATE
        defaultAssignmentGradeShouldNotBeFound("createdDate.lessThan=" + DEFAULT_CREATED_DATE);

        // Get all the assignmentGradeList where createdDate is less than UPDATED_CREATED_DATE
        defaultAssignmentGradeShouldBeFound("createdDate.lessThan=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllAssignmentGradesByCreatedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assignmentGradeRepository.saveAndFlush(assignmentGrade);

        // Get all the assignmentGradeList where createdDate is greater than DEFAULT_CREATED_DATE
        defaultAssignmentGradeShouldNotBeFound("createdDate.greaterThan=" + DEFAULT_CREATED_DATE);

        // Get all the assignmentGradeList where createdDate is greater than SMALLER_CREATED_DATE
        defaultAssignmentGradeShouldBeFound("createdDate.greaterThan=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllAssignmentGradesByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        assignmentGradeRepository.saveAndFlush(assignmentGrade);

        // Get all the assignmentGradeList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultAssignmentGradeShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the assignmentGradeList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultAssignmentGradeShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllAssignmentGradesByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        assignmentGradeRepository.saveAndFlush(assignmentGrade);

        // Get all the assignmentGradeList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultAssignmentGradeShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the assignmentGradeList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultAssignmentGradeShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllAssignmentGradesByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        assignmentGradeRepository.saveAndFlush(assignmentGrade);

        // Get all the assignmentGradeList where lastModifiedBy is not null
        defaultAssignmentGradeShouldBeFound("lastModifiedBy.specified=true");

        // Get all the assignmentGradeList where lastModifiedBy is null
        defaultAssignmentGradeShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllAssignmentGradesByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        assignmentGradeRepository.saveAndFlush(assignmentGrade);

        // Get all the assignmentGradeList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultAssignmentGradeShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the assignmentGradeList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultAssignmentGradeShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllAssignmentGradesByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        assignmentGradeRepository.saveAndFlush(assignmentGrade);

        // Get all the assignmentGradeList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultAssignmentGradeShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the assignmentGradeList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultAssignmentGradeShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllAssignmentGradesByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        assignmentGradeRepository.saveAndFlush(assignmentGrade);

        // Get all the assignmentGradeList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultAssignmentGradeShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the assignmentGradeList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultAssignmentGradeShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllAssignmentGradesByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        assignmentGradeRepository.saveAndFlush(assignmentGrade);

        // Get all the assignmentGradeList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultAssignmentGradeShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the assignmentGradeList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultAssignmentGradeShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllAssignmentGradesByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        assignmentGradeRepository.saveAndFlush(assignmentGrade);

        // Get all the assignmentGradeList where lastModifiedDate is not null
        defaultAssignmentGradeShouldBeFound("lastModifiedDate.specified=true");

        // Get all the assignmentGradeList where lastModifiedDate is null
        defaultAssignmentGradeShouldNotBeFound("lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAssignmentGradesByLastModifiedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assignmentGradeRepository.saveAndFlush(assignmentGrade);

        // Get all the assignmentGradeList where lastModifiedDate is greater than or equal to DEFAULT_LAST_MODIFIED_DATE
        defaultAssignmentGradeShouldBeFound("lastModifiedDate.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the assignmentGradeList where lastModifiedDate is greater than or equal to UPDATED_LAST_MODIFIED_DATE
        defaultAssignmentGradeShouldNotBeFound("lastModifiedDate.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllAssignmentGradesByLastModifiedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        assignmentGradeRepository.saveAndFlush(assignmentGrade);

        // Get all the assignmentGradeList where lastModifiedDate is less than or equal to DEFAULT_LAST_MODIFIED_DATE
        defaultAssignmentGradeShouldBeFound("lastModifiedDate.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the assignmentGradeList where lastModifiedDate is less than or equal to SMALLER_LAST_MODIFIED_DATE
        defaultAssignmentGradeShouldNotBeFound("lastModifiedDate.lessThanOrEqual=" + SMALLER_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllAssignmentGradesByLastModifiedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        assignmentGradeRepository.saveAndFlush(assignmentGrade);

        // Get all the assignmentGradeList where lastModifiedDate is less than DEFAULT_LAST_MODIFIED_DATE
        defaultAssignmentGradeShouldNotBeFound("lastModifiedDate.lessThan=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the assignmentGradeList where lastModifiedDate is less than UPDATED_LAST_MODIFIED_DATE
        defaultAssignmentGradeShouldBeFound("lastModifiedDate.lessThan=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllAssignmentGradesByLastModifiedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        assignmentGradeRepository.saveAndFlush(assignmentGrade);

        // Get all the assignmentGradeList where lastModifiedDate is greater than DEFAULT_LAST_MODIFIED_DATE
        defaultAssignmentGradeShouldNotBeFound("lastModifiedDate.greaterThan=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the assignmentGradeList where lastModifiedDate is greater than SMALLER_LAST_MODIFIED_DATE
        defaultAssignmentGradeShouldBeFound("lastModifiedDate.greaterThan=" + SMALLER_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllAssignmentGradesByAssignmentIsEqualToSomething() throws Exception {
        Assignment assignment;
        if (TestUtil.findAll(em, Assignment.class).isEmpty()) {
            assignmentGradeRepository.saveAndFlush(assignmentGrade);
            assignment = AssignmentResourceIT.createEntity(em);
        } else {
            assignment = TestUtil.findAll(em, Assignment.class).get(0);
        }
        em.persist(assignment);
        em.flush();
        assignmentGrade.setAssignment(assignment);
        assignmentGradeRepository.saveAndFlush(assignmentGrade);
        Long assignmentId = assignment.getId();

        // Get all the assignmentGradeList where assignment equals to assignmentId
        defaultAssignmentGradeShouldBeFound("assignmentId.equals=" + assignmentId);

        // Get all the assignmentGradeList where assignment equals to (assignmentId + 1)
        defaultAssignmentGradeShouldNotBeFound("assignmentId.equals=" + (assignmentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAssignmentGradeShouldBeFound(String filter) throws Exception {
        restAssignmentGradeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assignmentGrade.getId().intValue())))
            .andExpect(jsonPath("$.[*].studentId").value(hasItem(DEFAULT_STUDENT_ID)))
            .andExpect(jsonPath("$.[*].grade").value(hasItem(DEFAULT_GRADE.intValue())))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(sameInstant(DEFAULT_LAST_MODIFIED_DATE))));

        // Check, that the count call also returns 1
        restAssignmentGradeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAssignmentGradeShouldNotBeFound(String filter) throws Exception {
        restAssignmentGradeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAssignmentGradeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAssignmentGrade() throws Exception {
        // Get the assignmentGrade
        restAssignmentGradeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAssignmentGrade() throws Exception {
        // Initialize the database
        assignmentGradeRepository.saveAndFlush(assignmentGrade);

        int databaseSizeBeforeUpdate = assignmentGradeRepository.findAll().size();

        // Update the assignmentGrade
        AssignmentGrade updatedAssignmentGrade = assignmentGradeRepository.findById(assignmentGrade.getId()).get();
        // Disconnect from session so that the updates on updatedAssignmentGrade are not directly saved in db
        em.detach(updatedAssignmentGrade);
        updatedAssignmentGrade
            .studentId(UPDATED_STUDENT_ID)
            .grade(UPDATED_GRADE)
            .isDeleted(UPDATED_IS_DELETED)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restAssignmentGradeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAssignmentGrade.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAssignmentGrade))
            )
            .andExpect(status().isOk());

        // Validate the AssignmentGrade in the database
        List<AssignmentGrade> assignmentGradeList = assignmentGradeRepository.findAll();
        assertThat(assignmentGradeList).hasSize(databaseSizeBeforeUpdate);
        AssignmentGrade testAssignmentGrade = assignmentGradeList.get(assignmentGradeList.size() - 1);
        assertThat(testAssignmentGrade.getStudentId()).isEqualTo(UPDATED_STUDENT_ID);
        assertThat(testAssignmentGrade.getGrade()).isEqualTo(UPDATED_GRADE);
        assertThat(testAssignmentGrade.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testAssignmentGrade.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testAssignmentGrade.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testAssignmentGrade.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testAssignmentGrade.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingAssignmentGrade() throws Exception {
        int databaseSizeBeforeUpdate = assignmentGradeRepository.findAll().size();
        assignmentGrade.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssignmentGradeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assignmentGrade.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assignmentGrade))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssignmentGrade in the database
        List<AssignmentGrade> assignmentGradeList = assignmentGradeRepository.findAll();
        assertThat(assignmentGradeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAssignmentGrade() throws Exception {
        int databaseSizeBeforeUpdate = assignmentGradeRepository.findAll().size();
        assignmentGrade.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssignmentGradeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assignmentGrade))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssignmentGrade in the database
        List<AssignmentGrade> assignmentGradeList = assignmentGradeRepository.findAll();
        assertThat(assignmentGradeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAssignmentGrade() throws Exception {
        int databaseSizeBeforeUpdate = assignmentGradeRepository.findAll().size();
        assignmentGrade.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssignmentGradeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assignmentGrade))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AssignmentGrade in the database
        List<AssignmentGrade> assignmentGradeList = assignmentGradeRepository.findAll();
        assertThat(assignmentGradeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAssignmentGradeWithPatch() throws Exception {
        // Initialize the database
        assignmentGradeRepository.saveAndFlush(assignmentGrade);

        int databaseSizeBeforeUpdate = assignmentGradeRepository.findAll().size();

        // Update the assignmentGrade using partial update
        AssignmentGrade partialUpdatedAssignmentGrade = new AssignmentGrade();
        partialUpdatedAssignmentGrade.setId(assignmentGrade.getId());

        partialUpdatedAssignmentGrade.isDeleted(UPDATED_IS_DELETED).lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restAssignmentGradeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssignmentGrade.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssignmentGrade))
            )
            .andExpect(status().isOk());

        // Validate the AssignmentGrade in the database
        List<AssignmentGrade> assignmentGradeList = assignmentGradeRepository.findAll();
        assertThat(assignmentGradeList).hasSize(databaseSizeBeforeUpdate);
        AssignmentGrade testAssignmentGrade = assignmentGradeList.get(assignmentGradeList.size() - 1);
        assertThat(testAssignmentGrade.getStudentId()).isEqualTo(DEFAULT_STUDENT_ID);
        assertThat(testAssignmentGrade.getGrade()).isEqualTo(DEFAULT_GRADE);
        assertThat(testAssignmentGrade.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testAssignmentGrade.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testAssignmentGrade.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testAssignmentGrade.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testAssignmentGrade.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateAssignmentGradeWithPatch() throws Exception {
        // Initialize the database
        assignmentGradeRepository.saveAndFlush(assignmentGrade);

        int databaseSizeBeforeUpdate = assignmentGradeRepository.findAll().size();

        // Update the assignmentGrade using partial update
        AssignmentGrade partialUpdatedAssignmentGrade = new AssignmentGrade();
        partialUpdatedAssignmentGrade.setId(assignmentGrade.getId());

        partialUpdatedAssignmentGrade
            .studentId(UPDATED_STUDENT_ID)
            .grade(UPDATED_GRADE)
            .isDeleted(UPDATED_IS_DELETED)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restAssignmentGradeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssignmentGrade.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssignmentGrade))
            )
            .andExpect(status().isOk());

        // Validate the AssignmentGrade in the database
        List<AssignmentGrade> assignmentGradeList = assignmentGradeRepository.findAll();
        assertThat(assignmentGradeList).hasSize(databaseSizeBeforeUpdate);
        AssignmentGrade testAssignmentGrade = assignmentGradeList.get(assignmentGradeList.size() - 1);
        assertThat(testAssignmentGrade.getStudentId()).isEqualTo(UPDATED_STUDENT_ID);
        assertThat(testAssignmentGrade.getGrade()).isEqualTo(UPDATED_GRADE);
        assertThat(testAssignmentGrade.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testAssignmentGrade.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testAssignmentGrade.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testAssignmentGrade.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testAssignmentGrade.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingAssignmentGrade() throws Exception {
        int databaseSizeBeforeUpdate = assignmentGradeRepository.findAll().size();
        assignmentGrade.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssignmentGradeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, assignmentGrade.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assignmentGrade))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssignmentGrade in the database
        List<AssignmentGrade> assignmentGradeList = assignmentGradeRepository.findAll();
        assertThat(assignmentGradeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAssignmentGrade() throws Exception {
        int databaseSizeBeforeUpdate = assignmentGradeRepository.findAll().size();
        assignmentGrade.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssignmentGradeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assignmentGrade))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssignmentGrade in the database
        List<AssignmentGrade> assignmentGradeList = assignmentGradeRepository.findAll();
        assertThat(assignmentGradeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAssignmentGrade() throws Exception {
        int databaseSizeBeforeUpdate = assignmentGradeRepository.findAll().size();
        assignmentGrade.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssignmentGradeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assignmentGrade))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AssignmentGrade in the database
        List<AssignmentGrade> assignmentGradeList = assignmentGradeRepository.findAll();
        assertThat(assignmentGradeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAssignmentGrade() throws Exception {
        // Initialize the database
        assignmentGradeRepository.saveAndFlush(assignmentGrade);

        int databaseSizeBeforeDelete = assignmentGradeRepository.findAll().size();

        // Delete the assignmentGrade
        restAssignmentGradeMockMvc
            .perform(delete(ENTITY_API_URL_ID, assignmentGrade.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AssignmentGrade> assignmentGradeList = assignmentGradeRepository.findAll();
        assertThat(assignmentGradeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
