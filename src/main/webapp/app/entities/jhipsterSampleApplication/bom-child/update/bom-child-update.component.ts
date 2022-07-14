import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IBomChild, BomChild } from '../bom-child.model';
import { BomChildService } from '../service/bom-child.service';

@Component({
  selector: 'jhi-bom-child-update',
  templateUrl: './bom-child-update.component.html',
})
export class BomChildUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    productId: [],
    revision: [],
    quantity: [],
    relationType: [],
  });

  constructor(protected bomChildService: BomChildService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bomChild }) => {
      this.updateForm(bomChild);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bomChild = this.createFromForm();
    if (bomChild.id !== undefined) {
      this.subscribeToSaveResponse(this.bomChildService.update(bomChild));
    } else {
      this.subscribeToSaveResponse(this.bomChildService.create(bomChild));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBomChild>>): void {
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

  protected updateForm(bomChild: IBomChild): void {
    this.editForm.patchValue({
      id: bomChild.id,
      productId: bomChild.productId,
      revision: bomChild.revision,
      quantity: bomChild.quantity,
      relationType: bomChild.relationType,
    });
  }

  protected createFromForm(): IBomChild {
    return {
      ...new BomChild(),
      id: this.editForm.get(['id'])!.value,
      productId: this.editForm.get(['productId'])!.value,
      revision: this.editForm.get(['revision'])!.value,
      quantity: this.editForm.get(['quantity'])!.value,
      relationType: this.editForm.get(['relationType'])!.value,
    };
  }
}
