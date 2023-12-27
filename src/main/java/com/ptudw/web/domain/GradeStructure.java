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
 * A GradeStructure.
 */
@Entity
@Table(name = "grade_structure")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class GradeStructure implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "course_id", nullable = false)
    private Long courseId;

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

    @OneToMany(mappedBy = "gradeStructure")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "gradeStructure" }, allowSetters = true)
    private Set<GradeComposition> gradeCompositions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public GradeStructure id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCourseId() {
        return this.courseId;
    }

    public GradeStructure courseId(Long courseId) {
        this.setCourseId(courseId);
        return this;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public GradeStructure isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public GradeStructure createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getCreatedDate() {
        return this.createdDate;
    }

    public GradeStructure createdDate(ZonedDateTime createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public GradeStructure lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public ZonedDateTime getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public GradeStructure lastModifiedDate(ZonedDateTime lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(ZonedDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public GradeType getType() {
        return this.type;
    }

    public GradeStructure type(GradeType type) {
        this.setType(type);
        return this;
    }

    public void setType(GradeType type) {
        this.type = type;
    }

    public Set<GradeComposition> getGradeCompositions() {
        return this.gradeCompositions;
    }

    public void setGradeCompositions(Set<GradeComposition> gradeCompositions) {
        if (this.gradeCompositions != null) {
            this.gradeCompositions.forEach(i -> i.setGradeStructure(null));
        }
        if (gradeCompositions != null) {
            gradeCompositions.forEach(i -> i.setGradeStructure(this));
        }
        this.gradeCompositions = gradeCompositions;
    }

    public GradeStructure gradeCompositions(Set<GradeComposition> gradeCompositions) {
        this.setGradeCompositions(gradeCompositions);
        return this;
    }

    public GradeStructure addGradeCompositions(GradeComposition gradeComposition) {
        this.gradeCompositions.add(gradeComposition);
        gradeComposition.setGradeStructure(this);
        return this;
    }

    public GradeStructure removeGradeCompositions(GradeComposition gradeComposition) {
        this.gradeCompositions.remove(gradeComposition);
        gradeComposition.setGradeStructure(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GradeStructure)) {
            return false;
        }
        return id != null && id.equals(((GradeStructure) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GradeStructure{" +
            "id=" + getId() +
            ", courseId=" + getCourseId() +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
