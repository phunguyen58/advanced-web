package com.ptudw.web.web.rest;

import static com.ptudw.web.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ptudw.web.IntegrationTest;
import com.ptudw.web.domain.Categories;
import com.ptudw.web.domain.Product;
import com.ptudw.web.repository.CategoriesRepository;
import com.ptudw.web.service.criteria.CategoriesCriteria;
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
 * Integration tests for the {@link CategoriesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CategoriesResourceIT {

    private static final String DEFAULT_CATEGORY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY_URL = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY_URL = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CategoriesRepository categoriesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCategoriesMockMvc;

    private Categories categories;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Categories createEntity(EntityManager em) {
        Categories categories = new Categories()
            .categoryName(DEFAULT_CATEGORY_NAME)
            .categoryDescription(DEFAULT_CATEGORY_DESCRIPTION)
            .categoryUrl(DEFAULT_CATEGORY_URL)
            .createdBy(DEFAULT_CREATED_BY)
            .createdTime(DEFAULT_CREATED_TIME);
        return categories;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Categories createUpdatedEntity(EntityManager em) {
        Categories categories = new Categories()
            .categoryName(UPDATED_CATEGORY_NAME)
            .categoryDescription(UPDATED_CATEGORY_DESCRIPTION)
            .categoryUrl(UPDATED_CATEGORY_URL)
            .createdBy(UPDATED_CREATED_BY)
            .createdTime(UPDATED_CREATED_TIME);
        return categories;
    }

    @BeforeEach
    public void initTest() {
        categories = createEntity(em);
    }

    @Test
    @Transactional
    void createCategories() throws Exception {
        int databaseSizeBeforeCreate = categoriesRepository.findAll().size();
        // Create the Categories
        restCategoriesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categories)))
            .andExpect(status().isCreated());

        // Validate the Categories in the database
        List<Categories> categoriesList = categoriesRepository.findAll();
        assertThat(categoriesList).hasSize(databaseSizeBeforeCreate + 1);
        Categories testCategories = categoriesList.get(categoriesList.size() - 1);
        assertThat(testCategories.getCategoryName()).isEqualTo(DEFAULT_CATEGORY_NAME);
        assertThat(testCategories.getCategoryDescription()).isEqualTo(DEFAULT_CATEGORY_DESCRIPTION);
        assertThat(testCategories.getCategoryUrl()).isEqualTo(DEFAULT_CATEGORY_URL);
        assertThat(testCategories.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testCategories.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
    }

    @Test
    @Transactional
    void createCategoriesWithExistingId() throws Exception {
        // Create the Categories with an existing ID
        categories.setId(1L);

        int databaseSizeBeforeCreate = categoriesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCategoriesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categories)))
            .andExpect(status().isBadRequest());

        // Validate the Categories in the database
        List<Categories> categoriesList = categoriesRepository.findAll();
        assertThat(categoriesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCategoryNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = categoriesRepository.findAll().size();
        // set the field null
        categories.setCategoryName(null);

        // Create the Categories, which fails.

        restCategoriesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categories)))
            .andExpect(status().isBadRequest());

        List<Categories> categoriesList = categoriesRepository.findAll();
        assertThat(categoriesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCategories() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        // Get all the categoriesList
        restCategoriesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categories.getId().intValue())))
            .andExpect(jsonPath("$.[*].categoryName").value(hasItem(DEFAULT_CATEGORY_NAME)))
            .andExpect(jsonPath("$.[*].categoryDescription").value(hasItem(DEFAULT_CATEGORY_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].categoryUrl").value(hasItem(DEFAULT_CATEGORY_URL)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(sameInstant(DEFAULT_CREATED_TIME))));
    }

    @Test
    @Transactional
    void getCategories() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        // Get the categories
        restCategoriesMockMvc
            .perform(get(ENTITY_API_URL_ID, categories.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(categories.getId().intValue()))
            .andExpect(jsonPath("$.categoryName").value(DEFAULT_CATEGORY_NAME))
            .andExpect(jsonPath("$.categoryDescription").value(DEFAULT_CATEGORY_DESCRIPTION))
            .andExpect(jsonPath("$.categoryUrl").value(DEFAULT_CATEGORY_URL))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdTime").value(sameInstant(DEFAULT_CREATED_TIME)));
    }

    @Test
    @Transactional
    void getCategoriesByIdFiltering() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        Long id = categories.getId();

        defaultCategoriesShouldBeFound("id.equals=" + id);
        defaultCategoriesShouldNotBeFound("id.notEquals=" + id);

        defaultCategoriesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCategoriesShouldNotBeFound("id.greaterThan=" + id);

        defaultCategoriesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCategoriesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCategoriesByCategoryNameIsEqualToSomething() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        // Get all the categoriesList where categoryName equals to DEFAULT_CATEGORY_NAME
        defaultCategoriesShouldBeFound("categoryName.equals=" + DEFAULT_CATEGORY_NAME);

        // Get all the categoriesList where categoryName equals to UPDATED_CATEGORY_NAME
        defaultCategoriesShouldNotBeFound("categoryName.equals=" + UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllCategoriesByCategoryNameIsInShouldWork() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        // Get all the categoriesList where categoryName in DEFAULT_CATEGORY_NAME or UPDATED_CATEGORY_NAME
        defaultCategoriesShouldBeFound("categoryName.in=" + DEFAULT_CATEGORY_NAME + "," + UPDATED_CATEGORY_NAME);

        // Get all the categoriesList where categoryName equals to UPDATED_CATEGORY_NAME
        defaultCategoriesShouldNotBeFound("categoryName.in=" + UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllCategoriesByCategoryNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        // Get all the categoriesList where categoryName is not null
        defaultCategoriesShouldBeFound("categoryName.specified=true");

        // Get all the categoriesList where categoryName is null
        defaultCategoriesShouldNotBeFound("categoryName.specified=false");
    }

    @Test
    @Transactional
    void getAllCategoriesByCategoryNameContainsSomething() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        // Get all the categoriesList where categoryName contains DEFAULT_CATEGORY_NAME
        defaultCategoriesShouldBeFound("categoryName.contains=" + DEFAULT_CATEGORY_NAME);

        // Get all the categoriesList where categoryName contains UPDATED_CATEGORY_NAME
        defaultCategoriesShouldNotBeFound("categoryName.contains=" + UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllCategoriesByCategoryNameNotContainsSomething() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        // Get all the categoriesList where categoryName does not contain DEFAULT_CATEGORY_NAME
        defaultCategoriesShouldNotBeFound("categoryName.doesNotContain=" + DEFAULT_CATEGORY_NAME);

        // Get all the categoriesList where categoryName does not contain UPDATED_CATEGORY_NAME
        defaultCategoriesShouldBeFound("categoryName.doesNotContain=" + UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllCategoriesByCategoryDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        // Get all the categoriesList where categoryDescription equals to DEFAULT_CATEGORY_DESCRIPTION
        defaultCategoriesShouldBeFound("categoryDescription.equals=" + DEFAULT_CATEGORY_DESCRIPTION);

        // Get all the categoriesList where categoryDescription equals to UPDATED_CATEGORY_DESCRIPTION
        defaultCategoriesShouldNotBeFound("categoryDescription.equals=" + UPDATED_CATEGORY_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCategoriesByCategoryDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        // Get all the categoriesList where categoryDescription in DEFAULT_CATEGORY_DESCRIPTION or UPDATED_CATEGORY_DESCRIPTION
        defaultCategoriesShouldBeFound("categoryDescription.in=" + DEFAULT_CATEGORY_DESCRIPTION + "," + UPDATED_CATEGORY_DESCRIPTION);

        // Get all the categoriesList where categoryDescription equals to UPDATED_CATEGORY_DESCRIPTION
        defaultCategoriesShouldNotBeFound("categoryDescription.in=" + UPDATED_CATEGORY_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCategoriesByCategoryDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        // Get all the categoriesList where categoryDescription is not null
        defaultCategoriesShouldBeFound("categoryDescription.specified=true");

        // Get all the categoriesList where categoryDescription is null
        defaultCategoriesShouldNotBeFound("categoryDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllCategoriesByCategoryDescriptionContainsSomething() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        // Get all the categoriesList where categoryDescription contains DEFAULT_CATEGORY_DESCRIPTION
        defaultCategoriesShouldBeFound("categoryDescription.contains=" + DEFAULT_CATEGORY_DESCRIPTION);

        // Get all the categoriesList where categoryDescription contains UPDATED_CATEGORY_DESCRIPTION
        defaultCategoriesShouldNotBeFound("categoryDescription.contains=" + UPDATED_CATEGORY_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCategoriesByCategoryDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        // Get all the categoriesList where categoryDescription does not contain DEFAULT_CATEGORY_DESCRIPTION
        defaultCategoriesShouldNotBeFound("categoryDescription.doesNotContain=" + DEFAULT_CATEGORY_DESCRIPTION);

        // Get all the categoriesList where categoryDescription does not contain UPDATED_CATEGORY_DESCRIPTION
        defaultCategoriesShouldBeFound("categoryDescription.doesNotContain=" + UPDATED_CATEGORY_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCategoriesByCategoryUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        // Get all the categoriesList where categoryUrl equals to DEFAULT_CATEGORY_URL
        defaultCategoriesShouldBeFound("categoryUrl.equals=" + DEFAULT_CATEGORY_URL);

        // Get all the categoriesList where categoryUrl equals to UPDATED_CATEGORY_URL
        defaultCategoriesShouldNotBeFound("categoryUrl.equals=" + UPDATED_CATEGORY_URL);
    }

    @Test
    @Transactional
    void getAllCategoriesByCategoryUrlIsInShouldWork() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        // Get all the categoriesList where categoryUrl in DEFAULT_CATEGORY_URL or UPDATED_CATEGORY_URL
        defaultCategoriesShouldBeFound("categoryUrl.in=" + DEFAULT_CATEGORY_URL + "," + UPDATED_CATEGORY_URL);

        // Get all the categoriesList where categoryUrl equals to UPDATED_CATEGORY_URL
        defaultCategoriesShouldNotBeFound("categoryUrl.in=" + UPDATED_CATEGORY_URL);
    }

    @Test
    @Transactional
    void getAllCategoriesByCategoryUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        // Get all the categoriesList where categoryUrl is not null
        defaultCategoriesShouldBeFound("categoryUrl.specified=true");

        // Get all the categoriesList where categoryUrl is null
        defaultCategoriesShouldNotBeFound("categoryUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllCategoriesByCategoryUrlContainsSomething() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        // Get all the categoriesList where categoryUrl contains DEFAULT_CATEGORY_URL
        defaultCategoriesShouldBeFound("categoryUrl.contains=" + DEFAULT_CATEGORY_URL);

        // Get all the categoriesList where categoryUrl contains UPDATED_CATEGORY_URL
        defaultCategoriesShouldNotBeFound("categoryUrl.contains=" + UPDATED_CATEGORY_URL);
    }

    @Test
    @Transactional
    void getAllCategoriesByCategoryUrlNotContainsSomething() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        // Get all the categoriesList where categoryUrl does not contain DEFAULT_CATEGORY_URL
        defaultCategoriesShouldNotBeFound("categoryUrl.doesNotContain=" + DEFAULT_CATEGORY_URL);

        // Get all the categoriesList where categoryUrl does not contain UPDATED_CATEGORY_URL
        defaultCategoriesShouldBeFound("categoryUrl.doesNotContain=" + UPDATED_CATEGORY_URL);
    }

    @Test
    @Transactional
    void getAllCategoriesByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        // Get all the categoriesList where createdBy equals to DEFAULT_CREATED_BY
        defaultCategoriesShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the categoriesList where createdBy equals to UPDATED_CREATED_BY
        defaultCategoriesShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllCategoriesByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        // Get all the categoriesList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultCategoriesShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the categoriesList where createdBy equals to UPDATED_CREATED_BY
        defaultCategoriesShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllCategoriesByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        // Get all the categoriesList where createdBy is not null
        defaultCategoriesShouldBeFound("createdBy.specified=true");

        // Get all the categoriesList where createdBy is null
        defaultCategoriesShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllCategoriesByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        // Get all the categoriesList where createdBy contains DEFAULT_CREATED_BY
        defaultCategoriesShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the categoriesList where createdBy contains UPDATED_CREATED_BY
        defaultCategoriesShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllCategoriesByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        // Get all the categoriesList where createdBy does not contain DEFAULT_CREATED_BY
        defaultCategoriesShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the categoriesList where createdBy does not contain UPDATED_CREATED_BY
        defaultCategoriesShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllCategoriesByCreatedTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        // Get all the categoriesList where createdTime equals to DEFAULT_CREATED_TIME
        defaultCategoriesShouldBeFound("createdTime.equals=" + DEFAULT_CREATED_TIME);

        // Get all the categoriesList where createdTime equals to UPDATED_CREATED_TIME
        defaultCategoriesShouldNotBeFound("createdTime.equals=" + UPDATED_CREATED_TIME);
    }

    @Test
    @Transactional
    void getAllCategoriesByCreatedTimeIsInShouldWork() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        // Get all the categoriesList where createdTime in DEFAULT_CREATED_TIME or UPDATED_CREATED_TIME
        defaultCategoriesShouldBeFound("createdTime.in=" + DEFAULT_CREATED_TIME + "," + UPDATED_CREATED_TIME);

        // Get all the categoriesList where createdTime equals to UPDATED_CREATED_TIME
        defaultCategoriesShouldNotBeFound("createdTime.in=" + UPDATED_CREATED_TIME);
    }

    @Test
    @Transactional
    void getAllCategoriesByCreatedTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        // Get all the categoriesList where createdTime is not null
        defaultCategoriesShouldBeFound("createdTime.specified=true");

        // Get all the categoriesList where createdTime is null
        defaultCategoriesShouldNotBeFound("createdTime.specified=false");
    }

    @Test
    @Transactional
    void getAllCategoriesByCreatedTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        // Get all the categoriesList where createdTime is greater than or equal to DEFAULT_CREATED_TIME
        defaultCategoriesShouldBeFound("createdTime.greaterThanOrEqual=" + DEFAULT_CREATED_TIME);

        // Get all the categoriesList where createdTime is greater than or equal to UPDATED_CREATED_TIME
        defaultCategoriesShouldNotBeFound("createdTime.greaterThanOrEqual=" + UPDATED_CREATED_TIME);
    }

    @Test
    @Transactional
    void getAllCategoriesByCreatedTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        // Get all the categoriesList where createdTime is less than or equal to DEFAULT_CREATED_TIME
        defaultCategoriesShouldBeFound("createdTime.lessThanOrEqual=" + DEFAULT_CREATED_TIME);

        // Get all the categoriesList where createdTime is less than or equal to SMALLER_CREATED_TIME
        defaultCategoriesShouldNotBeFound("createdTime.lessThanOrEqual=" + SMALLER_CREATED_TIME);
    }

    @Test
    @Transactional
    void getAllCategoriesByCreatedTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        // Get all the categoriesList where createdTime is less than DEFAULT_CREATED_TIME
        defaultCategoriesShouldNotBeFound("createdTime.lessThan=" + DEFAULT_CREATED_TIME);

        // Get all the categoriesList where createdTime is less than UPDATED_CREATED_TIME
        defaultCategoriesShouldBeFound("createdTime.lessThan=" + UPDATED_CREATED_TIME);
    }

    @Test
    @Transactional
    void getAllCategoriesByCreatedTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        // Get all the categoriesList where createdTime is greater than DEFAULT_CREATED_TIME
        defaultCategoriesShouldNotBeFound("createdTime.greaterThan=" + DEFAULT_CREATED_TIME);

        // Get all the categoriesList where createdTime is greater than SMALLER_CREATED_TIME
        defaultCategoriesShouldBeFound("createdTime.greaterThan=" + SMALLER_CREATED_TIME);
    }

    @Test
    @Transactional
    void getAllCategoriesByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            categoriesRepository.saveAndFlush(categories);
            product = ProductResourceIT.createEntity(em);
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        em.persist(product);
        em.flush();
        categories.addProduct(product);
        categoriesRepository.saveAndFlush(categories);
        Long productId = product.getId();

        // Get all the categoriesList where product equals to productId
        defaultCategoriesShouldBeFound("productId.equals=" + productId);

        // Get all the categoriesList where product equals to (productId + 1)
        defaultCategoriesShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCategoriesShouldBeFound(String filter) throws Exception {
        restCategoriesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categories.getId().intValue())))
            .andExpect(jsonPath("$.[*].categoryName").value(hasItem(DEFAULT_CATEGORY_NAME)))
            .andExpect(jsonPath("$.[*].categoryDescription").value(hasItem(DEFAULT_CATEGORY_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].categoryUrl").value(hasItem(DEFAULT_CATEGORY_URL)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(sameInstant(DEFAULT_CREATED_TIME))));

        // Check, that the count call also returns 1
        restCategoriesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCategoriesShouldNotBeFound(String filter) throws Exception {
        restCategoriesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCategoriesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCategories() throws Exception {
        // Get the categories
        restCategoriesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCategories() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        int databaseSizeBeforeUpdate = categoriesRepository.findAll().size();

        // Update the categories
        Categories updatedCategories = categoriesRepository.findById(categories.getId()).get();
        // Disconnect from session so that the updates on updatedCategories are not directly saved in db
        em.detach(updatedCategories);
        updatedCategories
            .categoryName(UPDATED_CATEGORY_NAME)
            .categoryDescription(UPDATED_CATEGORY_DESCRIPTION)
            .categoryUrl(UPDATED_CATEGORY_URL)
            .createdBy(UPDATED_CREATED_BY)
            .createdTime(UPDATED_CREATED_TIME);

        restCategoriesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCategories.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCategories))
            )
            .andExpect(status().isOk());

        // Validate the Categories in the database
        List<Categories> categoriesList = categoriesRepository.findAll();
        assertThat(categoriesList).hasSize(databaseSizeBeforeUpdate);
        Categories testCategories = categoriesList.get(categoriesList.size() - 1);
        assertThat(testCategories.getCategoryName()).isEqualTo(UPDATED_CATEGORY_NAME);
        assertThat(testCategories.getCategoryDescription()).isEqualTo(UPDATED_CATEGORY_DESCRIPTION);
        assertThat(testCategories.getCategoryUrl()).isEqualTo(UPDATED_CATEGORY_URL);
        assertThat(testCategories.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testCategories.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
    }

    @Test
    @Transactional
    void putNonExistingCategories() throws Exception {
        int databaseSizeBeforeUpdate = categoriesRepository.findAll().size();
        categories.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoriesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categories.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categories))
            )
            .andExpect(status().isBadRequest());

        // Validate the Categories in the database
        List<Categories> categoriesList = categoriesRepository.findAll();
        assertThat(categoriesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCategories() throws Exception {
        int databaseSizeBeforeUpdate = categoriesRepository.findAll().size();
        categories.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoriesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categories))
            )
            .andExpect(status().isBadRequest());

        // Validate the Categories in the database
        List<Categories> categoriesList = categoriesRepository.findAll();
        assertThat(categoriesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCategories() throws Exception {
        int databaseSizeBeforeUpdate = categoriesRepository.findAll().size();
        categories.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoriesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categories)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Categories in the database
        List<Categories> categoriesList = categoriesRepository.findAll();
        assertThat(categoriesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCategoriesWithPatch() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        int databaseSizeBeforeUpdate = categoriesRepository.findAll().size();

        // Update the categories using partial update
        Categories partialUpdatedCategories = new Categories();
        partialUpdatedCategories.setId(categories.getId());

        partialUpdatedCategories
            .categoryDescription(UPDATED_CATEGORY_DESCRIPTION)
            .categoryUrl(UPDATED_CATEGORY_URL)
            .createdBy(UPDATED_CREATED_BY);

        restCategoriesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategories.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCategories))
            )
            .andExpect(status().isOk());

        // Validate the Categories in the database
        List<Categories> categoriesList = categoriesRepository.findAll();
        assertThat(categoriesList).hasSize(databaseSizeBeforeUpdate);
        Categories testCategories = categoriesList.get(categoriesList.size() - 1);
        assertThat(testCategories.getCategoryName()).isEqualTo(DEFAULT_CATEGORY_NAME);
        assertThat(testCategories.getCategoryDescription()).isEqualTo(UPDATED_CATEGORY_DESCRIPTION);
        assertThat(testCategories.getCategoryUrl()).isEqualTo(UPDATED_CATEGORY_URL);
        assertThat(testCategories.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testCategories.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
    }

    @Test
    @Transactional
    void fullUpdateCategoriesWithPatch() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        int databaseSizeBeforeUpdate = categoriesRepository.findAll().size();

        // Update the categories using partial update
        Categories partialUpdatedCategories = new Categories();
        partialUpdatedCategories.setId(categories.getId());

        partialUpdatedCategories
            .categoryName(UPDATED_CATEGORY_NAME)
            .categoryDescription(UPDATED_CATEGORY_DESCRIPTION)
            .categoryUrl(UPDATED_CATEGORY_URL)
            .createdBy(UPDATED_CREATED_BY)
            .createdTime(UPDATED_CREATED_TIME);

        restCategoriesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategories.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCategories))
            )
            .andExpect(status().isOk());

        // Validate the Categories in the database
        List<Categories> categoriesList = categoriesRepository.findAll();
        assertThat(categoriesList).hasSize(databaseSizeBeforeUpdate);
        Categories testCategories = categoriesList.get(categoriesList.size() - 1);
        assertThat(testCategories.getCategoryName()).isEqualTo(UPDATED_CATEGORY_NAME);
        assertThat(testCategories.getCategoryDescription()).isEqualTo(UPDATED_CATEGORY_DESCRIPTION);
        assertThat(testCategories.getCategoryUrl()).isEqualTo(UPDATED_CATEGORY_URL);
        assertThat(testCategories.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testCategories.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
    }

    @Test
    @Transactional
    void patchNonExistingCategories() throws Exception {
        int databaseSizeBeforeUpdate = categoriesRepository.findAll().size();
        categories.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoriesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, categories.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categories))
            )
            .andExpect(status().isBadRequest());

        // Validate the Categories in the database
        List<Categories> categoriesList = categoriesRepository.findAll();
        assertThat(categoriesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCategories() throws Exception {
        int databaseSizeBeforeUpdate = categoriesRepository.findAll().size();
        categories.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoriesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categories))
            )
            .andExpect(status().isBadRequest());

        // Validate the Categories in the database
        List<Categories> categoriesList = categoriesRepository.findAll();
        assertThat(categoriesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCategories() throws Exception {
        int databaseSizeBeforeUpdate = categoriesRepository.findAll().size();
        categories.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoriesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(categories))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Categories in the database
        List<Categories> categoriesList = categoriesRepository.findAll();
        assertThat(categoriesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCategories() throws Exception {
        // Initialize the database
        categoriesRepository.saveAndFlush(categories);

        int databaseSizeBeforeDelete = categoriesRepository.findAll().size();

        // Delete the categories
        restCategoriesMockMvc
            .perform(delete(ENTITY_API_URL_ID, categories.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Categories> categoriesList = categoriesRepository.findAll();
        assertThat(categoriesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
