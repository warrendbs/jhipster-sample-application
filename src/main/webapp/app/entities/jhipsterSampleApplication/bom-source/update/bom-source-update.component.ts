import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IBomSource, BomSource } from '../bom-source.model';
import { BomSourceService } from '../service/bom-source.service';
import { IBomChild } from 'app/entities/jhipsterSampleApplication/bom-child/bom-child.model';
import { BomChildService } from 'app/entities/jhipsterSampleApplication/bom-child/service/bom-child.service';

@Component({
  selector: 'jhi-bom-source-update',
  templateUrl: './bom-source-update.component.html',
})
export class BomSourceUpdateComponent implements OnInit {
  isSaving = false;

  bomChildrenSharedCollection: IBomChild[] = [];

  editForm = this.fb.group({
    id: [],
    type: [],
    bomChildren: [],
  });

  constructor(
    protected bomSourceService: BomSourceService,
    protected bomChildService: BomChildService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bomSource }) => {
      this.updateForm(bomSource);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bomSource = this.createFromForm();
    if (bomSource.id !== undefined) {
      this.subscribeToSaveResponse(this.bomSourceService.update(bomSource));
    } else {
      this.subscribeToSaveResponse(this.bomSourceService.create(bomSource));
    }
  }

  trackBomChildById(_index: number, item: IBomChild): number {
    return item.id!;
  }

  getSelectedBomChild(option: IBomChild, selectedVals?: IBomChild[]): IBomChild {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBomSource>>): void {
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

  protected updateForm(bomSource: IBomSource): void {
    this.editForm.patchValue({
      id: bomSource.id,
      type: bomSource.type,
      bomChildren: bomSource.bomChildren,
    });

    this.bomChildrenSharedCollection = this.bomChildService.addBomChildToCollectionIfMissing(
      this.bomChildrenSharedCollection,
      ...(bomSource.bomChildren ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.bomChildService
      .query()
      .pipe(map((res: HttpResponse<IBomChild[]>) => res.body ?? []))
      .pipe(
        map((bomChildren: IBomChild[]) =>
          this.bomChildService.addBomChildToCollectionIfMissing(bomChildren, ...(this.editForm.get('bomChildren')!.value ?? []))
        )
      )
      .subscribe((bomChildren: IBomChild[]) => (this.bomChildrenSharedCollection = bomChildren));
  }

  protected createFromForm(): IBomSource {
    return {
      ...new BomSource(),
      id: this.editForm.get(['id'])!.value,
      type: this.editForm.get(['type'])!.value,
      bomChildren: this.editForm.get(['bomChildren'])!.value,
    };
  }
}
