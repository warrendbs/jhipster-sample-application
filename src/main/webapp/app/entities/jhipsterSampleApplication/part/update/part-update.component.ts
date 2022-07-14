import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPart, Part } from '../part.model';
import { PartService } from '../service/part.service';
import { IPartSource } from 'app/entities/jhipsterSampleApplication/part-source/part-source.model';
import { PartSourceService } from 'app/entities/jhipsterSampleApplication/part-source/service/part-source.service';
import { IPartIntention } from 'app/entities/jhipsterSampleApplication/part-intention/part-intention.model';
import { PartIntentionService } from 'app/entities/jhipsterSampleApplication/part-intention/service/part-intention.service';
import { IImpactMatrix } from 'app/entities/jhipsterSampleApplication/impact-matrix/impact-matrix.model';
import { ImpactMatrixService } from 'app/entities/jhipsterSampleApplication/impact-matrix/service/impact-matrix.service';

@Component({
  selector: 'jhi-part-update',
  templateUrl: './part-update.component.html',
})
export class PartUpdateComponent implements OnInit {
  isSaving = false;

  partSourcesCollection: IPartSource[] = [];
  partIntentionsCollection: IPartIntention[] = [];
  impactMatricesSharedCollection: IImpactMatrix[] = [];

  editForm = this.fb.group({
    id: [],
    status: [],
    changeIndication: [],
    isParentPartBomChanged: [],
    partSource: [],
    partIntention: [],
    impactMatrix: [],
  });

  constructor(
    protected partService: PartService,
    protected partSourceService: PartSourceService,
    protected partIntentionService: PartIntentionService,
    protected impactMatrixService: ImpactMatrixService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ part }) => {
      this.updateForm(part);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const part = this.createFromForm();
    if (part.id !== undefined) {
      this.subscribeToSaveResponse(this.partService.update(part));
    } else {
      this.subscribeToSaveResponse(this.partService.create(part));
    }
  }

  trackPartSourceById(_index: number, item: IPartSource): number {
    return item.id!;
  }

  trackPartIntentionById(_index: number, item: IPartIntention): number {
    return item.id!;
  }

  trackImpactMatrixById(_index: number, item: IImpactMatrix): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPart>>): void {
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

  protected updateForm(part: IPart): void {
    this.editForm.patchValue({
      id: part.id,
      status: part.status,
      changeIndication: part.changeIndication,
      isParentPartBomChanged: part.isParentPartBomChanged,
      partSource: part.partSource,
      partIntention: part.partIntention,
      impactMatrix: part.impactMatrix,
    });

    this.partSourcesCollection = this.partSourceService.addPartSourceToCollectionIfMissing(this.partSourcesCollection, part.partSource);
    this.partIntentionsCollection = this.partIntentionService.addPartIntentionToCollectionIfMissing(
      this.partIntentionsCollection,
      part.partIntention
    );
    this.impactMatricesSharedCollection = this.impactMatrixService.addImpactMatrixToCollectionIfMissing(
      this.impactMatricesSharedCollection,
      part.impactMatrix
    );
  }

  protected loadRelationshipsOptions(): void {
    this.partSourceService
      .query({ filter: 'part-is-null' })
      .pipe(map((res: HttpResponse<IPartSource[]>) => res.body ?? []))
      .pipe(
        map((partSources: IPartSource[]) =>
          this.partSourceService.addPartSourceToCollectionIfMissing(partSources, this.editForm.get('partSource')!.value)
        )
      )
      .subscribe((partSources: IPartSource[]) => (this.partSourcesCollection = partSources));

    this.partIntentionService
      .query({ filter: 'part-is-null' })
      .pipe(map((res: HttpResponse<IPartIntention[]>) => res.body ?? []))
      .pipe(
        map((partIntentions: IPartIntention[]) =>
          this.partIntentionService.addPartIntentionToCollectionIfMissing(partIntentions, this.editForm.get('partIntention')!.value)
        )
      )
      .subscribe((partIntentions: IPartIntention[]) => (this.partIntentionsCollection = partIntentions));

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

  protected createFromForm(): IPart {
    return {
      ...new Part(),
      id: this.editForm.get(['id'])!.value,
      status: this.editForm.get(['status'])!.value,
      changeIndication: this.editForm.get(['changeIndication'])!.value,
      isParentPartBomChanged: this.editForm.get(['isParentPartBomChanged'])!.value,
      partSource: this.editForm.get(['partSource'])!.value,
      partIntention: this.editForm.get(['partIntention'])!.value,
      impactMatrix: this.editForm.get(['impactMatrix'])!.value,
    };
  }
}
