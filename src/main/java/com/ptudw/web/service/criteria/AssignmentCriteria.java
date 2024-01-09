package com.ptudw.web.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.ptudw.web.domain.Assignment} entity. This class is used
 * in {@link com.ptudw.web.web.rest.AssignmentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /assignments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AssignmentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private LongFilter weight;

    private BooleanFilter isDeleted;

    private StringFilter createdBy;

    private ZonedDateTimeFilter createdDate;

    private StringFilter lastModifiedBy;

    private ZonedDateTimeFilter lastModifiedDate;

    private LongFilter assignmentGradesId;

    private LongFilter courseId;

    private LongFilter gradeCompositionId;

    private Boolean distinct;

    public AssignmentCriteria() {}

    public AssignmentCriteria(AssignmentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.weight = other.weight == null ? null : other.weight.copy();
        this.isDeleted = other.isDeleted == null ? null : other.isDeleted.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.assignmentGradesId = other.assignmentGradesId == null ? null : other.assignmentGradesId.copy();
        this.courseId = other.courseId == null ? null : other.courseId.copy();
        this.gradeCompositionId = other.gradeCompositionId == null ? null : other.gradeCompositionId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AssignmentCriteria copy() {
        return new AssignmentCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public LongFilter getWeight() {
        return weight;
    }

    public LongFilter weight() {
        if (weight == null) {
            weight = new LongFilter();
        }
        return weight;
    }

    public void setWeight(LongFilter weight) {
        this.weight = weight;
    }

    public BooleanFilter getIsDeleted() {
        return isDeleted;
    }

    public BooleanFilter isDeleted() {
        if (isDeleted == null) {
            isDeleted = new BooleanFilter();
        }
        return isDeleted;
    }

    public void setIsDeleted(BooleanFilter isDeleted) {
        this.isDeleted = isDeleted;
    }

    public StringFilter getCreatedBy() {
        return createdBy;
    }

    public StringFilter createdBy() {
        if (createdBy == null) {
            createdBy = new StringFilter();
        }
        return createdBy;
    }

    public void setCreatedBy(StringFilter createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTimeFilter getCreatedDate() {
        return createdDate;
    }

    public ZonedDateTimeFilter createdDate() {
        if (createdDate == null) {
            createdDate = new ZonedDateTimeFilter();
        }
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTimeFilter createdDate) {
        this.createdDate = createdDate;
    }

    public StringFilter getLastModifiedBy() {
        return lastModifiedBy;
    }

    public StringFilter lastModifiedBy() {
        if (lastModifiedBy == null) {
            lastModifiedBy = new StringFilter();
        }
        return lastModifiedBy;
    }

    public void setLastModifiedBy(StringFilter lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public ZonedDateTimeFilter getLastModifiedDate() {
        return lastModifiedDate;
    }

    public ZonedDateTimeFilter lastModifiedDate() {
        if (lastModifiedDate == null) {
            lastModifiedDate = new ZonedDateTimeFilter();
        }
        return lastModifiedDate;
    }

    public void setLastModifiedDate(ZonedDateTimeFilter lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public LongFilter getAssignmentGradesId() {
        return assignmentGradesId;
    }

    public LongFilter assignmentGradesId() {
        if (assignmentGradesId == null) {
            assignmentGradesId = new LongFilter();
        }
        return assignmentGradesId;
    }

    public void setAssignmentGradesId(LongFilter assignmentGradesId) {
        this.assignmentGradesId = assignmentGradesId;
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
        final AssignmentCriteria that = (AssignmentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(weight, that.weight) &&
            Objects.equals(isDeleted, that.isDeleted) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(assignmentGradesId, that.assignmentGradesId) &&
            Objects.equals(courseId, that.courseId) &&
            Objects.equals(gradeCompositionId, that.gradeCompositionId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            description,
            weight,
            isDeleted,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            assignmentGradesId,
            courseId,
            gradeCompositionId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssignmentCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (weight != null ? "weight=" + weight + ", " : "") +
            (isDeleted != null ? "isDeleted=" + isDeleted + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
            (assignmentGradesId != null ? "assignmentGradesId=" + assignmentGradesId + ", " : "") +
            (courseId != null ? "courseId=" + courseId + ", " : "") +
            (gradeCompositionId != null ? "gradeCompositionId=" + gradeCompositionId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
