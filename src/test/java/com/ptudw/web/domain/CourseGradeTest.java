package com.ptudw.web.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ptudw.web.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CourseGradeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourseGrade.class);
        CourseGrade courseGrade1 = new CourseGrade();
        courseGrade1.setId(1L);
        CourseGrade courseGrade2 = new CourseGrade();
        courseGrade2.setId(courseGrade1.getId());
        assertThat(courseGrade1).isEqualTo(courseGrade2);
        courseGrade2.setId(2L);
        assertThat(courseGrade1).isNotEqualTo(courseGrade2);
        courseGrade1.setId(null);
        assertThat(courseGrade1).isNotEqualTo(courseGrade2);
    }
}
