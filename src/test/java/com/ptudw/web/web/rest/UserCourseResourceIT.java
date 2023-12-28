package com.ptudw.web.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ptudw.web.IntegrationTest;
import com.ptudw.web.domain.UserCourse;
import com.ptudw.web.repository.UserCourseRepository;
import com.ptudw.web.service.criteria.UserCourseCriteria;
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
 * Integration tests for the {@link UserCourseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserCourseResourceIT {

    private static final Long DEFAULT_COURSE_ID = 1L;
    private static final Long UPDATED_COURSE_ID = 2L;
    private static final Long SMALLER_COURSE_ID = 1L - 1L;

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;
    private static final Long SMALLER_USER_ID = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/user-courses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserCourseRepository userCourseRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserCourseMockMvc;

    private UserCourse userCourse;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserCourse createEntity(EntityManager em) {
        UserCourse userCourse = new UserCourse().courseId(DEFAULT_COURSE_ID).userId(DEFAULT_USER_ID);
        return userCourse;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserCourse createUpdatedEntity(EntityManager em) {
        UserCourse userCourse = new UserCourse().courseId(UPDATED_COURSE_ID).userId(UPDATED_USER_ID);
        return userCourse;
    }

    @BeforeEach
    public void initTest() {
        userCourse = createEntity(em);
    }

    @Test
    @Transactional
    void createUserCourse() throws Exception {
        int databaseSizeBeforeCreate = userCourseRepository.findAll().size();
        // Create the UserCourse
        restUserCourseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userCourse)))
            .andExpect(status().isCreated());

        // Validate the UserCourse in the database
        List<UserCourse> userCourseList = userCourseRepository.findAll();
        assertThat(userCourseList).hasSize(databaseSizeBeforeCreate + 1);
        UserCourse testUserCourse = userCourseList.get(userCourseList.size() - 1);
        assertThat(testUserCourse.getCourseId()).isEqualTo(DEFAULT_COURSE_ID);
        assertThat(testUserCourse.getUserId()).isEqualTo(DEFAULT_USER_ID);
    }

    @Test
    @Transactional
    void createUserCourseWithExistingId() throws Exception {
        // Create the UserCourse with an existing ID
        userCourse.setId(1L);

        int databaseSizeBeforeCreate = userCourseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserCourseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userCourse)))
            .andExpect(status().isBadRequest());

        // Validate the UserCourse in the database
        List<UserCourse> userCourseList = userCourseRepository.findAll();
        assertThat(userCourseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCourseIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = userCourseRepository.findAll().size();
        // set the field null
        userCourse.setCourseId(null);

        // Create the UserCourse, which fails.

        restUserCourseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userCourse)))
            .andExpect(status().isBadRequest());

        List<UserCourse> userCourseList = userCourseRepository.findAll();
        assertThat(userCourseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = userCourseRepository.findAll().size();
        // set the field null
        userCourse.setUserId(null);

        // Create the UserCourse, which fails.

        restUserCourseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userCourse)))
            .andExpect(status().isBadRequest());

        List<UserCourse> userCourseList = userCourseRepository.findAll();
        assertThat(userCourseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserCourses() throws Exception {
        // Initialize the database
        userCourseRepository.saveAndFlush(userCourse);

        // Get all the userCourseList
        restUserCourseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userCourse.getId().intValue())))
            .andExpect(jsonPath("$.[*].courseId").value(hasItem(DEFAULT_COURSE_ID.intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())));
    }

    @Test
    @Transactional
    void getUserCourse() throws Exception {
        // Initialize the database
        userCourseRepository.saveAndFlush(userCourse);

        // Get the userCourse
        restUserCourseMockMvc
            .perform(get(ENTITY_API_URL_ID, userCourse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userCourse.getId().intValue()))
            .andExpect(jsonPath("$.courseId").value(DEFAULT_COURSE_ID.intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()));
    }

    @Test
    @Transactional
    void getUserCoursesByIdFiltering() throws Exception {
        // Initialize the database
        userCourseRepository.saveAndFlush(userCourse);

        Long id = userCourse.getId();

        defaultUserCourseShouldBeFound("id.equals=" + id);
        defaultUserCourseShouldNotBeFound("id.notEquals=" + id);

        defaultUserCourseShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUserCourseShouldNotBeFound("id.greaterThan=" + id);

        defaultUserCourseShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUserCourseShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUserCoursesByCourseIdIsEqualToSomething() throws Exception {
        // Initialize the database
        userCourseRepository.saveAndFlush(userCourse);

        // Get all the userCourseList where courseId equals to DEFAULT_COURSE_ID
        defaultUserCourseShouldBeFound("courseId.equals=" + DEFAULT_COURSE_ID);

        // Get all the userCourseList where courseId equals to UPDATED_COURSE_ID
        defaultUserCourseShouldNotBeFound("courseId.equals=" + UPDATED_COURSE_ID);
    }

    @Test
    @Transactional
    void getAllUserCoursesByCourseIdIsInShouldWork() throws Exception {
        // Initialize the database
        userCourseRepository.saveAndFlush(userCourse);

        // Get all the userCourseList where courseId in DEFAULT_COURSE_ID or UPDATED_COURSE_ID
        defaultUserCourseShouldBeFound("courseId.in=" + DEFAULT_COURSE_ID + "," + UPDATED_COURSE_ID);

        // Get all the userCourseList where courseId equals to UPDATED_COURSE_ID
        defaultUserCourseShouldNotBeFound("courseId.in=" + UPDATED_COURSE_ID);
    }

    @Test
    @Transactional
    void getAllUserCoursesByCourseIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        userCourseRepository.saveAndFlush(userCourse);

        // Get all the userCourseList where courseId is not null
        defaultUserCourseShouldBeFound("courseId.specified=true");

        // Get all the userCourseList where courseId is null
        defaultUserCourseShouldNotBeFound("courseId.specified=false");
    }

    @Test
    @Transactional
    void getAllUserCoursesByCourseIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userCourseRepository.saveAndFlush(userCourse);

        // Get all the userCourseList where courseId is greater than or equal to DEFAULT_COURSE_ID
        defaultUserCourseShouldBeFound("courseId.greaterThanOrEqual=" + DEFAULT_COURSE_ID);

        // Get all the userCourseList where courseId is greater than or equal to UPDATED_COURSE_ID
        defaultUserCourseShouldNotBeFound("courseId.greaterThanOrEqual=" + UPDATED_COURSE_ID);
    }

    @Test
    @Transactional
    void getAllUserCoursesByCourseIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userCourseRepository.saveAndFlush(userCourse);

        // Get all the userCourseList where courseId is less than or equal to DEFAULT_COURSE_ID
        defaultUserCourseShouldBeFound("courseId.lessThanOrEqual=" + DEFAULT_COURSE_ID);

        // Get all the userCourseList where courseId is less than or equal to SMALLER_COURSE_ID
        defaultUserCourseShouldNotBeFound("courseId.lessThanOrEqual=" + SMALLER_COURSE_ID);
    }

    @Test
    @Transactional
    void getAllUserCoursesByCourseIdIsLessThanSomething() throws Exception {
        // Initialize the database
        userCourseRepository.saveAndFlush(userCourse);

        // Get all the userCourseList where courseId is less than DEFAULT_COURSE_ID
        defaultUserCourseShouldNotBeFound("courseId.lessThan=" + DEFAULT_COURSE_ID);

        // Get all the userCourseList where courseId is less than UPDATED_COURSE_ID
        defaultUserCourseShouldBeFound("courseId.lessThan=" + UPDATED_COURSE_ID);
    }

    @Test
    @Transactional
    void getAllUserCoursesByCourseIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        userCourseRepository.saveAndFlush(userCourse);

        // Get all the userCourseList where courseId is greater than DEFAULT_COURSE_ID
        defaultUserCourseShouldNotBeFound("courseId.greaterThan=" + DEFAULT_COURSE_ID);

        // Get all the userCourseList where courseId is greater than SMALLER_COURSE_ID
        defaultUserCourseShouldBeFound("courseId.greaterThan=" + SMALLER_COURSE_ID);
    }

    @Test
    @Transactional
    void getAllUserCoursesByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        userCourseRepository.saveAndFlush(userCourse);

        // Get all the userCourseList where userId equals to DEFAULT_USER_ID
        defaultUserCourseShouldBeFound("userId.equals=" + DEFAULT_USER_ID);

        // Get all the userCourseList where userId equals to UPDATED_USER_ID
        defaultUserCourseShouldNotBeFound("userId.equals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllUserCoursesByUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        userCourseRepository.saveAndFlush(userCourse);

        // Get all the userCourseList where userId in DEFAULT_USER_ID or UPDATED_USER_ID
        defaultUserCourseShouldBeFound("userId.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID);

        // Get all the userCourseList where userId equals to UPDATED_USER_ID
        defaultUserCourseShouldNotBeFound("userId.in=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllUserCoursesByUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        userCourseRepository.saveAndFlush(userCourse);

        // Get all the userCourseList where userId is not null
        defaultUserCourseShouldBeFound("userId.specified=true");

        // Get all the userCourseList where userId is null
        defaultUserCourseShouldNotBeFound("userId.specified=false");
    }

    @Test
    @Transactional
    void getAllUserCoursesByUserIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userCourseRepository.saveAndFlush(userCourse);

        // Get all the userCourseList where userId is greater than or equal to DEFAULT_USER_ID
        defaultUserCourseShouldBeFound("userId.greaterThanOrEqual=" + DEFAULT_USER_ID);

        // Get all the userCourseList where userId is greater than or equal to UPDATED_USER_ID
        defaultUserCourseShouldNotBeFound("userId.greaterThanOrEqual=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllUserCoursesByUserIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        userCourseRepository.saveAndFlush(userCourse);

        // Get all the userCourseList where userId is less than or equal to DEFAULT_USER_ID
        defaultUserCourseShouldBeFound("userId.lessThanOrEqual=" + DEFAULT_USER_ID);

        // Get all the userCourseList where userId is less than or equal to SMALLER_USER_ID
        defaultUserCourseShouldNotBeFound("userId.lessThanOrEqual=" + SMALLER_USER_ID);
    }

    @Test
    @Transactional
    void getAllUserCoursesByUserIdIsLessThanSomething() throws Exception {
        // Initialize the database
        userCourseRepository.saveAndFlush(userCourse);

        // Get all the userCourseList where userId is less than DEFAULT_USER_ID
        defaultUserCourseShouldNotBeFound("userId.lessThan=" + DEFAULT_USER_ID);

        // Get all the userCourseList where userId is less than UPDATED_USER_ID
        defaultUserCourseShouldBeFound("userId.lessThan=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllUserCoursesByUserIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        userCourseRepository.saveAndFlush(userCourse);

        // Get all the userCourseList where userId is greater than DEFAULT_USER_ID
        defaultUserCourseShouldNotBeFound("userId.greaterThan=" + DEFAULT_USER_ID);

        // Get all the userCourseList where userId is greater than SMALLER_USER_ID
        defaultUserCourseShouldBeFound("userId.greaterThan=" + SMALLER_USER_ID);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserCourseShouldBeFound(String filter) throws Exception {
        restUserCourseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userCourse.getId().intValue())))
            .andExpect(jsonPath("$.[*].courseId").value(hasItem(DEFAULT_COURSE_ID.intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())));

        // Check, that the count call also returns 1
        restUserCourseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserCourseShouldNotBeFound(String filter) throws Exception {
        restUserCourseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserCourseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUserCourse() throws Exception {
        // Get the userCourse
        restUserCourseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserCourse() throws Exception {
        // Initialize the database
        userCourseRepository.saveAndFlush(userCourse);

        int databaseSizeBeforeUpdate = userCourseRepository.findAll().size();

        // Update the userCourse
        UserCourse updatedUserCourse = userCourseRepository.findById(userCourse.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserCourse are not directly saved in db
        em.detach(updatedUserCourse);
        updatedUserCourse.courseId(UPDATED_COURSE_ID).userId(UPDATED_USER_ID);

        restUserCourseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUserCourse.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUserCourse))
            )
            .andExpect(status().isOk());

        // Validate the UserCourse in the database
        List<UserCourse> userCourseList = userCourseRepository.findAll();
        assertThat(userCourseList).hasSize(databaseSizeBeforeUpdate);
        UserCourse testUserCourse = userCourseList.get(userCourseList.size() - 1);
        assertThat(testUserCourse.getCourseId()).isEqualTo(UPDATED_COURSE_ID);
        assertThat(testUserCourse.getUserId()).isEqualTo(UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void putNonExistingUserCourse() throws Exception {
        int databaseSizeBeforeUpdate = userCourseRepository.findAll().size();
        userCourse.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserCourseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userCourse.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userCourse))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserCourse in the database
        List<UserCourse> userCourseList = userCourseRepository.findAll();
        assertThat(userCourseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserCourse() throws Exception {
        int databaseSizeBeforeUpdate = userCourseRepository.findAll().size();
        userCourse.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserCourseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userCourse))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserCourse in the database
        List<UserCourse> userCourseList = userCourseRepository.findAll();
        assertThat(userCourseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserCourse() throws Exception {
        int databaseSizeBeforeUpdate = userCourseRepository.findAll().size();
        userCourse.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserCourseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userCourse)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserCourse in the database
        List<UserCourse> userCourseList = userCourseRepository.findAll();
        assertThat(userCourseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserCourseWithPatch() throws Exception {
        // Initialize the database
        userCourseRepository.saveAndFlush(userCourse);

        int databaseSizeBeforeUpdate = userCourseRepository.findAll().size();

        // Update the userCourse using partial update
        UserCourse partialUpdatedUserCourse = new UserCourse();
        partialUpdatedUserCourse.setId(userCourse.getId());

        partialUpdatedUserCourse.userId(UPDATED_USER_ID);

        restUserCourseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserCourse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserCourse))
            )
            .andExpect(status().isOk());

        // Validate the UserCourse in the database
        List<UserCourse> userCourseList = userCourseRepository.findAll();
        assertThat(userCourseList).hasSize(databaseSizeBeforeUpdate);
        UserCourse testUserCourse = userCourseList.get(userCourseList.size() - 1);
        assertThat(testUserCourse.getCourseId()).isEqualTo(DEFAULT_COURSE_ID);
        assertThat(testUserCourse.getUserId()).isEqualTo(UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void fullUpdateUserCourseWithPatch() throws Exception {
        // Initialize the database
        userCourseRepository.saveAndFlush(userCourse);

        int databaseSizeBeforeUpdate = userCourseRepository.findAll().size();

        // Update the userCourse using partial update
        UserCourse partialUpdatedUserCourse = new UserCourse();
        partialUpdatedUserCourse.setId(userCourse.getId());

        partialUpdatedUserCourse.courseId(UPDATED_COURSE_ID).userId(UPDATED_USER_ID);

        restUserCourseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserCourse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserCourse))
            )
            .andExpect(status().isOk());

        // Validate the UserCourse in the database
        List<UserCourse> userCourseList = userCourseRepository.findAll();
        assertThat(userCourseList).hasSize(databaseSizeBeforeUpdate);
        UserCourse testUserCourse = userCourseList.get(userCourseList.size() - 1);
        assertThat(testUserCourse.getCourseId()).isEqualTo(UPDATED_COURSE_ID);
        assertThat(testUserCourse.getUserId()).isEqualTo(UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void patchNonExistingUserCourse() throws Exception {
        int databaseSizeBeforeUpdate = userCourseRepository.findAll().size();
        userCourse.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserCourseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userCourse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userCourse))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserCourse in the database
        List<UserCourse> userCourseList = userCourseRepository.findAll();
        assertThat(userCourseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserCourse() throws Exception {
        int databaseSizeBeforeUpdate = userCourseRepository.findAll().size();
        userCourse.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserCourseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userCourse))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserCourse in the database
        List<UserCourse> userCourseList = userCourseRepository.findAll();
        assertThat(userCourseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserCourse() throws Exception {
        int databaseSizeBeforeUpdate = userCourseRepository.findAll().size();
        userCourse.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserCourseMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(userCourse))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserCourse in the database
        List<UserCourse> userCourseList = userCourseRepository.findAll();
        assertThat(userCourseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserCourse() throws Exception {
        // Initialize the database
        userCourseRepository.saveAndFlush(userCourse);

        int databaseSizeBeforeDelete = userCourseRepository.findAll().size();

        // Delete the userCourse
        restUserCourseMockMvc
            .perform(delete(ENTITY_API_URL_ID, userCourse.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserCourse> userCourseList = userCourseRepository.findAll();
        assertThat(userCourseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
