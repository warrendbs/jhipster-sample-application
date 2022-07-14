package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A PlantSpecific.
 */
@Entity
@Table(name = "plant_specific")
public class PlantSpecific implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "object_dependancy")
    private String objectDependancy;

    @Column(name = "ref_material")
    private String refMaterial;

    @Column(name = "is_discontinued")
    private Boolean isDiscontinued;

    @ManyToMany(mappedBy = "plantSpecifics")
    @JsonIgnoreProperties(value = { "plantSpecifics", "part" }, allowSetters = true)
    private Set<PartSource> partSources = new HashSet<>();

    @ManyToMany(mappedBy = "plantSpecifics")
    @JsonIgnoreProperties(value = { "plantSpecifics", "itemReferences", "releasePackages", "part" }, allowSetters = true)
    private Set<PartIntention> partIntentions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PlantSpecific id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObjectDependancy() {
        return this.objectDependancy;
    }

    public PlantSpecific objectDependancy(String objectDependancy) {
        this.setObjectDependancy(objectDependancy);
        return this;
    }

    public void setObjectDependancy(String objectDependancy) {
        this.objectDependancy = objectDependancy;
    }

    public String getRefMaterial() {
        return this.refMaterial;
    }

    public PlantSpecific refMaterial(String refMaterial) {
        this.setRefMaterial(refMaterial);
        return this;
    }

    public void setRefMaterial(String refMaterial) {
        this.refMaterial = refMaterial;
    }

    public Boolean getIsDiscontinued() {
        return this.isDiscontinued;
    }

    public PlantSpecific isDiscontinued(Boolean isDiscontinued) {
        this.setIsDiscontinued(isDiscontinued);
        return this;
    }

    public void setIsDiscontinued(Boolean isDiscontinued) {
        this.isDiscontinued = isDiscontinued;
    }

    public Set<PartSource> getPartSources() {
        return this.partSources;
    }

    public void setPartSources(Set<PartSource> partSources) {
        if (this.partSources != null) {
            this.partSources.forEach(i -> i.removePlantSpecific(this));
        }
        if (partSources != null) {
            partSources.forEach(i -> i.addPlantSpecific(this));
        }
        this.partSources = partSources;
    }

    public PlantSpecific partSources(Set<PartSource> partSources) {
        this.setPartSources(partSources);
        return this;
    }

    public PlantSpecific addPartSource(PartSource partSource) {
        this.partSources.add(partSource);
        partSource.getPlantSpecifics().add(this);
        return this;
    }

    public PlantSpecific removePartSource(PartSource partSource) {
        this.partSources.remove(partSource);
        partSource.getPlantSpecifics().remove(this);
        return this;
    }

    public Set<PartIntention> getPartIntentions() {
        return this.partIntentions;
    }

    public void setPartIntentions(Set<PartIntention> partIntentions) {
        if (this.partIntentions != null) {
            this.partIntentions.forEach(i -> i.removePlantSpecific(this));
        }
        if (partIntentions != null) {
            partIntentions.forEach(i -> i.addPlantSpecific(this));
        }
        this.partIntentions = partIntentions;
    }

    public PlantSpecific partIntentions(Set<PartIntention> partIntentions) {
        this.setPartIntentions(partIntentions);
        return this;
    }

    public PlantSpecific addPartIntention(PartIntention partIntention) {
        this.partIntentions.add(partIntention);
        partIntention.getPlantSpecifics().add(this);
        return this;
    }

    public PlantSpecific removePartIntention(PartIntention partIntention) {
        this.partIntentions.remove(partIntention);
        partIntention.getPlantSpecifics().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlantSpecific)) {
            return false;
        }
        return id != null && id.equals(((PlantSpecific) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlantSpecific{" +
            "id=" + getId() +
            ", objectDependancy='" + getObjectDependancy() + "'" +
            ", refMaterial='" + getRefMaterial() + "'" +
            ", isDiscontinued='" + getIsDiscontinued() + "'" +
            "}";
    }
}
