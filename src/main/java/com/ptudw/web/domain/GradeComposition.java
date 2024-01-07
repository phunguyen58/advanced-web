package com.ptudw.web.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ptudw.web.domain.enumeration.GradeType;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A GradeComposition.
 */
@Entity
@Table(name = "grade_composition")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GradeComposition implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "scale")
    private Long scale;

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

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private GradeType type;

    @Column(name = "is_public")
    private Boolean isPublic;

    @Column(name = "position")
    private Long position;

    @OneToMany(mappedBy = "gradeComposition")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "assignmentGrades", "course", "gradeComposition" }, allowSetters = true)
    private Set<Assignment> assignments = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "assignments", "gradeCompositions" }, allowSetters = true)
    private Course course;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public GradeComposition id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public GradeComposition name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getScale() {
        return this.scale;
    }

    public GradeComposition scale(Long scale) {
        this.setScale(scale);
        return this;
    }

    public void setScale(Long scale) {
        this.scale = scale;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public GradeComposition isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public GradeComposition createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getCreatedDate() {
        return this.createdDate;
    }

    public GradeComposition createdDate(ZonedDateTime createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public GradeComposition lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public ZonedDateTime getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public GradeComposition lastModifiedDate(ZonedDateTime lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(ZonedDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public GradeType getType() {
        return this.type;
    }

    public GradeComposition type(GradeType type) {
        this.setType(type);
        return this;
    }

    public void setType(GradeType type) {
        this.type = type;
    }

    public Boolean getIsPublic() {
        return this.isPublic;
    }

    public GradeComposition isPublic(Boolean isPublic) {
        this.setIsPublic(isPublic);
        return this;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public Set<Assignment> getAssignments() {
        return this.assignments;
    }

    public void setAssignments(Set<Assignment> assignments) {
        if (this.assignments != null) {
            this.assignments.forEach(i -> i.setGradeComposition(null));
        }
        if (assignments != null) {
            assignments.forEach(i -> i.setGradeComposition(this));
        }
        this.assignments = assignments;
    }

    public GradeComposition assignments(Set<Assignment> assignments) {
        this.setAssignments(assignments);
        return this;
    }

    public GradeComposition addAssignments(Assignment assignment) {
        this.assignments.add(assignment);
        assignment.setGradeComposition(this);
        return this;
    }

    public GradeComposition removeAssignments(Assignment assignment) {
        this.assignments.remove(assignment);
        assignment.setGradeComposition(null);
        return this;
    }

    public Course getCourse() {
        return this.course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public GradeComposition course(Course course) {
        this.setCourse(course);
        return this;
    }

    public Long getPosition() {
        return this.position;
    }

    public void setPosition(Long position) {
        this.position = position;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GradeComposition)) {
            return false;
        }
        return id != null && id.equals(((GradeComposition) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GradeComposition{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", scale=" + getScale() +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", type='" + getType() + "'" +
            ", isPublic='" + getIsPublic() + "'" +
            ", position=" + getPosition() +
            "}";
    }
}
