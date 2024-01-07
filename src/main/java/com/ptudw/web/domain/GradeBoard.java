package com.ptudw.web.domain;

import com.ptudw.web.domain.enumeration.GradeType;
import java.util.ArrayList;
import java.util.List;

public class GradeBoard {

    User user;
    String studentId;
    List<AssignmentGrade> userAssignmentGradesInCourse;
    List<Assignment> assignmentsInCourse;
    Double finalGrade;
    GradeType gradeType;
    Course course;

    public GradeBoard(
        User user,
        String studentId,
        List<AssignmentGrade> userAssignmentGradesInCourse,
        List<Assignment> assignmentsInCourse,
        Double finalGrade,
        GradeType gradeType,
        Course course
    ) {
        this.user = user;
        this.studentId = studentId;
        this.userAssignmentGradesInCourse = userAssignmentGradesInCourse;
        this.assignmentsInCourse = assignmentsInCourse;
        this.finalGrade = finalGrade;
        this.gradeType = gradeType;
        this.course = course;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public List<AssignmentGrade> getUserAssignmentGradesInCourse() {
        return userAssignmentGradesInCourse;
    }

    public void setUserAssignmentGradesInCourse(List<AssignmentGrade> userAssignmentGradesInCourse) {
        this.userAssignmentGradesInCourse = userAssignmentGradesInCourse;
    }

    public List<Assignment> getAssignmentsInCourse() {
        return assignmentsInCourse;
    }

    public void setAssignmentsInCourse(List<Assignment> assignmentsInCourse) {
        this.assignmentsInCourse = assignmentsInCourse;
    }

    public Double getFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(Double finalGrade) {
        this.finalGrade = finalGrade;
    }

    public GradeType getGradeType() {
        return gradeType;
    }

    public void setGradeType(GradeType gradeType) {
        this.gradeType = gradeType;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @Override
    public String toString() {
        return (
            "GradeBoard [user=" +
            user +
            ", studentId=" +
            studentId +
            ", userAssignmentGradesInCourse=" +
            userAssignmentGradesInCourse +
            ", assignmentsInCourse=" +
            assignmentsInCourse +
            ", finalGrade=" +
            finalGrade +
            ", gradeType=" +
            gradeType +
            ", course=" +
            course +
            "]"
        );
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((user == null) ? 0 : user.hashCode());
        result = prime * result + ((studentId == null) ? 0 : studentId.hashCode());
        result = prime * result + ((userAssignmentGradesInCourse == null) ? 0 : userAssignmentGradesInCourse.hashCode());
        result = prime * result + ((assignmentsInCourse == null) ? 0 : assignmentsInCourse.hashCode());
        result = prime * result + ((finalGrade == null) ? 0 : finalGrade.hashCode());
        result = prime * result + ((gradeType == null) ? 0 : gradeType.hashCode());
        result = prime * result + ((course == null) ? 0 : course.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        GradeBoard other = (GradeBoard) obj;
        if (user == null) {
            if (other.user != null) return false;
        } else if (!user.equals(other.user)) return false;
        if (studentId == null) {
            if (other.studentId != null) return false;
        } else if (!studentId.equals(other.studentId)) return false;
        if (userAssignmentGradesInCourse == null) {
            if (other.userAssignmentGradesInCourse != null) return false;
        } else if (!userAssignmentGradesInCourse.equals(other.userAssignmentGradesInCourse)) return false;
        if (assignmentsInCourse == null) {
            if (other.assignmentsInCourse != null) return false;
        } else if (!assignmentsInCourse.equals(other.assignmentsInCourse)) return false;
        if (finalGrade == null) {
            if (other.finalGrade != null) return false;
        } else if (!finalGrade.equals(other.finalGrade)) return false;
        if (gradeType != other.gradeType) return false;
        if (course == null) {
            if (other.course != null) return false;
        } else if (!course.equals(other.course)) return false;
        return true;
    }
}
