import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPartSource, PartSource } from '../part-source.model';
import { PartSourceService } from '../service/part-source.service';
import { IPlantSpecific } from 'app/entities/jhipsterSampleApplication/plant-specific/plant-specific.model';
import { PlantSpecificService } from 'app/entities/jhipsterSampleApplication/plant-specific/service/plant-specific.service';

@Component({
  selector: 'jhi-part-source-update',
  templateUrl: './part-source-update.component.html',
})
export class PartSourceUpdateComponent implements OnInit {
  isSaving = false;

  plantSpecificsSharedCollection: IPlantSpecific[] = [];

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
  });

  constructor(
    protected partSourceService: PartSourceService,
    protected plantSpecificService: PlantSpecificService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ partSource }) => {
      this.updateForm(partSource);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const partSource = this.createFromForm();
    if (partSource.id !== undefined) {
      this.subscribeToSaveResponse(this.partSourceService.update(partSource));
    } else {
      this.subscribeToSaveResponse(this.partSourceService.create(partSource));
    }
  }

  trackPlantSpecificById(_index: number, item: IPlantSpecific): number {
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPartSource>>): void {
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

  protected updateForm(partSource: IPartSource): void {
    this.editForm.patchValue({
      id: partSource.id,
      productId: partSource.productId,
      revision: partSource.revision,
      name: partSource.name,
      description: partSource.description,
      vqi: partSource.vqi,
      procurementType: partSource.procurementType,
      materialType: partSource.materialType,
      serialNumberProfile: partSource.serialNumberProfile,
      criticalConfigurationItemIndicator: partSource.criticalConfigurationItemIndicator,
      regularPartIndicator: partSource.regularPartIndicator,
      historyIndicator: partSource.historyIndicator,
      crossPlantStatus: partSource.crossPlantStatus,
      crossPlantStatusToBe: partSource.crossPlantStatusToBe,
      toolPackCategory: partSource.toolPackCategory,
      tcChangeControl: partSource.tcChangeControl,
      sapChangeControl: partSource.sapChangeControl,
      allowBomRestructuring: partSource.allowBomRestructuring,
      unitOfMeasure: partSource.unitOfMeasure,
      itemUsage: partSource.itemUsage,
      isPhantom: partSource.isPhantom,
      failureRate: partSource.failureRate,
      inHouseProductionTime: partSource.inHouseProductionTime,
      slAbcCode: partSource.slAbcCode,
      productionPlant: partSource.productionPlant,
      limitedDriving12Nc: partSource.limitedDriving12Nc,
      limitedDriving12Ncflag: partSource.limitedDriving12Ncflag,
      multiPlant: partSource.multiPlant,
      type: partSource.type,
      successorPartId: partSource.successorPartId,
      plantSpecifics: partSource.plantSpecifics,
    });

    this.plantSpecificsSharedCollection = this.plantSpecificService.addPlantSpecificToCollectionIfMissing(
      this.plantSpecificsSharedCollection,
      ...(partSource.plantSpecifics ?? [])
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
  }

  protected createFromForm(): IPartSource {
    return {
      ...new PartSource(),
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
    };
  }
}
