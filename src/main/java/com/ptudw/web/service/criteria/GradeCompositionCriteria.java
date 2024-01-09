package com.ptudw.web.service.criteria;

import com.ptudw.web.domain.enumeration.GradeType;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.ptudw.web.domain.GradeComposition} entity. This class is used
 * in {@link com.ptudw.web.web.rest.GradeCompositionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /grade-compositions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GradeCompositionCriteria implements Serializable, Criteria {

    /**
     * Class for filtering GradeType
     */
    public static class GradeTypeFilter extends Filter<GradeType> {

        public GradeTypeFilter() {}

        public GradeTypeFilter(GradeTypeFilter filter) {
            super(filter);
        }

        @Override
        public GradeTypeFilter copy() {
            return new GradeTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LongFilter scale;

    private BooleanFilter isDeleted;

    private StringFilter createdBy;

    private ZonedDateTimeFilter createdDate;

    private StringFilter lastModifiedBy;

    private ZonedDateTimeFilter lastModifiedDate;

    private GradeTypeFilter type;

    private BooleanFilter isPublic;

    private LongFilter assignmentsId;

    private LongFilter courseId;

    private LongFilter position;

    private Boolean distinct;

    public GradeCompositionCriteria() {}

    public GradeCompositionCriteria(GradeCompositionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.scale = other.scale == null ? null : other.scale.copy();
        this.isDeleted = other.isDeleted == null ? null : other.isDeleted.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.isPublic = other.isPublic == null ? null : other.isPublic.copy();
        this.assignmentsId = other.assignmentsId == null ? null : other.assignmentsId.copy();
        this.courseId = other.courseId == null ? null : other.courseId.copy();
        this.position = other.position == null ? null : other.position.copy();
        this.distinct = other.distinct;
    }

    @Override
    public GradeCompositionCriteria copy() {
        return new GradeCompositionCriteria(this);
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

    public LongFilter getScale() {
        return scale;
    }

    public LongFilter scale() {
        if (scale == null) {
            scale = new LongFilter();
        }
        return scale;
    }

    public void setScale(LongFilter scale) {
        this.scale = scale;
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

    public GradeTypeFilter getType() {
        return type;
    }

    public GradeTypeFilter type() {
        if (type == null) {
            type = new GradeTypeFilter();
        }
        return type;
    }

    public void setType(GradeTypeFilter type) {
        this.type = type;
    }

    public BooleanFilter getIsPublic() {
        return isPublic;
    }

    public BooleanFilter isPublic() {
        if (isPublic == null) {
            isPublic = new BooleanFilter();
        }
        return isPublic;
    }

    public void setIsPublic(BooleanFilter isPublic) {
        this.isPublic = isPublic;
    }

    public LongFilter getAssignmentsId() {
        return assignmentsId;
    }

    public LongFilter assignmentsId() {
        if (assignmentsId == null) {
            assignmentsId = new LongFilter();
        }
        return assignmentsId;
    }

    public void setAssignmentsId(LongFilter assignmentsId) {
        this.assignmentsId = assignmentsId;
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

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    public LongFilter getPosition() {
        return position;
    }

    public LongFilter position() {
        if (position == null) {
            position = new LongFilter();
        }
        return position;
    }

    public void setPosition(LongFilter position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final GradeCompositionCriteria that = (GradeCompositionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(scale, that.scale) &&
            Objects.equals(isDeleted, that.isDeleted) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(type, that.type) &&
            Objects.equals(isPublic, that.isPublic) &&
            Objects.equals(assignmentsId, that.assignmentsId) &&
            Objects.equals(courseId, that.courseId) &&
            Objects.equals(position, that.position) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            scale,
            isDeleted,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            type,
            isPublic,
            assignmentsId,
            courseId,
            position,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GradeCompositionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (scale != null ? "scale=" + scale + ", " : "") +
            (isDeleted != null ? "isDeleted=" + isDeleted + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (isPublic != null ? "isPublic=" + isPublic + ", " : "") +
            (assignmentsId != null ? "assignmentsId=" + assignmentsId + ", " : "") +
            (courseId != null ? "courseId=" + courseId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            (position != null ? "position=" + position + ", " : "") +
            "}";
    }
}
