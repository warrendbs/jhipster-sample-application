import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDocumentIntention, DocumentIntention } from '../document-intention.model';
import { DocumentIntentionService } from '../service/document-intention.service';
import { IReleasePackage } from 'app/entities/jhipsterSampleApplication/release-package/release-package.model';
import { ReleasePackageService } from 'app/entities/jhipsterSampleApplication/release-package/service/release-package.service';

@Component({
  selector: 'jhi-document-intention-update',
  templateUrl: './document-intention-update.component.html',
})
export class DocumentIntentionUpdateComponent implements OnInit {
  isSaving = false;

  releasePackagesSharedCollection: IReleasePackage[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    description: [],
    changeIndicator: [],
    type: [],
    subtype: [],
    group: [],
    sheet: [],
    releasePackages: [],
  });

  constructor(
    protected documentIntentionService: DocumentIntentionService,
    protected releasePackageService: ReleasePackageService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documentIntention }) => {
      this.updateForm(documentIntention);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const documentIntention = this.createFromForm();
    if (documentIntention.id !== undefined) {
      this.subscribeToSaveResponse(this.documentIntentionService.update(documentIntention));
    } else {
      this.subscribeToSaveResponse(this.documentIntentionService.create(documentIntention));
    }
  }

  trackReleasePackageById(_index: number, item: IReleasePackage): number {
    return item.id!;
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocumentIntention>>): void {
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

  protected updateForm(documentIntention: IDocumentIntention): void {
    this.editForm.patchValue({
      id: documentIntention.id,
      name: documentIntention.name,
      description: documentIntention.description,
      changeIndicator: documentIntention.changeIndicator,
      type: documentIntention.type,
      subtype: documentIntention.subtype,
      group: documentIntention.group,
      sheet: documentIntention.sheet,
      releasePackages: documentIntention.releasePackages,
    });

    this.releasePackagesSharedCollection = this.releasePackageService.addReleasePackageToCollectionIfMissing(
      this.releasePackagesSharedCollection,
      ...(documentIntention.releasePackages ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
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

  protected createFromForm(): IDocumentIntention {
    return {
      ...new DocumentIntention(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      changeIndicator: this.editForm.get(['changeIndicator'])!.value,
      type: this.editForm.get(['type'])!.value,
      subtype: this.editForm.get(['subtype'])!.value,
      group: this.editForm.get(['group'])!.value,
      sheet: this.editForm.get(['sheet'])!.value,
      releasePackages: this.editForm.get(['releasePackages'])!.value,
    };
  }
}
