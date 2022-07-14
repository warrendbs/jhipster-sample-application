import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IReleasePackage, ReleasePackage } from '../release-package.model';
import { ReleasePackageService } from '../service/release-package.service';

@Component({
  selector: 'jhi-release-package-update',
  templateUrl: './release-package-update.component.html',
})
export class ReleasePackageUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    title: [],
    releasePackageNumber: [],
    releasePackageTitle: [],
    status: [],
    ecn: [],
  });

  constructor(
    protected releasePackageService: ReleasePackageService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ releasePackage }) => {
      this.updateForm(releasePackage);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const releasePackage = this.createFromForm();
    if (releasePackage.id !== undefined) {
      this.subscribeToSaveResponse(this.releasePackageService.update(releasePackage));
    } else {
      this.subscribeToSaveResponse(this.releasePackageService.create(releasePackage));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReleasePackage>>): void {
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

  protected updateForm(releasePackage: IReleasePackage): void {
    this.editForm.patchValue({
      id: releasePackage.id,
      title: releasePackage.title,
      releasePackageNumber: releasePackage.releasePackageNumber,
      releasePackageTitle: releasePackage.releasePackageTitle,
      status: releasePackage.status,
      ecn: releasePackage.ecn,
    });
  }

  protected createFromForm(): IReleasePackage {
    return {
      ...new ReleasePackage(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      releasePackageNumber: this.editForm.get(['releasePackageNumber'])!.value,
      releasePackageTitle: this.editForm.get(['releasePackageTitle'])!.value,
      status: this.editForm.get(['status'])!.value,
      ecn: this.editForm.get(['ecn'])!.value,
    };
  }
}
