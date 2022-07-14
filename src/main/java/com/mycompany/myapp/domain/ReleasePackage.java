package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A ReleasePackage.
 */
@Entity
@Table(name = "release_package")
public class ReleasePackage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "release_package_number")
    private String releasePackageNumber;

    @Column(name = "release_package_title")
    private String releasePackageTitle;

    @Column(name = "status")
    private String status;

    @Column(name = "ecn")
    private String ecn;

    @ManyToMany(mappedBy = "releasePackages")
    @JsonIgnoreProperties(value = { "bomChildren", "releasePackages", "bom" }, allowSetters = true)
    private Set<BomIntention> bomIntentions = new HashSet<>();

    @ManyToMany(mappedBy = "releasePackages")
    @JsonIgnoreProperties(value = { "releasePackages", "document" }, allowSetters = true)
    private Set<DocumentIntention> documentIntentions = new HashSet<>();

    @ManyToMany(mappedBy = "releasePackages")
    @JsonIgnoreProperties(value = { "plantSpecifics", "itemReferences", "releasePackages", "part" }, allowSetters = true)
    private Set<PartIntention> partIntentions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ReleasePackage id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public ReleasePackage title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleasePackageNumber() {
        return this.releasePackageNumber;
    }

    public ReleasePackage releasePackageNumber(String releasePackageNumber) {
        this.setReleasePackageNumber(releasePackageNumber);
        return this;
    }

    public void setReleasePackageNumber(String releasePackageNumber) {
        this.releasePackageNumber = releasePackageNumber;
    }

    public String getReleasePackageTitle() {
        return this.releasePackageTitle;
    }

    public ReleasePackage releasePackageTitle(String releasePackageTitle) {
        this.setReleasePackageTitle(releasePackageTitle);
        return this;
    }

    public void setReleasePackageTitle(String releasePackageTitle) {
        this.releasePackageTitle = releasePackageTitle;
    }

    public String getStatus() {
        return this.status;
    }

    public ReleasePackage status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEcn() {
        return this.ecn;
    }

    public ReleasePackage ecn(String ecn) {
        this.setEcn(ecn);
        return this;
    }

    public void setEcn(String ecn) {
        this.ecn = ecn;
    }

    public Set<BomIntention> getBomIntentions() {
        return this.bomIntentions;
    }

    public void setBomIntentions(Set<BomIntention> bomIntentions) {
        if (this.bomIntentions != null) {
            this.bomIntentions.forEach(i -> i.removeReleasePackage(this));
        }
        if (bomIntentions != null) {
            bomIntentions.forEach(i -> i.addReleasePackage(this));
        }
        this.bomIntentions = bomIntentions;
    }

    public ReleasePackage bomIntentions(Set<BomIntention> bomIntentions) {
        this.setBomIntentions(bomIntentions);
        return this;
    }

    public ReleasePackage addBomIntention(BomIntention bomIntention) {
        this.bomIntentions.add(bomIntention);
        bomIntention.getReleasePackages().add(this);
        return this;
    }

    public ReleasePackage removeBomIntention(BomIntention bomIntention) {
        this.bomIntentions.remove(bomIntention);
        bomIntention.getReleasePackages().remove(this);
        return this;
    }

    public Set<DocumentIntention> getDocumentIntentions() {
        return this.documentIntentions;
    }

    public void setDocumentIntentions(Set<DocumentIntention> documentIntentions) {
        if (this.documentIntentions != null) {
            this.documentIntentions.forEach(i -> i.removeReleasePackage(this));
        }
        if (documentIntentions != null) {
            documentIntentions.forEach(i -> i.addReleasePackage(this));
        }
        this.documentIntentions = documentIntentions;
    }

    public ReleasePackage documentIntentions(Set<DocumentIntention> documentIntentions) {
        this.setDocumentIntentions(documentIntentions);
        return this;
    }

    public ReleasePackage addDocumentIntention(DocumentIntention documentIntention) {
        this.documentIntentions.add(documentIntention);
        documentIntention.getReleasePackages().add(this);
        return this;
    }

    public ReleasePackage removeDocumentIntention(DocumentIntention documentIntention) {
        this.documentIntentions.remove(documentIntention);
        documentIntention.getReleasePackages().remove(this);
        return this;
    }

    public Set<PartIntention> getPartIntentions() {
        return this.partIntentions;
    }

    public void setPartIntentions(Set<PartIntention> partIntentions) {
        if (this.partIntentions != null) {
            this.partIntentions.forEach(i -> i.removeReleasePackage(this));
        }
        if (partIntentions != null) {
            partIntentions.forEach(i -> i.addReleasePackage(this));
        }
        this.partIntentions = partIntentions;
    }

    public ReleasePackage partIntentions(Set<PartIntention> partIntentions) {
        this.setPartIntentions(partIntentions);
        return this;
    }

    public ReleasePackage addPartIntention(PartIntention partIntention) {
        this.partIntentions.add(partIntention);
        partIntention.getReleasePackages().add(this);
        return this;
    }

    public ReleasePackage removePartIntention(PartIntention partIntention) {
        this.partIntentions.remove(partIntention);
        partIntention.getReleasePackages().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReleasePackage)) {
            return false;
        }
        return id != null && id.equals(((ReleasePackage) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReleasePackage{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", releasePackageNumber='" + getReleasePackageNumber() + "'" +
            ", releasePackageTitle='" + getReleasePackageTitle() + "'" +
            ", status='" + getStatus() + "'" +
            ", ecn='" + getEcn() + "'" +
            "}";
    }
}
