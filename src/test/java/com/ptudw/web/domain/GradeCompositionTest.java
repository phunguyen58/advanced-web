package com.ptudw.web.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ptudw.web.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GradeCompositionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GradeComposition.class);
        GradeComposition gradeComposition1 = new GradeComposition();
        gradeComposition1.setId(1L);
        GradeComposition gradeComposition2 = new GradeComposition();
        gradeComposition2.setId(gradeComposition1.getId());
        assertThat(gradeComposition1).isEqualTo(gradeComposition2);
        gradeComposition2.setId(2L);
        assertThat(gradeComposition1).isNotEqualTo(gradeComposition2);
        gradeComposition1.setId(null);
        assertThat(gradeComposition1).isNotEqualTo(gradeComposition2);
    }
}
