import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IContext, Context } from '../context.model';
import { ContextService } from '../service/context.service';
import { IImpactMatrix } from 'app/entities/jhipsterSampleApplication/impact-matrix/impact-matrix.model';
import { ImpactMatrixService } from 'app/entities/jhipsterSampleApplication/impact-matrix/service/impact-matrix.service';

@Component({
  selector: 'jhi-context-update',
  templateUrl: './context-update.component.html',
})
export class ContextUpdateComponent implements OnInit {
  isSaving = false;

  impactMatricesSharedCollection: IImpactMatrix[] = [];

  editForm = this.fb.group({
    id: [],
    type: [],
    name: [],
    status: [],
    impactMatrix: [],
  });

  constructor(
    protected contextService: ContextService,
    protected impactMatrixService: ImpactMatrixService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ context }) => {
      this.updateForm(context);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const context = this.createFromForm();
    if (context.id !== undefined) {
      this.subscribeToSaveResponse(this.contextService.update(context));
    } else {
      this.subscribeToSaveResponse(this.contextService.create(context));
    }
  }

  trackImpactMatrixById(_index: number, item: IImpactMatrix): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContext>>): void {
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

  protected updateForm(context: IContext): void {
    this.editForm.patchValue({
      id: context.id,
      type: context.type,
      name: context.name,
      status: context.status,
      impactMatrix: context.impactMatrix,
    });

    this.impactMatricesSharedCollection = this.impactMatrixService.addImpactMatrixToCollectionIfMissing(
      this.impactMatricesSharedCollection,
      context.impactMatrix
    );
  }

  protected loadRelationshipsOptions(): void {
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

  protected createFromForm(): IContext {
    return {
      ...new Context(),
      id: this.editForm.get(['id'])!.value,
      type: this.editForm.get(['type'])!.value,
      name: this.editForm.get(['name'])!.value,
      status: this.editForm.get(['status'])!.value,
      impactMatrix: this.editForm.get(['impactMatrix'])!.value,
    };
  }
}
