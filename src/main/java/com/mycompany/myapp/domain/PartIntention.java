package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A PartIntention.
 */
@Entity
@Table(name = "part_intention")
public class PartIntention implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "revision")
    private String revision;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "vqi")
    private String vqi;

    @Column(name = "procurement_type")
    private String procurementType;

    @Column(name = "material_type")
    private String materialType;

    @Column(name = "serial_number_profile")
    private String serialNumberProfile;

    @Column(name = "critical_configuration_item_indicator")
    private Boolean criticalConfigurationItemIndicator;

    @Column(name = "regular_part_indicator")
    private String regularPartIndicator;

    @Column(name = "history_indicator")
    private String historyIndicator;

    @Column(name = "cross_plant_status")
    private String crossPlantStatus;

    @Column(name = "cross_plant_status_to_be")
    private String crossPlantStatusToBe;

    @Column(name = "tool_pack_category")
    private String toolPackCategory;

    @Column(name = "tc_change_control")
    private Boolean tcChangeControl;

    @Column(name = "sap_change_control")
    private Boolean sapChangeControl;

    @Column(name = "allow_bom_restructuring")
    private Boolean allowBomRestructuring;

    @Column(name = "unit_of_measure")
    private String unitOfMeasure;

    @Column(name = "item_usage")
    private String itemUsage;

    @Column(name = "is_phantom")
    private Boolean isPhantom;

    @Column(name = "failure_rate")
    private String failureRate;

    @Column(name = "in_house_production_time")
    private Long inHouseProductionTime;

    @Column(name = "sl_abc_code")
    private String slAbcCode;

    @Column(name = "production_plant")
    private String productionPlant;

    @Column(name = "limited_driving_12_nc")
    private String limitedDriving12Nc;

    @Column(name = "limited_driving_12_ncflag")
    private String limitedDriving12Ncflag;

    @Column(name = "multi_plant")
    private String multiPlant;

    @Column(name = "type")
    private String type;

    @Column(name = "successor_part_id")
    private Long successorPartId;

    @ManyToMany
    @JoinTable(
        name = "rel_part_intention__plant_specific",
        joinColumns = @JoinColumn(name = "part_intention_id"),
        inverseJoinColumns = @JoinColumn(name = "plant_specific_id")
    )
    @JsonIgnoreProperties(value = { "partSources", "partIntentions" }, allowSetters = true)
    private Set<PlantSpecific> plantSpecifics = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_part_intention__item_reference",
        joinColumns = @JoinColumn(name = "part_intention_id"),
        inverseJoinColumns = @JoinColumn(name = "item_reference_id")
    )
    @JsonIgnoreProperties(value = { "boms", "documents", "partIntentions" }, allowSetters = true)
    private Set<ItemReference> itemReferences = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_part_intention__release_package",
        joinColumns = @JoinColumn(name = "part_intention_id"),
        inverseJoinColumns = @JoinColumn(name = "release_package_id")
    )
    @JsonIgnoreProperties(value = { "bomIntentions", "documentIntentions", "partIntentions" }, allowSetters = true)
    private Set<ReleasePackage> releasePackages = new HashSet<>();

    @JsonIgnoreProperties(value = { "partSource", "partIntention", "impactMatrix" }, allowSetters = true)
    @OneToOne(mappedBy = "partIntention")
    private Part part;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PartIntention id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return this.productId;
    }

    public PartIntention productId(Long productId) {
        this.setProductId(productId);
        return this;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getRevision() {
        return this.revision;
    }

    public PartIntention revision(String revision) {
        this.setRevision(revision);
        return this;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getName() {
        return this.name;
    }

    public PartIntention name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public PartIntention description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVqi() {
        return this.vqi;
    }

    public PartIntention vqi(String vqi) {
        this.setVqi(vqi);
        return this;
    }

    public void setVqi(String vqi) {
        this.vqi = vqi;
    }

    public String getProcurementType() {
        return this.procurementType;
    }

    public PartIntention procurementType(String procurementType) {
        this.setProcurementType(procurementType);
        return this;
    }

    public void setProcurementType(String procurementType) {
        this.procurementType = procurementType;
    }

    public String getMaterialType() {
        return this.materialType;
    }

    public PartIntention materialType(String materialType) {
        this.setMaterialType(materialType);
        return this;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public String getSerialNumberProfile() {
        return this.serialNumberProfile;
    }

    public PartIntention serialNumberProfile(String serialNumberProfile) {
        this.setSerialNumberProfile(serialNumberProfile);
        return this;
    }

    public void setSerialNumberProfile(String serialNumberProfile) {
        this.serialNumberProfile = serialNumberProfile;
    }

    public Boolean getCriticalConfigurationItemIndicator() {
        return this.criticalConfigurationItemIndicator;
    }

    public PartIntention criticalConfigurationItemIndicator(Boolean criticalConfigurationItemIndicator) {
        this.setCriticalConfigurationItemIndicator(criticalConfigurationItemIndicator);
        return this;
    }

    public void setCriticalConfigurationItemIndicator(Boolean criticalConfigurationItemIndicator) {
        this.criticalConfigurationItemIndicator = criticalConfigurationItemIndicator;
    }

    public String getRegularPartIndicator() {
        return this.regularPartIndicator;
    }

    public PartIntention regularPartIndicator(String regularPartIndicator) {
        this.setRegularPartIndicator(regularPartIndicator);
        return this;
    }

    public void setRegularPartIndicator(String regularPartIndicator) {
        this.regularPartIndicator = regularPartIndicator;
    }

    public String getHistoryIndicator() {
        return this.historyIndicator;
    }

    public PartIntention historyIndicator(String historyIndicator) {
        this.setHistoryIndicator(historyIndicator);
        return this;
    }

    public void setHistoryIndicator(String historyIndicator) {
        this.historyIndicator = historyIndicator;
    }

    public String getCrossPlantStatus() {
        return this.crossPlantStatus;
    }

    public PartIntention crossPlantStatus(String crossPlantStatus) {
        this.setCrossPlantStatus(crossPlantStatus);
        return this;
    }

    public void setCrossPlantStatus(String crossPlantStatus) {
        this.crossPlantStatus = crossPlantStatus;
    }

    public String getCrossPlantStatusToBe() {
        return this.crossPlantStatusToBe;
    }

    public PartIntention crossPlantStatusToBe(String crossPlantStatusToBe) {
        this.setCrossPlantStatusToBe(crossPlantStatusToBe);
        return this;
    }

    public void setCrossPlantStatusToBe(String crossPlantStatusToBe) {
        this.crossPlantStatusToBe = crossPlantStatusToBe;
    }

    public String getToolPackCategory() {
        return this.toolPackCategory;
    }

    public PartIntention toolPackCategory(String toolPackCategory) {
        this.setToolPackCategory(toolPackCategory);
        return this;
    }

    public void setToolPackCategory(String toolPackCategory) {
        this.toolPackCategory = toolPackCategory;
    }

    public Boolean getTcChangeControl() {
        return this.tcChangeControl;
    }

    public PartIntention tcChangeControl(Boolean tcChangeControl) {
        this.setTcChangeControl(tcChangeControl);
        return this;
    }

    public void setTcChangeControl(Boolean tcChangeControl) {
        this.tcChangeControl = tcChangeControl;
    }

    public Boolean getSapChangeControl() {
        return this.sapChangeControl;
    }

    public PartIntention sapChangeControl(Boolean sapChangeControl) {
        this.setSapChangeControl(sapChangeControl);
        return this;
    }

    public void setSapChangeControl(Boolean sapChangeControl) {
        this.sapChangeControl = sapChangeControl;
    }

    public Boolean getAllowBomRestructuring() {
        return this.allowBomRestructuring;
    }

    public PartIntention allowBomRestructuring(Boolean allowBomRestructuring) {
        this.setAllowBomRestructuring(allowBomRestructuring);
        return this;
    }

    public void setAllowBomRestructuring(Boolean allowBomRestructuring) {
        this.allowBomRestructuring = allowBomRestructuring;
    }

    public String getUnitOfMeasure() {
        return this.unitOfMeasure;
    }

    public PartIntention unitOfMeasure(String unitOfMeasure) {
        this.setUnitOfMeasure(unitOfMeasure);
        return this;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public String getItemUsage() {
        return this.itemUsage;
    }

    public PartIntention itemUsage(String itemUsage) {
        this.setItemUsage(itemUsage);
        return this;
    }

    public void setItemUsage(String itemUsage) {
        this.itemUsage = itemUsage;
    }

    public Boolean getIsPhantom() {
        return this.isPhantom;
    }

    public PartIntention isPhantom(Boolean isPhantom) {
        this.setIsPhantom(isPhantom);
        return this;
    }

    public void setIsPhantom(Boolean isPhantom) {
        this.isPhantom = isPhantom;
    }

    public String getFailureRate() {
        return this.failureRate;
    }

    public PartIntention failureRate(String failureRate) {
        this.setFailureRate(failureRate);
        return this;
    }

    public void setFailureRate(String failureRate) {
        this.failureRate = failureRate;
    }

    public Long getInHouseProductionTime() {
        return this.inHouseProductionTime;
    }

    public PartIntention inHouseProductionTime(Long inHouseProductionTime) {
        this.setInHouseProductionTime(inHouseProductionTime);
        return this;
    }

    public void setInHouseProductionTime(Long inHouseProductionTime) {
        this.inHouseProductionTime = inHouseProductionTime;
    }

    public String getSlAbcCode() {
        return this.slAbcCode;
    }

    public PartIntention slAbcCode(String slAbcCode) {
        this.setSlAbcCode(slAbcCode);
        return this;
    }

    public void setSlAbcCode(String slAbcCode) {
        this.slAbcCode = slAbcCode;
    }

    public String getProductionPlant() {
        return this.productionPlant;
    }

    public PartIntention productionPlant(String productionPlant) {
        this.setProductionPlant(productionPlant);
        return this;
    }

    public void setProductionPlant(String productionPlant) {
        this.productionPlant = productionPlant;
    }

    public String getLimitedDriving12Nc() {
        return this.limitedDriving12Nc;
    }

    public PartIntention limitedDriving12Nc(String limitedDriving12Nc) {
        this.setLimitedDriving12Nc(limitedDriving12Nc);
        return this;
    }

    public void setLimitedDriving12Nc(String limitedDriving12Nc) {
        this.limitedDriving12Nc = limitedDriving12Nc;
    }

    public String getLimitedDriving12Ncflag() {
        return this.limitedDriving12Ncflag;
    }

    public PartIntention limitedDriving12Ncflag(String limitedDriving12Ncflag) {
        this.setLimitedDriving12Ncflag(limitedDriving12Ncflag);
        return this;
    }

    public void setLimitedDriving12Ncflag(String limitedDriving12Ncflag) {
        this.limitedDriving12Ncflag = limitedDriving12Ncflag;
    }

    public String getMultiPlant() {
        return this.multiPlant;
    }

    public PartIntention multiPlant(String multiPlant) {
        this.setMultiPlant(multiPlant);
        return this;
    }

    public void setMultiPlant(String multiPlant) {
        this.multiPlant = multiPlant;
    }

    public String getType() {
        return this.type;
    }

    public PartIntention type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getSuccessorPartId() {
        return this.successorPartId;
    }

    public PartIntention successorPartId(Long successorPartId) {
        this.setSuccessorPartId(successorPartId);
        return this;
    }

    public void setSuccessorPartId(Long successorPartId) {
        this.successorPartId = successorPartId;
    }

    public Set<PlantSpecific> getPlantSpecifics() {
        return this.plantSpecifics;
    }

    public void setPlantSpecifics(Set<PlantSpecific> plantSpecifics) {
        this.plantSpecifics = plantSpecifics;
    }

    public PartIntention plantSpecifics(Set<PlantSpecific> plantSpecifics) {
        this.setPlantSpecifics(plantSpecifics);
        return this;
    }

    public PartIntention addPlantSpecific(PlantSpecific plantSpecific) {
        this.plantSpecifics.add(plantSpecific);
        plantSpecific.getPartIntentions().add(this);
        return this;
    }

    public PartIntention removePlantSpecific(PlantSpecific plantSpecific) {
        this.plantSpecifics.remove(plantSpecific);
        plantSpecific.getPartIntentions().remove(this);
        return this;
    }

    public Set<ItemReference> getItemReferences() {
        return this.itemReferences;
    }

    public void setItemReferences(Set<ItemReference> itemReferences) {
        this.itemReferences = itemReferences;
    }

    public PartIntention itemReferences(Set<ItemReference> itemReferences) {
        this.setItemReferences(itemReferences);
        return this;
    }

    public PartIntention addItemReference(ItemReference itemReference) {
        this.itemReferences.add(itemReference);
        itemReference.getPartIntentions().add(this);
        return this;
    }

    public PartIntention removeItemReference(ItemReference itemReference) {
        this.itemReferences.remove(itemReference);
        itemReference.getPartIntentions().remove(this);
        return this;
    }

    public Set<ReleasePackage> getReleasePackages() {
        return this.releasePackages;
    }

    public void setReleasePackages(Set<ReleasePackage> releasePackages) {
        this.releasePackages = releasePackages;
    }

    public PartIntention releasePackages(Set<ReleasePackage> releasePackages) {
        this.setReleasePackages(releasePackages);
        return this;
    }

    public PartIntention addReleasePackage(ReleasePackage releasePackage) {
        this.releasePackages.add(releasePackage);
        releasePackage.getPartIntentions().add(this);
        return this;
    }

    public PartIntention removeReleasePackage(ReleasePackage releasePackage) {
        this.releasePackages.remove(releasePackage);
        releasePackage.getPartIntentions().remove(this);
        return this;
    }

    public Part getPart() {
        return this.part;
    }

    public void setPart(Part part) {
        if (this.part != null) {
            this.part.setPartIntention(null);
        }
        if (part != null) {
            part.setPartIntention(this);
        }
        this.part = part;
    }

    public PartIntention part(Part part) {
        this.setPart(part);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PartIntention)) {
            return false;
        }
        return id != null && id.equals(((PartIntention) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PartIntention{" +
            "id=" + getId() +
            ", productId=" + getProductId() +
            ", revision='" + getRevision() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", vqi='" + getVqi() + "'" +
            ", procurementType='" + getProcurementType() + "'" +
            ", materialType='" + getMaterialType() + "'" +
            ", serialNumberProfile='" + getSerialNumberProfile() + "'" +
            ", criticalConfigurationItemIndicator='" + getCriticalConfigurationItemIndicator() + "'" +
            ", regularPartIndicator='" + getRegularPartIndicator() + "'" +
            ", historyIndicator='" + getHistoryIndicator() + "'" +
            ", crossPlantStatus='" + getCrossPlantStatus() + "'" +
            ", crossPlantStatusToBe='" + getCrossPlantStatusToBe() + "'" +
            ", toolPackCategory='" + getToolPackCategory() + "'" +
            ", tcChangeControl='" + getTcChangeControl() + "'" +
            ", sapChangeControl='" + getSapChangeControl() + "'" +
            ", allowBomRestructuring='" + getAllowBomRestructuring() + "'" +
            ", unitOfMeasure='" + getUnitOfMeasure() + "'" +
            ", itemUsage='" + getItemUsage() + "'" +
            ", isPhantom='" + getIsPhantom() + "'" +
            ", failureRate='" + getFailureRate() + "'" +
            ", inHouseProductionTime=" + getInHouseProductionTime() +
            ", slAbcCode='" + getSlAbcCode() + "'" +
            ", productionPlant='" + getProductionPlant() + "'" +
            ", limitedDriving12Nc='" + getLimitedDriving12Nc() + "'" +
            ", limitedDriving12Ncflag='" + getLimitedDriving12Ncflag() + "'" +
            ", multiPlant='" + getMultiPlant() + "'" +
            ", type='" + getType() + "'" +
            ", successorPartId=" + getSuccessorPartId() +
            "}";
    }
}
