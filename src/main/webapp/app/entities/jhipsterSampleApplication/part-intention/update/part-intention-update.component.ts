import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPartIntention, PartIntention } from '../part-intention.model';
import { PartIntentionService } from '../service/part-intention.service';
import { IPlantSpecific } from 'app/entities/jhipsterSampleApplication/plant-specific/plant-specific.model';
import { PlantSpecificService } from 'app/entities/jhipsterSampleApplication/plant-specific/service/plant-specific.service';
import { IItemReference } from 'app/entities/jhipsterSampleApplication/item-reference/item-reference.model';
import { ItemReferenceService } from 'app/entities/jhipsterSampleApplication/item-reference/service/item-reference.service';
import { IReleasePackage } from 'app/entities/jhipsterSampleApplication/release-package/release-package.model';
import { ReleasePackageService } from 'app/entities/jhipsterSampleApplication/release-package/service/release-package.service';

@Component({
  selector: 'jhi-part-intention-update',
  templateUrl: './part-intention-update.component.html',
})
export class PartIntentionUpdateComponent implements OnInit {
  isSaving = false;

  plantSpecificsSharedCollection: IPlantSpecific[] = [];
  itemReferencesSharedCollection: IItemReference[] = [];
  releasePackagesSharedCollection: IReleasePackage[] = [];

  editForm = this.fb.group({
    id: [],
    productId: [],
    revision: [],
    name: [],
    description: [],
    vqi: [],
    procurementType: [],
    materialType: [],
    serialNumberProfile: [],
    criticalConfigurationItemIndicator: [],
    regularPartIndicator: [],
    historyIndicator: [],
    crossPlantStatus: [],
    crossPlantStatusToBe: [],
    toolPackCategory: [],
    tcChangeControl: [],
    sapChangeControl: [],
    allowBomRestructuring: [],
    unitOfMeasure: [],
    itemUsage: [],
    isPhantom: [],
    failureRate: [],
    inHouseProductionTime: [],
    slAbcCode: [],
    productionPlant: [],
    limitedDriving12Nc: [],
    limitedDriving12Ncflag: [],
    multiPlant: [],
    type: [],
    successorPartId: [],
    plantSpecifics: [],
    itemReferences: [],
    releasePackages: [],
  });

  constructor(
    protected partIntentionService: PartIntentionService,
    protected plantSpecificService: PlantSpecificService,
    protected itemReferenceService: ItemReferenceService,
    protected releasePackageService: ReleasePackageService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ partIntention }) => {
      this.updateForm(partIntention);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const partIntention = this.createFromForm();
    if (partIntention.id !== undefined) {
      this.subscribeToSaveResponse(this.partIntentionService.update(partIntention));
    } else {
      this.subscribeToSaveResponse(this.partIntentionService.create(partIntention));
    }
  }

  trackPlantSpecificById(_index: number, item: IPlantSpecific): number {
    return item.id!;
  }

  trackItemReferenceById(_index: number, item: IItemReference): number {
    return item.id!;
  }

  trackReleasePackageById(_index: number, item: IReleasePackage): number {
    return item.id!;
  }

  getSelectedPlantSpecific(option: IPlantSpecific, selectedVals?: IPlantSpecific[]): IPlantSpecific {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  getSelectedItemReference(option: IItemReference, selectedVals?: IItemReference[]): IItemReference {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  getSelectedReleasePackage(option: IReleasePackage, selectedVals?: IReleasePackage[]): IReleasePackage {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPartIntention>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(partIntention: IPartIntention): void {
    this.editForm.patchValue({
      id: partIntention.id,
      productId: partIntention.productId,
      revision: partIntention.revision,
      name: partIntention.name,
      description: partIntention.description,
      vqi: partIntention.vqi,
      procurementType: partIntention.procurementType,
      materialType: partIntention.materialType,
      serialNumberProfile: partIntention.serialNumberProfile,
      criticalConfigurationItemIndicator: partIntention.criticalConfigurationItemIndicator,
      regularPartIndicator: partIntention.regularPartIndicator,
      historyIndicator: partIntention.historyIndicator,
      crossPlantStatus: partIntention.crossPlantStatus,
      crossPlantStatusToBe: partIntention.crossPlantStatusToBe,
      toolPackCategory: partIntention.toolPackCategory,
      tcChangeControl: partIntention.tcChangeControl,
      sapChangeControl: partIntention.sapChangeControl,
      allowBomRestructuring: partIntention.allowBomRestructuring,
      unitOfMeasure: partIntention.unitOfMeasure,
      itemUsage: partIntention.itemUsage,
      isPhantom: partIntention.isPhantom,
      failureRate: partIntention.failureRate,
      inHouseProductionTime: partIntention.inHouseProductionTime,
      slAbcCode: partIntention.slAbcCode,
      productionPlant: partIntention.productionPlant,
      limitedDriving12Nc: partIntention.limitedDriving12Nc,
      limitedDriving12Ncflag: partIntention.limitedDriving12Ncflag,
      multiPlant: partIntention.multiPlant,
      type: partIntention.type,
      successorPartId: partIntention.successorPartId,
      plantSpecifics: partIntention.plantSpecifics,
      itemReferences: partIntention.itemReferences,
      releasePackages: partIntention.releasePackages,
    });

    this.plantSpecificsSharedCollection = this.plantSpecificService.addPlantSpecificToCollectionIfMissing(
      this.plantSpecificsSharedCollection,
      ...(partIntention.plantSpecifics ?? [])
    );
    this.itemReferencesSharedCollection = this.itemReferenceService.addItemReferenceToCollectionIfMissing(
      this.itemReferencesSharedCollection,
      ...(partIntention.itemReferences ?? [])
    );
    this.releasePackagesSharedCollection = this.releasePackageService.addReleasePackageToCollectionIfMissing(
      this.releasePackagesSharedCollection,
      ...(partIntention.releasePackages ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.plantSpecificService
      .query()
      .pipe(map((res: HttpResponse<IPlantSpecific[]>) => res.body ?? []))
      .pipe(
        map((plantSpecifics: IPlantSpecific[]) =>
          this.plantSpecificService.addPlantSpecificToCollectionIfMissing(
            plantSpecifics,
            ...(this.editForm.get('plantSpecifics')!.value ?? [])
          )
        )
      )
      .subscribe((plantSpecifics: IPlantSpecific[]) => (this.plantSpecificsSharedCollection = plantSpecifics));

    this.itemReferenceService
      .query()
      .pipe(map((res: HttpResponse<IItemReference[]>) => res.body ?? []))
      .pipe(
        map((itemReferences: IItemReference[]) =>
          this.itemReferenceService.addItemReferenceToCollectionIfMissing(
            itemReferences,
            ...(this.editForm.get('itemReferences')!.value ?? [])
          )
        )
      )
      .subscribe((itemReferences: IItemReference[]) => (this.itemReferencesSharedCollection = itemReferences));

    this.releasePackageService
      .query()
      .pipe(map((res: HttpResponse<IReleasePackage[]>) => res.body ?? []))
      .pipe(
        map((releasePackages: IReleasePackage[]) =>
          this.releasePackageService.addReleasePackageToCollectionIfMissing(
            releasePackages,
            ...(this.editForm.get('releasePackages')!.value ?? [])
          )
        )
      )
      .subscribe((releasePackages: IReleasePackage[]) => (this.releasePackagesSharedCollection = releasePackages));
  }

  protected createFromForm(): IPartIntention {
    return {
      ...new PartIntention(),
      id: this.editForm.get(['id'])!.value,
      productId: this.editForm.get(['productId'])!.value,
      revision: this.editForm.get(['revision'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      vqi: this.editForm.get(['vqi'])!.value,
      procurementType: this.editForm.get(['procurementType'])!.value,
      materialType: this.editForm.get(['materialType'])!.value,
      serialNumberProfile: this.editForm.get(['serialNumberProfile'])!.value,
      criticalConfigurationItemIndicator: this.editForm.get(['criticalConfigurationItemIndicator'])!.value,
      regularPartIndicator: this.editForm.get(['regularPartIndicator'])!.value,
      historyIndicator: this.editForm.get(['historyIndicator'])!.value,
      crossPlantStatus: this.editForm.get(['crossPlantStatus'])!.value,
      crossPlantStatusToBe: this.editForm.get(['crossPlantStatusToBe'])!.value,
      toolPackCategory: this.editForm.get(['toolPackCategory'])!.value,
      tcChangeControl: this.editForm.get(['tcChangeControl'])!.value,
      sapChangeControl: this.editForm.get(['sapChangeControl'])!.value,
      allowBomRestructuring: this.editForm.get(['allowBomRestructuring'])!.value,
      unitOfMeasure: this.editForm.get(['unitOfMeasure'])!.value,
      itemUsage: this.editForm.get(['itemUsage'])!.value,
      isPhantom: this.editForm.get(['isPhantom'])!.value,
      failureRate: this.editForm.get(['failureRate'])!.value,
      inHouseProductionTime: this.editForm.get(['inHouseProductionTime'])!.value,
      slAbcCode: this.editForm.get(['slAbcCode'])!.value,
      productionPlant: this.editForm.get(['productionPlant'])!.value,
      limitedDriving12Nc: this.editForm.get(['limitedDriving12Nc'])!.value,
      limitedDriving12Ncflag: this.editForm.get(['limitedDriving12Ncflag'])!.value,
      multiPlant: this.editForm.get(['multiPlant'])!.value,
      type: this.editForm.get(['type'])!.value,
      successorPartId: this.editForm.get(['successorPartId'])!.value,
      plantSpecifics: this.editForm.get(['plantSpecifics'])!.value,
      itemReferences: this.editForm.get(['itemReferences'])!.value,
      releasePackages: this.editForm.get(['releasePackages'])!.value,
    };
  }
}
