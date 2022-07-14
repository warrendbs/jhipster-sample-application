package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Bom.
 */
@Entity
@Table(name = "bom")
public class Bom implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "status")
    private Integer status;

    @JsonIgnoreProperties(value = { "bomChildren", "bom" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private BomSource bomSource;

    @JsonIgnoreProperties(value = { "bomChildren", "releasePackages", "bom" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private BomIntention bomIntention;

    @ManyToMany
    @JoinTable(
        name = "rel_bom__item_reference",
        joinColumns = @JoinColumn(name = "bom_id"),
        inverseJoinColumns = @JoinColumn(name = "item_reference_id")
    )
    @JsonIgnoreProperties(value = { "boms", "documents", "partIntentions" }, allowSetters = true)
    private Set<ItemReference> itemReferences = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "contexts", "boms", "documents", "parts" }, allowSetters = true)
    private ImpactMatrix impactMatrix;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Bom id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStatus() {
        return this.status;
    }

    public Bom status(Integer status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public BomSource getBomSource() {
        return this.bomSource;
    }

    public void setBomSource(BomSource bomSource) {
        this.bomSource = bomSource;
    }

    public Bom bomSource(BomSource bomSource) {
        this.setBomSource(bomSource);
        return this;
    }

    public BomIntention getBomIntention() {
        return this.bomIntention;
    }

    public void setBomIntention(BomIntention bomIntention) {
        this.bomIntention = bomIntention;
    }

    public Bom bomIntention(BomIntention bomIntention) {
        this.setBomIntention(bomIntention);
        return this;
    }

    public Set<ItemReference> getItemReferences() {
        return this.itemReferences;
    }

    public void setItemReferences(Set<ItemReference> itemReferences) {
        this.itemReferences = itemReferences;
    }

    public Bom itemReferences(Set<ItemReference> itemReferences) {
        this.setItemReferences(itemReferences);
        return this;
    }

    public Bom addItemReference(ItemReference itemReference) {
        this.itemReferences.add(itemReference);
        itemReference.getBoms().add(this);
        return this;
    }

    public Bom removeItemReference(ItemReference itemReference) {
        this.itemReferences.remove(itemReference);
        itemReference.getBoms().remove(this);
        return this;
    }

    public ImpactMatrix getImpactMatrix() {
        return this.impactMatrix;
    }

    public void setImpactMatrix(ImpactMatrix impactMatrix) {
        this.impactMatrix = impactMatrix;
    }

    public Bom impactMatrix(ImpactMatrix impactMatrix) {
        this.setImpactMatrix(impactMatrix);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Bom)) {
            return false;
        }
        return id != null && id.equals(((Bom) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Bom{" +
            "id=" + getId() +
            ", status=" + getStatus() +
            "}";
    }
}
