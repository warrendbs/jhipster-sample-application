package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A DocumentSource.
 */
@Entity
@Table(name = "document_source")
public class DocumentSource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "change_indicator")
    private Boolean changeIndicator;

    @Column(name = "type")
    private String type;

    @Column(name = "subtype")
    private String subtype;

    @Column(name = "jhi_group")
    private String group;

    @Column(name = "sheet")
    private String sheet;

    @JsonIgnoreProperties(
        value = { "documentSource", "documentIntention", "itemReferences", "impactMatrix", "documentType" },
        allowSetters = true
    )
    @OneToOne(mappedBy = "documentSource")
    private Document document;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DocumentSource id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public DocumentSource name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public DocumentSource description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getChangeIndicator() {
        return this.changeIndicator;
    }

    public DocumentSource changeIndicator(Boolean changeIndicator) {
        this.setChangeIndicator(changeIndicator);
        return this;
    }

    public void setChangeIndicator(Boolean changeIndicator) {
        this.changeIndicator = changeIndicator;
    }

    public String getType() {
        return this.type;
    }

    public DocumentSource type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubtype() {
        return this.subtype;
    }

    public DocumentSource subtype(String subtype) {
        this.setSubtype(subtype);
        return this;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getGroup() {
        return this.group;
    }

    public DocumentSource group(String group) {
        this.setGroup(group);
        return this;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getSheet() {
        return this.sheet;
    }

    public DocumentSource sheet(String sheet) {
        this.setSheet(sheet);
        return this;
    }

    public void setSheet(String sheet) {
        this.sheet = sheet;
    }

    public Document getDocument() {
        return this.document;
    }

    public void setDocument(Document document) {
        if (this.document != null) {
            this.document.setDocumentSource(null);
        }
        if (document != null) {
            document.setDocumentSource(this);
        }
        this.document = document;
    }

    public DocumentSource document(Document document) {
        this.setDocument(document);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentSource)) {
            return false;
        }
        return id != null && id.equals(((DocumentSource) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentSource{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", changeIndicator='" + getChangeIndicator() + "'" +
            ", type='" + getType() + "'" +
            ", subtype='" + getSubtype() + "'" +
            ", group='" + getGroup() + "'" +
            ", sheet='" + getSheet() + "'" +
            "}";
    }
}
