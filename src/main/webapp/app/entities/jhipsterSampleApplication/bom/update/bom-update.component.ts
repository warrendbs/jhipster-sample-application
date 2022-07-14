import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IBom, Bom } from '../bom.model';
import { BomService } from '../service/bom.service';
import { IBomSource } from 'app/entities/jhipsterSampleApplication/bom-source/bom-source.model';
import { BomSourceService } from 'app/entities/jhipsterSampleApplication/bom-source/service/bom-source.service';
import { IBomIntention } from 'app/entities/jhipsterSampleApplication/bom-intention/bom-intention.model';
import { BomIntentionService } from 'app/entities/jhipsterSampleApplication/bom-intention/service/bom-intention.service';
import { IItemReference } from 'app/entities/jhipsterSampleApplication/item-reference/item-reference.model';
import { ItemReferenceService } from 'app/entities/jhipsterSampleApplication/item-reference/service/item-reference.service';
import { IImpactMatrix } from 'app/entities/jhipsterSampleApplication/impact-matrix/impact-matrix.model';
import { ImpactMatrixService } from 'app/entities/jhipsterSampleApplication/impact-matrix/service/impact-matrix.service';

@Component({
  selector: 'jhi-bom-update',
  templateUrl: './bom-update.component.html',
})
export class BomUpdateComponent implements OnInit {
  isSaving = false;

  bomSourcesCollection: IBomSource[] = [];
  bomIntentionsCollection: IBomIntention[] = [];
  itemReferencesSharedCollection: IItemReference[] = [];
  impactMatricesSharedCollection: IImpactMatrix[] = [];

  editForm = this.fb.group({
    id: [],
    status: [],
    bomSource: [],
    bomIntention: [],
    itemReferences: [],
    impactMatrix: [],
  });

  constructor(
    protected bomService: BomService,
    protected bomSourceService: BomSourceService,
    protected bomIntentionService: BomIntentionService,
    protected itemReferenceService: ItemReferenceService,
    protected impactMatrixService: ImpactMatrixService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bom }) => {
      this.updateForm(bom);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bom = this.createFromForm();
    if (bom.id !== undefined) {
      this.subscribeToSaveResponse(this.bomService.update(bom));
    } else {
      this.subscribeToSaveResponse(this.bomService.create(bom));
    }
  }

  trackBomSourceById(_index: number, item: IBomSource): number {
    return item.id!;
  }

  trackBomIntentionById(_index: number, item: IBomIntention): number {
    return item.id!;
  }

  trackItemReferenceById(_index: number, item: IItemReference): number {
    return item.id!;
  }

  trackImpactMatrixById(_index: number, item: IImpactMatrix): number {
    return item.id!;
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBom>>): void {
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

  protected updateForm(bom: IBom): void {
    this.editForm.patchValue({
      id: bom.id,
      status: bom.status,
      bomSource: bom.bomSource,
      bomIntention: bom.bomIntention,
      itemReferences: bom.itemReferences,
      impactMatrix: bom.impactMatrix,
    });

    this.bomSourcesCollection = this.bomSourceService.addBomSourceToCollectionIfMissing(this.bomSourcesCollection, bom.bomSource);
    this.bomIntentionsCollection = this.bomIntentionService.addBomIntentionToCollectionIfMissing(
      this.bomIntentionsCollection,
      bom.bomIntention
    );
    this.itemReferencesSharedCollection = this.itemReferenceService.addItemReferenceToCollectionIfMissing(
      this.itemReferencesSharedCollection,
      ...(bom.itemReferences ?? [])
    );
    this.impactMatricesSharedCollection = this.impactMatrixService.addImpactMatrixToCollectionIfMissing(
      this.impactMatricesSharedCollection,
      bom.impactMatrix
    );
  }

  protected loadRelationshipsOptions(): void {
    this.bomSourceService
      .query({ filter: 'bom-is-null' })
      .pipe(map((res: HttpResponse<IBomSource[]>) => res.body ?? []))
      .pipe(
        map((bomSources: IBomSource[]) =>
          this.bomSourceService.addBomSourceToCollectionIfMissing(bomSources, this.editForm.get('bomSource')!.value)
        )
      )
      .subscribe((bomSources: IBomSource[]) => (this.bomSourcesCollection = bomSources));

    this.bomIntentionService
      .query({ filter: 'bom-is-null' })
      .pipe(map((res: HttpResponse<IBomIntention[]>) => res.body ?? []))
      .pipe(
        map((bomIntentions: IBomIntention[]) =>
          this.bomIntentionService.addBomIntentionToCollectionIfMissing(bomIntentions, this.editForm.get('bomIntention')!.value)
        )
      )
      .subscribe((bomIntentions: IBomIntention[]) => (this.bomIntentionsCollection = bomIntentions));

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

    this.impactMatrixService
      .query()
      .pipe(map((res: HttpResponse<IImpactMatrix[]>) => res.body ?? []))
      .pipe(
        map((impactMatrices: IImpactMatrix[]) =>
          this.impactMatrixService.addImpactMatrixToCollectionIfMissing(impactMatrices, this.editForm.get('impactMatrix')!.value)
        )
      )
      .subscribe((impactMatrices: IImpactMatrix[]) => (this.impactMatricesSharedCollection = impactMatrices));
  }

  protected createFromForm(): IBom {
    return {
      ...new Bom(),
      id: this.editForm.get(['id'])!.value,
      status: this.editForm.get(['status'])!.value,
      bomSource: this.editForm.get(['bomSource'])!.value,
      bomIntention: this.editForm.get(['bomIntention'])!.value,
      itemReferences: this.editForm.get(['itemReferences'])!.value,
      impactMatrix: this.editForm.get(['impactMatrix'])!.value,
    };
  }
}
