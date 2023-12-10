package com.ptudw.web.service.criteria;

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

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LongFilter minGradeScale;

    private LongFilter maxGradeScale;

    private LongFilter position;

    private BooleanFilter isDeleted;

    private StringFilter createdBy;

    private ZonedDateTimeFilter createdDate;

    private StringFilter lastModifiedBy;

    private ZonedDateTimeFilter lastModifiedDate;

    private LongFilter gradeStructureId;

    private Boolean distinct;

    public GradeCompositionCriteria() {}

    public GradeCompositionCriteria(GradeCompositionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.minGradeScale = other.minGradeScale == null ? null : other.minGradeScale.copy();
        this.maxGradeScale = other.maxGradeScale == null ? null : other.maxGradeScale.copy();
        this.position = other.position == null ? null : other.position.copy();
        this.isDeleted = other.isDeleted == null ? null : other.isDeleted.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.gradeStructureId = other.gradeStructureId == null ? null : other.gradeStructureId.copy();
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

    public LongFilter getMinGradeScale() {
        return minGradeScale;
    }

    public LongFilter minGradeScale() {
        if (minGradeScale == null) {
            minGradeScale = new LongFilter();
        }
        return minGradeScale;
    }

    public void setMinGradeScale(LongFilter minGradeScale) {
        this.minGradeScale = minGradeScale;
    }

    public LongFilter getMaxGradeScale() {
        return maxGradeScale;
    }

    public LongFilter maxGradeScale() {
        if (maxGradeScale == null) {
            maxGradeScale = new LongFilter();
        }
        return maxGradeScale;
    }

    public void setMaxGradeScale(LongFilter maxGradeScale) {
        this.maxGradeScale = maxGradeScale;
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

    public LongFilter getGradeStructureId() {
        return gradeStructureId;
    }

    public LongFilter gradeStructureId() {
        if (gradeStructureId == null) {
            gradeStructureId = new LongFilter();
        }
        return gradeStructureId;
    }

    public void setGradeStructureId(LongFilter gradeStructureId) {
        this.gradeStructureId = gradeStructureId;
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
        final GradeCompositionCriteria that = (GradeCompositionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(minGradeScale, that.minGradeScale) &&
            Objects.equals(maxGradeScale, that.maxGradeScale) &&
            Objects.equals(position, that.position) &&
            Objects.equals(isDeleted, that.isDeleted) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(gradeStructureId, that.gradeStructureId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            minGradeScale,
            maxGradeScale,
            position,
            isDeleted,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            gradeStructureId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GradeCompositionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (minGradeScale != null ? "minGradeScale=" + minGradeScale + ", " : "") +
            (maxGradeScale != null ? "maxGradeScale=" + maxGradeScale + ", " : "") +
            (position != null ? "position=" + position + ", " : "") +
            (isDeleted != null ? "isDeleted=" + isDeleted + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
            (gradeStructureId != null ? "gradeStructureId=" + gradeStructureId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
