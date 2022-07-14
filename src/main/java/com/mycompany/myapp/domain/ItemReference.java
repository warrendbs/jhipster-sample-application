package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A ItemReference.
 */
@Entity
@Table(name = "item_reference")
public class ItemReference implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "reference_id")
    private Long referenceId;

    @Column(name = "type")
    private String type;

    @ManyToMany(mappedBy = "itemReferences")
    @JsonIgnoreProperties(value = { "bomSource", "bomIntention", "itemReferences", "impactMatrix" }, allowSetters = true)
    private Set<Bom> boms = new HashSet<>();

    @ManyToMany(mappedBy = "itemReferences")
    @JsonIgnoreProperties(
        value = { "documentSource", "documentIntention", "itemReferences", "impactMatrix", "documentType" },
        allowSetters = true
    )
    private Set<Document> documents = new HashSet<>();

    @ManyToMany(mappedBy = "itemReferences")
    @JsonIgnoreProperties(value = { "plantSpecifics", "itemReferences", "releasePackages", "part" }, allowSetters = true)
    private Set<PartIntention> partIntentions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ItemReference id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReferenceId() {
        return this.referenceId;
    }

    public ItemReference referenceId(Long referenceId) {
        this.setReferenceId(referenceId);
        return this;
    }

    public void setReferenceId(Long referenceId) {
        this.referenceId = referenceId;
    }

    public String getType() {
        return this.type;
    }

    public ItemReference type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<Bom> getBoms() {
        return this.boms;
    }

    public void setBoms(Set<Bom> boms) {
        if (this.boms != null) {
            this.boms.forEach(i -> i.removeItemReference(this));
        }
        if (boms != null) {
            boms.forEach(i -> i.addItemReference(this));
        }
        this.boms = boms;
    }

    public ItemReference boms(Set<Bom> boms) {
        this.setBoms(boms);
        return this;
    }

    public ItemReference addBom(Bom bom) {
        this.boms.add(bom);
        bom.getItemReferences().add(this);
        return this;
    }

    public ItemReference removeBom(Bom bom) {
        this.boms.remove(bom);
        bom.getItemReferences().remove(this);
        return this;
    }

    public Set<Document> getDocuments() {
        return this.documents;
    }

    public void setDocuments(Set<Document> documents) {
        if (this.documents != null) {
            this.documents.forEach(i -> i.removeItemReference(this));
        }
        if (documents != null) {
            documents.forEach(i -> i.addItemReference(this));
        }
        this.documents = documents;
    }

    public ItemReference documents(Set<Document> documents) {
        this.setDocuments(documents);
        return this;
    }

    public ItemReference addDocument(Document document) {
        this.documents.add(document);
        document.getItemReferences().add(this);
        return this;
    }

    public ItemReference removeDocument(Document document) {
        this.documents.remove(document);
        document.getItemReferences().remove(this);
        return this;
    }

    public Set<PartIntention> getPartIntentions() {
        return this.partIntentions;
    }

    public void setPartIntentions(Set<PartIntention> partIntentions) {
        if (this.partIntentions != null) {
            this.partIntentions.forEach(i -> i.removeItemReference(this));
        }
        if (partIntentions != null) {
            partIntentions.forEach(i -> i.addItemReference(this));
        }
        this.partIntentions = partIntentions;
    }

    public ItemReference partIntentions(Set<PartIntention> partIntentions) {
        this.setPartIntentions(partIntentions);
        return this;
    }

    public ItemReference addPartIntention(PartIntention partIntention) {
        this.partIntentions.add(partIntention);
        partIntention.getItemReferences().add(this);
        return this;
    }

    public ItemReference removePartIntention(PartIntention partIntention) {
        this.partIntentions.remove(partIntention);
        partIntention.getItemReferences().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemReference)) {
            return false;
        }
        return id != null && id.equals(((ItemReference) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ItemReference{" +
            "id=" + getId() +
            ", referenceId=" + getReferenceId() +
            ", type='" + getType() + "'" +
            "}";
    }
}
