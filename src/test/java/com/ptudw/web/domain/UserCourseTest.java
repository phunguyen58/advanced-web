package com.ptudw.web.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ptudw.web.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserCourseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserCourse.class);
        UserCourse userCourse1 = new UserCourse();
        userCourse1.setId(1L);
        UserCourse userCourse2 = new UserCourse();
        userCourse2.setId(userCourse1.getId());
        assertThat(userCourse1).isEqualTo(userCourse2);
        userCourse2.setId(2L);
        assertThat(userCourse1).isNotEqualTo(userCourse2);
        userCourse1.setId(null);
        assertThat(userCourse1).isNotEqualTo(userCourse2);
    }
}
