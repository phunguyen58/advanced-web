package com.ptudw.web.web.rest;

import static com.ptudw.web.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ptudw.web.IntegrationTest;
import com.ptudw.web.domain.Categories;
import com.ptudw.web.domain.Product;
import com.ptudw.web.repository.ProductRepository;
import com.ptudw.web.service.criteria.ProductCriteria;
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
 * Integration tests for the {@link ProductResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductResourceIT {

    private static final String DEFAULT_PRODUCT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_PRODUCT_PRICE = 1D;
    private static final Double UPDATED_PRODUCT_PRICE = 2D;
    private static final Double SMALLER_PRODUCT_PRICE = 1D - 1D;

    private static final Double DEFAULT_PRODUCT_PRICE_SALE = 1D;
    private static final Double UPDATED_PRODUCT_PRICE_SALE = 2D;
    private static final Double SMALLER_PRODUCT_PRICE_SALE = 1D - 1D;

    private static final String DEFAULT_PRODUCT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_SHORT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_SHORT_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_PRODUCT_QUANTITY = 1;
    private static final Integer UPDATED_PRODUCT_QUANTITY = 2;
    private static final Integer SMALLER_PRODUCT_QUANTITY = 1 - 1;

    private static final String DEFAULT_PRODUCT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_CODE = "BBBBBBBBBB";

    private static final Double DEFAULT_PRODUCT_POINT_RATING = 1D;
    private static final Double UPDATED_PRODUCT_POINT_RATING = 2D;
    private static final Double SMALLER_PRODUCT_POINT_RATING = 1D - 1D;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/products";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductMockMvc;

    private Product product;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createEntity(EntityManager em) {
        Product product = new Product()
            .productName(DEFAULT_PRODUCT_NAME)
            .productPrice(DEFAULT_PRODUCT_PRICE)
            .productPriceSale(DEFAULT_PRODUCT_PRICE_SALE)
            .productDescription(DEFAULT_PRODUCT_DESCRIPTION)
            .productShortDescription(DEFAULT_PRODUCT_SHORT_DESCRIPTION)
            .productQuantity(DEFAULT_PRODUCT_QUANTITY)
            .productCode(DEFAULT_PRODUCT_CODE)
            .productPointRating(DEFAULT_PRODUCT_POINT_RATING)
            .createdBy(DEFAULT_CREATED_BY)
            .createdTime(DEFAULT_CREATED_TIME);
        return product;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createUpdatedEntity(EntityManager em) {
        Product product = new Product()
            .productName(UPDATED_PRODUCT_NAME)
            .productPrice(UPDATED_PRODUCT_PRICE)
            .productPriceSale(UPDATED_PRODUCT_PRICE_SALE)
            .productDescription(UPDATED_PRODUCT_DESCRIPTION)
            .productShortDescription(UPDATED_PRODUCT_SHORT_DESCRIPTION)
            .productQuantity(UPDATED_PRODUCT_QUANTITY)
            .productCode(UPDATED_PRODUCT_CODE)
            .productPointRating(UPDATED_PRODUCT_POINT_RATING)
            .createdBy(UPDATED_CREATED_BY)
            .createdTime(UPDATED_CREATED_TIME);
        return product;
    }

    @BeforeEach
    public void initTest() {
        product = createEntity(em);
    }

    @Test
    @Transactional
    void createProduct() throws Exception {
        int databaseSizeBeforeCreate = productRepository.findAll().size();
        // Create the Product
        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isCreated());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate + 1);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getProductName()).isEqualTo(DEFAULT_PRODUCT_NAME);
        assertThat(testProduct.getProductPrice()).isEqualTo(DEFAULT_PRODUCT_PRICE);
        assertThat(testProduct.getProductPriceSale()).isEqualTo(DEFAULT_PRODUCT_PRICE_SALE);
        assertThat(testProduct.getProductDescription()).isEqualTo(DEFAULT_PRODUCT_DESCRIPTION);
        assertThat(testProduct.getProductShortDescription()).isEqualTo(DEFAULT_PRODUCT_SHORT_DESCRIPTION);
        assertThat(testProduct.getProductQuantity()).isEqualTo(DEFAULT_PRODUCT_QUANTITY);
        assertThat(testProduct.getProductCode()).isEqualTo(DEFAULT_PRODUCT_CODE);
        assertThat(testProduct.getProductPointRating()).isEqualTo(DEFAULT_PRODUCT_POINT_RATING);
        assertThat(testProduct.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testProduct.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
    }

    @Test
    @Transactional
    void createProductWithExistingId() throws Exception {
        // Create the Product with an existing ID
        product.setId(1L);

        int databaseSizeBeforeCreate = productRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkProductNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setProductName(null);

        // Create the Product, which fails.

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProductPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setProductPrice(null);

        // Create the Product, which fails.

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProductDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setProductDescription(null);

        // Create the Product, which fails.

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProducts() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].productName").value(hasItem(DEFAULT_PRODUCT_NAME)))
            .andExpect(jsonPath("$.[*].productPrice").value(hasItem(DEFAULT_PRODUCT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].productPriceSale").value(hasItem(DEFAULT_PRODUCT_PRICE_SALE.doubleValue())))
            .andExpect(jsonPath("$.[*].productDescription").value(hasItem(DEFAULT_PRODUCT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].productShortDescription").value(hasItem(DEFAULT_PRODUCT_SHORT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].productQuantity").value(hasItem(DEFAULT_PRODUCT_QUANTITY)))
            .andExpect(jsonPath("$.[*].productCode").value(hasItem(DEFAULT_PRODUCT_CODE)))
            .andExpect(jsonPath("$.[*].productPointRating").value(hasItem(DEFAULT_PRODUCT_POINT_RATING.doubleValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(sameInstant(DEFAULT_CREATED_TIME))));
    }

    @Test
    @Transactional
    void getProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get the product
        restProductMockMvc
            .perform(get(ENTITY_API_URL_ID, product.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(product.getId().intValue()))
            .andExpect(jsonPath("$.productName").value(DEFAULT_PRODUCT_NAME))
            .andExpect(jsonPath("$.productPrice").value(DEFAULT_PRODUCT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.productPriceSale").value(DEFAULT_PRODUCT_PRICE_SALE.doubleValue()))
            .andExpect(jsonPath("$.productDescription").value(DEFAULT_PRODUCT_DESCRIPTION))
            .andExpect(jsonPath("$.productShortDescription").value(DEFAULT_PRODUCT_SHORT_DESCRIPTION))
            .andExpect(jsonPath("$.productQuantity").value(DEFAULT_PRODUCT_QUANTITY))
            .andExpect(jsonPath("$.productCode").value(DEFAULT_PRODUCT_CODE))
            .andExpect(jsonPath("$.productPointRating").value(DEFAULT_PRODUCT_POINT_RATING.doubleValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdTime").value(sameInstant(DEFAULT_CREATED_TIME)));
    }

    @Test
    @Transactional
    void getProductsByIdFiltering() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        Long id = product.getId();

        defaultProductShouldBeFound("id.equals=" + id);
        defaultProductShouldNotBeFound("id.notEquals=" + id);

        defaultProductShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProductShouldNotBeFound("id.greaterThan=" + id);

        defaultProductShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProductShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductsByProductNameIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productName equals to DEFAULT_PRODUCT_NAME
        defaultProductShouldBeFound("productName.equals=" + DEFAULT_PRODUCT_NAME);

        // Get all the productList where productName equals to UPDATED_PRODUCT_NAME
        defaultProductShouldNotBeFound("productName.equals=" + UPDATED_PRODUCT_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByProductNameIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productName in DEFAULT_PRODUCT_NAME or UPDATED_PRODUCT_NAME
        defaultProductShouldBeFound("productName.in=" + DEFAULT_PRODUCT_NAME + "," + UPDATED_PRODUCT_NAME);

        // Get all the productList where productName equals to UPDATED_PRODUCT_NAME
        defaultProductShouldNotBeFound("productName.in=" + UPDATED_PRODUCT_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByProductNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productName is not null
        defaultProductShouldBeFound("productName.specified=true");

        // Get all the productList where productName is null
        defaultProductShouldNotBeFound("productName.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByProductNameContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productName contains DEFAULT_PRODUCT_NAME
        defaultProductShouldBeFound("productName.contains=" + DEFAULT_PRODUCT_NAME);

        // Get all the productList where productName contains UPDATED_PRODUCT_NAME
        defaultProductShouldNotBeFound("productName.contains=" + UPDATED_PRODUCT_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByProductNameNotContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productName does not contain DEFAULT_PRODUCT_NAME
        defaultProductShouldNotBeFound("productName.doesNotContain=" + DEFAULT_PRODUCT_NAME);

        // Get all the productList where productName does not contain UPDATED_PRODUCT_NAME
        defaultProductShouldBeFound("productName.doesNotContain=" + UPDATED_PRODUCT_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByProductPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productPrice equals to DEFAULT_PRODUCT_PRICE
        defaultProductShouldBeFound("productPrice.equals=" + DEFAULT_PRODUCT_PRICE);

        // Get all the productList where productPrice equals to UPDATED_PRODUCT_PRICE
        defaultProductShouldNotBeFound("productPrice.equals=" + UPDATED_PRODUCT_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByProductPriceIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productPrice in DEFAULT_PRODUCT_PRICE or UPDATED_PRODUCT_PRICE
        defaultProductShouldBeFound("productPrice.in=" + DEFAULT_PRODUCT_PRICE + "," + UPDATED_PRODUCT_PRICE);

        // Get all the productList where productPrice equals to UPDATED_PRODUCT_PRICE
        defaultProductShouldNotBeFound("productPrice.in=" + UPDATED_PRODUCT_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByProductPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productPrice is not null
        defaultProductShouldBeFound("productPrice.specified=true");

        // Get all the productList where productPrice is null
        defaultProductShouldNotBeFound("productPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByProductPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productPrice is greater than or equal to DEFAULT_PRODUCT_PRICE
        defaultProductShouldBeFound("productPrice.greaterThanOrEqual=" + DEFAULT_PRODUCT_PRICE);

        // Get all the productList where productPrice is greater than or equal to UPDATED_PRODUCT_PRICE
        defaultProductShouldNotBeFound("productPrice.greaterThanOrEqual=" + UPDATED_PRODUCT_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByProductPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productPrice is less than or equal to DEFAULT_PRODUCT_PRICE
        defaultProductShouldBeFound("productPrice.lessThanOrEqual=" + DEFAULT_PRODUCT_PRICE);

        // Get all the productList where productPrice is less than or equal to SMALLER_PRODUCT_PRICE
        defaultProductShouldNotBeFound("productPrice.lessThanOrEqual=" + SMALLER_PRODUCT_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByProductPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productPrice is less than DEFAULT_PRODUCT_PRICE
        defaultProductShouldNotBeFound("productPrice.lessThan=" + DEFAULT_PRODUCT_PRICE);

        // Get all the productList where productPrice is less than UPDATED_PRODUCT_PRICE
        defaultProductShouldBeFound("productPrice.lessThan=" + UPDATED_PRODUCT_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByProductPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productPrice is greater than DEFAULT_PRODUCT_PRICE
        defaultProductShouldNotBeFound("productPrice.greaterThan=" + DEFAULT_PRODUCT_PRICE);

        // Get all the productList where productPrice is greater than SMALLER_PRODUCT_PRICE
        defaultProductShouldBeFound("productPrice.greaterThan=" + SMALLER_PRODUCT_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByProductPriceSaleIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productPriceSale equals to DEFAULT_PRODUCT_PRICE_SALE
        defaultProductShouldBeFound("productPriceSale.equals=" + DEFAULT_PRODUCT_PRICE_SALE);

        // Get all the productList where productPriceSale equals to UPDATED_PRODUCT_PRICE_SALE
        defaultProductShouldNotBeFound("productPriceSale.equals=" + UPDATED_PRODUCT_PRICE_SALE);
    }

    @Test
    @Transactional
    void getAllProductsByProductPriceSaleIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productPriceSale in DEFAULT_PRODUCT_PRICE_SALE or UPDATED_PRODUCT_PRICE_SALE
        defaultProductShouldBeFound("productPriceSale.in=" + DEFAULT_PRODUCT_PRICE_SALE + "," + UPDATED_PRODUCT_PRICE_SALE);

        // Get all the productList where productPriceSale equals to UPDATED_PRODUCT_PRICE_SALE
        defaultProductShouldNotBeFound("productPriceSale.in=" + UPDATED_PRODUCT_PRICE_SALE);
    }

    @Test
    @Transactional
    void getAllProductsByProductPriceSaleIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productPriceSale is not null
        defaultProductShouldBeFound("productPriceSale.specified=true");

        // Get all the productList where productPriceSale is null
        defaultProductShouldNotBeFound("productPriceSale.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByProductPriceSaleIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productPriceSale is greater than or equal to DEFAULT_PRODUCT_PRICE_SALE
        defaultProductShouldBeFound("productPriceSale.greaterThanOrEqual=" + DEFAULT_PRODUCT_PRICE_SALE);

        // Get all the productList where productPriceSale is greater than or equal to UPDATED_PRODUCT_PRICE_SALE
        defaultProductShouldNotBeFound("productPriceSale.greaterThanOrEqual=" + UPDATED_PRODUCT_PRICE_SALE);
    }

    @Test
    @Transactional
    void getAllProductsByProductPriceSaleIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productPriceSale is less than or equal to DEFAULT_PRODUCT_PRICE_SALE
        defaultProductShouldBeFound("productPriceSale.lessThanOrEqual=" + DEFAULT_PRODUCT_PRICE_SALE);

        // Get all the productList where productPriceSale is less than or equal to SMALLER_PRODUCT_PRICE_SALE
        defaultProductShouldNotBeFound("productPriceSale.lessThanOrEqual=" + SMALLER_PRODUCT_PRICE_SALE);
    }

    @Test
    @Transactional
    void getAllProductsByProductPriceSaleIsLessThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productPriceSale is less than DEFAULT_PRODUCT_PRICE_SALE
        defaultProductShouldNotBeFound("productPriceSale.lessThan=" + DEFAULT_PRODUCT_PRICE_SALE);

        // Get all the productList where productPriceSale is less than UPDATED_PRODUCT_PRICE_SALE
        defaultProductShouldBeFound("productPriceSale.lessThan=" + UPDATED_PRODUCT_PRICE_SALE);
    }

    @Test
    @Transactional
    void getAllProductsByProductPriceSaleIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productPriceSale is greater than DEFAULT_PRODUCT_PRICE_SALE
        defaultProductShouldNotBeFound("productPriceSale.greaterThan=" + DEFAULT_PRODUCT_PRICE_SALE);

        // Get all the productList where productPriceSale is greater than SMALLER_PRODUCT_PRICE_SALE
        defaultProductShouldBeFound("productPriceSale.greaterThan=" + SMALLER_PRODUCT_PRICE_SALE);
    }

    @Test
    @Transactional
    void getAllProductsByProductDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productDescription equals to DEFAULT_PRODUCT_DESCRIPTION
        defaultProductShouldBeFound("productDescription.equals=" + DEFAULT_PRODUCT_DESCRIPTION);

        // Get all the productList where productDescription equals to UPDATED_PRODUCT_DESCRIPTION
        defaultProductShouldNotBeFound("productDescription.equals=" + UPDATED_PRODUCT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByProductDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productDescription in DEFAULT_PRODUCT_DESCRIPTION or UPDATED_PRODUCT_DESCRIPTION
        defaultProductShouldBeFound("productDescription.in=" + DEFAULT_PRODUCT_DESCRIPTION + "," + UPDATED_PRODUCT_DESCRIPTION);

        // Get all the productList where productDescription equals to UPDATED_PRODUCT_DESCRIPTION
        defaultProductShouldNotBeFound("productDescription.in=" + UPDATED_PRODUCT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByProductDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productDescription is not null
        defaultProductShouldBeFound("productDescription.specified=true");

        // Get all the productList where productDescription is null
        defaultProductShouldNotBeFound("productDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByProductDescriptionContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productDescription contains DEFAULT_PRODUCT_DESCRIPTION
        defaultProductShouldBeFound("productDescription.contains=" + DEFAULT_PRODUCT_DESCRIPTION);

        // Get all the productList where productDescription contains UPDATED_PRODUCT_DESCRIPTION
        defaultProductShouldNotBeFound("productDescription.contains=" + UPDATED_PRODUCT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByProductDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productDescription does not contain DEFAULT_PRODUCT_DESCRIPTION
        defaultProductShouldNotBeFound("productDescription.doesNotContain=" + DEFAULT_PRODUCT_DESCRIPTION);

        // Get all the productList where productDescription does not contain UPDATED_PRODUCT_DESCRIPTION
        defaultProductShouldBeFound("productDescription.doesNotContain=" + UPDATED_PRODUCT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByProductShortDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productShortDescription equals to DEFAULT_PRODUCT_SHORT_DESCRIPTION
        defaultProductShouldBeFound("productShortDescription.equals=" + DEFAULT_PRODUCT_SHORT_DESCRIPTION);

        // Get all the productList where productShortDescription equals to UPDATED_PRODUCT_SHORT_DESCRIPTION
        defaultProductShouldNotBeFound("productShortDescription.equals=" + UPDATED_PRODUCT_SHORT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByProductShortDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productShortDescription in DEFAULT_PRODUCT_SHORT_DESCRIPTION or UPDATED_PRODUCT_SHORT_DESCRIPTION
        defaultProductShouldBeFound(
            "productShortDescription.in=" + DEFAULT_PRODUCT_SHORT_DESCRIPTION + "," + UPDATED_PRODUCT_SHORT_DESCRIPTION
        );

        // Get all the productList where productShortDescription equals to UPDATED_PRODUCT_SHORT_DESCRIPTION
        defaultProductShouldNotBeFound("productShortDescription.in=" + UPDATED_PRODUCT_SHORT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByProductShortDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productShortDescription is not null
        defaultProductShouldBeFound("productShortDescription.specified=true");

        // Get all the productList where productShortDescription is null
        defaultProductShouldNotBeFound("productShortDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByProductShortDescriptionContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productShortDescription contains DEFAULT_PRODUCT_SHORT_DESCRIPTION
        defaultProductShouldBeFound("productShortDescription.contains=" + DEFAULT_PRODUCT_SHORT_DESCRIPTION);

        // Get all the productList where productShortDescription contains UPDATED_PRODUCT_SHORT_DESCRIPTION
        defaultProductShouldNotBeFound("productShortDescription.contains=" + UPDATED_PRODUCT_SHORT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByProductShortDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productShortDescription does not contain DEFAULT_PRODUCT_SHORT_DESCRIPTION
        defaultProductShouldNotBeFound("productShortDescription.doesNotContain=" + DEFAULT_PRODUCT_SHORT_DESCRIPTION);

        // Get all the productList where productShortDescription does not contain UPDATED_PRODUCT_SHORT_DESCRIPTION
        defaultProductShouldBeFound("productShortDescription.doesNotContain=" + UPDATED_PRODUCT_SHORT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByProductQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productQuantity equals to DEFAULT_PRODUCT_QUANTITY
        defaultProductShouldBeFound("productQuantity.equals=" + DEFAULT_PRODUCT_QUANTITY);

        // Get all the productList where productQuantity equals to UPDATED_PRODUCT_QUANTITY
        defaultProductShouldNotBeFound("productQuantity.equals=" + UPDATED_PRODUCT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllProductsByProductQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productQuantity in DEFAULT_PRODUCT_QUANTITY or UPDATED_PRODUCT_QUANTITY
        defaultProductShouldBeFound("productQuantity.in=" + DEFAULT_PRODUCT_QUANTITY + "," + UPDATED_PRODUCT_QUANTITY);

        // Get all the productList where productQuantity equals to UPDATED_PRODUCT_QUANTITY
        defaultProductShouldNotBeFound("productQuantity.in=" + UPDATED_PRODUCT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllProductsByProductQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productQuantity is not null
        defaultProductShouldBeFound("productQuantity.specified=true");

        // Get all the productList where productQuantity is null
        defaultProductShouldNotBeFound("productQuantity.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByProductQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productQuantity is greater than or equal to DEFAULT_PRODUCT_QUANTITY
        defaultProductShouldBeFound("productQuantity.greaterThanOrEqual=" + DEFAULT_PRODUCT_QUANTITY);

        // Get all the productList where productQuantity is greater than or equal to UPDATED_PRODUCT_QUANTITY
        defaultProductShouldNotBeFound("productQuantity.greaterThanOrEqual=" + UPDATED_PRODUCT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllProductsByProductQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productQuantity is less than or equal to DEFAULT_PRODUCT_QUANTITY
        defaultProductShouldBeFound("productQuantity.lessThanOrEqual=" + DEFAULT_PRODUCT_QUANTITY);

        // Get all the productList where productQuantity is less than or equal to SMALLER_PRODUCT_QUANTITY
        defaultProductShouldNotBeFound("productQuantity.lessThanOrEqual=" + SMALLER_PRODUCT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllProductsByProductQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productQuantity is less than DEFAULT_PRODUCT_QUANTITY
        defaultProductShouldNotBeFound("productQuantity.lessThan=" + DEFAULT_PRODUCT_QUANTITY);

        // Get all the productList where productQuantity is less than UPDATED_PRODUCT_QUANTITY
        defaultProductShouldBeFound("productQuantity.lessThan=" + UPDATED_PRODUCT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllProductsByProductQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productQuantity is greater than DEFAULT_PRODUCT_QUANTITY
        defaultProductShouldNotBeFound("productQuantity.greaterThan=" + DEFAULT_PRODUCT_QUANTITY);

        // Get all the productList where productQuantity is greater than SMALLER_PRODUCT_QUANTITY
        defaultProductShouldBeFound("productQuantity.greaterThan=" + SMALLER_PRODUCT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllProductsByProductCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productCode equals to DEFAULT_PRODUCT_CODE
        defaultProductShouldBeFound("productCode.equals=" + DEFAULT_PRODUCT_CODE);

        // Get all the productList where productCode equals to UPDATED_PRODUCT_CODE
        defaultProductShouldNotBeFound("productCode.equals=" + UPDATED_PRODUCT_CODE);
    }

    @Test
    @Transactional
    void getAllProductsByProductCodeIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productCode in DEFAULT_PRODUCT_CODE or UPDATED_PRODUCT_CODE
        defaultProductShouldBeFound("productCode.in=" + DEFAULT_PRODUCT_CODE + "," + UPDATED_PRODUCT_CODE);

        // Get all the productList where productCode equals to UPDATED_PRODUCT_CODE
        defaultProductShouldNotBeFound("productCode.in=" + UPDATED_PRODUCT_CODE);
    }

    @Test
    @Transactional
    void getAllProductsByProductCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productCode is not null
        defaultProductShouldBeFound("productCode.specified=true");

        // Get all the productList where productCode is null
        defaultProductShouldNotBeFound("productCode.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByProductCodeContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productCode contains DEFAULT_PRODUCT_CODE
        defaultProductShouldBeFound("productCode.contains=" + DEFAULT_PRODUCT_CODE);

        // Get all the productList where productCode contains UPDATED_PRODUCT_CODE
        defaultProductShouldNotBeFound("productCode.contains=" + UPDATED_PRODUCT_CODE);
    }

    @Test
    @Transactional
    void getAllProductsByProductCodeNotContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productCode does not contain DEFAULT_PRODUCT_CODE
        defaultProductShouldNotBeFound("productCode.doesNotContain=" + DEFAULT_PRODUCT_CODE);

        // Get all the productList where productCode does not contain UPDATED_PRODUCT_CODE
        defaultProductShouldBeFound("productCode.doesNotContain=" + UPDATED_PRODUCT_CODE);
    }

    @Test
    @Transactional
    void getAllProductsByProductPointRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productPointRating equals to DEFAULT_PRODUCT_POINT_RATING
        defaultProductShouldBeFound("productPointRating.equals=" + DEFAULT_PRODUCT_POINT_RATING);

        // Get all the productList where productPointRating equals to UPDATED_PRODUCT_POINT_RATING
        defaultProductShouldNotBeFound("productPointRating.equals=" + UPDATED_PRODUCT_POINT_RATING);
    }

    @Test
    @Transactional
    void getAllProductsByProductPointRatingIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productPointRating in DEFAULT_PRODUCT_POINT_RATING or UPDATED_PRODUCT_POINT_RATING
        defaultProductShouldBeFound("productPointRating.in=" + DEFAULT_PRODUCT_POINT_RATING + "," + UPDATED_PRODUCT_POINT_RATING);

        // Get all the productList where productPointRating equals to UPDATED_PRODUCT_POINT_RATING
        defaultProductShouldNotBeFound("productPointRating.in=" + UPDATED_PRODUCT_POINT_RATING);
    }

    @Test
    @Transactional
    void getAllProductsByProductPointRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productPointRating is not null
        defaultProductShouldBeFound("productPointRating.specified=true");

        // Get all the productList where productPointRating is null
        defaultProductShouldNotBeFound("productPointRating.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByProductPointRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productPointRating is greater than or equal to DEFAULT_PRODUCT_POINT_RATING
        defaultProductShouldBeFound("productPointRating.greaterThanOrEqual=" + DEFAULT_PRODUCT_POINT_RATING);

        // Get all the productList where productPointRating is greater than or equal to UPDATED_PRODUCT_POINT_RATING
        defaultProductShouldNotBeFound("productPointRating.greaterThanOrEqual=" + UPDATED_PRODUCT_POINT_RATING);
    }

    @Test
    @Transactional
    void getAllProductsByProductPointRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productPointRating is less than or equal to DEFAULT_PRODUCT_POINT_RATING
        defaultProductShouldBeFound("productPointRating.lessThanOrEqual=" + DEFAULT_PRODUCT_POINT_RATING);

        // Get all the productList where productPointRating is less than or equal to SMALLER_PRODUCT_POINT_RATING
        defaultProductShouldNotBeFound("productPointRating.lessThanOrEqual=" + SMALLER_PRODUCT_POINT_RATING);
    }

    @Test
    @Transactional
    void getAllProductsByProductPointRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productPointRating is less than DEFAULT_PRODUCT_POINT_RATING
        defaultProductShouldNotBeFound("productPointRating.lessThan=" + DEFAULT_PRODUCT_POINT_RATING);

        // Get all the productList where productPointRating is less than UPDATED_PRODUCT_POINT_RATING
        defaultProductShouldBeFound("productPointRating.lessThan=" + UPDATED_PRODUCT_POINT_RATING);
    }

    @Test
    @Transactional
    void getAllProductsByProductPointRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where productPointRating is greater than DEFAULT_PRODUCT_POINT_RATING
        defaultProductShouldNotBeFound("productPointRating.greaterThan=" + DEFAULT_PRODUCT_POINT_RATING);

        // Get all the productList where productPointRating is greater than SMALLER_PRODUCT_POINT_RATING
        defaultProductShouldBeFound("productPointRating.greaterThan=" + SMALLER_PRODUCT_POINT_RATING);
    }

    @Test
    @Transactional
    void getAllProductsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where createdBy equals to DEFAULT_CREATED_BY
        defaultProductShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the productList where createdBy equals to UPDATED_CREATED_BY
        defaultProductShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllProductsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultProductShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the productList where createdBy equals to UPDATED_CREATED_BY
        defaultProductShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllProductsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where createdBy is not null
        defaultProductShouldBeFound("createdBy.specified=true");

        // Get all the productList where createdBy is null
        defaultProductShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where createdBy contains DEFAULT_CREATED_BY
        defaultProductShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the productList where createdBy contains UPDATED_CREATED_BY
        defaultProductShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllProductsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where createdBy does not contain DEFAULT_CREATED_BY
        defaultProductShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the productList where createdBy does not contain UPDATED_CREATED_BY
        defaultProductShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllProductsByCreatedTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where createdTime equals to DEFAULT_CREATED_TIME
        defaultProductShouldBeFound("createdTime.equals=" + DEFAULT_CREATED_TIME);

        // Get all the productList where createdTime equals to UPDATED_CREATED_TIME
        defaultProductShouldNotBeFound("createdTime.equals=" + UPDATED_CREATED_TIME);
    }

    @Test
    @Transactional
    void getAllProductsByCreatedTimeIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where createdTime in DEFAULT_CREATED_TIME or UPDATED_CREATED_TIME
        defaultProductShouldBeFound("createdTime.in=" + DEFAULT_CREATED_TIME + "," + UPDATED_CREATED_TIME);

        // Get all the productList where createdTime equals to UPDATED_CREATED_TIME
        defaultProductShouldNotBeFound("createdTime.in=" + UPDATED_CREATED_TIME);
    }

    @Test
    @Transactional
    void getAllProductsByCreatedTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where createdTime is not null
        defaultProductShouldBeFound("createdTime.specified=true");

        // Get all the productList where createdTime is null
        defaultProductShouldNotBeFound("createdTime.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByCreatedTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where createdTime is greater than or equal to DEFAULT_CREATED_TIME
        defaultProductShouldBeFound("createdTime.greaterThanOrEqual=" + DEFAULT_CREATED_TIME);

        // Get all the productList where createdTime is greater than or equal to UPDATED_CREATED_TIME
        defaultProductShouldNotBeFound("createdTime.greaterThanOrEqual=" + UPDATED_CREATED_TIME);
    }

    @Test
    @Transactional
    void getAllProductsByCreatedTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where createdTime is less than or equal to DEFAULT_CREATED_TIME
        defaultProductShouldBeFound("createdTime.lessThanOrEqual=" + DEFAULT_CREATED_TIME);

        // Get all the productList where createdTime is less than or equal to SMALLER_CREATED_TIME
        defaultProductShouldNotBeFound("createdTime.lessThanOrEqual=" + SMALLER_CREATED_TIME);
    }

    @Test
    @Transactional
    void getAllProductsByCreatedTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where createdTime is less than DEFAULT_CREATED_TIME
        defaultProductShouldNotBeFound("createdTime.lessThan=" + DEFAULT_CREATED_TIME);

        // Get all the productList where createdTime is less than UPDATED_CREATED_TIME
        defaultProductShouldBeFound("createdTime.lessThan=" + UPDATED_CREATED_TIME);
    }

    @Test
    @Transactional
    void getAllProductsByCreatedTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where createdTime is greater than DEFAULT_CREATED_TIME
        defaultProductShouldNotBeFound("createdTime.greaterThan=" + DEFAULT_CREATED_TIME);

        // Get all the productList where createdTime is greater than SMALLER_CREATED_TIME
        defaultProductShouldBeFound("createdTime.greaterThan=" + SMALLER_CREATED_TIME);
    }

    @Test
    @Transactional
    void getAllProductsByCategoriesIsEqualToSomething() throws Exception {
        Categories categories;
        if (TestUtil.findAll(em, Categories.class).isEmpty()) {
            productRepository.saveAndFlush(product);
            categories = CategoriesResourceIT.createEntity(em);
        } else {
            categories = TestUtil.findAll(em, Categories.class).get(0);
        }
        em.persist(categories);
        em.flush();
        product.setCategories(categories);
        productRepository.saveAndFlush(product);
        Long categoriesId = categories.getId();

        // Get all the productList where categories equals to categoriesId
        defaultProductShouldBeFound("categoriesId.equals=" + categoriesId);

        // Get all the productList where categories equals to (categoriesId + 1)
        defaultProductShouldNotBeFound("categoriesId.equals=" + (categoriesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductShouldBeFound(String filter) throws Exception {
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].productName").value(hasItem(DEFAULT_PRODUCT_NAME)))
            .andExpect(jsonPath("$.[*].productPrice").value(hasItem(DEFAULT_PRODUCT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].productPriceSale").value(hasItem(DEFAULT_PRODUCT_PRICE_SALE.doubleValue())))
            .andExpect(jsonPath("$.[*].productDescription").value(hasItem(DEFAULT_PRODUCT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].productShortDescription").value(hasItem(DEFAULT_PRODUCT_SHORT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].productQuantity").value(hasItem(DEFAULT_PRODUCT_QUANTITY)))
            .andExpect(jsonPath("$.[*].productCode").value(hasItem(DEFAULT_PRODUCT_CODE)))
            .andExpect(jsonPath("$.[*].productPointRating").value(hasItem(DEFAULT_PRODUCT_POINT_RATING.doubleValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(sameInstant(DEFAULT_CREATED_TIME))));

        // Check, that the count call also returns 1
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductShouldNotBeFound(String filter) throws Exception {
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProduct() throws Exception {
        // Get the product
        restProductMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Update the product
        Product updatedProduct = productRepository.findById(product.getId()).get();
        // Disconnect from session so that the updates on updatedProduct are not directly saved in db
        em.detach(updatedProduct);
        updatedProduct
            .productName(UPDATED_PRODUCT_NAME)
            .productPrice(UPDATED_PRODUCT_PRICE)
            .productPriceSale(UPDATED_PRODUCT_PRICE_SALE)
            .productDescription(UPDATED_PRODUCT_DESCRIPTION)
            .productShortDescription(UPDATED_PRODUCT_SHORT_DESCRIPTION)
            .productQuantity(UPDATED_PRODUCT_QUANTITY)
            .productCode(UPDATED_PRODUCT_CODE)
            .productPointRating(UPDATED_PRODUCT_POINT_RATING)
            .createdBy(UPDATED_CREATED_BY)
            .createdTime(UPDATED_CREATED_TIME);

        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProduct.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProduct))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getProductName()).isEqualTo(UPDATED_PRODUCT_NAME);
        assertThat(testProduct.getProductPrice()).isEqualTo(UPDATED_PRODUCT_PRICE);
        assertThat(testProduct.getProductPriceSale()).isEqualTo(UPDATED_PRODUCT_PRICE_SALE);
        assertThat(testProduct.getProductDescription()).isEqualTo(UPDATED_PRODUCT_DESCRIPTION);
        assertThat(testProduct.getProductShortDescription()).isEqualTo(UPDATED_PRODUCT_SHORT_DESCRIPTION);
        assertThat(testProduct.getProductQuantity()).isEqualTo(UPDATED_PRODUCT_QUANTITY);
        assertThat(testProduct.getProductCode()).isEqualTo(UPDATED_PRODUCT_CODE);
        assertThat(testProduct.getProductPointRating()).isEqualTo(UPDATED_PRODUCT_POINT_RATING);
        assertThat(testProduct.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testProduct.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
    }

    @Test
    @Transactional
    void putNonExistingProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, product.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(product))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(product))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductWithPatch() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Update the product using partial update
        Product partialUpdatedProduct = new Product();
        partialUpdatedProduct.setId(product.getId());

        partialUpdatedProduct.productShortDescription(UPDATED_PRODUCT_SHORT_DESCRIPTION).productQuantity(UPDATED_PRODUCT_QUANTITY);

        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProduct))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getProductName()).isEqualTo(DEFAULT_PRODUCT_NAME);
        assertThat(testProduct.getProductPrice()).isEqualTo(DEFAULT_PRODUCT_PRICE);
        assertThat(testProduct.getProductPriceSale()).isEqualTo(DEFAULT_PRODUCT_PRICE_SALE);
        assertThat(testProduct.getProductDescription()).isEqualTo(DEFAULT_PRODUCT_DESCRIPTION);
        assertThat(testProduct.getProductShortDescription()).isEqualTo(UPDATED_PRODUCT_SHORT_DESCRIPTION);
        assertThat(testProduct.getProductQuantity()).isEqualTo(UPDATED_PRODUCT_QUANTITY);
        assertThat(testProduct.getProductCode()).isEqualTo(DEFAULT_PRODUCT_CODE);
        assertThat(testProduct.getProductPointRating()).isEqualTo(DEFAULT_PRODUCT_POINT_RATING);
        assertThat(testProduct.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testProduct.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
    }

    @Test
    @Transactional
    void fullUpdateProductWithPatch() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Update the product using partial update
        Product partialUpdatedProduct = new Product();
        partialUpdatedProduct.setId(product.getId());

        partialUpdatedProduct
            .productName(UPDATED_PRODUCT_NAME)
            .productPrice(UPDATED_PRODUCT_PRICE)
            .productPriceSale(UPDATED_PRODUCT_PRICE_SALE)
            .productDescription(UPDATED_PRODUCT_DESCRIPTION)
            .productShortDescription(UPDATED_PRODUCT_SHORT_DESCRIPTION)
            .productQuantity(UPDATED_PRODUCT_QUANTITY)
            .productCode(UPDATED_PRODUCT_CODE)
            .productPointRating(UPDATED_PRODUCT_POINT_RATING)
            .createdBy(UPDATED_CREATED_BY)
            .createdTime(UPDATED_CREATED_TIME);

        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProduct))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getProductName()).isEqualTo(UPDATED_PRODUCT_NAME);
        assertThat(testProduct.getProductPrice()).isEqualTo(UPDATED_PRODUCT_PRICE);
        assertThat(testProduct.getProductPriceSale()).isEqualTo(UPDATED_PRODUCT_PRICE_SALE);
        assertThat(testProduct.getProductDescription()).isEqualTo(UPDATED_PRODUCT_DESCRIPTION);
        assertThat(testProduct.getProductShortDescription()).isEqualTo(UPDATED_PRODUCT_SHORT_DESCRIPTION);
        assertThat(testProduct.getProductQuantity()).isEqualTo(UPDATED_PRODUCT_QUANTITY);
        assertThat(testProduct.getProductCode()).isEqualTo(UPDATED_PRODUCT_CODE);
        assertThat(testProduct.getProductPointRating()).isEqualTo(UPDATED_PRODUCT_POINT_RATING);
        assertThat(testProduct.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testProduct.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
    }

    @Test
    @Transactional
    void patchNonExistingProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, product.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(product))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(product))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeDelete = productRepository.findAll().size();

        // Delete the product
        restProductMockMvc
            .perform(delete(ENTITY_API_URL_ID, product.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
