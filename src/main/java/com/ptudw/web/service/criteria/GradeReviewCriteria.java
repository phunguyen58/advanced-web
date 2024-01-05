package com.ptudw.web.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.ptudw.web.domain.GradeReview} entity. This class is used
 * in {@link com.ptudw.web.web.rest.GradeReviewResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /grade-reviews?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GradeReviewCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter gradeCompositionId;

    private StringFilter studentId;

    private LongFilter courseId;

    private LongFilter reviewerId;

    private LongFilter assigmentId;

    private LongFilter assimentGradeId;

    private LongFilter currentGrade;

    private LongFilter expectationGrade;

    private StringFilter studentExplanation;

    private StringFilter teacherComment;

    private BooleanFilter isFinal;

    private Boolean distinct;

    public GradeReviewCriteria() {}

    public GradeReviewCriteria(GradeReviewCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.gradeCompositionId = other.gradeCompositionId == null ? null : other.gradeCompositionId.copy();
        this.studentId = other.studentId == null ? null : other.studentId.copy();
        this.courseId = other.courseId == null ? null : other.courseId.copy();
        this.reviewerId = other.reviewerId == null ? null : other.reviewerId.copy();
        this.assigmentId = other.assigmentId == null ? null : other.assigmentId.copy();
        this.assimentGradeId = other.assimentGradeId == null ? null : other.assimentGradeId.copy();
        this.currentGrade = other.currentGrade == null ? null : other.currentGrade.copy();
        this.expectationGrade = other.expectationGrade == null ? null : other.expectationGrade.copy();
        this.studentExplanation = other.studentExplanation == null ? null : other.studentExplanation.copy();
        this.teacherComment = other.teacherComment == null ? null : other.teacherComment.copy();
        this.isFinal = other.isFinal == null ? null : other.isFinal.copy();
        this.distinct = other.distinct;
    }

    @Override
    public GradeReviewCriteria copy() {
        return new GradeReviewCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getGradeCompositionId() {
        return gradeCompositionId;
    }

    public LongFilter gradeCompositionId() {
        if (gradeCompositionId == null) {
            gradeCompositionId = new LongFilter();
        }
        return gradeCompositionId;
    }

    public void setGradeCompositionId(LongFilter gradeCompositionId) {
        this.gradeCompositionId = gradeCompositionId;
    }

    public StringFilter getStudentId() {
        return studentId;
    }

    public StringFilter studentId() {
        if (studentId == null) {
            studentId = new StringFilter();
        }
        return studentId;
    }

    public void setStudentId(StringFilter studentId) {
        this.studentId = studentId;
    }

    public LongFilter getCourseId() {
        return courseId;
    }

    public LongFilter courseId() {
        if (courseId == null) {
            courseId = new LongFilter();
        }
        return courseId;
    }

    public void setCourseId(LongFilter courseId) {
        this.courseId = courseId;
    }

    public LongFilter getReviewerId() {
        return reviewerId;
    }

    public LongFilter reviewerId() {
        if (reviewerId == null) {
            reviewerId = new LongFilter();
        }
        return reviewerId;
    }

    public void setReviewerId(LongFilter reviewerId) {
        this.reviewerId = reviewerId;
    }

    public LongFilter getAssigmentId() {
        return assigmentId;
    }

    public LongFilter assigmentId() {
        if (assigmentId == null) {
            assigmentId = new LongFilter();
        }
        return assigmentId;
    }

    public void setAssigmentId(LongFilter assigmentId) {
        this.assigmentId = assigmentId;
    }

    public LongFilter getAssimentGradeId() {
        return assimentGradeId;
    }

    public LongFilter assimentGradeId() {
        if (assimentGradeId == null) {
            assimentGradeId = new LongFilter();
        }
        return assimentGradeId;
    }

    public void setAssimentGradeId(LongFilter assimentGradeId) {
        this.assimentGradeId = assimentGradeId;
    }

    public LongFilter getCurrentGrade() {
        return currentGrade;
    }

    public LongFilter currentGrade() {
        if (currentGrade == null) {
            currentGrade = new LongFilter();
        }
        return currentGrade;
    }

    public void setCurrentGrade(LongFilter currentGrade) {
        this.currentGrade = currentGrade;
    }

    public LongFilter getExpectationGrade() {
        return expectationGrade;
    }

    public LongFilter expectationGrade() {
        if (expectationGrade == null) {
            expectationGrade = new LongFilter();
        }
        return expectationGrade;
    }

    public void setExpectationGrade(LongFilter expectationGrade) {
        this.expectationGrade = expectationGrade;
    }

    public StringFilter getStudentExplanation() {
        return studentExplanation;
    }

    public StringFilter studentExplanation() {
        if (studentExplanation == null) {
            studentExplanation = new StringFilter();
        }
        return studentExplanation;
    }

    public void setStudentExplanation(StringFilter studentExplanation) {
        this.studentExplanation = studentExplanation;
    }

    public StringFilter getTeacherComment() {
        return teacherComment;
    }

    public StringFilter teacherComment() {
        if (teacherComment == null) {
            teacherComment = new StringFilter();
        }
        return teacherComment;
    }

    public void setTeacherComment(StringFilter teacherComment) {
        this.teacherComment = teacherComment;
    }

    public BooleanFilter getIsFinal() {
        return isFinal;
    }

    public BooleanFilter isFinal() {
        if (isFinal == null) {
            isFinal = new BooleanFilter();
        }
        return isFinal;
    }

    public void setIsFinal(BooleanFilter isFinal) {
        this.isFinal = isFinal;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final GradeReviewCriteria that = (GradeReviewCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(gradeCompositionId, that.gradeCompositionId) &&
            Objects.equals(studentId, that.studentId) &&
            Objects.equals(courseId, that.courseId) &&
            Objects.equals(reviewerId, that.reviewerId) &&
            Objects.equals(assigmentId, that.assigmentId) &&
            Objects.equals(assimentGradeId, that.assimentGradeId) &&
            Objects.equals(currentGrade, that.currentGrade) &&
            Objects.equals(expectationGrade, that.expectationGrade) &&
            Objects.equals(studentExplanation, that.studentExplanation) &&
            Objects.equals(teacherComment, that.teacherComment) &&
            Objects.equals(isFinal, that.isFinal) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            gradeCompositionId,
            studentId,
            courseId,
            reviewerId,
            assigmentId,
            assimentGradeId,
            currentGrade,
            expectationGrade,
            studentExplanation,
            teacherComment,
            isFinal,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GradeReviewCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (gradeCompositionId != null ? "gradeCompositionId=" + gradeCompositionId + ", " : "") +
            (studentId != null ? "studentId=" + studentId + ", " : "") +
            (courseId != null ? "courseId=" + courseId + ", " : "") +
            (reviewerId != null ? "reviewerId=" + reviewerId + ", " : "") +
            (assigmentId != null ? "assigmentId=" + assigmentId + ", " : "") +
            (assimentGradeId != null ? "assimentGradeId=" + assimentGradeId + ", " : "") +
            (currentGrade != null ? "currentGrade=" + currentGrade + ", " : "") +
            (expectationGrade != null ? "expectationGrade=" + expectationGrade + ", " : "") +
            (studentExplanation != null ? "studentExplanation=" + studentExplanation + ", " : "") +
            (teacherComment != null ? "teacherComment=" + teacherComment + ", " : "") +
            (isFinal != null ? "isFinal=" + isFinal + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
