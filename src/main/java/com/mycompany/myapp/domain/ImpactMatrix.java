package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A ImpactMatrix.
 */
@Entity
@Table(name = "impact_matrix")
public class ImpactMatrix implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "impact_matrix_number")
    private Long impactMatrixNumber;

    @Column(name = "status")
    private Integer status;

    @Column(name = "revision")
    private String revision;

    @Column(name = "reviser")
    private String reviser;

    @Column(name = "revision_description")
    private String revisionDescription;

    @Column(name = "date_revised")
    private String dateRevised;

    @Column(name = "title")
    private String title;

    @Column(name = "is_auto_layout_enabled")
    private Boolean isAutoLayoutEnabled;

    @OneToMany(mappedBy = "impactMatrix")
    @JsonIgnoreProperties(value = { "impactMatrix" }, allowSetters = true)
    private Set<Context> contexts = new HashSet<>();

    @OneToMany(mappedBy = "impactMatrix")
    @JsonIgnoreProperties(value = { "bomSource", "bomIntention", "itemReferences", "impactMatrix" }, allowSetters = true)
    private Set<Bom> boms = new HashSet<>();

    @OneToMany(mappedBy = "impactMatrix")
    @JsonIgnoreProperties(
        value = { "documentSource", "documentIntention", "itemReferences", "impactMatrix", "documentType" },
        allowSetters = true
    )
    private Set<Document> documents = new HashSet<>();

    @OneToMany(mappedBy = "impactMatrix")
    @JsonIgnoreProperties(value = { "partSource", "partIntention", "impactMatrix" }, allowSetters = true)
    private Set<Part> parts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ImpactMatrix id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getImpactMatrixNumber() {
        return this.impactMatrixNumber;
    }

    public ImpactMatrix impactMatrixNumber(Long impactMatrixNumber) {
        this.setImpactMatrixNumber(impactMatrixNumber);
        return this;
    }

    public void setImpactMatrixNumber(Long impactMatrixNumber) {
        this.impactMatrixNumber = impactMatrixNumber;
    }

    public Integer getStatus() {
        return this.status;
    }

    public ImpactMatrix status(Integer status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRevision() {
        return this.revision;
    }

    public ImpactMatrix revision(String revision) {
        this.setRevision(revision);
        return this;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getReviser() {
        return this.reviser;
    }

    public ImpactMatrix reviser(String reviser) {
        this.setReviser(reviser);
        return this;
    }

    public void setReviser(String reviser) {
        this.reviser = reviser;
    }

    public String getRevisionDescription() {
        return this.revisionDescription;
    }

    public ImpactMatrix revisionDescription(String revisionDescription) {
        this.setRevisionDescription(revisionDescription);
        return this;
    }

    public void setRevisionDescription(String revisionDescription) {
        this.revisionDescription = revisionDescription;
    }

    public String getDateRevised() {
        return this.dateRevised;
    }

    public ImpactMatrix dateRevised(String dateRevised) {
        this.setDateRevised(dateRevised);
        return this;
    }

    public void setDateRevised(String dateRevised) {
        this.dateRevised = dateRevised;
    }

    public String getTitle() {
        return this.title;
    }

    public ImpactMatrix title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getIsAutoLayoutEnabled() {
        return this.isAutoLayoutEnabled;
    }

    public ImpactMatrix isAutoLayoutEnabled(Boolean isAutoLayoutEnabled) {
        this.setIsAutoLayoutEnabled(isAutoLayoutEnabled);
        return this;
    }

    public void setIsAutoLayoutEnabled(Boolean isAutoLayoutEnabled) {
        this.isAutoLayoutEnabled = isAutoLayoutEnabled;
    }

    public Set<Context> getContexts() {
        return this.contexts;
    }

    public void setContexts(Set<Context> contexts) {
        if (this.contexts != null) {
            this.contexts.forEach(i -> i.setImpactMatrix(null));
        }
        if (contexts != null) {
            contexts.forEach(i -> i.setImpactMatrix(this));
        }
        this.contexts = contexts;
    }

    public ImpactMatrix contexts(Set<Context> contexts) {
        this.setContexts(contexts);
        return this;
    }

    public ImpactMatrix addContext(Context context) {
        this.contexts.add(context);
        context.setImpactMatrix(this);
        return this;
    }

    public ImpactMatrix removeContext(Context context) {
        this.contexts.remove(context);
        context.setImpactMatrix(null);
        return this;
    }

    public Set<Bom> getBoms() {
        return this.boms;
    }

    public void setBoms(Set<Bom> boms) {
        if (this.boms != null) {
            this.boms.forEach(i -> i.setImpactMatrix(null));
        }
        if (boms != null) {
            boms.forEach(i -> i.setImpactMatrix(this));
        }
        this.boms = boms;
    }

    public ImpactMatrix boms(Set<Bom> boms) {
        this.setBoms(boms);
        return this;
    }

    public ImpactMatrix addBom(Bom bom) {
        this.boms.add(bom);
        bom.setImpactMatrix(this);
        return this;
    }

    public ImpactMatrix removeBom(Bom bom) {
        this.boms.remove(bom);
        bom.setImpactMatrix(null);
        return this;
    }

    public Set<Document> getDocuments() {
        return this.documents;
    }

    public void setDocuments(Set<Document> documents) {
        if (this.documents != null) {
            this.documents.forEach(i -> i.setImpactMatrix(null));
        }
        if (documents != null) {
            documents.forEach(i -> i.setImpactMatrix(this));
        }
        this.documents = documents;
    }

    public ImpactMatrix documents(Set<Document> documents) {
        this.setDocuments(documents);
        return this;
    }

    public ImpactMatrix addDocument(Document document) {
        this.documents.add(document);
        document.setImpactMatrix(this);
        return this;
    }

    public ImpactMatrix removeDocument(Document document) {
        this.documents.remove(document);
        document.setImpactMatrix(null);
        return this;
    }

    public Set<Part> getParts() {
        return this.parts;
    }

    public void setParts(Set<Part> parts) {
        if (this.parts != null) {
            this.parts.forEach(i -> i.setImpactMatrix(null));
        }
        if (parts != null) {
            parts.forEach(i -> i.setImpactMatrix(this));
        }
        this.parts = parts;
    }

    public ImpactMatrix parts(Set<Part> parts) {
        this.setParts(parts);
        return this;
    }

    public ImpactMatrix addPart(Part part) {
        this.parts.add(part);
        part.setImpactMatrix(this);
        return this;
    }

    public ImpactMatrix removePart(Part part) {
        this.parts.remove(part);
        part.setImpactMatrix(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ImpactMatrix)) {
            return false;
        }
        return id != null && id.equals(((ImpactMatrix) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImpactMatrix{" +
            "id=" + getId() +
            ", impactMatrixNumber=" + getImpactMatrixNumber() +
            ", status=" + getStatus() +
            ", revision='" + getRevision() + "'" +
            ", reviser='" + getReviser() + "'" +
            ", revisionDescription='" + getRevisionDescription() + "'" +
            ", dateRevised='" + getDateRevised() + "'" +
            ", title='" + getTitle() + "'" +
            ", isAutoLayoutEnabled='" + getIsAutoLayoutEnabled() + "'" +
            "}";
    }
}
