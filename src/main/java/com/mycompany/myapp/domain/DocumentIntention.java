package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A DocumentIntention.
 */
@Entity
@Table(name = "document_intention")
public class DocumentIntention implements Serializable {

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

    @ManyToMany
    @JoinTable(
        name = "rel_document_intention__release_package",
        joinColumns = @JoinColumn(name = "document_intention_id"),
        inverseJoinColumns = @JoinColumn(name = "release_package_id")
    )
    @JsonIgnoreProperties(value = { "bomIntentions", "documentIntentions", "partIntentions" }, allowSetters = true)
    private Set<ReleasePackage> releasePackages = new HashSet<>();

    @JsonIgnoreProperties(
        value = { "documentSource", "documentIntention", "itemReferences", "impactMatrix", "documentType" },
        allowSetters = true
    )
    @OneToOne(mappedBy = "documentIntention")
    private Document document;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DocumentIntention id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public DocumentIntention name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public DocumentIntention description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getChangeIndicator() {
        return this.changeIndicator;
    }

    public DocumentIntention changeIndicator(Boolean changeIndicator) {
        this.setChangeIndicator(changeIndicator);
        return this;
    }

    public void setChangeIndicator(Boolean changeIndicator) {
        this.changeIndicator = changeIndicator;
    }

    public String getType() {
        return this.type;
    }

    public DocumentIntention type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubtype() {
        return this.subtype;
    }

    public DocumentIntention subtype(String subtype) {
        this.setSubtype(subtype);
        return this;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getGroup() {
        return this.group;
    }

    public DocumentIntention group(String group) {
        this.setGroup(group);
        return this;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getSheet() {
        return this.sheet;
    }

    public DocumentIntention sheet(String sheet) {
        this.setSheet(sheet);
        return this;
    }

    public void setSheet(String sheet) {
        this.sheet = sheet;
    }

    public Set<ReleasePackage> getReleasePackages() {
        return this.releasePackages;
    }

    public void setReleasePackages(Set<ReleasePackage> releasePackages) {
        this.releasePackages = releasePackages;
    }

    public DocumentIntention releasePackages(Set<ReleasePackage> releasePackages) {
        this.setReleasePackages(releasePackages);
        return this;
    }

    public DocumentIntention addReleasePackage(ReleasePackage releasePackage) {
        this.releasePackages.add(releasePackage);
        releasePackage.getDocumentIntentions().add(this);
        return this;
    }

    public DocumentIntention removeReleasePackage(ReleasePackage releasePackage) {
        this.releasePackages.remove(releasePackage);
        releasePackage.getDocumentIntentions().remove(this);
        return this;
    }

    public Document getDocument() {
        return this.document;
    }

    public void setDocument(Document document) {
        if (this.document != null) {
            this.document.setDocumentIntention(null);
        }
        if (document != null) {
            document.setDocumentIntention(this);
        }
        this.document = document;
    }

    public DocumentIntention document(Document document) {
        this.setDocument(document);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentIntention)) {
            return false;
        }
        return id != null && id.equals(((DocumentIntention) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentIntention{" +
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
