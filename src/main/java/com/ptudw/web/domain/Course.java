package com.ptudw.web.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Course.
 */
@Entity
@Table(name = "course")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "invitation_code", nullable = false)
    private String invitationCode;

    @Column(name = "expiration_date")
    private ZonedDateTime expirationDate;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @NotNull
    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private ZonedDateTime createdDate;

    @NotNull
    @Column(name = "last_modified_by", nullable = false)
    private String lastModifiedBy;

    @NotNull
    @Column(name = "last_modified_date", nullable = false)
    private ZonedDateTime lastModifiedDate;

    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "assignmentGrades", "course", "gradeComposition" }, allowSetters = true)
    private Set<Assignment> assignments = new HashSet<>();

    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "assignments", "course" }, allowSetters = true)
    private Set<GradeComposition> gradeCompositions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Course id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Course code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public Course name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getOwnerId() {
        return this.ownerId;
    }

    public Course ownerId(Long ownerId) {
        this.setOwnerId(ownerId);
        return this;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getDescription() {
        return this.description;
    }

    public Course description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInvitationCode() {
        return this.invitationCode;
    }

    public Course invitationCode(String invitationCode) {
        this.setInvitationCode(invitationCode);
        return this;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }

    public ZonedDateTime getExpirationDate() {
        return this.expirationDate;
    }

    public Course expirationDate(ZonedDateTime expirationDate) {
        this.setExpirationDate(expirationDate);
        return this;
    }

    public void setExpirationDate(ZonedDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public Course isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Course createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getCreatedDate() {
        return this.createdDate;
    }

    public Course createdDate(ZonedDateTime createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public Course lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public ZonedDateTime getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public Course lastModifiedDate(ZonedDateTime lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(ZonedDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Set<Assignment> getAssignments() {
        return this.assignments;
    }

    public void setAssignments(Set<Assignment> assignments) {
        if (this.assignments != null) {
            this.assignments.forEach(i -> i.setCourse(null));
        }
        if (assignments != null) {
            assignments.forEach(i -> i.setCourse(this));
        }
        this.assignments = assignments;
    }

    public Course assignments(Set<Assignment> assignments) {
        this.setAssignments(assignments);
        return this;
    }

    public Course addAssignments(Assignment assignment) {
        this.assignments.add(assignment);
        assignment.setCourse(this);
        return this;
    }

    public Course removeAssignments(Assignment assignment) {
        this.assignments.remove(assignment);
        assignment.setCourse(null);
        return this;
    }

    public Set<GradeComposition> getGradeCompositions() {
        return this.gradeCompositions;
    }

    public void setGradeCompositions(Set<GradeComposition> gradeCompositions) {
        if (this.gradeCompositions != null) {
            this.gradeCompositions.forEach(i -> i.setCourse(null));
        }
        if (gradeCompositions != null) {
            gradeCompositions.forEach(i -> i.setCourse(this));
        }
        this.gradeCompositions = gradeCompositions;
    }

    public Course gradeCompositions(Set<GradeComposition> gradeCompositions) {
        this.setGradeCompositions(gradeCompositions);
        return this;
    }

    public Course addGradeCompositions(GradeComposition gradeComposition) {
        this.gradeCompositions.add(gradeComposition);
        gradeComposition.setCourse(this);
        return this;
    }

    public Course removeGradeCompositions(GradeComposition gradeComposition) {
        this.gradeCompositions.remove(gradeComposition);
        gradeComposition.setCourse(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Course)) {
            return false;
        }
        return id != null && id.equals(((Course) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Course{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", ownerId=" + getOwnerId() +
            ", description='" + getDescription() + "'" +
            ", invitationCode='" + getInvitationCode() + "'" +
            ", expirationDate='" + getExpirationDate() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
