package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A BomChild.
 */
@Entity
@Table(name = "bom_child")
public class BomChild implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "product_id")
    private String productId;

    @Column(name = "revision")
    private String revision;

    @Column(name = "quantity")
    private Double quantity;

    @Column(name = "relation_type")
    private String relationType;

    @ManyToMany(mappedBy = "bomChildren")
    @JsonIgnoreProperties(value = { "bomChildren", "bom" }, allowSetters = true)
    private Set<BomSource> bomSources = new HashSet<>();

    @ManyToMany(mappedBy = "bomChildren")
    @JsonIgnoreProperties(value = { "bomChildren", "releasePackages", "bom" }, allowSetters = true)
    private Set<BomIntention> bomIntentions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BomChild id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductId() {
        return this.productId;
    }

    public BomChild productId(String productId) {
        this.setProductId(productId);
        return this;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getRevision() {
        return this.revision;
    }

    public BomChild revision(String revision) {
        this.setRevision(revision);
        return this;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public Double getQuantity() {
        return this.quantity;
    }

    public BomChild quantity(Double quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getRelationType() {
        return this.relationType;
    }

    public BomChild relationType(String relationType) {
        this.setRelationType(relationType);
        return this;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    public Set<BomSource> getBomSources() {
        return this.bomSources;
    }

    public void setBomSources(Set<BomSource> bomSources) {
        if (this.bomSources != null) {
            this.bomSources.forEach(i -> i.removeBomChild(this));
        }
        if (bomSources != null) {
            bomSources.forEach(i -> i.addBomChild(this));
        }
        this.bomSources = bomSources;
    }

    public BomChild bomSources(Set<BomSource> bomSources) {
        this.setBomSources(bomSources);
        return this;
    }

    public BomChild addBomSource(BomSource bomSource) {
        this.bomSources.add(bomSource);
        bomSource.getBomChildren().add(this);
        return this;
    }

    public BomChild removeBomSource(BomSource bomSource) {
        this.bomSources.remove(bomSource);
        bomSource.getBomChildren().remove(this);
        return this;
    }

    public Set<BomIntention> getBomIntentions() {
        return this.bomIntentions;
    }

    public void setBomIntentions(Set<BomIntention> bomIntentions) {
        if (this.bomIntentions != null) {
            this.bomIntentions.forEach(i -> i.removeBomChild(this));
        }
        if (bomIntentions != null) {
            bomIntentions.forEach(i -> i.addBomChild(this));
        }
        this.bomIntentions = bomIntentions;
    }

    public BomChild bomIntentions(Set<BomIntention> bomIntentions) {
        this.setBomIntentions(bomIntentions);
        return this;
    }

    public BomChild addBomIntention(BomIntention bomIntention) {
        this.bomIntentions.add(bomIntention);
        bomIntention.getBomChildren().add(this);
        return this;
    }

    public BomChild removeBomIntention(BomIntention bomIntention) {
        this.bomIntentions.remove(bomIntention);
        bomIntention.getBomChildren().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BomChild)) {
            return false;
        }
        return id != null && id.equals(((BomChild) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BomChild{" +
            "id=" + getId() +
            ", productId='" + getProductId() + "'" +
            ", revision='" + getRevision() + "'" +
            ", quantity=" + getQuantity() +
            ", relationType='" + getRelationType() + "'" +
            "}";
    }
}
