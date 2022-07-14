package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A DocumentType.
 */
@Entity
@Table(name = "document_type")
public class DocumentType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "desctiption")
    private String desctiption;

    @OneToMany(mappedBy = "documentType")
    @JsonIgnoreProperties(
        value = { "documentSource", "documentIntention", "itemReferences", "impactMatrix", "documentType" },
        allowSetters = true
    )
    private Set<Document> documents = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DocumentType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesctiption() {
        return this.desctiption;
    }

    public DocumentType desctiption(String desctiption) {
        this.setDesctiption(desctiption);
        return this;
    }

    public void setDesctiption(String desctiption) {
        this.desctiption = desctiption;
    }

    public Set<Document> getDocuments() {
        return this.documents;
    }

    public void setDocuments(Set<Document> documents) {
        if (this.documents != null) {
            this.documents.forEach(i -> i.setDocumentType(null));
        }
        if (documents != null) {
            documents.forEach(i -> i.setDocumentType(this));
        }
        this.documents = documents;
    }

    public DocumentType documents(Set<Document> documents) {
        this.setDocuments(documents);
        return this;
    }

    public DocumentType addDocument(Document document) {
        this.documents.add(document);
        document.setDocumentType(this);
        return this;
    }

    public DocumentType removeDocument(Document document) {
        this.documents.remove(document);
        document.setDocumentType(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentType)) {
            return false;
        }
        return id != null && id.equals(((DocumentType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentType{" +
            "id=" + getId() +
            ", desctiption='" + getDesctiption() + "'" +
            "}";
    }
}
