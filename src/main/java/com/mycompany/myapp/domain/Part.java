package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A Part.
 */
@Entity
@Table(name = "part")
public class Part implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "status")
    private String status;

    @Column(name = "change_indication")
    private Boolean changeIndication;

    @Column(name = "is_parent_part_bom_changed")
    private Boolean isParentPartBomChanged;

    @JsonIgnoreProperties(value = { "plantSpecifics", "part" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private PartSource partSource;

    @JsonIgnoreProperties(value = { "plantSpecifics", "itemReferences", "releasePackages", "part" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private PartIntention partIntention;

    @ManyToOne
    @JsonIgnoreProperties(value = { "contexts", "boms", "documents", "parts" }, allowSetters = true)
    private ImpactMatrix impactMatrix;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Part id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return this.status;
    }

    public Part status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getChangeIndication() {
        return this.changeIndication;
    }

    public Part changeIndication(Boolean changeIndication) {
        this.setChangeIndication(changeIndication);
        return this;
    }

    public void setChangeIndication(Boolean changeIndication) {
        this.changeIndication = changeIndication;
    }

    public Boolean getIsParentPartBomChanged() {
        return this.isParentPartBomChanged;
    }

    public Part isParentPartBomChanged(Boolean isParentPartBomChanged) {
        this.setIsParentPartBomChanged(isParentPartBomChanged);
        return this;
    }

    public void setIsParentPartBomChanged(Boolean isParentPartBomChanged) {
        this.isParentPartBomChanged = isParentPartBomChanged;
    }

    public PartSource getPartSource() {
        return this.partSource;
    }

    public void setPartSource(PartSource partSource) {
        this.partSource = partSource;
    }

    public Part partSource(PartSource partSource) {
        this.setPartSource(partSource);
        return this;
    }

    public PartIntention getPartIntention() {
        return this.partIntention;
    }

    public void setPartIntention(PartIntention partIntention) {
        this.partIntention = partIntention;
    }

    public Part partIntention(PartIntention partIntention) {
        this.setPartIntention(partIntention);
        return this;
    }

    public ImpactMatrix getImpactMatrix() {
        return this.impactMatrix;
    }

    public void setImpactMatrix(ImpactMatrix impactMatrix) {
        this.impactMatrix = impactMatrix;
    }

    public Part impactMatrix(ImpactMatrix impactMatrix) {
        this.setImpactMatrix(impactMatrix);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Part)) {
            return false;
        }
        return id != null && id.equals(((Part) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Part{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", changeIndication='" + getChangeIndication() + "'" +
            ", isParentPartBomChanged='" + getIsParentPartBomChanged() + "'" +
            "}";
    }
}
