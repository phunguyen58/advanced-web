package com.ptudw.web.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ptudw.web.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GradeReviewTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GradeReview.class);
        GradeReview gradeReview1 = new GradeReview();
        gradeReview1.setId(1L);
        GradeReview gradeReview2 = new GradeReview();
        gradeReview2.setId(gradeReview1.getId());
        assertThat(gradeReview1).isEqualTo(gradeReview2);
        gradeReview2.setId(2L);
        assertThat(gradeReview1).isNotEqualTo(gradeReview2);
        gradeReview1.setId(null);
        assertThat(gradeReview1).isNotEqualTo(gradeReview2);
    }
}
