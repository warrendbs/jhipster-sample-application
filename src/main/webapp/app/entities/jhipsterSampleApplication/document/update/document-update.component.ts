import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDocument, Document } from '../document.model';
import { DocumentService } from '../service/document.service';
import { IDocumentSource } from 'app/entities/jhipsterSampleApplication/document-source/document-source.model';
import { DocumentSourceService } from 'app/entities/jhipsterSampleApplication/document-source/service/document-source.service';
import { IDocumentIntention } from 'app/entities/jhipsterSampleApplication/document-intention/document-intention.model';
import { DocumentIntentionService } from 'app/entities/jhipsterSampleApplication/document-intention/service/document-intention.service';
import { IItemReference } from 'app/entities/jhipsterSampleApplication/item-reference/item-reference.model';
import { ItemReferenceService } from 'app/entities/jhipsterSampleApplication/item-reference/service/item-reference.service';
import { IImpactMatrix } from 'app/entities/jhipsterSampleApplication/impact-matrix/impact-matrix.model';
import { ImpactMatrixService } from 'app/entities/jhipsterSampleApplication/impact-matrix/service/impact-matrix.service';
import { IDocumentType } from 'app/entities/jhipsterSampleApplication/document-type/document-type.model';
import { DocumentTypeService } from 'app/entities/jhipsterSampleApplication/document-type/service/document-type.service';

@Component({
  selector: 'jhi-document-update',
  templateUrl: './document-update.component.html',
})
export class DocumentUpdateComponent implements OnInit {
  isSaving = false;

  documentSourcesCollection: IDocumentSource[] = [];
  documentIntentionsCollection: IDocumentIntention[] = [];
  itemReferencesSharedCollection: IItemReference[] = [];
  impactMatricesSharedCollection: IImpactMatrix[] = [];
  documentTypesSharedCollection: IDocumentType[] = [];

  editForm = this.fb.group({
    id: [],
    title: [],
    status: [],
    documentSource: [],
    documentIntention: [],
    itemReferences: [],
    impactMatrix: [],
    documentType: [],
  });

  constructor(
    protected documentService: DocumentService,
    protected documentSourceService: DocumentSourceService,
    protected documentIntentionService: DocumentIntentionService,
    protected itemReferenceService: ItemReferenceService,
    protected impactMatrixService: ImpactMatrixService,
    protected documentTypeService: DocumentTypeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ document }) => {
      this.updateForm(document);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const document = this.createFromForm();
    if (document.id !== undefined) {
      this.subscribeToSaveResponse(this.documentService.update(document));
    } else {
      this.subscribeToSaveResponse(this.documentService.create(document));
    }
  }

  trackDocumentSourceById(_index: number, item: IDocumentSource): number {
    return item.id!;
  }

  trackDocumentIntentionById(_index: number, item: IDocumentIntention): number {
    return item.id!;
  }

  trackItemReferenceById(_index: number, item: IItemReference): number {
    return item.id!;
  }

  trackImpactMatrixById(_index: number, item: IImpactMatrix): number {
    return item.id!;
  }

  trackDocumentTypeById(_index: number, item: IDocumentType): number {
    return item.id!;
  }

  getSelectedItemReference(option: IItemReference, selectedVals?: IItemReference[]): IItemReference {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocument>>): void {
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

  protected updateForm(document: IDocument): void {
    this.editForm.patchValue({
      id: document.id,
      title: document.title,
      status: document.status,
      documentSource: document.documentSource,
      documentIntention: document.documentIntention,
      itemReferences: document.itemReferences,
      impactMatrix: document.impactMatrix,
      documentType: document.documentType,
    });

    this.documentSourcesCollection = this.documentSourceService.addDocumentSourceToCollectionIfMissing(
      this.documentSourcesCollection,
      document.documentSource
    );
    this.documentIntentionsCollection = this.documentIntentionService.addDocumentIntentionToCollectionIfMissing(
      this.documentIntentionsCollection,
      document.documentIntention
    );
    this.itemReferencesSharedCollection = this.itemReferenceService.addItemReferenceToCollectionIfMissing(
      this.itemReferencesSharedCollection,
      ...(document.itemReferences ?? [])
    );
    this.impactMatricesSharedCollection = this.impactMatrixService.addImpactMatrixToCollectionIfMissing(
      this.impactMatricesSharedCollection,
      document.impactMatrix
    );
    this.documentTypesSharedCollection = this.documentTypeService.addDocumentTypeToCollectionIfMissing(
      this.documentTypesSharedCollection,
      document.documentType
    );
  }

  protected loadRelationshipsOptions(): void {
    this.documentSourceService
      .query({ filter: 'document-is-null' })
      .pipe(map((res: HttpResponse<IDocumentSource[]>) => res.body ?? []))
      .pipe(
        map((documentSources: IDocumentSource[]) =>
          this.documentSourceService.addDocumentSourceToCollectionIfMissing(documentSources, this.editForm.get('documentSource')!.value)
        )
      )
      .subscribe((documentSources: IDocumentSource[]) => (this.documentSourcesCollection = documentSources));

    this.documentIntentionService
      .query({ filter: 'document-is-null' })
      .pipe(map((res: HttpResponse<IDocumentIntention[]>) => res.body ?? []))
      .pipe(
        map((documentIntentions: IDocumentIntention[]) =>
          this.documentIntentionService.addDocumentIntentionToCollectionIfMissing(
            documentIntentions,
            this.editForm.get('documentIntention')!.value
          )
        )
      )
      .subscribe((documentIntentions: IDocumentIntention[]) => (this.documentIntentionsCollection = documentIntentions));

    this.itemReferenceService
      .query()
      .pipe(map((res: HttpResponse<IItemReference[]>) => res.body ?? []))
      .pipe(
        map((itemReferences: IItemReference[]) =>
          this.itemReferenceService.addItemReferenceToCollectionIfMissing(
            itemReferences,
            ...(this.editForm.get('itemReferences')!.value ?? [])
          )
        )
      )
      .subscribe((itemReferences: IItemReference[]) => (this.itemReferencesSharedCollection = itemReferences));

    this.impactMatrixService
      .query()
      .pipe(map((res: HttpResponse<IImpactMatrix[]>) => res.body ?? []))
      .pipe(
        map((impactMatrices: IImpactMatrix[]) =>
          this.impactMatrixService.addImpactMatrixToCollectionIfMissing(impactMatrices, this.editForm.get('impactMatrix')!.value)
        )
      )
      .subscribe((impactMatrices: IImpactMatrix[]) => (this.impactMatricesSharedCollection = impactMatrices));

    this.documentTypeService
      .query()
      .pipe(map((res: HttpResponse<IDocumentType[]>) => res.body ?? []))
      .pipe(
        map((documentTypes: IDocumentType[]) =>
          this.documentTypeService.addDocumentTypeToCollectionIfMissing(documentTypes, this.editForm.get('documentType')!.value)
        )
      )
      .subscribe((documentTypes: IDocumentType[]) => (this.documentTypesSharedCollection = documentTypes));
  }

  protected createFromForm(): IDocument {
    return {
      ...new Document(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      status: this.editForm.get(['status'])!.value,
      documentSource: this.editForm.get(['documentSource'])!.value,
      documentIntention: this.editForm.get(['documentIntention'])!.value,
      itemReferences: this.editForm.get(['itemReferences'])!.value,
      impactMatrix: this.editForm.get(['impactMatrix'])!.value,
      documentType: this.editForm.get(['documentType'])!.value,
    };
  }
}
