// package com.ptudw.web.web.rest;

// import static com.ptudw.web.web.rest.TestUtil.sameInstant;
// import static org.assertj.core.api.Assertions.assertThat;
// import static org.hamcrest.Matchers.hasItem;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// import com.ptudw.web.IntegrationTest;
// import com.ptudw.web.domain.GradeComposition;
// import com.ptudw.web.domain.GradeStructure;
// import com.ptudw.web.repository.GradeStructureRepository;
// import com.ptudw.web.service.criteria.GradeStructureCriteria;
// import java.time.Instant;
// import java.time.ZoneId;
// import java.time.ZoneOffset;
// import java.time.ZonedDateTime;
// import java.util.List;
// import java.util.Random;
// import java.util.concurrent.atomic.AtomicLong;
// import javax.persistence.EntityManager;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.http.MediaType;
// import org.springframework.security.test.context.support.WithMockUser;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.transaction.annotation.Transactional;

// /**
//  * Integration tests for the {@link GradeStructureResource} REST controller.
//  */
// @IntegrationTest
// @AutoConfigureMockMvc
// @WithMockUser
// class GradeStructureResourceIT {

//     private static final Long DEFAULT_COURSE_ID = 1L;
//     private static final Long UPDATED_COURSE_ID = 2L;
//     private static final Long SMALLER_COURSE_ID = 1L - 1L;

//     private static final Boolean DEFAULT_IS_DELETED = false;
//     private static final Boolean UPDATED_IS_DELETED = true;

//     private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
//     private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

//     private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
//     private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
//     private static final ZonedDateTime SMALLER_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

//     private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
//     private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

//     private static final ZonedDateTime DEFAULT_LAST_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
//     private static final ZonedDateTime UPDATED_LAST_MODIFIED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
//     private static final ZonedDateTime SMALLER_LAST_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

//     private static final String ENTITY_API_URL = "/api/grade-structures";
//     private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

//     private static Random random = new Random();
//     private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

//     @Autowired
//     private GradeStructureRepository gradeStructureRepository;

//     @Autowired
//     private EntityManager em;

//     @Autowired
//     private MockMvc restGradeStructureMockMvc;

//     private GradeStructure gradeStructure;

//     /**
//      * Create an entity for this test.
//      *
//      * This is a static method, as tests for other entities might also need it,
//      * if they test an entity which requires the current entity.
//      */
//     public static GradeStructure createEntity(EntityManager em) {
//         GradeStructure gradeStructure = new GradeStructure()
//             .courseId(DEFAULT_COURSE_ID)
//             .isDeleted(DEFAULT_IS_DELETED)
//             .createdBy(DEFAULT_CREATED_BY)
//             .createdDate(DEFAULT_CREATED_DATE)
//             .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
//             .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
//         return gradeStructure;
//     }

//     /**
//      * Create an updated entity for this test.
//      *
//      * This is a static method, as tests for other entities might also need it,
//      * if they test an entity which requires the current entity.
//      */
//     public static GradeStructure createUpdatedEntity(EntityManager em) {
//         GradeStructure gradeStructure = new GradeStructure()
//             .courseId(UPDATED_COURSE_ID)
//             .isDeleted(UPDATED_IS_DELETED)
//             .createdBy(UPDATED_CREATED_BY)
//             .createdDate(UPDATED_CREATED_DATE)
//             .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
//             .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
//         return gradeStructure;
//     }

//     @BeforeEach
//     public void initTest() {
//         gradeStructure = createEntity(em);
//     }

//     @Test
//     @Transactional
//     void createGradeStructure() throws Exception {
//         int databaseSizeBeforeCreate = gradeStructureRepository.findAll().size();
//         // Create the GradeStructure
//         restGradeStructureMockMvc
//             .perform(
//                 post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gradeStructure))
//             )
//             .andExpect(status().isCreated());

//         // Validate the GradeStructure in the database
//         List<GradeStructure> gradeStructureList = gradeStructureRepository.findAll();
//         assertThat(gradeStructureList).hasSize(databaseSizeBeforeCreate + 1);
//         GradeStructure testGradeStructure = gradeStructureList.get(gradeStructureList.size() - 1);
//         assertThat(testGradeStructure.getCourseId()).isEqualTo(DEFAULT_COURSE_ID);
//         assertThat(testGradeStructure.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
//         assertThat(testGradeStructure.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
//         assertThat(testGradeStructure.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
//         assertThat(testGradeStructure.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
//         assertThat(testGradeStructure.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
//     }

//     @Test
//     @Transactional
//     void createGradeStructureWithExistingId() throws Exception {
//         // Create the GradeStructure with an existing ID
//         gradeStructure.setId(1L);

//         int databaseSizeBeforeCreate = gradeStructureRepository.findAll().size();

//         // An entity with an existing ID cannot be created, so this API call must fail
//         restGradeStructureMockMvc
//             .perform(
//                 post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gradeStructure))
//             )
//             .andExpect(status().isBadRequest());

//         // Validate the GradeStructure in the database
//         List<GradeStructure> gradeStructureList = gradeStructureRepository.findAll();
//         assertThat(gradeStructureList).hasSize(databaseSizeBeforeCreate);
//     }

//     @Test
//     @Transactional
//     void checkCourseIdIsRequired() throws Exception {
//         int databaseSizeBeforeTest = gradeStructureRepository.findAll().size();
//         // set the field null
//         gradeStructure.setCourseId(null);

//         // Create the GradeStructure, which fails.

//         restGradeStructureMockMvc
//             .perform(
//                 post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gradeStructure))
//             )
//             .andExpect(status().isBadRequest());

//         List<GradeStructure> gradeStructureList = gradeStructureRepository.findAll();
//         assertThat(gradeStructureList).hasSize(databaseSizeBeforeTest);
//     }

//     @Test
//     @Transactional
//     void checkCreatedByIsRequired() throws Exception {
//         int databaseSizeBeforeTest = gradeStructureRepository.findAll().size();
//         // set the field null
//         gradeStructure.setCreatedBy(null);

//         // Create the GradeStructure, which fails.

//         restGradeStructureMockMvc
//             .perform(
//                 post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gradeStructure))
//             )
//             .andExpect(status().isBadRequest());

//         List<GradeStructure> gradeStructureList = gradeStructureRepository.findAll();
//         assertThat(gradeStructureList).hasSize(databaseSizeBeforeTest);
//     }

//     @Test
//     @Transactional
//     void checkCreatedDateIsRequired() throws Exception {
//         int databaseSizeBeforeTest = gradeStructureRepository.findAll().size();
//         // set the field null
//         gradeStructure.setCreatedDate(null);

//         // Create the GradeStructure, which fails.

//         restGradeStructureMockMvc
//             .perform(
//                 post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gradeStructure))
//             )
//             .andExpect(status().isBadRequest());

//         List<GradeStructure> gradeStructureList = gradeStructureRepository.findAll();
//         assertThat(gradeStructureList).hasSize(databaseSizeBeforeTest);
//     }

//     @Test
//     @Transactional
//     void checkLastModifiedByIsRequired() throws Exception {
//         int databaseSizeBeforeTest = gradeStructureRepository.findAll().size();
//         // set the field null
//         gradeStructure.setLastModifiedBy(null);

//         // Create the GradeStructure, which fails.

//         restGradeStructureMockMvc
//             .perform(
//                 post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gradeStructure))
//             )
//             .andExpect(status().isBadRequest());

//         List<GradeStructure> gradeStructureList = gradeStructureRepository.findAll();
//         assertThat(gradeStructureList).hasSize(databaseSizeBeforeTest);
//     }

//     @Test
//     @Transactional
//     void checkLastModifiedDateIsRequired() throws Exception {
//         int databaseSizeBeforeTest = gradeStructureRepository.findAll().size();
//         // set the field null
//         gradeStructure.setLastModifiedDate(null);

//         // Create the GradeStructure, which fails.

//         restGradeStructureMockMvc
//             .perform(
//                 post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gradeStructure))
//             )
//             .andExpect(status().isBadRequest());

//         List<GradeStructure> gradeStructureList = gradeStructureRepository.findAll();
//         assertThat(gradeStructureList).hasSize(databaseSizeBeforeTest);
//     }

//     @Test
//     @Transactional
//     void getAllGradeStructures() throws Exception {
//         // Initialize the database
//         gradeStructureRepository.saveAndFlush(gradeStructure);

//         // Get all the gradeStructureList
//         restGradeStructureMockMvc
//             .perform(get(ENTITY_API_URL + "?sort=id,desc"))
//             .andExpect(status().isOk())
//             .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//             .andExpect(jsonPath("$.[*].id").value(hasItem(gradeStructure.getId().intValue())))
//             .andExpect(jsonPath("$.[*].courseId").value(hasItem(DEFAULT_COURSE_ID.intValue())))
//             .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
//             .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
//             .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
//             .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
//             .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(sameInstant(DEFAULT_LAST_MODIFIED_DATE))));
//     }

//     @Test
//     @Transactional
//     void getGradeStructure() throws Exception {
//         // Initialize the database
//         gradeStructureRepository.saveAndFlush(gradeStructure);

//         // Get the gradeStructure
//         restGradeStructureMockMvc
//             .perform(get(ENTITY_API_URL_ID, gradeStructure.getId()))
//             .andExpect(status().isOk())
//             .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//             .andExpect(jsonPath("$.id").value(gradeStructure.getId().intValue()))
//             .andExpect(jsonPath("$.courseId").value(DEFAULT_COURSE_ID.intValue()))
//             .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()))
//             .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
//             .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
//             .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
//             .andExpect(jsonPath("$.lastModifiedDate").value(sameInstant(DEFAULT_LAST_MODIFIED_DATE)));
//     }

//     @Test
//     @Transactional
//     void getGradeStructuresByIdFiltering() throws Exception {
//         // Initialize the database
//         gradeStructureRepository.saveAndFlush(gradeStructure);

//         Long id = gradeStructure.getId();

//         defaultGradeStructureShouldBeFound("id.equals=" + id);
//         defaultGradeStructureShouldNotBeFound("id.notEquals=" + id);

//         defaultGradeStructureShouldBeFound("id.greaterThanOrEqual=" + id);
//         defaultGradeStructureShouldNotBeFound("id.greaterThan=" + id);

//         defaultGradeStructureShouldBeFound("id.lessThanOrEqual=" + id);
//         defaultGradeStructureShouldNotBeFound("id.lessThan=" + id);
//     }

//     @Test
//     @Transactional
//     void getAllGradeStructuresByCourseIdIsEqualToSomething() throws Exception {
//         // Initialize the database
//         gradeStructureRepository.saveAndFlush(gradeStructure);

//         // Get all the gradeStructureList where courseId equals to DEFAULT_COURSE_ID
//         defaultGradeStructureShouldBeFound("courseId.equals=" + DEFAULT_COURSE_ID);

//         // Get all the gradeStructureList where courseId equals to UPDATED_COURSE_ID
//         defaultGradeStructureShouldNotBeFound("courseId.equals=" + UPDATED_COURSE_ID);
//     }

//     @Test
//     @Transactional
//     void getAllGradeStructuresByCourseIdIsInShouldWork() throws Exception {
//         // Initialize the database
//         gradeStructureRepository.saveAndFlush(gradeStructure);

//         // Get all the gradeStructureList where courseId in DEFAULT_COURSE_ID or UPDATED_COURSE_ID
//         defaultGradeStructureShouldBeFound("courseId.in=" + DEFAULT_COURSE_ID + "," + UPDATED_COURSE_ID);

//         // Get all the gradeStructureList where courseId equals to UPDATED_COURSE_ID
//         defaultGradeStructureShouldNotBeFound("courseId.in=" + UPDATED_COURSE_ID);
//     }

//     @Test
//     @Transactional
//     void getAllGradeStructuresByCourseIdIsNullOrNotNull() throws Exception {
//         // Initialize the database
//         gradeStructureRepository.saveAndFlush(gradeStructure);

//         // Get all the gradeStructureList where courseId is not null
//         defaultGradeStructureShouldBeFound("courseId.specified=true");

//         // Get all the gradeStructureList where courseId is null
//         defaultGradeStructureShouldNotBeFound("courseId.specified=false");
//     }

//     @Test
//     @Transactional
//     void getAllGradeStructuresByCourseIdIsGreaterThanOrEqualToSomething() throws Exception {
//         // Initialize the database
//         gradeStructureRepository.saveAndFlush(gradeStructure);

//         // Get all the gradeStructureList where courseId is greater than or equal to DEFAULT_COURSE_ID
//         defaultGradeStructureShouldBeFound("courseId.greaterThanOrEqual=" + DEFAULT_COURSE_ID);

//         // Get all the gradeStructureList where courseId is greater than or equal to UPDATED_COURSE_ID
//         defaultGradeStructureShouldNotBeFound("courseId.greaterThanOrEqual=" + UPDATED_COURSE_ID);
//     }

//     @Test
//     @Transactional
//     void getAllGradeStructuresByCourseIdIsLessThanOrEqualToSomething() throws Exception {
//         // Initialize the database
//         gradeStructureRepository.saveAndFlush(gradeStructure);

//         // Get all the gradeStructureList where courseId is less than or equal to DEFAULT_COURSE_ID
//         defaultGradeStructureShouldBeFound("courseId.lessThanOrEqual=" + DEFAULT_COURSE_ID);

//         // Get all the gradeStructureList where courseId is less than or equal to SMALLER_COURSE_ID
//         defaultGradeStructureShouldNotBeFound("courseId.lessThanOrEqual=" + SMALLER_COURSE_ID);
//     }

//     @Test
//     @Transactional
//     void getAllGradeStructuresByCourseIdIsLessThanSomething() throws Exception {
//         // Initialize the database
//         gradeStructureRepository.saveAndFlush(gradeStructure);

//         // Get all the gradeStructureList where courseId is less than DEFAULT_COURSE_ID
//         defaultGradeStructureShouldNotBeFound("courseId.lessThan=" + DEFAULT_COURSE_ID);

//         // Get all the gradeStructureList where courseId is less than UPDATED_COURSE_ID
//         defaultGradeStructureShouldBeFound("courseId.lessThan=" + UPDATED_COURSE_ID);
//     }

//     @Test
//     @Transactional
//     void getAllGradeStructuresByCourseIdIsGreaterThanSomething() throws Exception {
//         // Initialize the database
//         gradeStructureRepository.saveAndFlush(gradeStructure);

//         // Get all the gradeStructureList where courseId is greater than DEFAULT_COURSE_ID
//         defaultGradeStructureShouldNotBeFound("courseId.greaterThan=" + DEFAULT_COURSE_ID);

//         // Get all the gradeStructureList where courseId is greater than SMALLER_COURSE_ID
//         defaultGradeStructureShouldBeFound("courseId.greaterThan=" + SMALLER_COURSE_ID);
//     }

//     @Test
//     @Transactional
//     void getAllGradeStructuresByIsDeletedIsEqualToSomething() throws Exception {
//         // Initialize the database
//         gradeStructureRepository.saveAndFlush(gradeStructure);

//         // Get all the gradeStructureList where isDeleted equals to DEFAULT_IS_DELETED
//         defaultGradeStructureShouldBeFound("isDeleted.equals=" + DEFAULT_IS_DELETED);

//         // Get all the gradeStructureList where isDeleted equals to UPDATED_IS_DELETED
//         defaultGradeStructureShouldNotBeFound("isDeleted.equals=" + UPDATED_IS_DELETED);
//     }

//     @Test
//     @Transactional
//     void getAllGradeStructuresByIsDeletedIsInShouldWork() throws Exception {
//         // Initialize the database
//         gradeStructureRepository.saveAndFlush(gradeStructure);

//         // Get all the gradeStructureList where isDeleted in DEFAULT_IS_DELETED or UPDATED_IS_DELETED
//         defaultGradeStructureShouldBeFound("isDeleted.in=" + DEFAULT_IS_DELETED + "," + UPDATED_IS_DELETED);

//         // Get all the gradeStructureList where isDeleted equals to UPDATED_IS_DELETED
//         defaultGradeStructureShouldNotBeFound("isDeleted.in=" + UPDATED_IS_DELETED);
//     }

//     @Test
//     @Transactional
//     void getAllGradeStructuresByIsDeletedIsNullOrNotNull() throws Exception {
//         // Initialize the database
//         gradeStructureRepository.saveAndFlush(gradeStructure);

//         // Get all the gradeStructureList where isDeleted is not null
//         defaultGradeStructureShouldBeFound("isDeleted.specified=true");

//         // Get all the gradeStructureList where isDeleted is null
//         defaultGradeStructureShouldNotBeFound("isDeleted.specified=false");
//     }

//     @Test
//     @Transactional
//     void getAllGradeStructuresByCreatedByIsEqualToSomething() throws Exception {
//         // Initialize the database
//         gradeStructureRepository.saveAndFlush(gradeStructure);

//         // Get all the gradeStructureList where createdBy equals to DEFAULT_CREATED_BY
//         defaultGradeStructureShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

//         // Get all the gradeStructureList where createdBy equals to UPDATED_CREATED_BY
//         defaultGradeStructureShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
//     }

//     @Test
//     @Transactional
//     void getAllGradeStructuresByCreatedByIsInShouldWork() throws Exception {
//         // Initialize the database
//         gradeStructureRepository.saveAndFlush(gradeStructure);

//         // Get all the gradeStructureList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
//         defaultGradeStructureShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

//         // Get all the gradeStructureList where createdBy equals to UPDATED_CREATED_BY
//         defaultGradeStructureShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
//     }

//     @Test
//     @Transactional
//     void getAllGradeStructuresByCreatedByIsNullOrNotNull() throws Exception {
//         // Initialize the database
//         gradeStructureRepository.saveAndFlush(gradeStructure);

//         // Get all the gradeStructureList where createdBy is not null
//         defaultGradeStructureShouldBeFound("createdBy.specified=true");

//         // Get all the gradeStructureList where createdBy is null
//         defaultGradeStructureShouldNotBeFound("createdBy.specified=false");
//     }

//     @Test
//     @Transactional
//     void getAllGradeStructuresByCreatedByContainsSomething() throws Exception {
//         // Initialize the database
//         gradeStructureRepository.saveAndFlush(gradeStructure);

//         // Get all the gradeStructureList where createdBy contains DEFAULT_CREATED_BY
//         defaultGradeStructureShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

//         // Get all the gradeStructureList where createdBy contains UPDATED_CREATED_BY
//         defaultGradeStructureShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
//     }

//     @Test
//     @Transactional
//     void getAllGradeStructuresByCreatedByNotContainsSomething() throws Exception {
//         // Initialize the database
//         gradeStructureRepository.saveAndFlush(gradeStructure);

//         // Get all the gradeStructureList where createdBy does not contain DEFAULT_CREATED_BY
//         defaultGradeStructureShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

//         // Get all the gradeStructureList where createdBy does not contain UPDATED_CREATED_BY
//         defaultGradeStructureShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
//     }

//     @Test
//     @Transactional
//     void getAllGradeStructuresByCreatedDateIsEqualToSomething() throws Exception {
//         // Initialize the database
//         gradeStructureRepository.saveAndFlush(gradeStructure);

//         // Get all the gradeStructureList where createdDate equals to DEFAULT_CREATED_DATE
//         defaultGradeStructureShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

//         // Get all the gradeStructureList where createdDate equals to UPDATED_CREATED_DATE
//         defaultGradeStructureShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
//     }

//     @Test
//     @Transactional
//     void getAllGradeStructuresByCreatedDateIsInShouldWork() throws Exception {
//         // Initialize the database
//         gradeStructureRepository.saveAndFlush(gradeStructure);

//         // Get all the gradeStructureList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
//         defaultGradeStructureShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

//         // Get all the gradeStructureList where createdDate equals to UPDATED_CREATED_DATE
//         defaultGradeStructureShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
//     }

//     @Test
//     @Transactional
//     void getAllGradeStructuresByCreatedDateIsNullOrNotNull() throws Exception {
//         // Initialize the database
//         gradeStructureRepository.saveAndFlush(gradeStructure);

//         // Get all the gradeStructureList where createdDate is not null
//         defaultGradeStructureShouldBeFound("createdDate.specified=true");

//         // Get all the gradeStructureList where createdDate is null
//         defaultGradeStructureShouldNotBeFound("createdDate.specified=false");
//     }

//     @Test
//     @Transactional
//     void getAllGradeStructuresByCreatedDateIsGreaterThanOrEqualToSomething() throws Exception {
//         // Initialize the database
//         gradeStructureRepository.saveAndFlush(gradeStructure);

//         // Get all the gradeStructureList where createdDate is greater than or equal to DEFAULT_CREATED_DATE
//         defaultGradeStructureShouldBeFound("createdDate.greaterThanOrEqual=" + DEFAULT_CREATED_DATE);

//         // Get all the gradeStructureList where createdDate is greater than or equal to UPDATED_CREATED_DATE
//         defaultGradeStructureShouldNotBeFound("createdDate.greaterThanOrEqual=" + UPDATED_CREATED_DATE);
//     }

//     @Test
//     @Transactional
//     void getAllGradeStructuresByCreatedDateIsLessThanOrEqualToSomething() throws Exception {
//         // Initialize the database
//         gradeStructureRepository.saveAndFlush(gradeStructure);

//         // Get all the gradeStructureList where createdDate is less than or equal to DEFAULT_CREATED_DATE
//         defaultGradeStructureShouldBeFound("createdDate.lessThanOrEqual=" + DEFAULT_CREATED_DATE);

//         // Get all the gradeStructureList where createdDate is less than or equal to SMALLER_CREATED_DATE
//         defaultGradeStructureShouldNotBeFound("createdDate.lessThanOrEqual=" + SMALLER_CREATED_DATE);
//     }

//     @Test
//     @Transactional
//     void getAllGradeStructuresByCreatedDateIsLessThanSomething() throws Exception {
//         // Initialize the database
//         gradeStructureRepository.saveAndFlush(gradeStructure);

//         // Get all the gradeStructureList where createdDate is less than DEFAULT_CREATED_DATE
//         defaultGradeStructureShouldNotBeFound("createdDate.lessThan=" + DEFAULT_CREATED_DATE);

//         // Get all the gradeStructureList where createdDate is less than UPDATED_CREATED_DATE
//         defaultGradeStructureShouldBeFound("createdDate.lessThan=" + UPDATED_CREATED_DATE);
//     }

//     @Test
//     @Transactional
//     void getAllGradeStructuresByCreatedDateIsGreaterThanSomething() throws Exception {
//         // Initialize the database
//         gradeStructureRepository.saveAndFlush(gradeStructure);

//         // Get all the gradeStructureList where createdDate is greater than DEFAULT_CREATED_DATE
//         defaultGradeStructureShouldNotBeFound("createdDate.greaterThan=" + DEFAULT_CREATED_DATE);

//         // Get all the gradeStructureList where createdDate is greater than SMALLER_CREATED_DATE
//         defaultGradeStructureShouldBeFound("createdDate.greaterThan=" + SMALLER_CREATED_DATE);
//     }

//     @Test
//     @Transactional
//     void getAllGradeStructuresByLastModifiedByIsEqualToSomething() throws Exception {
//         // Initialize the database
//         gradeStructureRepository.saveAndFlush(gradeStructure);

//         // Get all the gradeStructureList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
//         defaultGradeStructureShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

//         // Get all the gradeStructureList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
//         defaultGradeStructureShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
//     }

//     @Test
//     @Transactional
//     void getAllGradeStructuresByLastModifiedByIsInShouldWork() throws Exception {
//         // Initialize the database
//         gradeStructureRepository.saveAndFlush(gradeStructure);

//         // Get all the gradeStructureList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
//         defaultGradeStructureShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

//         // Get all the gradeStructureList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
//         defaultGradeStructureShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
//     }

//     @Test
//     @Transactional
//     void getAllGradeStructuresByLastModifiedByIsNullOrNotNull() throws Exception {
//         // Initialize the database
//         gradeStructureRepository.saveAndFlush(gradeStructure);

//         // Get all the gradeStructureList where lastModifiedBy is not null
//         defaultGradeStructureShouldBeFound("lastModifiedBy.specified=true");

//         // Get all the gradeStructureList where lastModifiedBy is null
//         defaultGradeStructureShouldNotBeFound("lastModifiedBy.specified=false");
//     }

//     @Test
//     @Transactional
//     void getAllGradeStructuresByLastModifiedByContainsSomething() throws Exception {
//         // Initialize the database
//         gradeStructureRepository.saveAndFlush(gradeStructure);

//         // Get all the gradeStructureList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
//         defaultGradeStructureShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

//         // Get all the gradeStructureList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
//         defaultGradeStructureShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
//     }

//     @Test
//     @Transactional
//     void getAllGradeStructuresByLastModifiedByNotContainsSomething() throws Exception {
//         // Initialize the database
//         gradeStructureRepository.saveAndFlush(gradeStructure);

//         // Get all the gradeStructureList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
//         defaultGradeStructureShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

//         // Get all the gradeStructureList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
//         defaultGradeStructureShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
//     }

//     @Test
//     @Transactional
//     void getAllGradeStructuresByLastModifiedDateIsEqualToSomething() throws Exception {
//         // Initialize the database
//         gradeStructureRepository.saveAndFlush(gradeStructure);

//         // Get all the gradeStructureList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
//         defaultGradeStructureShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

//         // Get all the gradeStructureList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
//         defaultGradeStructureShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
//     }

//     @Test
//     @Transactional
//     void getAllGradeStructuresByLastModifiedDateIsInShouldWork() throws Exception {
//         // Initialize the database
//         gradeStructureRepository.saveAndFlush(gradeStructure);

//         // Get all the gradeStructureList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
//         defaultGradeStructureShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

//         // Get all the gradeStructureList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
//         defaultGradeStructureShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
//     }

//     @Test
//     @Transactional
//     void getAllGradeStructuresByLastModifiedDateIsNullOrNotNull() throws Exception {
//         // Initialize the database
//         gradeStructureRepository.saveAndFlush(gradeStructure);

//         // Get all the gradeStructureList where lastModifiedDate is not null
//         defaultGradeStructureShouldBeFound("lastModifiedDate.specified=true");

//         // Get all the gradeStructureList where lastModifiedDate is null
//         defaultGradeStructureShouldNotBeFound("lastModifiedDate.specified=false");
//     }

//     @Test
//     @Transactional
//     void getAllGradeStructuresByLastModifiedDateIsGreaterThanOrEqualToSomething() throws Exception {
//         // Initialize the database
//         gradeStructureRepository.saveAndFlush(gradeStructure);

//         // Get all the gradeStructureList where lastModifiedDate is greater than or equal to DEFAULT_LAST_MODIFIED_DATE
//         defaultGradeStructureShouldBeFound("lastModifiedDate.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED_DATE);

//         // Get all the gradeStructureList where lastModifiedDate is greater than or equal to UPDATED_LAST_MODIFIED_DATE
//         defaultGradeStructureShouldNotBeFound("lastModifiedDate.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED_DATE);
//     }

//     @Test
//     @Transactional
//     void getAllGradeStructuresByLastModifiedDateIsLessThanOrEqualToSomething() throws Exception {
//         // Initialize the database
//         gradeStructureRepository.saveAndFlush(gradeStructure);

//         // Get all the gradeStructureList where lastModifiedDate is less than or equal to DEFAULT_LAST_MODIFIED_DATE
//         defaultGradeStructureShouldBeFound("lastModifiedDate.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED_DATE);

//         // Get all the gradeStructureList where lastModifiedDate is less than or equal to SMALLER_LAST_MODIFIED_DATE
//         defaultGradeStructureShouldNotBeFound("lastModifiedDate.lessThanOrEqual=" + SMALLER_LAST_MODIFIED_DATE);
//     }

//     @Test
//     @Transactional
//     void getAllGradeStructuresByLastModifiedDateIsLessThanSomething() throws Exception {
//         // Initialize the database
//         gradeStructureRepository.saveAndFlush(gradeStructure);

//         // Get all the gradeStructureList where lastModifiedDate is less than DEFAULT_LAST_MODIFIED_DATE
//         defaultGradeStructureShouldNotBeFound("lastModifiedDate.lessThan=" + DEFAULT_LAST_MODIFIED_DATE);

//         // Get all the gradeStructureList where lastModifiedDate is less than UPDATED_LAST_MODIFIED_DATE
//         defaultGradeStructureShouldBeFound("lastModifiedDate.lessThan=" + UPDATED_LAST_MODIFIED_DATE);
//     }

//     @Test
//     @Transactional
//     void getAllGradeStructuresByLastModifiedDateIsGreaterThanSomething() throws Exception {
//         // Initialize the database
//         gradeStructureRepository.saveAndFlush(gradeStructure);

//         // Get all the gradeStructureList where lastModifiedDate is greater than DEFAULT_LAST_MODIFIED_DATE
//         defaultGradeStructureShouldNotBeFound("lastModifiedDate.greaterThan=" + DEFAULT_LAST_MODIFIED_DATE);

//         // Get all the gradeStructureList where lastModifiedDate is greater than SMALLER_LAST_MODIFIED_DATE
//         defaultGradeStructureShouldBeFound("lastModifiedDate.greaterThan=" + SMALLER_LAST_MODIFIED_DATE);
//     }

//     @Test
//     @Transactional
//     void getAllGradeStructuresByGradeCompositionsIsEqualToSomething() throws Exception {
//         GradeComposition gradeCompositions;
//         if (TestUtil.findAll(em, GradeComposition.class).isEmpty()) {
//             gradeStructureRepository.saveAndFlush(gradeStructure);
//             gradeCompositions = GradeCompositionResourceIT.createEntity(em);
//         } else {
//             gradeCompositions = TestUtil.findAll(em, GradeComposition.class).get(0);
//         }
//         em.persist(gradeCompositions);
//         em.flush();
//         gradeStructure.setGradeCompositions(gradeCompositions);
//         gradeStructureRepository.saveAndFlush(gradeStructure);
//         Long gradeCompositionsId = gradeCompositions.getId();

//         // Get all the gradeStructureList where gradeCompositions equals to gradeCompositionsId
//         defaultGradeStructureShouldBeFound("gradeCompositionsId.equals=" + gradeCompositionsId);

//         // Get all the gradeStructureList where gradeCompositions equals to (gradeCompositionsId + 1)
//         defaultGradeStructureShouldNotBeFound("gradeCompositionsId.equals=" + (gradeCompositionsId + 1));
//     }

//     /**
//      * Executes the search, and checks that the default entity is returned.
//      */
//     private void defaultGradeStructureShouldBeFound(String filter) throws Exception {
//         restGradeStructureMockMvc
//             .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
//             .andExpect(status().isOk())
//             .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//             .andExpect(jsonPath("$.[*].id").value(hasItem(gradeStructure.getId().intValue())))
//             .andExpect(jsonPath("$.[*].courseId").value(hasItem(DEFAULT_COURSE_ID.intValue())))
//             .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
//             .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
//             .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
//             .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
//             .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(sameInstant(DEFAULT_LAST_MODIFIED_DATE))));

//         // Check, that the count call also returns 1
//         restGradeStructureMockMvc
//             .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
//             .andExpect(status().isOk())
//             .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//             .andExpect(content().string("1"));
//     }

//     /**
//      * Executes the search, and checks that the default entity is not returned.
//      */
//     private void defaultGradeStructureShouldNotBeFound(String filter) throws Exception {
//         restGradeStructureMockMvc
//             .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
//             .andExpect(status().isOk())
//             .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//             .andExpect(jsonPath("$").isArray())
//             .andExpect(jsonPath("$").isEmpty());

//         // Check, that the count call also returns 0
//         restGradeStructureMockMvc
//             .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
//             .andExpect(status().isOk())
//             .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//             .andExpect(content().string("0"));
//     }

//     @Test
//     @Transactional
//     void getNonExistingGradeStructure() throws Exception {
//         // Get the gradeStructure
//         restGradeStructureMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
//     }

//     @Test
//     @Transactional
//     void putExistingGradeStructure() throws Exception {
//         // Initialize the database
//         gradeStructureRepository.saveAndFlush(gradeStructure);

//         int databaseSizeBeforeUpdate = gradeStructureRepository.findAll().size();

//         // Update the gradeStructure
//         GradeStructure updatedGradeStructure = gradeStructureRepository.findById(gradeStructure.getId()).get();
//         // Disconnect from session so that the updates on updatedGradeStructure are not directly saved in db
//         em.detach(updatedGradeStructure);
//         updatedGradeStructure
//             .courseId(UPDATED_COURSE_ID)
//             .isDeleted(UPDATED_IS_DELETED)
//             .createdBy(UPDATED_CREATED_BY)
//             .createdDate(UPDATED_CREATED_DATE)
//             .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
//             .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

//         restGradeStructureMockMvc
//             .perform(
//                 put(ENTITY_API_URL_ID, updatedGradeStructure.getId())
//                     .contentType(MediaType.APPLICATION_JSON)
//                     .content(TestUtil.convertObjectToJsonBytes(updatedGradeStructure))
//             )
//             .andExpect(status().isOk());

//         // Validate the GradeStructure in the database
//         List<GradeStructure> gradeStructureList = gradeStructureRepository.findAll();
//         assertThat(gradeStructureList).hasSize(databaseSizeBeforeUpdate);
//         GradeStructure testGradeStructure = gradeStructureList.get(gradeStructureList.size() - 1);
//         assertThat(testGradeStructure.getCourseId()).isEqualTo(UPDATED_COURSE_ID);
//         assertThat(testGradeStructure.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
//         assertThat(testGradeStructure.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
//         assertThat(testGradeStructure.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
//         assertThat(testGradeStructure.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
//         assertThat(testGradeStructure.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
//     }

//     @Test
//     @Transactional
//     void putNonExistingGradeStructure() throws Exception {
//         int databaseSizeBeforeUpdate = gradeStructureRepository.findAll().size();
//         gradeStructure.setId(count.incrementAndGet());

//         // If the entity doesn't have an ID, it will throw BadRequestAlertException
//         restGradeStructureMockMvc
//             .perform(
//                 put(ENTITY_API_URL_ID, gradeStructure.getId())
//                     .contentType(MediaType.APPLICATION_JSON)
//                     .content(TestUtil.convertObjectToJsonBytes(gradeStructure))
//             )
//             .andExpect(status().isBadRequest());

//         // Validate the GradeStructure in the database
//         List<GradeStructure> gradeStructureList = gradeStructureRepository.findAll();
//         assertThat(gradeStructureList).hasSize(databaseSizeBeforeUpdate);
//     }

//     @Test
//     @Transactional
//     void putWithIdMismatchGradeStructure() throws Exception {
//         int databaseSizeBeforeUpdate = gradeStructureRepository.findAll().size();
//         gradeStructure.setId(count.incrementAndGet());

//         // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//         restGradeStructureMockMvc
//             .perform(
//                 put(ENTITY_API_URL_ID, count.incrementAndGet())
//                     .contentType(MediaType.APPLICATION_JSON)
//                     .content(TestUtil.convertObjectToJsonBytes(gradeStructure))
//             )
//             .andExpect(status().isBadRequest());

//         // Validate the GradeStructure in the database
//         List<GradeStructure> gradeStructureList = gradeStructureRepository.findAll();
//         assertThat(gradeStructureList).hasSize(databaseSizeBeforeUpdate);
//     }

//     @Test
//     @Transactional
//     void putWithMissingIdPathParamGradeStructure() throws Exception {
//         int databaseSizeBeforeUpdate = gradeStructureRepository.findAll().size();
//         gradeStructure.setId(count.incrementAndGet());

//         // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//         restGradeStructureMockMvc
//             .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gradeStructure)))
//             .andExpect(status().isMethodNotAllowed());

//         // Validate the GradeStructure in the database
//         List<GradeStructure> gradeStructureList = gradeStructureRepository.findAll();
//         assertThat(gradeStructureList).hasSize(databaseSizeBeforeUpdate);
//     }

//     @Test
//     @Transactional
//     void partialUpdateGradeStructureWithPatch() throws Exception {
//         // Initialize the database
//         gradeStructureRepository.saveAndFlush(gradeStructure);

//         int databaseSizeBeforeUpdate = gradeStructureRepository.findAll().size();

//         // Update the gradeStructure using partial update
//         GradeStructure partialUpdatedGradeStructure = new GradeStructure();
//         partialUpdatedGradeStructure.setId(gradeStructure.getId());

//         partialUpdatedGradeStructure.courseId(UPDATED_COURSE_ID);

//         restGradeStructureMockMvc
//             .perform(
//                 patch(ENTITY_API_URL_ID, partialUpdatedGradeStructure.getId())
//                     .contentType("application/merge-patch+json")
//                     .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGradeStructure))
//             )
//             .andExpect(status().isOk());

//         // Validate the GradeStructure in the database
//         List<GradeStructure> gradeStructureList = gradeStructureRepository.findAll();
//         assertThat(gradeStructureList).hasSize(databaseSizeBeforeUpdate);
//         GradeStructure testGradeStructure = gradeStructureList.get(gradeStructureList.size() - 1);
//         assertThat(testGradeStructure.getCourseId()).isEqualTo(UPDATED_COURSE_ID);
//         assertThat(testGradeStructure.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
//         assertThat(testGradeStructure.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
//         assertThat(testGradeStructure.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
//         assertThat(testGradeStructure.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
//         assertThat(testGradeStructure.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
//     }

//     @Test
//     @Transactional
//     void fullUpdateGradeStructureWithPatch() throws Exception {
//         // Initialize the database
//         gradeStructureRepository.saveAndFlush(gradeStructure);

//         int databaseSizeBeforeUpdate = gradeStructureRepository.findAll().size();

//         // Update the gradeStructure using partial update
//         GradeStructure partialUpdatedGradeStructure = new GradeStructure();
//         partialUpdatedGradeStructure.setId(gradeStructure.getId());

//         partialUpdatedGradeStructure
//             .courseId(UPDATED_COURSE_ID)
//             .isDeleted(UPDATED_IS_DELETED)
//             .createdBy(UPDATED_CREATED_BY)
//             .createdDate(UPDATED_CREATED_DATE)
//             .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
//             .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

//         restGradeStructureMockMvc
//             .perform(
//                 patch(ENTITY_API_URL_ID, partialUpdatedGradeStructure.getId())
//                     .contentType("application/merge-patch+json")
//                     .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGradeStructure))
//             )
//             .andExpect(status().isOk());

//         // Validate the GradeStructure in the database
//         List<GradeStructure> gradeStructureList = gradeStructureRepository.findAll();
//         assertThat(gradeStructureList).hasSize(databaseSizeBeforeUpdate);
//         GradeStructure testGradeStructure = gradeStructureList.get(gradeStructureList.size() - 1);
//         assertThat(testGradeStructure.getCourseId()).isEqualTo(UPDATED_COURSE_ID);
//         assertThat(testGradeStructure.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
//         assertThat(testGradeStructure.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
//         assertThat(testGradeStructure.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
//         assertThat(testGradeStructure.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
//         assertThat(testGradeStructure.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
//     }

//     @Test
//     @Transactional
//     void patchNonExistingGradeStructure() throws Exception {
//         int databaseSizeBeforeUpdate = gradeStructureRepository.findAll().size();
//         gradeStructure.setId(count.incrementAndGet());

//         // If the entity doesn't have an ID, it will throw BadRequestAlertException
//         restGradeStructureMockMvc
//             .perform(
//                 patch(ENTITY_API_URL_ID, gradeStructure.getId())
//                     .contentType("application/merge-patch+json")
//                     .content(TestUtil.convertObjectToJsonBytes(gradeStructure))
//             )
//             .andExpect(status().isBadRequest());

//         // Validate the GradeStructure in the database
//         List<GradeStructure> gradeStructureList = gradeStructureRepository.findAll();
//         assertThat(gradeStructureList).hasSize(databaseSizeBeforeUpdate);
//     }

//     @Test
//     @Transactional
//     void patchWithIdMismatchGradeStructure() throws Exception {
//         int databaseSizeBeforeUpdate = gradeStructureRepository.findAll().size();
//         gradeStructure.setId(count.incrementAndGet());

//         // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//         restGradeStructureMockMvc
//             .perform(
//                 patch(ENTITY_API_URL_ID, count.incrementAndGet())
//                     .contentType("application/merge-patch+json")
//                     .content(TestUtil.convertObjectToJsonBytes(gradeStructure))
//             )
//             .andExpect(status().isBadRequest());

//         // Validate the GradeStructure in the database
//         List<GradeStructure> gradeStructureList = gradeStructureRepository.findAll();
//         assertThat(gradeStructureList).hasSize(databaseSizeBeforeUpdate);
//     }

//     @Test
//     @Transactional
//     void patchWithMissingIdPathParamGradeStructure() throws Exception {
//         int databaseSizeBeforeUpdate = gradeStructureRepository.findAll().size();
//         gradeStructure.setId(count.incrementAndGet());

//         // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//         restGradeStructureMockMvc
//             .perform(
//                 patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(gradeStructure))
//             )
//             .andExpect(status().isMethodNotAllowed());

//         // Validate the GradeStructure in the database
//         List<GradeStructure> gradeStructureList = gradeStructureRepository.findAll();
//         assertThat(gradeStructureList).hasSize(databaseSizeBeforeUpdate);
//     }

//     @Test
//     @Transactional
//     void deleteGradeStructure() throws Exception {
//         // Initialize the database
//         gradeStructureRepository.saveAndFlush(gradeStructure);

//         int databaseSizeBeforeDelete = gradeStructureRepository.findAll().size();

//         // Delete the gradeStructure
//         restGradeStructureMockMvc
//             .perform(delete(ENTITY_API_URL_ID, gradeStructure.getId()).accept(MediaType.APPLICATION_JSON))
//             .andExpect(status().isNoContent());

//         // Validate the database contains one less item
//         List<GradeStructure> gradeStructureList = gradeStructureRepository.findAll();
//         assertThat(gradeStructureList).hasSize(databaseSizeBeforeDelete - 1);
//     }
// }
