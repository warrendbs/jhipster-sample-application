import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DocumentService } from '../service/document.service';
import { IDocument, Document } from '../document.model';
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

import { DocumentUpdateComponent } from './document-update.component';

describe('Document Management Update Component', () => {
  let comp: DocumentUpdateComponent;
  let fixture: ComponentFixture<DocumentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let documentService: DocumentService;
  let documentSourceService: DocumentSourceService;
  let documentIntentionService: DocumentIntentionService;
  let itemReferenceService: ItemReferenceService;
  let impactMatrixService: ImpactMatrixService;
  let documentTypeService: DocumentTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DocumentUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(DocumentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DocumentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    documentService = TestBed.inject(DocumentService);
    documentSourceService = TestBed.inject(DocumentSourceService);
    documentIntentionService = TestBed.inject(DocumentIntentionService);
    itemReferenceService = TestBed.inject(ItemReferenceService);
    impactMatrixService = TestBed.inject(ImpactMatrixService);
    documentTypeService = TestBed.inject(DocumentTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call documentSource query and add missing value', () => {
      const document: IDocument = { id: 456 };
      const documentSource: IDocumentSource = { id: 77747 };
      document.documentSource = documentSource;

      const documentSourceCollection: IDocumentSource[] = [{ id: 27028 }];
      jest.spyOn(documentSourceService, 'query').mockReturnValue(of(new HttpResponse({ body: documentSourceCollection })));
      const expectedCollection: IDocumentSource[] = [documentSource, ...documentSourceCollection];
      jest.spyOn(documentSourceService, 'addDocumentSourceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ document });
      comp.ngOnInit();

      expect(documentSourceService.query).toHaveBeenCalled();
      expect(documentSourceService.addDocumentSourceToCollectionIfMissing).toHaveBeenCalledWith(documentSourceCollection, documentSource);
      expect(comp.documentSourcesCollection).toEqual(expectedCollection);
    });

    it('Should call documentIntention query and add missing value', () => {
      const document: IDocument = { id: 456 };
      const documentIntention: IDocumentIntention = { id: 6556 };
      document.documentIntention = documentIntention;

      const documentIntentionCollection: IDocumentIntention[] = [{ id: 21563 }];
      jest.spyOn(documentIntentionService, 'query').mockReturnValue(of(new HttpResponse({ body: documentIntentionCollection })));
      const expectedCollection: IDocumentIntention[] = [documentIntention, ...documentIntentionCollection];
      jest.spyOn(documentIntentionService, 'addDocumentIntentionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ document });
      comp.ngOnInit();

      expect(documentIntentionService.query).toHaveBeenCalled();
      expect(documentIntentionService.addDocumentIntentionToCollectionIfMissing).toHaveBeenCalledWith(
        documentIntentionCollection,
        documentIntention
      );
      expect(comp.documentIntentionsCollection).toEqual(expectedCollection);
    });

    it('Should call ItemReference query and add missing value', () => {
      const document: IDocument = { id: 456 };
      const itemReferences: IItemReference[] = [{ id: 9768 }];
      document.itemReferences = itemReferences;

      const itemReferenceCollection: IItemReference[] = [{ id: 77207 }];
      jest.spyOn(itemReferenceService, 'query').mockReturnValue(of(new HttpResponse({ body: itemReferenceCollection })));
      const additionalItemReferences = [...itemReferences];
      const expectedCollection: IItemReference[] = [...additionalItemReferences, ...itemReferenceCollection];
      jest.spyOn(itemReferenceService, 'addItemReferenceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ document });
      comp.ngOnInit();

      expect(itemReferenceService.query).toHaveBeenCalled();
      expect(itemReferenceService.addItemReferenceToCollectionIfMissing).toHaveBeenCalledWith(
        itemReferenceCollection,
        ...additionalItemReferences
      );
      expect(comp.itemReferencesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ImpactMatrix query and add missing value', () => {
      const document: IDocument = { id: 456 };
      const impactMatrix: IImpactMatrix = { id: 57204 };
      document.impactMatrix = impactMatrix;

      const impactMatrixCollection: IImpactMatrix[] = [{ id: 22317 }];
      jest.spyOn(impactMatrixService, 'query').mockReturnValue(of(new HttpResponse({ body: impactMatrixCollection })));
      const additionalImpactMatrices = [impactMatrix];
      const expectedCollection: IImpactMatrix[] = [...additionalImpactMatrices, ...impactMatrixCollection];
      jest.spyOn(impactMatrixService, 'addImpactMatrixToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ document });
      comp.ngOnInit();

      expect(impactMatrixService.query).toHaveBeenCalled();
      expect(impactMatrixService.addImpactMatrixToCollectionIfMissing).toHaveBeenCalledWith(
        impactMatrixCollection,
        ...additionalImpactMatrices
      );
      expect(comp.impactMatricesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call DocumentType query and add missing value', () => {
      const document: IDocument = { id: 456 };
      const documentType: IDocumentType = { id: 93954 };
      document.documentType = documentType;

      const documentTypeCollection: IDocumentType[] = [{ id: 32246 }];
      jest.spyOn(documentTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: documentTypeCollection })));
      const additionalDocumentTypes = [documentType];
      const expectedCollection: IDocumentType[] = [...additionalDocumentTypes, ...documentTypeCollection];
      jest.spyOn(documentTypeService, 'addDocumentTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ document });
      comp.ngOnInit();

      expect(documentTypeService.query).toHaveBeenCalled();
      expect(documentTypeService.addDocumentTypeToCollectionIfMissing).toHaveBeenCalledWith(
        documentTypeCollection,
        ...additionalDocumentTypes
      );
      expect(comp.documentTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const document: IDocument = { id: 456 };
      const documentSource: IDocumentSource = { id: 58753 };
      document.documentSource = documentSource;
      const documentIntention: IDocumentIntention = { id: 65280 };
      document.documentIntention = documentIntention;
      const itemReferences: IItemReference = { id: 1793 };
      document.itemReferences = [itemReferences];
      const impactMatrix: IImpactMatrix = { id: 77701 };
      document.impactMatrix = impactMatrix;
      const documentType: IDocumentType = { id: 34580 };
      document.documentType = documentType;

      activatedRoute.data = of({ document });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(document));
      expect(comp.documentSourcesCollection).toContain(documentSource);
      expect(comp.documentIntentionsCollection).toContain(documentIntention);
      expect(comp.itemReferencesSharedCollection).toContain(itemReferences);
      expect(comp.impactMatricesSharedCollection).toContain(impactMatrix);
      expect(comp.documentTypesSharedCollection).toContain(documentType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Document>>();
      const document = { id: 123 };
      jest.spyOn(documentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ document });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: document }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(documentService.update).toHaveBeenCalledWith(document);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Document>>();
      const document = new Document();
      jest.spyOn(documentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ document });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: document }));
      saveSubject.complete();

      // THEN
      expect(documentService.create).toHaveBeenCalledWith(document);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Document>>();
      const document = { id: 123 };
      jest.spyOn(documentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ document });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(documentService.update).toHaveBeenCalledWith(document);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackDocumentSourceById', () => {
      it('Should return tracked DocumentSource primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDocumentSourceById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackDocumentIntentionById', () => {
      it('Should return tracked DocumentIntention primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDocumentIntentionById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackItemReferenceById', () => {
      it('Should return tracked ItemReference primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackItemReferenceById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackImpactMatrixById', () => {
      it('Should return tracked ImpactMatrix primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackImpactMatrixById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackDocumentTypeById', () => {
      it('Should return tracked DocumentType primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDocumentTypeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedItemReference', () => {
      it('Should return option if no ItemReference is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedItemReference(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected ItemReference for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedItemReference(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this ItemReference is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedItemReference(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
