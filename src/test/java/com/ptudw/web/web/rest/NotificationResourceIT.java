package com.ptudw.web.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ptudw.web.IntegrationTest;
import com.ptudw.web.domain.Notification;
import com.ptudw.web.repository.NotificationRepository;
import com.ptudw.web.service.criteria.NotificationCriteria;
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
 * Integration tests for the {@link NotificationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NotificationResourceIT {

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final String DEFAULT_TOPIC = "AAAAAAAAAA";
    private static final String UPDATED_TOPIC = "BBBBBBBBBB";

    private static final String DEFAULT_RECEIVERS = "AAAAAAAAAA";
    private static final String UPDATED_RECEIVERS = "BBBBBBBBBB";

    private static final String DEFAULT_SENDER = "AAAAAAAAAA";
    private static final String UPDATED_SENDER = "BBBBBBBBBB";

    private static final String DEFAULT_ROLE = "AAAAAAAAAA";
    private static final String UPDATED_ROLE = "BBBBBBBBBB";

    private static final String DEFAULT_LINK = "AAAAAAAAAA";
    private static final String UPDATED_LINK = "BBBBBBBBBB";

    private static final Long DEFAULT_GRADE_REVIEW_ID = 1L;
    private static final Long UPDATED_GRADE_REVIEW_ID = 2L;
    private static final Long SMALLER_GRADE_REVIEW_ID = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/notifications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNotificationMockMvc;

    private Notification notification;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notification createEntity(EntityManager em) {
        Notification notification = new Notification()
            .message(DEFAULT_MESSAGE)
            .topic(DEFAULT_TOPIC)
            .receivers(DEFAULT_RECEIVERS)
            .sender(DEFAULT_SENDER)
            .role(DEFAULT_ROLE)
            .link(DEFAULT_LINK)
            .gradeReviewId(DEFAULT_GRADE_REVIEW_ID);
        return notification;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notification createUpdatedEntity(EntityManager em) {
        Notification notification = new Notification()
            .message(UPDATED_MESSAGE)
            .topic(UPDATED_TOPIC)
            .receivers(UPDATED_RECEIVERS)
            .sender(UPDATED_SENDER)
            .role(UPDATED_ROLE)
            .link(UPDATED_LINK)
            .gradeReviewId(UPDATED_GRADE_REVIEW_ID);
        return notification;
    }

    @BeforeEach
    public void initTest() {
        notification = createEntity(em);
    }

    @Test
    @Transactional
    void createNotification() throws Exception {
        int databaseSizeBeforeCreate = notificationRepository.findAll().size();
        // Create the Notification
        restNotificationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(notification)))
            .andExpect(status().isCreated());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeCreate + 1);
        Notification testNotification = notificationList.get(notificationList.size() - 1);
        assertThat(testNotification.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testNotification.getTopic()).isEqualTo(DEFAULT_TOPIC);
        assertThat(testNotification.getReceivers()).isEqualTo(DEFAULT_RECEIVERS);
        assertThat(testNotification.getSender()).isEqualTo(DEFAULT_SENDER);
        assertThat(testNotification.getRole()).isEqualTo(DEFAULT_ROLE);
        assertThat(testNotification.getLink()).isEqualTo(DEFAULT_LINK);
        assertThat(testNotification.getGradeReviewId()).isEqualTo(DEFAULT_GRADE_REVIEW_ID);
    }

    @Test
    @Transactional
    void createNotificationWithExistingId() throws Exception {
        // Create the Notification with an existing ID
        notification.setId(1L);

        int databaseSizeBeforeCreate = notificationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotificationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(notification)))
            .andExpect(status().isBadRequest());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllNotifications() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList
        restNotificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notification.getId().intValue())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].topic").value(hasItem(DEFAULT_TOPIC)))
            .andExpect(jsonPath("$.[*].receivers").value(hasItem(DEFAULT_RECEIVERS)))
            .andExpect(jsonPath("$.[*].sender").value(hasItem(DEFAULT_SENDER)))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE)))
            .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK)))
            .andExpect(jsonPath("$.[*].gradeReviewId").value(hasItem(DEFAULT_GRADE_REVIEW_ID.intValue())));
    }

    @Test
    @Transactional
    void getNotification() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get the notification
        restNotificationMockMvc
            .perform(get(ENTITY_API_URL_ID, notification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(notification.getId().intValue()))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE))
            .andExpect(jsonPath("$.topic").value(DEFAULT_TOPIC))
            .andExpect(jsonPath("$.receivers").value(DEFAULT_RECEIVERS))
            .andExpect(jsonPath("$.sender").value(DEFAULT_SENDER))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE))
            .andExpect(jsonPath("$.link").value(DEFAULT_LINK))
            .andExpect(jsonPath("$.gradeReviewId").value(DEFAULT_GRADE_REVIEW_ID.intValue()));
    }

    @Test
    @Transactional
    void getNotificationsByIdFiltering() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        Long id = notification.getId();

        defaultNotificationShouldBeFound("id.equals=" + id);
        defaultNotificationShouldNotBeFound("id.notEquals=" + id);

        defaultNotificationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultNotificationShouldNotBeFound("id.greaterThan=" + id);

        defaultNotificationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultNotificationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNotificationsByMessageIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where message equals to DEFAULT_MESSAGE
        defaultNotificationShouldBeFound("message.equals=" + DEFAULT_MESSAGE);

        // Get all the notificationList where message equals to UPDATED_MESSAGE
        defaultNotificationShouldNotBeFound("message.equals=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void getAllNotificationsByMessageIsInShouldWork() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where message in DEFAULT_MESSAGE or UPDATED_MESSAGE
        defaultNotificationShouldBeFound("message.in=" + DEFAULT_MESSAGE + "," + UPDATED_MESSAGE);

        // Get all the notificationList where message equals to UPDATED_MESSAGE
        defaultNotificationShouldNotBeFound("message.in=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void getAllNotificationsByMessageIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where message is not null
        defaultNotificationShouldBeFound("message.specified=true");

        // Get all the notificationList where message is null
        defaultNotificationShouldNotBeFound("message.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationsByMessageContainsSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where message contains DEFAULT_MESSAGE
        defaultNotificationShouldBeFound("message.contains=" + DEFAULT_MESSAGE);

        // Get all the notificationList where message contains UPDATED_MESSAGE
        defaultNotificationShouldNotBeFound("message.contains=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void getAllNotificationsByMessageNotContainsSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where message does not contain DEFAULT_MESSAGE
        defaultNotificationShouldNotBeFound("message.doesNotContain=" + DEFAULT_MESSAGE);

        // Get all the notificationList where message does not contain UPDATED_MESSAGE
        defaultNotificationShouldBeFound("message.doesNotContain=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void getAllNotificationsByTopicIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where topic equals to DEFAULT_TOPIC
        defaultNotificationShouldBeFound("topic.equals=" + DEFAULT_TOPIC);

        // Get all the notificationList where topic equals to UPDATED_TOPIC
        defaultNotificationShouldNotBeFound("topic.equals=" + UPDATED_TOPIC);
    }

    @Test
    @Transactional
    void getAllNotificationsByTopicIsInShouldWork() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where topic in DEFAULT_TOPIC or UPDATED_TOPIC
        defaultNotificationShouldBeFound("topic.in=" + DEFAULT_TOPIC + "," + UPDATED_TOPIC);

        // Get all the notificationList where topic equals to UPDATED_TOPIC
        defaultNotificationShouldNotBeFound("topic.in=" + UPDATED_TOPIC);
    }

    @Test
    @Transactional
    void getAllNotificationsByTopicIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where topic is not null
        defaultNotificationShouldBeFound("topic.specified=true");

        // Get all the notificationList where topic is null
        defaultNotificationShouldNotBeFound("topic.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationsByTopicContainsSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where topic contains DEFAULT_TOPIC
        defaultNotificationShouldBeFound("topic.contains=" + DEFAULT_TOPIC);

        // Get all the notificationList where topic contains UPDATED_TOPIC
        defaultNotificationShouldNotBeFound("topic.contains=" + UPDATED_TOPIC);
    }

    @Test
    @Transactional
    void getAllNotificationsByTopicNotContainsSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where topic does not contain DEFAULT_TOPIC
        defaultNotificationShouldNotBeFound("topic.doesNotContain=" + DEFAULT_TOPIC);

        // Get all the notificationList where topic does not contain UPDATED_TOPIC
        defaultNotificationShouldBeFound("topic.doesNotContain=" + UPDATED_TOPIC);
    }

    @Test
    @Transactional
    void getAllNotificationsByReceiversIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where receivers equals to DEFAULT_RECEIVERS
        defaultNotificationShouldBeFound("receivers.equals=" + DEFAULT_RECEIVERS);

        // Get all the notificationList where receivers equals to UPDATED_RECEIVERS
        defaultNotificationShouldNotBeFound("receivers.equals=" + UPDATED_RECEIVERS);
    }

    @Test
    @Transactional
    void getAllNotificationsByReceiversIsInShouldWork() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where receivers in DEFAULT_RECEIVERS or UPDATED_RECEIVERS
        defaultNotificationShouldBeFound("receivers.in=" + DEFAULT_RECEIVERS + "," + UPDATED_RECEIVERS);

        // Get all the notificationList where receivers equals to UPDATED_RECEIVERS
        defaultNotificationShouldNotBeFound("receivers.in=" + UPDATED_RECEIVERS);
    }

    @Test
    @Transactional
    void getAllNotificationsByReceiversIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where receivers is not null
        defaultNotificationShouldBeFound("receivers.specified=true");

        // Get all the notificationList where receivers is null
        defaultNotificationShouldNotBeFound("receivers.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationsByReceiversContainsSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where receivers contains DEFAULT_RECEIVERS
        defaultNotificationShouldBeFound("receivers.contains=" + DEFAULT_RECEIVERS);

        // Get all the notificationList where receivers contains UPDATED_RECEIVERS
        defaultNotificationShouldNotBeFound("receivers.contains=" + UPDATED_RECEIVERS);
    }

    @Test
    @Transactional
    void getAllNotificationsByReceiversNotContainsSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where receivers does not contain DEFAULT_RECEIVERS
        defaultNotificationShouldNotBeFound("receivers.doesNotContain=" + DEFAULT_RECEIVERS);

        // Get all the notificationList where receivers does not contain UPDATED_RECEIVERS
        defaultNotificationShouldBeFound("receivers.doesNotContain=" + UPDATED_RECEIVERS);
    }

    @Test
    @Transactional
    void getAllNotificationsBySenderIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where sender equals to DEFAULT_SENDER
        defaultNotificationShouldBeFound("sender.equals=" + DEFAULT_SENDER);

        // Get all the notificationList where sender equals to UPDATED_SENDER
        defaultNotificationShouldNotBeFound("sender.equals=" + UPDATED_SENDER);
    }

    @Test
    @Transactional
    void getAllNotificationsBySenderIsInShouldWork() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where sender in DEFAULT_SENDER or UPDATED_SENDER
        defaultNotificationShouldBeFound("sender.in=" + DEFAULT_SENDER + "," + UPDATED_SENDER);

        // Get all the notificationList where sender equals to UPDATED_SENDER
        defaultNotificationShouldNotBeFound("sender.in=" + UPDATED_SENDER);
    }

    @Test
    @Transactional
    void getAllNotificationsBySenderIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where sender is not null
        defaultNotificationShouldBeFound("sender.specified=true");

        // Get all the notificationList where sender is null
        defaultNotificationShouldNotBeFound("sender.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationsBySenderContainsSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where sender contains DEFAULT_SENDER
        defaultNotificationShouldBeFound("sender.contains=" + DEFAULT_SENDER);

        // Get all the notificationList where sender contains UPDATED_SENDER
        defaultNotificationShouldNotBeFound("sender.contains=" + UPDATED_SENDER);
    }

    @Test
    @Transactional
    void getAllNotificationsBySenderNotContainsSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where sender does not contain DEFAULT_SENDER
        defaultNotificationShouldNotBeFound("sender.doesNotContain=" + DEFAULT_SENDER);

        // Get all the notificationList where sender does not contain UPDATED_SENDER
        defaultNotificationShouldBeFound("sender.doesNotContain=" + UPDATED_SENDER);
    }

    @Test
    @Transactional
    void getAllNotificationsByRoleIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where role equals to DEFAULT_ROLE
        defaultNotificationShouldBeFound("role.equals=" + DEFAULT_ROLE);

        // Get all the notificationList where role equals to UPDATED_ROLE
        defaultNotificationShouldNotBeFound("role.equals=" + UPDATED_ROLE);
    }

    @Test
    @Transactional
    void getAllNotificationsByRoleIsInShouldWork() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where role in DEFAULT_ROLE or UPDATED_ROLE
        defaultNotificationShouldBeFound("role.in=" + DEFAULT_ROLE + "," + UPDATED_ROLE);

        // Get all the notificationList where role equals to UPDATED_ROLE
        defaultNotificationShouldNotBeFound("role.in=" + UPDATED_ROLE);
    }

    @Test
    @Transactional
    void getAllNotificationsByRoleIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where role is not null
        defaultNotificationShouldBeFound("role.specified=true");

        // Get all the notificationList where role is null
        defaultNotificationShouldNotBeFound("role.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationsByRoleContainsSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where role contains DEFAULT_ROLE
        defaultNotificationShouldBeFound("role.contains=" + DEFAULT_ROLE);

        // Get all the notificationList where role contains UPDATED_ROLE
        defaultNotificationShouldNotBeFound("role.contains=" + UPDATED_ROLE);
    }

    @Test
    @Transactional
    void getAllNotificationsByRoleNotContainsSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where role does not contain DEFAULT_ROLE
        defaultNotificationShouldNotBeFound("role.doesNotContain=" + DEFAULT_ROLE);

        // Get all the notificationList where role does not contain UPDATED_ROLE
        defaultNotificationShouldBeFound("role.doesNotContain=" + UPDATED_ROLE);
    }

    @Test
    @Transactional
    void getAllNotificationsByLinkIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where link equals to DEFAULT_LINK
        defaultNotificationShouldBeFound("link.equals=" + DEFAULT_LINK);

        // Get all the notificationList where link equals to UPDATED_LINK
        defaultNotificationShouldNotBeFound("link.equals=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    void getAllNotificationsByLinkIsInShouldWork() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where link in DEFAULT_LINK or UPDATED_LINK
        defaultNotificationShouldBeFound("link.in=" + DEFAULT_LINK + "," + UPDATED_LINK);

        // Get all the notificationList where link equals to UPDATED_LINK
        defaultNotificationShouldNotBeFound("link.in=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    void getAllNotificationsByLinkIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where link is not null
        defaultNotificationShouldBeFound("link.specified=true");

        // Get all the notificationList where link is null
        defaultNotificationShouldNotBeFound("link.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationsByLinkContainsSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where link contains DEFAULT_LINK
        defaultNotificationShouldBeFound("link.contains=" + DEFAULT_LINK);

        // Get all the notificationList where link contains UPDATED_LINK
        defaultNotificationShouldNotBeFound("link.contains=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    void getAllNotificationsByLinkNotContainsSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where link does not contain DEFAULT_LINK
        defaultNotificationShouldNotBeFound("link.doesNotContain=" + DEFAULT_LINK);

        // Get all the notificationList where link does not contain UPDATED_LINK
        defaultNotificationShouldBeFound("link.doesNotContain=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    void getAllNotificationsByGradeReviewIdIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where gradeReviewId equals to DEFAULT_GRADE_REVIEW_ID
        defaultNotificationShouldBeFound("gradeReviewId.equals=" + DEFAULT_GRADE_REVIEW_ID);

        // Get all the notificationList where gradeReviewId equals to UPDATED_GRADE_REVIEW_ID
        defaultNotificationShouldNotBeFound("gradeReviewId.equals=" + UPDATED_GRADE_REVIEW_ID);
    }

    @Test
    @Transactional
    void getAllNotificationsByGradeReviewIdIsInShouldWork() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where gradeReviewId in DEFAULT_GRADE_REVIEW_ID or UPDATED_GRADE_REVIEW_ID
        defaultNotificationShouldBeFound("gradeReviewId.in=" + DEFAULT_GRADE_REVIEW_ID + "," + UPDATED_GRADE_REVIEW_ID);

        // Get all the notificationList where gradeReviewId equals to UPDATED_GRADE_REVIEW_ID
        defaultNotificationShouldNotBeFound("gradeReviewId.in=" + UPDATED_GRADE_REVIEW_ID);
    }

    @Test
    @Transactional
    void getAllNotificationsByGradeReviewIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where gradeReviewId is not null
        defaultNotificationShouldBeFound("gradeReviewId.specified=true");

        // Get all the notificationList where gradeReviewId is null
        defaultNotificationShouldNotBeFound("gradeReviewId.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationsByGradeReviewIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where gradeReviewId is greater than or equal to DEFAULT_GRADE_REVIEW_ID
        defaultNotificationShouldBeFound("gradeReviewId.greaterThanOrEqual=" + DEFAULT_GRADE_REVIEW_ID);

        // Get all the notificationList where gradeReviewId is greater than or equal to UPDATED_GRADE_REVIEW_ID
        defaultNotificationShouldNotBeFound("gradeReviewId.greaterThanOrEqual=" + UPDATED_GRADE_REVIEW_ID);
    }

    @Test
    @Transactional
    void getAllNotificationsByGradeReviewIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where gradeReviewId is less than or equal to DEFAULT_GRADE_REVIEW_ID
        defaultNotificationShouldBeFound("gradeReviewId.lessThanOrEqual=" + DEFAULT_GRADE_REVIEW_ID);

        // Get all the notificationList where gradeReviewId is less than or equal to SMALLER_GRADE_REVIEW_ID
        defaultNotificationShouldNotBeFound("gradeReviewId.lessThanOrEqual=" + SMALLER_GRADE_REVIEW_ID);
    }

    @Test
    @Transactional
    void getAllNotificationsByGradeReviewIdIsLessThanSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where gradeReviewId is less than DEFAULT_GRADE_REVIEW_ID
        defaultNotificationShouldNotBeFound("gradeReviewId.lessThan=" + DEFAULT_GRADE_REVIEW_ID);

        // Get all the notificationList where gradeReviewId is less than UPDATED_GRADE_REVIEW_ID
        defaultNotificationShouldBeFound("gradeReviewId.lessThan=" + UPDATED_GRADE_REVIEW_ID);
    }

    @Test
    @Transactional
    void getAllNotificationsByGradeReviewIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where gradeReviewId is greater than DEFAULT_GRADE_REVIEW_ID
        defaultNotificationShouldNotBeFound("gradeReviewId.greaterThan=" + DEFAULT_GRADE_REVIEW_ID);

        // Get all the notificationList where gradeReviewId is greater than SMALLER_GRADE_REVIEW_ID
        defaultNotificationShouldBeFound("gradeReviewId.greaterThan=" + SMALLER_GRADE_REVIEW_ID);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNotificationShouldBeFound(String filter) throws Exception {
        restNotificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notification.getId().intValue())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].topic").value(hasItem(DEFAULT_TOPIC)))
            .andExpect(jsonPath("$.[*].receivers").value(hasItem(DEFAULT_RECEIVERS)))
            .andExpect(jsonPath("$.[*].sender").value(hasItem(DEFAULT_SENDER)))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE)))
            .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK)))
            .andExpect(jsonPath("$.[*].gradeReviewId").value(hasItem(DEFAULT_GRADE_REVIEW_ID.intValue())));

        // Check, that the count call also returns 1
        restNotificationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNotificationShouldNotBeFound(String filter) throws Exception {
        restNotificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNotificationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNotification() throws Exception {
        // Get the notification
        restNotificationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNotification() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        int databaseSizeBeforeUpdate = notificationRepository.findAll().size();

        // Update the notification
        Notification updatedNotification = notificationRepository.findById(notification.getId()).get();
        // Disconnect from session so that the updates on updatedNotification are not directly saved in db
        em.detach(updatedNotification);
        updatedNotification
            .message(UPDATED_MESSAGE)
            .topic(UPDATED_TOPIC)
            .receivers(UPDATED_RECEIVERS)
            .sender(UPDATED_SENDER)
            .role(UPDATED_ROLE)
            .link(UPDATED_LINK)
            .gradeReviewId(UPDATED_GRADE_REVIEW_ID);

        restNotificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNotification.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedNotification))
            )
            .andExpect(status().isOk());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
        Notification testNotification = notificationList.get(notificationList.size() - 1);
        assertThat(testNotification.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testNotification.getTopic()).isEqualTo(UPDATED_TOPIC);
        assertThat(testNotification.getReceivers()).isEqualTo(UPDATED_RECEIVERS);
        assertThat(testNotification.getSender()).isEqualTo(UPDATED_SENDER);
        assertThat(testNotification.getRole()).isEqualTo(UPDATED_ROLE);
        assertThat(testNotification.getLink()).isEqualTo(UPDATED_LINK);
        assertThat(testNotification.getGradeReviewId()).isEqualTo(UPDATED_GRADE_REVIEW_ID);
    }

    @Test
    @Transactional
    void putNonExistingNotification() throws Exception {
        int databaseSizeBeforeUpdate = notificationRepository.findAll().size();
        notification.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, notification.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notification))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNotification() throws Exception {
        int databaseSizeBeforeUpdate = notificationRepository.findAll().size();
        notification.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notification))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNotification() throws Exception {
        int databaseSizeBeforeUpdate = notificationRepository.findAll().size();
        notification.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(notification)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNotificationWithPatch() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        int databaseSizeBeforeUpdate = notificationRepository.findAll().size();

        // Update the notification using partial update
        Notification partialUpdatedNotification = new Notification();
        partialUpdatedNotification.setId(notification.getId());

        partialUpdatedNotification
            .receivers(UPDATED_RECEIVERS)
            .sender(UPDATED_SENDER)
            .role(UPDATED_ROLE)
            .link(UPDATED_LINK)
            .gradeReviewId(UPDATED_GRADE_REVIEW_ID);

        restNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotification.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNotification))
            )
            .andExpect(status().isOk());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
        Notification testNotification = notificationList.get(notificationList.size() - 1);
        assertThat(testNotification.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testNotification.getTopic()).isEqualTo(DEFAULT_TOPIC);
        assertThat(testNotification.getReceivers()).isEqualTo(UPDATED_RECEIVERS);
        assertThat(testNotification.getSender()).isEqualTo(UPDATED_SENDER);
        assertThat(testNotification.getRole()).isEqualTo(UPDATED_ROLE);
        assertThat(testNotification.getLink()).isEqualTo(UPDATED_LINK);
        assertThat(testNotification.getGradeReviewId()).isEqualTo(UPDATED_GRADE_REVIEW_ID);
    }

    @Test
    @Transactional
    void fullUpdateNotificationWithPatch() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        int databaseSizeBeforeUpdate = notificationRepository.findAll().size();

        // Update the notification using partial update
        Notification partialUpdatedNotification = new Notification();
        partialUpdatedNotification.setId(notification.getId());

        partialUpdatedNotification
            .message(UPDATED_MESSAGE)
            .topic(UPDATED_TOPIC)
            .receivers(UPDATED_RECEIVERS)
            .sender(UPDATED_SENDER)
            .role(UPDATED_ROLE)
            .link(UPDATED_LINK)
            .gradeReviewId(UPDATED_GRADE_REVIEW_ID);

        restNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotification.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNotification))
            )
            .andExpect(status().isOk());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
        Notification testNotification = notificationList.get(notificationList.size() - 1);
        assertThat(testNotification.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testNotification.getTopic()).isEqualTo(UPDATED_TOPIC);
        assertThat(testNotification.getReceivers()).isEqualTo(UPDATED_RECEIVERS);
        assertThat(testNotification.getSender()).isEqualTo(UPDATED_SENDER);
        assertThat(testNotification.getRole()).isEqualTo(UPDATED_ROLE);
        assertThat(testNotification.getLink()).isEqualTo(UPDATED_LINK);
        assertThat(testNotification.getGradeReviewId()).isEqualTo(UPDATED_GRADE_REVIEW_ID);
    }

    @Test
    @Transactional
    void patchNonExistingNotification() throws Exception {
        int databaseSizeBeforeUpdate = notificationRepository.findAll().size();
        notification.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, notification.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(notification))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNotification() throws Exception {
        int databaseSizeBeforeUpdate = notificationRepository.findAll().size();
        notification.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(notification))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNotification() throws Exception {
        int databaseSizeBeforeUpdate = notificationRepository.findAll().size();
        notification.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(notification))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNotification() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        int databaseSizeBeforeDelete = notificationRepository.findAll().size();

        // Delete the notification
        restNotificationMockMvc
            .perform(delete(ENTITY_API_URL_ID, notification.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
