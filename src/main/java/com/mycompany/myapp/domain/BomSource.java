package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A BomSource.
 */
@Entity
@Table(name = "bom_source")
public class BomSource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "type")
    private String type;

    @ManyToMany
    @JoinTable(
        name = "rel_bom_source__bom_child",
        joinColumns = @JoinColumn(name = "bom_source_id"),
        inverseJoinColumns = @JoinColumn(name = "bom_child_id")
    )
    @JsonIgnoreProperties(value = { "bomSources", "bomIntentions" }, allowSetters = true)
    private Set<BomChild> bomChildren = new HashSet<>();

    @JsonIgnoreProperties(value = { "bomSource", "bomIntention", "itemReferences", "impactMatrix" }, allowSetters = true)
    @OneToOne(mappedBy = "bomSource")
    private Bom bom;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BomSource id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public BomSource type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<BomChild> getBomChildren() {
        return this.bomChildren;
    }

    public void setBomChildren(Set<BomChild> bomChildren) {
        this.bomChildren = bomChildren;
    }

    public BomSource bomChildren(Set<BomChild> bomChildren) {
        this.setBomChildren(bomChildren);
        return this;
    }

    public BomSource addBomChild(BomChild bomChild) {
        this.bomChildren.add(bomChild);
        bomChild.getBomSources().add(this);
        return this;
    }

    public BomSource removeBomChild(BomChild bomChild) {
        this.bomChildren.remove(bomChild);
        bomChild.getBomSources().remove(this);
        return this;
    }

    public Bom getBom() {
        return this.bom;
    }

    public void setBom(Bom bom) {
        if (this.bom != null) {
            this.bom.setBomSource(null);
        }
        if (bom != null) {
            bom.setBomSource(this);
        }
        this.bom = bom;
    }

    public BomSource bom(Bom bom) {
        this.setBom(bom);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BomSource)) {
            return false;
        }
        return id != null && id.equals(((BomSource) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BomSource{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            "}";
    }
}
