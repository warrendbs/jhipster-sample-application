import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IItemReference, ItemReference } from '../item-reference.model';
import { ItemReferenceService } from '../service/item-reference.service';

@Component({
  selector: 'jhi-item-reference-update',
  templateUrl: './item-reference-update.component.html',
})
export class ItemReferenceUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    referenceId: [],
    type: [],
  });

  constructor(protected itemReferenceService: ItemReferenceService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ itemReference }) => {
      this.updateForm(itemReference);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const itemReference = this.createFromForm();
    if (itemReference.id !== undefined) {
      this.subscribeToSaveResponse(this.itemReferenceService.update(itemReference));
    } else {
      this.subscribeToSaveResponse(this.itemReferenceService.create(itemReference));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IItemReference>>): void {
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

  protected updateForm(itemReference: IItemReference): void {
    this.editForm.patchValue({
      id: itemReference.id,
      referenceId: itemReference.referenceId,
      type: itemReference.type,
    });
  }

  protected createFromForm(): IItemReference {
    return {
      ...new ItemReference(),
      id: this.editForm.get(['id'])!.value,
      referenceId: this.editForm.get(['referenceId'])!.value,
      type: this.editForm.get(['type'])!.value,
    };
  }
}
