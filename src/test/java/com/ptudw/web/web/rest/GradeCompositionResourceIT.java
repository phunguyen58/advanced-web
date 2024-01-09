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
import com.ptudw.web.domain.enumeration.GradeType;
import com.ptudw.web.repository.GradeCompositionRepository;
import com.ptudw.web.service.criteria.GradeCompositionCriteria;
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
 * Integration tests for the {@link GradeCompositionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GradeCompositionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_SCALE = 1L;
    private static final Long UPDATED_SCALE = 2L;
    private static final Long SMALLER_SCALE = 1L - 1L;

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

    private static final GradeType DEFAULT_TYPE = GradeType.PERCENTAGE;
    private static final GradeType UPDATED_TYPE = GradeType.POINT;

    private static final Boolean DEFAULT_IS_PUBLIC = false;
    private static final Boolean UPDATED_IS_PUBLIC = true;

    private static final String ENTITY_API_URL = "/api/grade-compositions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GradeCompositionRepository gradeCompositionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGradeCompositionMockMvc;

    private GradeComposition gradeComposition;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GradeComposition createEntity(EntityManager em) {
        GradeComposition gradeComposition = new GradeComposition()
            .name(DEFAULT_NAME)
            .scale(DEFAULT_SCALE)
            .isDeleted(DEFAULT_IS_DELETED)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .type(DEFAULT_TYPE)
            .isPublic(DEFAULT_IS_PUBLIC);
        return gradeComposition;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GradeComposition createUpdatedEntity(EntityManager em) {
        GradeComposition gradeComposition = new GradeComposition()
            .name(UPDATED_NAME)
            .scale(UPDATED_SCALE)
            .isDeleted(UPDATED_IS_DELETED)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .type(UPDATED_TYPE)
            .isPublic(UPDATED_IS_PUBLIC);
        return gradeComposition;
    }

    @BeforeEach
    public void initTest() {
        gradeComposition = createEntity(em);
    }

    @Test
    @Transactional
    void createGradeComposition() throws Exception {
        int databaseSizeBeforeCreate = gradeCompositionRepository.findAll().size();
        // Create the GradeComposition
        restGradeCompositionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gradeComposition))
            )
            .andExpect(status().isCreated());

        // Validate the GradeComposition in the database
        List<GradeComposition> gradeCompositionList = gradeCompositionRepository.findAll();
        assertThat(gradeCompositionList).hasSize(databaseSizeBeforeCreate + 1);
        GradeComposition testGradeComposition = gradeCompositionList.get(gradeCompositionList.size() - 1);
        assertThat(testGradeComposition.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testGradeComposition.getScale()).isEqualTo(DEFAULT_SCALE);
        assertThat(testGradeComposition.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testGradeComposition.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testGradeComposition.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testGradeComposition.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testGradeComposition.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testGradeComposition.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testGradeComposition.getIsPublic()).isEqualTo(DEFAULT_IS_PUBLIC);
    }

    @Test
    @Transactional
    void createGradeCompositionWithExistingId() throws Exception {
        // Create the GradeComposition with an existing ID
        gradeComposition.setId(1L);

        int databaseSizeBeforeCreate = gradeCompositionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGradeCompositionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gradeComposition))
            )
            .andExpect(status().isBadRequest());

        // Validate the GradeComposition in the database
        List<GradeComposition> gradeCompositionList = gradeCompositionRepository.findAll();
        assertThat(gradeCompositionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = gradeCompositionRepository.findAll().size();
        // set the field null
        gradeComposition.setName(null);

        // Create the GradeComposition, which fails.

        restGradeCompositionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gradeComposition))
            )
            .andExpect(status().isBadRequest());

        List<GradeComposition> gradeCompositionList = gradeCompositionRepository.findAll();
        assertThat(gradeCompositionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = gradeCompositionRepository.findAll().size();
        // set the field null
        gradeComposition.setCreatedBy(null);

        // Create the GradeComposition, which fails.

        restGradeCompositionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gradeComposition))
            )
            .andExpect(status().isBadRequest());

        List<GradeComposition> gradeCompositionList = gradeCompositionRepository.findAll();
        assertThat(gradeCompositionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = gradeCompositionRepository.findAll().size();
        // set the field null
        gradeComposition.setCreatedDate(null);

        // Create the GradeComposition, which fails.

        restGradeCompositionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gradeComposition))
            )
            .andExpect(status().isBadRequest());

        List<GradeComposition> gradeCompositionList = gradeCompositionRepository.findAll();
        assertThat(gradeCompositionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastModifiedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = gradeCompositionRepository.findAll().size();
        // set the field null
        gradeComposition.setLastModifiedBy(null);

        // Create the GradeComposition, which fails.

        restGradeCompositionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gradeComposition))
            )
            .andExpect(status().isBadRequest());

        List<GradeComposition> gradeCompositionList = gradeCompositionRepository.findAll();
        assertThat(gradeCompositionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastModifiedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = gradeCompositionRepository.findAll().size();
        // set the field null
        gradeComposition.setLastModifiedDate(null);

        // Create the GradeComposition, which fails.

        restGradeCompositionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gradeComposition))
            )
            .andExpect(status().isBadRequest());

        List<GradeComposition> gradeCompositionList = gradeCompositionRepository.findAll();
        assertThat(gradeCompositionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = gradeCompositionRepository.findAll().size();
        // set the field null
        gradeComposition.setType(null);

        // Create the GradeComposition, which fails.

        restGradeCompositionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gradeComposition))
            )
            .andExpect(status().isBadRequest());

        List<GradeComposition> gradeCompositionList = gradeCompositionRepository.findAll();
        assertThat(gradeCompositionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGradeCompositions() throws Exception {
        // Initialize the database
        gradeCompositionRepository.saveAndFlush(gradeComposition);

        // Get all the gradeCompositionList
        restGradeCompositionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gradeComposition.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].scale").value(hasItem(DEFAULT_SCALE.intValue())))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(sameInstant(DEFAULT_LAST_MODIFIED_DATE))))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].isPublic").value(hasItem(DEFAULT_IS_PUBLIC.booleanValue())));
    }

    @Test
    @Transactional
    void getGradeComposition() throws Exception {
        // Initialize the database
        gradeCompositionRepository.saveAndFlush(gradeComposition);

        // Get the gradeComposition
        restGradeCompositionMockMvc
            .perform(get(ENTITY_API_URL_ID, gradeComposition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(gradeComposition.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.scale").value(DEFAULT_SCALE.intValue()))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(sameInstant(DEFAULT_LAST_MODIFIED_DATE)))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.isPublic").value(DEFAULT_IS_PUBLIC.booleanValue()));
    }

    @Test
    @Transactional
    void getGradeCompositionsByIdFiltering() throws Exception {
        // Initialize the database
        gradeCompositionRepository.saveAndFlush(gradeComposition);

        Long id = gradeComposition.getId();

        defaultGradeCompositionShouldBeFound("id.equals=" + id);
        defaultGradeCompositionShouldNotBeFound("id.notEquals=" + id);

        defaultGradeCompositionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultGradeCompositionShouldNotBeFound("id.greaterThan=" + id);

        defaultGradeCompositionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultGradeCompositionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllGradeCompositionsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        gradeCompositionRepository.saveAndFlush(gradeComposition);

        // Get all the gradeCompositionList where name equals to DEFAULT_NAME
        defaultGradeCompositionShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the gradeCompositionList where name equals to UPDATED_NAME
        defaultGradeCompositionShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGradeCompositionsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        gradeCompositionRepository.saveAndFlush(gradeComposition);

        // Get all the gradeCompositionList where name in DEFAULT_NAME or UPDATED_NAME
        defaultGradeCompositionShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the gradeCompositionList where name equals to UPDATED_NAME
        defaultGradeCompositionShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGradeCompositionsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        gradeCompositionRepository.saveAndFlush(gradeComposition);

        // Get all the gradeCompositionList where name is not null
        defaultGradeCompositionShouldBeFound("name.specified=true");

        // Get all the gradeCompositionList where name is null
        defaultGradeCompositionShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllGradeCompositionsByNameContainsSomething() throws Exception {
        // Initialize the database
        gradeCompositionRepository.saveAndFlush(gradeComposition);

        // Get all the gradeCompositionList where name contains DEFAULT_NAME
        defaultGradeCompositionShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the gradeCompositionList where name contains UPDATED_NAME
        defaultGradeCompositionShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGradeCompositionsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        gradeCompositionRepository.saveAndFlush(gradeComposition);

        // Get all the gradeCompositionList where name does not contain DEFAULT_NAME
        defaultGradeCompositionShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the gradeCompositionList where name does not contain UPDATED_NAME
        defaultGradeCompositionShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGradeCompositionsByScaleIsEqualToSomething() throws Exception {
        // Initialize the database
        gradeCompositionRepository.saveAndFlush(gradeComposition);

        // Get all the gradeCompositionList where scale equals to DEFAULT_SCALE
        defaultGradeCompositionShouldBeFound("scale.equals=" + DEFAULT_SCALE);

        // Get all the gradeCompositionList where scale equals to UPDATED_SCALE
        defaultGradeCompositionShouldNotBeFound("scale.equals=" + UPDATED_SCALE);
    }

    @Test
    @Transactional
    void getAllGradeCompositionsByScaleIsInShouldWork() throws Exception {
        // Initialize the database
        gradeCompositionRepository.saveAndFlush(gradeComposition);

        // Get all the gradeCompositionList where scale in DEFAULT_SCALE or UPDATED_SCALE
        defaultGradeCompositionShouldBeFound("scale.in=" + DEFAULT_SCALE + "," + UPDATED_SCALE);

        // Get all the gradeCompositionList where scale equals to UPDATED_SCALE
        defaultGradeCompositionShouldNotBeFound("scale.in=" + UPDATED_SCALE);
    }

    @Test
    @Transactional
    void getAllGradeCompositionsByScaleIsNullOrNotNull() throws Exception {
        // Initialize the database
        gradeCompositionRepository.saveAndFlush(gradeComposition);

        // Get all the gradeCompositionList where scale is not null
        defaultGradeCompositionShouldBeFound("scale.specified=true");

        // Get all the gradeCompositionList where scale is null
        defaultGradeCompositionShouldNotBeFound("scale.specified=false");
    }

    @Test
    @Transactional
    void getAllGradeCompositionsByScaleIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gradeCompositionRepository.saveAndFlush(gradeComposition);

        // Get all the gradeCompositionList where scale is greater than or equal to DEFAULT_SCALE
        defaultGradeCompositionShouldBeFound("scale.greaterThanOrEqual=" + DEFAULT_SCALE);

        // Get all the gradeCompositionList where scale is greater than or equal to UPDATED_SCALE
        defaultGradeCompositionShouldNotBeFound("scale.greaterThanOrEqual=" + UPDATED_SCALE);
    }

    @Test
    @Transactional
    void getAllGradeCompositionsByScaleIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gradeCompositionRepository.saveAndFlush(gradeComposition);

        // Get all the gradeCompositionList where scale is less than or equal to DEFAULT_SCALE
        defaultGradeCompositionShouldBeFound("scale.lessThanOrEqual=" + DEFAULT_SCALE);

        // Get all the gradeCompositionList where scale is less than or equal to SMALLER_SCALE
        defaultGradeCompositionShouldNotBeFound("scale.lessThanOrEqual=" + SMALLER_SCALE);
    }

    @Test
    @Transactional
    void getAllGradeCompositionsByScaleIsLessThanSomething() throws Exception {
        // Initialize the database
        gradeCompositionRepository.saveAndFlush(gradeComposition);

        // Get all the gradeCompositionList where scale is less than DEFAULT_SCALE
        defaultGradeCompositionShouldNotBeFound("scale.lessThan=" + DEFAULT_SCALE);

        // Get all the gradeCompositionList where scale is less than UPDATED_SCALE
        defaultGradeCompositionShouldBeFound("scale.lessThan=" + UPDATED_SCALE);
    }

    @Test
    @Transactional
    void getAllGradeCompositionsByScaleIsGreaterThanSomething() throws Exception {
        // Initialize the database
        gradeCompositionRepository.saveAndFlush(gradeComposition);

        // Get all the gradeCompositionList where scale is greater than DEFAULT_SCALE
        defaultGradeCompositionShouldNotBeFound("scale.greaterThan=" + DEFAULT_SCALE);

        // Get all the gradeCompositionList where scale is greater than SMALLER_SCALE
        defaultGradeCompositionShouldBeFound("scale.greaterThan=" + SMALLER_SCALE);
    }

    @Test
    @Transactional
    void getAllGradeCompositionsByIsDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        gradeCompositionRepository.saveAndFlush(gradeComposition);

        // Get all the gradeCompositionList where isDeleted equals to DEFAULT_IS_DELETED
        defaultGradeCompositionShouldBeFound("isDeleted.equals=" + DEFAULT_IS_DELETED);

        // Get all the gradeCompositionList where isDeleted equals to UPDATED_IS_DELETED
        defaultGradeCompositionShouldNotBeFound("isDeleted.equals=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllGradeCompositionsByIsDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        gradeCompositionRepository.saveAndFlush(gradeComposition);

        // Get all the gradeCompositionList where isDeleted in DEFAULT_IS_DELETED or UPDATED_IS_DELETED
        defaultGradeCompositionShouldBeFound("isDeleted.in=" + DEFAULT_IS_DELETED + "," + UPDATED_IS_DELETED);

        // Get all the gradeCompositionList where isDeleted equals to UPDATED_IS_DELETED
        defaultGradeCompositionShouldNotBeFound("isDeleted.in=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllGradeCompositionsByIsDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        gradeCompositionRepository.saveAndFlush(gradeComposition);

        // Get all the gradeCompositionList where isDeleted is not null
        defaultGradeCompositionShouldBeFound("isDeleted.specified=true");

        // Get all the gradeCompositionList where isDeleted is null
        defaultGradeCompositionShouldNotBeFound("isDeleted.specified=false");
    }

    @Test
    @Transactional
    void getAllGradeCompositionsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        gradeCompositionRepository.saveAndFlush(gradeComposition);

        // Get all the gradeCompositionList where createdBy equals to DEFAULT_CREATED_BY
        defaultGradeCompositionShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the gradeCompositionList where createdBy equals to UPDATED_CREATED_BY
        defaultGradeCompositionShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllGradeCompositionsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        gradeCompositionRepository.saveAndFlush(gradeComposition);

        // Get all the gradeCompositionList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultGradeCompositionShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the gradeCompositionList where createdBy equals to UPDATED_CREATED_BY
        defaultGradeCompositionShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllGradeCompositionsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        gradeCompositionRepository.saveAndFlush(gradeComposition);

        // Get all the gradeCompositionList where createdBy is not null
        defaultGradeCompositionShouldBeFound("createdBy.specified=true");

        // Get all the gradeCompositionList where createdBy is null
        defaultGradeCompositionShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllGradeCompositionsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        gradeCompositionRepository.saveAndFlush(gradeComposition);

        // Get all the gradeCompositionList where createdBy contains DEFAULT_CREATED_BY
        defaultGradeCompositionShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the gradeCompositionList where createdBy contains UPDATED_CREATED_BY
        defaultGradeCompositionShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllGradeCompositionsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        gradeCompositionRepository.saveAndFlush(gradeComposition);

        // Get all the gradeCompositionList where createdBy does not contain DEFAULT_CREATED_BY
        defaultGradeCompositionShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the gradeCompositionList where createdBy does not contain UPDATED_CREATED_BY
        defaultGradeCompositionShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllGradeCompositionsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        gradeCompositionRepository.saveAndFlush(gradeComposition);

        // Get all the gradeCompositionList where createdDate equals to DEFAULT_CREATED_DATE
        defaultGradeCompositionShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the gradeCompositionList where createdDate equals to UPDATED_CREATED_DATE
        defaultGradeCompositionShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllGradeCompositionsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        gradeCompositionRepository.saveAndFlush(gradeComposition);

        // Get all the gradeCompositionList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultGradeCompositionShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the gradeCompositionList where createdDate equals to UPDATED_CREATED_DATE
        defaultGradeCompositionShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllGradeCompositionsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        gradeCompositionRepository.saveAndFlush(gradeComposition);

        // Get all the gradeCompositionList where createdDate is not null
        defaultGradeCompositionShouldBeFound("createdDate.specified=true");

        // Get all the gradeCompositionList where createdDate is null
        defaultGradeCompositionShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllGradeCompositionsByCreatedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gradeCompositionRepository.saveAndFlush(gradeComposition);

        // Get all the gradeCompositionList where createdDate is greater than or equal to DEFAULT_CREATED_DATE
        defaultGradeCompositionShouldBeFound("createdDate.greaterThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the gradeCompositionList where createdDate is greater than or equal to UPDATED_CREATED_DATE
        defaultGradeCompositionShouldNotBeFound("createdDate.greaterThanOrEqual=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllGradeCompositionsByCreatedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gradeCompositionRepository.saveAndFlush(gradeComposition);

        // Get all the gradeCompositionList where createdDate is less than or equal to DEFAULT_CREATED_DATE
        defaultGradeCompositionShouldBeFound("createdDate.lessThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the gradeCompositionList where createdDate is less than or equal to SMALLER_CREATED_DATE
        defaultGradeCompositionShouldNotBeFound("createdDate.lessThanOrEqual=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllGradeCompositionsByCreatedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        gradeCompositionRepository.saveAndFlush(gradeComposition);

        // Get all the gradeCompositionList where createdDate is less than DEFAULT_CREATED_DATE
        defaultGradeCompositionShouldNotBeFound("createdDate.lessThan=" + DEFAULT_CREATED_DATE);

        // Get all the gradeCompositionList where createdDate is less than UPDATED_CREATED_DATE
        defaultGradeCompositionShouldBeFound("createdDate.lessThan=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllGradeCompositionsByCreatedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        gradeCompositionRepository.saveAndFlush(gradeComposition);

        // Get all the gradeCompositionList where createdDate is greater than DEFAULT_CREATED_DATE
        defaultGradeCompositionShouldNotBeFound("createdDate.greaterThan=" + DEFAULT_CREATED_DATE);

        // Get all the gradeCompositionList where createdDate is greater than SMALLER_CREATED_DATE
        defaultGradeCompositionShouldBeFound("createdDate.greaterThan=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllGradeCompositionsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        gradeCompositionRepository.saveAndFlush(gradeComposition);

        // Get all the gradeCompositionList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultGradeCompositionShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the gradeCompositionList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultGradeCompositionShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllGradeCompositionsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        gradeCompositionRepository.saveAndFlush(gradeComposition);

        // Get all the gradeCompositionList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultGradeCompositionShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the gradeCompositionList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultGradeCompositionShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllGradeCompositionsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        gradeCompositionRepository.saveAndFlush(gradeComposition);

        // Get all the gradeCompositionList where lastModifiedBy is not null
        defaultGradeCompositionShouldBeFound("lastModifiedBy.specified=true");

        // Get all the gradeCompositionList where lastModifiedBy is null
        defaultGradeCompositionShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllGradeCompositionsByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        gradeCompositionRepository.saveAndFlush(gradeComposition);

        // Get all the gradeCompositionList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultGradeCompositionShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the gradeCompositionList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultGradeCompositionShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllGradeCompositionsByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        gradeCompositionRepository.saveAndFlush(gradeComposition);

        // Get all the gradeCompositionList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultGradeCompositionShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the gradeCompositionList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultGradeCompositionShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllGradeCompositionsByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        gradeCompositionRepository.saveAndFlush(gradeComposition);

        // Get all the gradeCompositionList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultGradeCompositionShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the gradeCompositionList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultGradeCompositionShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllGradeCompositionsByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        gradeCompositionRepository.saveAndFlush(gradeComposition);

        // Get all the gradeCompositionList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultGradeCompositionShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the gradeCompositionList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultGradeCompositionShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllGradeCompositionsByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        gradeCompositionRepository.saveAndFlush(gradeComposition);

        // Get all the gradeCompositionList where lastModifiedDate is not null
        defaultGradeCompositionShouldBeFound("lastModifiedDate.specified=true");

        // Get all the gradeCompositionList where lastModifiedDate is null
        defaultGradeCompositionShouldNotBeFound("lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllGradeCompositionsByLastModifiedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gradeCompositionRepository.saveAndFlush(gradeComposition);

        // Get all the gradeCompositionList where lastModifiedDate is greater than or equal to DEFAULT_LAST_MODIFIED_DATE
        defaultGradeCompositionShouldBeFound("lastModifiedDate.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the gradeCompositionList where lastModifiedDate is greater than or equal to UPDATED_LAST_MODIFIED_DATE
        defaultGradeCompositionShouldNotBeFound("lastModifiedDate.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllGradeCompositionsByLastModifiedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gradeCompositionRepository.saveAndFlush(gradeComposition);

        // Get all the gradeCompositionList where lastModifiedDate is less than or equal to DEFAULT_LAST_MODIFIED_DATE
        defaultGradeCompositionShouldBeFound("lastModifiedDate.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the gradeCompositionList where lastModifiedDate is less than or equal to SMALLER_LAST_MODIFIED_DATE
        defaultGradeCompositionShouldNotBeFound("lastModifiedDate.lessThanOrEqual=" + SMALLER_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllGradeCompositionsByLastModifiedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        gradeCompositionRepository.saveAndFlush(gradeComposition);

        // Get all the gradeCompositionList where lastModifiedDate is less than DEFAULT_LAST_MODIFIED_DATE
        defaultGradeCompositionShouldNotBeFound("lastModifiedDate.lessThan=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the gradeCompositionList where lastModifiedDate is less than UPDATED_LAST_MODIFIED_DATE
        defaultGradeCompositionShouldBeFound("lastModifiedDate.lessThan=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllGradeCompositionsByLastModifiedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        gradeCompositionRepository.saveAndFlush(gradeComposition);

        // Get all the gradeCompositionList where lastModifiedDate is greater than DEFAULT_LAST_MODIFIED_DATE
        defaultGradeCompositionShouldNotBeFound("lastModifiedDate.greaterThan=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the gradeCompositionList where lastModifiedDate is greater than SMALLER_LAST_MODIFIED_DATE
        defaultGradeCompositionShouldBeFound("lastModifiedDate.greaterThan=" + SMALLER_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllGradeCompositionsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        gradeCompositionRepository.saveAndFlush(gradeComposition);

        // Get all the gradeCompositionList where type equals to DEFAULT_TYPE
        defaultGradeCompositionShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the gradeCompositionList where type equals to UPDATED_TYPE
        defaultGradeCompositionShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllGradeCompositionsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        gradeCompositionRepository.saveAndFlush(gradeComposition);

        // Get all the gradeCompositionList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultGradeCompositionShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the gradeCompositionList where type equals to UPDATED_TYPE
        defaultGradeCompositionShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllGradeCompositionsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        gradeCompositionRepository.saveAndFlush(gradeComposition);

        // Get all the gradeCompositionList where type is not null
        defaultGradeCompositionShouldBeFound("type.specified=true");

        // Get all the gradeCompositionList where type is null
        defaultGradeCompositionShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllGradeCompositionsByIsPublicIsEqualToSomething() throws Exception {
        // Initialize the database
        gradeCompositionRepository.saveAndFlush(gradeComposition);

        // Get all the gradeCompositionList where isPublic equals to DEFAULT_IS_PUBLIC
        defaultGradeCompositionShouldBeFound("isPublic.equals=" + DEFAULT_IS_PUBLIC);

        // Get all the gradeCompositionList where isPublic equals to UPDATED_IS_PUBLIC
        defaultGradeCompositionShouldNotBeFound("isPublic.equals=" + UPDATED_IS_PUBLIC);
    }

    @Test
    @Transactional
    void getAllGradeCompositionsByIsPublicIsInShouldWork() throws Exception {
        // Initialize the database
        gradeCompositionRepository.saveAndFlush(gradeComposition);

        // Get all the gradeCompositionList where isPublic in DEFAULT_IS_PUBLIC or UPDATED_IS_PUBLIC
        defaultGradeCompositionShouldBeFound("isPublic.in=" + DEFAULT_IS_PUBLIC + "," + UPDATED_IS_PUBLIC);

        // Get all the gradeCompositionList where isPublic equals to UPDATED_IS_PUBLIC
        defaultGradeCompositionShouldNotBeFound("isPublic.in=" + UPDATED_IS_PUBLIC);
    }

    @Test
    @Transactional
    void getAllGradeCompositionsByIsPublicIsNullOrNotNull() throws Exception {
        // Initialize the database
        gradeCompositionRepository.saveAndFlush(gradeComposition);

        // Get all the gradeCompositionList where isPublic is not null
        defaultGradeCompositionShouldBeFound("isPublic.specified=true");

        // Get all the gradeCompositionList where isPublic is null
        defaultGradeCompositionShouldNotBeFound("isPublic.specified=false");
    }

    @Test
    @Transactional
    void getAllGradeCompositionsByAssignmentsIsEqualToSomething() throws Exception {
        Assignment assignments;
        if (TestUtil.findAll(em, Assignment.class).isEmpty()) {
            gradeCompositionRepository.saveAndFlush(gradeComposition);
            assignments = AssignmentResourceIT.createEntity(em);
        } else {
            assignments = TestUtil.findAll(em, Assignment.class).get(0);
        }
        em.persist(assignments);
        em.flush();
        gradeComposition.addAssignments(assignments);
        gradeCompositionRepository.saveAndFlush(gradeComposition);
        Long assignmentsId = assignments.getId();

        // Get all the gradeCompositionList where assignments equals to assignmentsId
        defaultGradeCompositionShouldBeFound("assignmentsId.equals=" + assignmentsId);

        // Get all the gradeCompositionList where assignments equals to (assignmentsId + 1)
        defaultGradeCompositionShouldNotBeFound("assignmentsId.equals=" + (assignmentsId + 1));
    }

    @Test
    @Transactional
    void getAllGradeCompositionsByCourseIsEqualToSomething() throws Exception {
        Course course;
        if (TestUtil.findAll(em, Course.class).isEmpty()) {
            gradeCompositionRepository.saveAndFlush(gradeComposition);
            course = CourseResourceIT.createEntity(em);
        } else {
            course = TestUtil.findAll(em, Course.class).get(0);
        }
        em.persist(course);
        em.flush();
        gradeComposition.setCourse(course);
        gradeCompositionRepository.saveAndFlush(gradeComposition);
        Long courseId = course.getId();

        // Get all the gradeCompositionList where course equals to courseId
        defaultGradeCompositionShouldBeFound("courseId.equals=" + courseId);

        // Get all the gradeCompositionList where course equals to (courseId + 1)
        defaultGradeCompositionShouldNotBeFound("courseId.equals=" + (courseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGradeCompositionShouldBeFound(String filter) throws Exception {
        restGradeCompositionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gradeComposition.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].scale").value(hasItem(DEFAULT_SCALE.intValue())))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(sameInstant(DEFAULT_LAST_MODIFIED_DATE))))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].isPublic").value(hasItem(DEFAULT_IS_PUBLIC.booleanValue())));

        // Check, that the count call also returns 1
        restGradeCompositionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGradeCompositionShouldNotBeFound(String filter) throws Exception {
        restGradeCompositionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGradeCompositionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingGradeComposition() throws Exception {
        // Get the gradeComposition
        restGradeCompositionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGradeComposition() throws Exception {
        // Initialize the database
        gradeCompositionRepository.saveAndFlush(gradeComposition);

        int databaseSizeBeforeUpdate = gradeCompositionRepository.findAll().size();

        // Update the gradeComposition
        GradeComposition updatedGradeComposition = gradeCompositionRepository.findById(gradeComposition.getId()).get();
        // Disconnect from session so that the updates on updatedGradeComposition are not directly saved in db
        em.detach(updatedGradeComposition);
        updatedGradeComposition
            .name(UPDATED_NAME)
            .scale(UPDATED_SCALE)
            .isDeleted(UPDATED_IS_DELETED)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .type(UPDATED_TYPE)
            .isPublic(UPDATED_IS_PUBLIC);

        restGradeCompositionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGradeComposition.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedGradeComposition))
            )
            .andExpect(status().isOk());

        // Validate the GradeComposition in the database
        List<GradeComposition> gradeCompositionList = gradeCompositionRepository.findAll();
        assertThat(gradeCompositionList).hasSize(databaseSizeBeforeUpdate);
        GradeComposition testGradeComposition = gradeCompositionList.get(gradeCompositionList.size() - 1);
        assertThat(testGradeComposition.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGradeComposition.getScale()).isEqualTo(UPDATED_SCALE);
        assertThat(testGradeComposition.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testGradeComposition.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testGradeComposition.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testGradeComposition.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testGradeComposition.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testGradeComposition.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testGradeComposition.getIsPublic()).isEqualTo(UPDATED_IS_PUBLIC);
    }

    @Test
    @Transactional
    void putNonExistingGradeComposition() throws Exception {
        int databaseSizeBeforeUpdate = gradeCompositionRepository.findAll().size();
        gradeComposition.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGradeCompositionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gradeComposition.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gradeComposition))
            )
            .andExpect(status().isBadRequest());

        // Validate the GradeComposition in the database
        List<GradeComposition> gradeCompositionList = gradeCompositionRepository.findAll();
        assertThat(gradeCompositionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGradeComposition() throws Exception {
        int databaseSizeBeforeUpdate = gradeCompositionRepository.findAll().size();
        gradeComposition.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGradeCompositionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gradeComposition))
            )
            .andExpect(status().isBadRequest());

        // Validate the GradeComposition in the database
        List<GradeComposition> gradeCompositionList = gradeCompositionRepository.findAll();
        assertThat(gradeCompositionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGradeComposition() throws Exception {
        int databaseSizeBeforeUpdate = gradeCompositionRepository.findAll().size();
        gradeComposition.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGradeCompositionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gradeComposition))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GradeComposition in the database
        List<GradeComposition> gradeCompositionList = gradeCompositionRepository.findAll();
        assertThat(gradeCompositionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGradeCompositionWithPatch() throws Exception {
        // Initialize the database
        gradeCompositionRepository.saveAndFlush(gradeComposition);

        int databaseSizeBeforeUpdate = gradeCompositionRepository.findAll().size();

        // Update the gradeComposition using partial update
        GradeComposition partialUpdatedGradeComposition = new GradeComposition();
        partialUpdatedGradeComposition.setId(gradeComposition.getId());

        partialUpdatedGradeComposition
            .name(UPDATED_NAME)
            .scale(UPDATED_SCALE)
            .isDeleted(UPDATED_IS_DELETED)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .type(UPDATED_TYPE);

        restGradeCompositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGradeComposition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGradeComposition))
            )
            .andExpect(status().isOk());

        // Validate the GradeComposition in the database
        List<GradeComposition> gradeCompositionList = gradeCompositionRepository.findAll();
        assertThat(gradeCompositionList).hasSize(databaseSizeBeforeUpdate);
        GradeComposition testGradeComposition = gradeCompositionList.get(gradeCompositionList.size() - 1);
        assertThat(testGradeComposition.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGradeComposition.getScale()).isEqualTo(UPDATED_SCALE);
        assertThat(testGradeComposition.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testGradeComposition.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testGradeComposition.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testGradeComposition.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testGradeComposition.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testGradeComposition.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testGradeComposition.getIsPublic()).isEqualTo(DEFAULT_IS_PUBLIC);
    }

    @Test
    @Transactional
    void fullUpdateGradeCompositionWithPatch() throws Exception {
        // Initialize the database
        gradeCompositionRepository.saveAndFlush(gradeComposition);

        int databaseSizeBeforeUpdate = gradeCompositionRepository.findAll().size();

        // Update the gradeComposition using partial update
        GradeComposition partialUpdatedGradeComposition = new GradeComposition();
        partialUpdatedGradeComposition.setId(gradeComposition.getId());

        partialUpdatedGradeComposition
            .name(UPDATED_NAME)
            .scale(UPDATED_SCALE)
            .isDeleted(UPDATED_IS_DELETED)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .type(UPDATED_TYPE)
            .isPublic(UPDATED_IS_PUBLIC);

        restGradeCompositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGradeComposition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGradeComposition))
            )
            .andExpect(status().isOk());

        // Validate the GradeComposition in the database
        List<GradeComposition> gradeCompositionList = gradeCompositionRepository.findAll();
        assertThat(gradeCompositionList).hasSize(databaseSizeBeforeUpdate);
        GradeComposition testGradeComposition = gradeCompositionList.get(gradeCompositionList.size() - 1);
        assertThat(testGradeComposition.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGradeComposition.getScale()).isEqualTo(UPDATED_SCALE);
        assertThat(testGradeComposition.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testGradeComposition.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testGradeComposition.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testGradeComposition.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testGradeComposition.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testGradeComposition.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testGradeComposition.getIsPublic()).isEqualTo(UPDATED_IS_PUBLIC);
    }

    @Test
    @Transactional
    void patchNonExistingGradeComposition() throws Exception {
        int databaseSizeBeforeUpdate = gradeCompositionRepository.findAll().size();
        gradeComposition.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGradeCompositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, gradeComposition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gradeComposition))
            )
            .andExpect(status().isBadRequest());

        // Validate the GradeComposition in the database
        List<GradeComposition> gradeCompositionList = gradeCompositionRepository.findAll();
        assertThat(gradeCompositionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGradeComposition() throws Exception {
        int databaseSizeBeforeUpdate = gradeCompositionRepository.findAll().size();
        gradeComposition.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGradeCompositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gradeComposition))
            )
            .andExpect(status().isBadRequest());

        // Validate the GradeComposition in the database
        List<GradeComposition> gradeCompositionList = gradeCompositionRepository.findAll();
        assertThat(gradeCompositionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGradeComposition() throws Exception {
        int databaseSizeBeforeUpdate = gradeCompositionRepository.findAll().size();
        gradeComposition.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGradeCompositionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gradeComposition))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GradeComposition in the database
        List<GradeComposition> gradeCompositionList = gradeCompositionRepository.findAll();
        assertThat(gradeCompositionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGradeComposition() throws Exception {
        // Initialize the database
        gradeCompositionRepository.saveAndFlush(gradeComposition);

        int databaseSizeBeforeDelete = gradeCompositionRepository.findAll().size();

        // Delete the gradeComposition
        restGradeCompositionMockMvc
            .perform(delete(ENTITY_API_URL_ID, gradeComposition.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GradeComposition> gradeCompositionList = gradeCompositionRepository.findAll();
        assertThat(gradeCompositionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
