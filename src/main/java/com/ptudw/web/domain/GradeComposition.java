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

    @NotNull
    @Column(name = "min_grade_scale", nullable = false)
    private Long minGradeScale;

    @NotNull
    @Column(name = "max_grade_scale", nullable = false)
    private Long maxGradeScale;

    @NotNull
    @Column(name = "position", nullable = false)
    private Long position;

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

    @OneToMany(mappedBy = "gradeCompositions")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "gradeCompositions" }, allowSetters = true)
    private Set<GradeStructure> gradeStructures = new HashSet<>();

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

    public Long getMinGradeScale() {
        return this.minGradeScale;
    }

    public GradeComposition minGradeScale(Long minGradeScale) {
        this.setMinGradeScale(minGradeScale);
        return this;
    }

    public void setMinGradeScale(Long minGradeScale) {
        this.minGradeScale = minGradeScale;
    }

    public Long getMaxGradeScale() {
        return this.maxGradeScale;
    }

    public GradeComposition maxGradeScale(Long maxGradeScale) {
        this.setMaxGradeScale(maxGradeScale);
        return this;
    }

    public void setMaxGradeScale(Long maxGradeScale) {
        this.maxGradeScale = maxGradeScale;
    }

    public Long getPosition() {
        return this.position;
    }

    public GradeComposition position(Long position) {
        this.setPosition(position);
        return this;
    }

    public void setPosition(Long position) {
        this.position = position;
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

    public Set<GradeStructure> getGradeStructures() {
        return this.gradeStructures;
    }

    public void setGradeStructures(Set<GradeStructure> gradeStructures) {
        if (this.gradeStructures != null) {
            this.gradeStructures.forEach(i -> i.setGradeCompositions(null));
        }
        if (gradeStructures != null) {
            gradeStructures.forEach(i -> i.setGradeCompositions(this));
        }
        this.gradeStructures = gradeStructures;
    }

    public GradeComposition gradeStructures(Set<GradeStructure> gradeStructures) {
        this.setGradeStructures(gradeStructures);
        return this;
    }

    public GradeComposition addGradeStructure(GradeStructure gradeStructure) {
        this.gradeStructures.add(gradeStructure);
        gradeStructure.setGradeCompositions(this);
        return this;
    }

    public GradeComposition removeGradeStructure(GradeStructure gradeStructure) {
        this.gradeStructures.remove(gradeStructure);
        gradeStructure.setGradeCompositions(null);
        return this;
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
            ", minGradeScale=" + getMinGradeScale() +
            ", maxGradeScale=" + getMaxGradeScale() +
            ", position=" + getPosition() +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
