package com.ptudw.web.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ptudw.web.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AssignmentGradeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssignmentGrade.class);
        AssignmentGrade assignmentGrade1 = new AssignmentGrade();
        assignmentGrade1.setId(1L);
        AssignmentGrade assignmentGrade2 = new AssignmentGrade();
        assignmentGrade2.setId(assignmentGrade1.getId());
        assertThat(assignmentGrade1).isEqualTo(assignmentGrade2);
        assignmentGrade2.setId(2L);
        assertThat(assignmentGrade1).isNotEqualTo(assignmentGrade2);
        assignmentGrade1.setId(null);
        assertThat(assignmentGrade1).isNotEqualTo(assignmentGrade2);
    }
}
