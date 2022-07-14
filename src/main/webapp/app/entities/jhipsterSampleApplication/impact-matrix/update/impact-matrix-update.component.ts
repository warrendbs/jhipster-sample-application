import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IImpactMatrix, ImpactMatrix } from '../impact-matrix.model';
import { ImpactMatrixService } from '../service/impact-matrix.service';

@Component({
  selector: 'jhi-impact-matrix-update',
  templateUrl: './impact-matrix-update.component.html',
})
export class ImpactMatrixUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    impactMatrixNumber: [],
    status: [],
    revision: [],
    reviser: [],
    revisionDescription: [],
    dateRevised: [],
    title: [],
    isAutoLayoutEnabled: [],
  });

  constructor(protected impactMatrixService: ImpactMatrixService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ impactMatrix }) => {
      this.updateForm(impactMatrix);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const impactMatrix = this.createFromForm();
    if (impactMatrix.id !== undefined) {
      this.subscribeToSaveResponse(this.impactMatrixService.update(impactMatrix));
    } else {
      this.subscribeToSaveResponse(this.impactMatrixService.create(impactMatrix));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IImpactMatrix>>): void {
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

  protected updateForm(impactMatrix: IImpactMatrix): void {
    this.editForm.patchValue({
      id: impactMatrix.id,
      impactMatrixNumber: impactMatrix.impactMatrixNumber,
      status: impactMatrix.status,
      revision: impactMatrix.revision,
      reviser: impactMatrix.reviser,
      revisionDescription: impactMatrix.revisionDescription,
      dateRevised: impactMatrix.dateRevised,
      title: impactMatrix.title,
      isAutoLayoutEnabled: impactMatrix.isAutoLayoutEnabled,
    });
  }

  protected createFromForm(): IImpactMatrix {
    return {
      ...new ImpactMatrix(),
      id: this.editForm.get(['id'])!.value,
      impactMatrixNumber: this.editForm.get(['impactMatrixNumber'])!.value,
      status: this.editForm.get(['status'])!.value,
      revision: this.editForm.get(['revision'])!.value,
      reviser: this.editForm.get(['reviser'])!.value,
      revisionDescription: this.editForm.get(['revisionDescription'])!.value,
      dateRevised: this.editForm.get(['dateRevised'])!.value,
      title: this.editForm.get(['title'])!.value,
      isAutoLayoutEnabled: this.editForm.get(['isAutoLayoutEnabled'])!.value,
    };
  }
}
