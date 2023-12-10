package com.ptudw.web.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ptudw.web.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GradeStructureTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GradeStructure.class);
        GradeStructure gradeStructure1 = new GradeStructure();
        gradeStructure1.setId(1L);
        GradeStructure gradeStructure2 = new GradeStructure();
        gradeStructure2.setId(gradeStructure1.getId());
        assertThat(gradeStructure1).isEqualTo(gradeStructure2);
        gradeStructure2.setId(2L);
        assertThat(gradeStructure1).isNotEqualTo(gradeStructure2);
        gradeStructure1.setId(null);
        assertThat(gradeStructure1).isNotEqualTo(gradeStructure2);
    }
}
