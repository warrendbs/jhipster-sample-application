package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Document.
 */
@Entity
@Table(name = "document")
public class Document implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "status")
    private String status;

    @JsonIgnoreProperties(value = { "document" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private DocumentSource documentSource;

    @JsonIgnoreProperties(value = { "releasePackages", "document" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private DocumentIntention documentIntention;

    @ManyToMany
    @JoinTable(
        name = "rel_document__item_reference",
        joinColumns = @JoinColumn(name = "document_id"),
        inverseJoinColumns = @JoinColumn(name = "item_reference_id")
    )
    @JsonIgnoreProperties(value = { "boms", "documents", "partIntentions" }, allowSetters = true)
    private Set<ItemReference> itemReferences = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "contexts", "boms", "documents", "parts" }, allowSetters = true)
    private ImpactMatrix impactMatrix;

    @ManyToOne
    @JsonIgnoreProperties(value = { "documents" }, allowSetters = true)
    private DocumentType documentType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Document id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Document title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return this.status;
    }

    public Document status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DocumentSource getDocumentSource() {
        return this.documentSource;
    }

    public void setDocumentSource(DocumentSource documentSource) {
        this.documentSource = documentSource;
    }

    public Document documentSource(DocumentSource documentSource) {
        this.setDocumentSource(documentSource);
        return this;
    }

    public DocumentIntention getDocumentIntention() {
        return this.documentIntention;
    }

    public void setDocumentIntention(DocumentIntention documentIntention) {
        this.documentIntention = documentIntention;
    }

    public Document documentIntention(DocumentIntention documentIntention) {
        this.setDocumentIntention(documentIntention);
        return this;
    }

    public Set<ItemReference> getItemReferences() {
        return this.itemReferences;
    }

    public void setItemReferences(Set<ItemReference> itemReferences) {
        this.itemReferences = itemReferences;
    }

    public Document itemReferences(Set<ItemReference> itemReferences) {
        this.setItemReferences(itemReferences);
        return this;
    }

    public Document addItemReference(ItemReference itemReference) {
        this.itemReferences.add(itemReference);
        itemReference.getDocuments().add(this);
        return this;
    }

    public Document removeItemReference(ItemReference itemReference) {
        this.itemReferences.remove(itemReference);
        itemReference.getDocuments().remove(this);
        return this;
    }

    public ImpactMatrix getImpactMatrix() {
        return this.impactMatrix;
    }

    public void setImpactMatrix(ImpactMatrix impactMatrix) {
        this.impactMatrix = impactMatrix;
    }

    public Document impactMatrix(ImpactMatrix impactMatrix) {
        this.setImpactMatrix(impactMatrix);
        return this;
    }

    public DocumentType getDocumentType() {
        return this.documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public Document documentType(DocumentType documentType) {
        this.setDocumentType(documentType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Document)) {
            return false;
        }
        return id != null && id.equals(((Document) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Document{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
