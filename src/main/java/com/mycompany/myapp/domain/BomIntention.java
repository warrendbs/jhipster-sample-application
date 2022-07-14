package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A BomIntention.
 */
@Entity
@Table(name = "bom_intention")
public class BomIntention implements Serializable {

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
        name = "rel_bom_intention__bom_child",
        joinColumns = @JoinColumn(name = "bom_intention_id"),
        inverseJoinColumns = @JoinColumn(name = "bom_child_id")
    )
    @JsonIgnoreProperties(value = { "bomSources", "bomIntentions" }, allowSetters = true)
    private Set<BomChild> bomChildren = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_bom_intention__release_package",
        joinColumns = @JoinColumn(name = "bom_intention_id"),
        inverseJoinColumns = @JoinColumn(name = "release_package_id")
    )
    @JsonIgnoreProperties(value = { "bomIntentions", "documentIntentions", "partIntentions" }, allowSetters = true)
    private Set<ReleasePackage> releasePackages = new HashSet<>();

    @JsonIgnoreProperties(value = { "bomSource", "bomIntention", "itemReferences", "impactMatrix" }, allowSetters = true)
    @OneToOne(mappedBy = "bomIntention")
    private Bom bom;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BomIntention id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public BomIntention type(String type) {
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

    public BomIntention bomChildren(Set<BomChild> bomChildren) {
        this.setBomChildren(bomChildren);
        return this;
    }

    public BomIntention addBomChild(BomChild bomChild) {
        this.bomChildren.add(bomChild);
        bomChild.getBomIntentions().add(this);
        return this;
    }

    public BomIntention removeBomChild(BomChild bomChild) {
        this.bomChildren.remove(bomChild);
        bomChild.getBomIntentions().remove(this);
        return this;
    }

    public Set<ReleasePackage> getReleasePackages() {
        return this.releasePackages;
    }

    public void setReleasePackages(Set<ReleasePackage> releasePackages) {
        this.releasePackages = releasePackages;
    }

    public BomIntention releasePackages(Set<ReleasePackage> releasePackages) {
        this.setReleasePackages(releasePackages);
        return this;
    }

    public BomIntention addReleasePackage(ReleasePackage releasePackage) {
        this.releasePackages.add(releasePackage);
        releasePackage.getBomIntentions().add(this);
        return this;
    }

    public BomIntention removeReleasePackage(ReleasePackage releasePackage) {
        this.releasePackages.remove(releasePackage);
        releasePackage.getBomIntentions().remove(this);
        return this;
    }

    public Bom getBom() {
        return this.bom;
    }

    public void setBom(Bom bom) {
        if (this.bom != null) {
            this.bom.setBomIntention(null);
        }
        if (bom != null) {
            bom.setBomIntention(this);
        }
        this.bom = bom;
    }

    public BomIntention bom(Bom bom) {
        this.setBom(bom);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BomIntention)) {
            return false;
        }
        return id != null && id.equals(((BomIntention) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BomIntention{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            "}";
    }
}
