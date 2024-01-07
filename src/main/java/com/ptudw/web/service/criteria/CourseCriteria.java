package com.ptudw.web.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.ptudw.web.domain.Course} entity. This class is used
 * in {@link com.ptudw.web.web.rest.CourseResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /courses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CourseCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private StringFilter name;

    private LongFilter ownerId;

    private StringFilter description;

    private StringFilter invitationCode;

    private ZonedDateTimeFilter expirationDate;

    private BooleanFilter isDeleted;

    private StringFilter createdBy;

    private ZonedDateTimeFilter createdDate;

    private StringFilter lastModifiedBy;

    private ZonedDateTimeFilter lastModifiedDate;

    private LongFilter assignmentsId;

    private LongFilter gradeCompositionsId;

    private Boolean distinct;

    public CourseCriteria() {}

    public CourseCriteria(CourseCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.ownerId = other.ownerId == null ? null : other.ownerId.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.invitationCode = other.invitationCode == null ? null : other.invitationCode.copy();
        this.expirationDate = other.expirationDate == null ? null : other.expirationDate.copy();
        this.isDeleted = other.isDeleted == null ? null : other.isDeleted.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.assignmentsId = other.assignmentsId == null ? null : other.assignmentsId.copy();
        this.gradeCompositionsId = other.gradeCompositionsId == null ? null : other.gradeCompositionsId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CourseCriteria copy() {
        return new CourseCriteria(this);
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

    public StringFilter getCode() {
        return code;
    }

    public StringFilter code() {
        if (code == null) {
            code = new StringFilter();
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
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

    public LongFilter getOwnerId() {
        return ownerId;
    }

    public LongFilter ownerId() {
        if (ownerId == null) {
            ownerId = new LongFilter();
        }
        return ownerId;
    }

    public void setOwnerId(LongFilter ownerId) {
        this.ownerId = ownerId;
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

    public StringFilter getInvitationCode() {
        return invitationCode;
    }

    public StringFilter invitationCode() {
        if (invitationCode == null) {
            invitationCode = new StringFilter();
        }
        return invitationCode;
    }

    public void setInvitationCode(StringFilter invitationCode) {
        this.invitationCode = invitationCode;
    }

    public ZonedDateTimeFilter getExpirationDate() {
        return expirationDate;
    }

    public ZonedDateTimeFilter expirationDate() {
        if (expirationDate == null) {
            expirationDate = new ZonedDateTimeFilter();
        }
        return expirationDate;
    }

    public void setExpirationDate(ZonedDateTimeFilter expirationDate) {
        this.expirationDate = expirationDate;
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

    public LongFilter getGradeCompositionsId() {
        return gradeCompositionsId;
    }

    public LongFilter gradeCompositionsId() {
        if (gradeCompositionsId == null) {
            gradeCompositionsId = new LongFilter();
        }
        return gradeCompositionsId;
    }

    public void setGradeCompositionsId(LongFilter gradeCompositionsId) {
        this.gradeCompositionsId = gradeCompositionsId;
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
        final CourseCriteria that = (CourseCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(name, that.name) &&
            Objects.equals(ownerId, that.ownerId) &&
            Objects.equals(description, that.description) &&
            Objects.equals(invitationCode, that.invitationCode) &&
            Objects.equals(expirationDate, that.expirationDate) &&
            Objects.equals(isDeleted, that.isDeleted) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(assignmentsId, that.assignmentsId) &&
            Objects.equals(gradeCompositionsId, that.gradeCompositionsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            code,
            name,
            ownerId,
            description,
            invitationCode,
            expirationDate,
            isDeleted,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            assignmentsId,
            gradeCompositionsId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CourseCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (ownerId != null ? "ownerId=" + ownerId + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (invitationCode != null ? "invitationCode=" + invitationCode + ", " : "") +
            (expirationDate != null ? "expirationDate=" + expirationDate + ", " : "") +
            (isDeleted != null ? "isDeleted=" + isDeleted + ", " : "") +
            (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
            (assignmentsId != null ? "assignmentsId=" + assignmentsId + ", " : "") +
            (gradeCompositionsId != null ? "gradeCompositionsId=" + gradeCompositionsId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
