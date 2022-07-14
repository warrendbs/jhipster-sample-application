import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IBomIntention, BomIntention } from '../bom-intention.model';
import { BomIntentionService } from '../service/bom-intention.service';
import { IBomChild } from 'app/entities/jhipsterSampleApplication/bom-child/bom-child.model';
import { BomChildService } from 'app/entities/jhipsterSampleApplication/bom-child/service/bom-child.service';
import { IReleasePackage } from 'app/entities/jhipsterSampleApplication/release-package/release-package.model';
import { ReleasePackageService } from 'app/entities/jhipsterSampleApplication/release-package/service/release-package.service';

@Component({
  selector: 'jhi-bom-intention-update',
  templateUrl: './bom-intention-update.component.html',
})
export class BomIntentionUpdateComponent implements OnInit {
  isSaving = false;

  bomChildrenSharedCollection: IBomChild[] = [];
  releasePackagesSharedCollection: IReleasePackage[] = [];

  editForm = this.fb.group({
    id: [],
    type: [],
    bomChildren: [],
    releasePackages: [],
  });

  constructor(
    protected bomIntentionService: BomIntentionService,
    protected bomChildService: BomChildService,
    protected releasePackageService: ReleasePackageService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bomIntention }) => {
      this.updateForm(bomIntention);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bomIntention = this.createFromForm();
    if (bomIntention.id !== undefined) {
      this.subscribeToSaveResponse(this.bomIntentionService.update(bomIntention));
    } else {
      this.subscribeToSaveResponse(this.bomIntentionService.create(bomIntention));
    }
  }

  trackBomChildById(_index: number, item: IBomChild): number {
    return item.id!;
  }

  trackReleasePackageById(_index: number, item: IReleasePackage): number {
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBomIntention>>): void {
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

  protected updateForm(bomIntention: IBomIntention): void {
    this.editForm.patchValue({
      id: bomIntention.id,
      type: bomIntention.type,
      bomChildren: bomIntention.bomChildren,
      releasePackages: bomIntention.releasePackages,
    });

    this.bomChildrenSharedCollection = this.bomChildService.addBomChildToCollectionIfMissing(
      this.bomChildrenSharedCollection,
      ...(bomIntention.bomChildren ?? [])
    );
    this.releasePackagesSharedCollection = this.releasePackageService.addReleasePackageToCollectionIfMissing(
      this.releasePackagesSharedCollection,
      ...(bomIntention.releasePackages ?? [])
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

  protected createFromForm(): IBomIntention {
    return {
      ...new BomIntention(),
      id: this.editForm.get(['id'])!.value,
      type: this.editForm.get(['type'])!.value,
      bomChildren: this.editForm.get(['bomChildren'])!.value,
      releasePackages: this.editForm.get(['releasePackages'])!.value,
    };
  }
}
