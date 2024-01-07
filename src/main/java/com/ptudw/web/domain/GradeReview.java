package com.ptudw.web.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A GradeReview.
 */
@Entity
@Table(name = "grade_review")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GradeReview implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "grade_composition_id", nullable = false)
    private Long gradeCompositionId;

    @NotNull
    @Column(name = "student_id", nullable = false)
    private String studentId;

    @NotNull
    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Column(name = "reviewer_id")
    private Long reviewerId;

    @Column(name = "assigment_id")
    private Long assigmentId;

    @Column(name = "assiment_grade_id")
    private Long assimentGradeId;

    @Column(name = "current_grade")
    private Long currentGrade;

    @Column(name = "expectation_grade")
    private Long expectationGrade;

    @Column(name = "final_grade")
    private Long finalGrade;

    @Column(name = "student_explanation")
    private String studentExplanation;

    @Column(name = "teacher_comment")
    private String teacherComment;

    @Column(name = "is_final")
    private Boolean isFinal;

    @Column(name = "comment")
    private String comment;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public GradeReview id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGradeCompositionId() {
        return this.gradeCompositionId;
    }

    public GradeReview gradeCompositionId(Long gradeCompositionId) {
        this.setGradeCompositionId(gradeCompositionId);
        return this;
    }

    public void setGradeCompositionId(Long gradeCompositionId) {
        this.gradeCompositionId = gradeCompositionId;
    }

    public String getStudentId() {
        return this.studentId;
    }

    public GradeReview studentId(String studentId) {
        this.setStudentId(studentId);
        return this;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public Long getCourseId() {
        return this.courseId;
    }

    public GradeReview courseId(Long courseId) {
        this.setCourseId(courseId);
        return this;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getReviewerId() {
        return this.reviewerId;
    }

    public GradeReview reviewerId(Long reviewerId) {
        this.setReviewerId(reviewerId);
        return this;
    }

    public void setReviewerId(Long reviewerId) {
        this.reviewerId = reviewerId;
    }

    public Long getAssigmentId() {
        return this.assigmentId;
    }

    public GradeReview assigmentId(Long assigmentId) {
        this.setAssigmentId(assigmentId);
        return this;
    }

    public void setAssigmentId(Long assigmentId) {
        this.assigmentId = assigmentId;
    }

    public Long getAssimentGradeId() {
        return this.assimentGradeId;
    }

    public GradeReview assimentGradeId(Long assimentGradeId) {
        this.setAssimentGradeId(assimentGradeId);
        return this;
    }

    public void setAssimentGradeId(Long assimentGradeId) {
        this.assimentGradeId = assimentGradeId;
    }

    public Long getCurrentGrade() {
        return this.currentGrade;
    }

    public GradeReview currentGrade(Long currentGrade) {
        this.setCurrentGrade(currentGrade);
        return this;
    }

    public void setCurrentGrade(Long currentGrade) {
        this.currentGrade = currentGrade;
    }

    public Long getExpectationGrade() {
        return this.expectationGrade;
    }

    public GradeReview expectationGrade(Long expectationGrade) {
        this.setExpectationGrade(expectationGrade);
        return this;
    }

    public void setExpectationGrade(Long expectationGrade) {
        this.expectationGrade = expectationGrade;
    }

    public Long getFinalGrade() {
        return this.finalGrade;
    }

    public GradeReview finalGrade(Long finalGrade) {
        this.setFinalGrade(finalGrade);
        return this;
    }

    public void setFinalGrade(Long finalGrade) {
        this.finalGrade = finalGrade;
    }

    public String getStudentExplanation() {
        return this.studentExplanation;
    }

    public GradeReview studentExplanation(String studentExplanation) {
        this.setStudentExplanation(studentExplanation);
        return this;
    }

    public void setStudentExplanation(String studentExplanation) {
        this.studentExplanation = studentExplanation;
    }

    public String getTeacherComment() {
        return this.teacherComment;
    }

    public GradeReview teacherComment(String teacherComment) {
        this.setTeacherComment(teacherComment);
        return this;
    }

    public void setTeacherComment(String teacherComment) {
        this.teacherComment = teacherComment;
    }

    public Boolean getIsFinal() {
        return this.isFinal;
    }

    public GradeReview isFinal(Boolean isFinal) {
        this.setIsFinal(isFinal);
        return this;
    }

    public void setIsFinal(Boolean isFinal) {
        this.isFinal = isFinal;
    }

    public String getComment() {
        return this.comment;
    }

    public GradeReview comment(String comment) {
        this.setComment(comment);
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GradeReview)) {
            return false;
        }
        return id != null && id.equals(((GradeReview) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GradeReview{" +
            "id=" + getId() +
            ", gradeCompositionId=" + getGradeCompositionId() +
            ", studentId='" + getStudentId() + "'" +
            ", courseId=" + getCourseId() +
            ", reviewerId=" + getReviewerId() +
            ", assigmentId=" + getAssigmentId() +
            ", assimentGradeId=" + getAssimentGradeId() +
            ", currentGrade=" + getCurrentGrade() +
            ", expectationGrade=" + getExpectationGrade() +
            ", finalGrade=" + getFinalGrade() +
            ", studentExplanation='" + getStudentExplanation() + "'" +
            ", teacherComment='" + getTeacherComment() + "'" +
            ", isFinal='" + getIsFinal() + "'" +
            ", comment='" + getComment() + "'" +
            "}";
    }
}
