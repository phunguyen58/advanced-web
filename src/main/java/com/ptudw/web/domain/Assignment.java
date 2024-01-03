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
 * A Assignment.
 */
@Entity
@Table(name = "assignment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Assignment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "weight")
    private Long weight;

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

    @OneToMany(mappedBy = "assignment")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "assignment" }, allowSetters = true)
    private Set<AssignmentGrade> assignmentGrades = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "assignments", "gradeCompositions" }, allowSetters = true)
    private Course course;

    @ManyToOne
    @JsonIgnoreProperties(value = { "assignments", "course" }, allowSetters = true)
    private GradeComposition gradeComposition;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Assignment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Assignment name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Assignment description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getWeight() {
        return this.weight;
    }

    public Assignment weight(Long weight) {
        this.setWeight(weight);
        return this;
    }

    public void setWeight(Long weight) {
        this.weight = weight;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public Assignment isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Assignment createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getCreatedDate() {
        return this.createdDate;
    }

    public Assignment createdDate(ZonedDateTime createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public Assignment lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public ZonedDateTime getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public Assignment lastModifiedDate(ZonedDateTime lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(ZonedDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Set<AssignmentGrade> getAssignmentGrades() {
        return this.assignmentGrades;
    }

    public void setAssignmentGrades(Set<AssignmentGrade> assignmentGrades) {
        if (this.assignmentGrades != null) {
            this.assignmentGrades.forEach(i -> i.setAssignment(null));
        }
        if (assignmentGrades != null) {
            assignmentGrades.forEach(i -> i.setAssignment(this));
        }
        this.assignmentGrades = assignmentGrades;
    }

    public Assignment assignmentGrades(Set<AssignmentGrade> assignmentGrades) {
        this.setAssignmentGrades(assignmentGrades);
        return this;
    }

    public Assignment addAssignmentGrades(AssignmentGrade assignmentGrade) {
        this.assignmentGrades.add(assignmentGrade);
        assignmentGrade.setAssignment(this);
        return this;
    }

    public Assignment removeAssignmentGrades(AssignmentGrade assignmentGrade) {
        this.assignmentGrades.remove(assignmentGrade);
        assignmentGrade.setAssignment(null);
        return this;
    }

    public Course getCourse() {
        return this.course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Assignment course(Course course) {
        this.setCourse(course);
        return this;
    }

    public GradeComposition getGradeComposition() {
        return this.gradeComposition;
    }

    public void setGradeComposition(GradeComposition gradeComposition) {
        this.gradeComposition = gradeComposition;
    }

    public Assignment gradeComposition(GradeComposition gradeComposition) {
        this.setGradeComposition(gradeComposition);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Assignment)) {
            return false;
        }
        return id != null && id.equals(((Assignment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Assignment{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", weight=" + getWeight() +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
