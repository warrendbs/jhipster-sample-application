import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IDocumentSource, DocumentSource } from '../document-source.model';
import { DocumentSourceService } from '../service/document-source.service';

@Component({
  selector: 'jhi-document-source-update',
  templateUrl: './document-source-update.component.html',
})
export class DocumentSourceUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
    description: [],
    changeIndicator: [],
    type: [],
    subtype: [],
    group: [],
    sheet: [],
  });

  constructor(
    protected documentSourceService: DocumentSourceService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documentSource }) => {
      this.updateForm(documentSource);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const documentSource = this.createFromForm();
    if (documentSource.id !== undefined) {
      this.subscribeToSaveResponse(this.documentSourceService.update(documentSource));
    } else {
      this.subscribeToSaveResponse(this.documentSourceService.create(documentSource));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocumentSource>>): void {
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

  protected updateForm(documentSource: IDocumentSource): void {
    this.editForm.patchValue({
      id: documentSource.id,
      name: documentSource.name,
      description: documentSource.description,
      changeIndicator: documentSource.changeIndicator,
      type: documentSource.type,
      subtype: documentSource.subtype,
      group: documentSource.group,
      sheet: documentSource.sheet,
    });
  }

  protected createFromForm(): IDocumentSource {
    return {
      ...new DocumentSource(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      changeIndicator: this.editForm.get(['changeIndicator'])!.value,
      type: this.editForm.get(['type'])!.value,
      subtype: this.editForm.get(['subtype'])!.value,
      group: this.editForm.get(['group'])!.value,
      sheet: this.editForm.get(['sheet'])!.value,
    };
  }
}
