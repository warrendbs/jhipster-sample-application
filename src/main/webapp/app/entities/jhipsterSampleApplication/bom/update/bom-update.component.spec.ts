import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BomService } from '../service/bom.service';
import { IBom, Bom } from '../bom.model';
import { IBomSource } from 'app/entities/jhipsterSampleApplication/bom-source/bom-source.model';
import { BomSourceService } from 'app/entities/jhipsterSampleApplication/bom-source/service/bom-source.service';
import { IBomIntention } from 'app/entities/jhipsterSampleApplication/bom-intention/bom-intention.model';
import { BomIntentionService } from 'app/entities/jhipsterSampleApplication/bom-intention/service/bom-intention.service';
import { IItemReference } from 'app/entities/jhipsterSampleApplication/item-reference/item-reference.model';
import { ItemReferenceService } from 'app/entities/jhipsterSampleApplication/item-reference/service/item-reference.service';
import { IImpactMatrix } from 'app/entities/jhipsterSampleApplication/impact-matrix/impact-matrix.model';
import { ImpactMatrixService } from 'app/entities/jhipsterSampleApplication/impact-matrix/service/impact-matrix.service';

import { BomUpdateComponent } from './bom-update.component';

describe('Bom Management Update Component', () => {
  let comp: BomUpdateComponent;
  let fixture: ComponentFixture<BomUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let bomService: BomService;
  let bomSourceService: BomSourceService;
  let bomIntentionService: BomIntentionService;
  let itemReferenceService: ItemReferenceService;
  let impactMatrixService: ImpactMatrixService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BomUpdateComponent],
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
      .overrideTemplate(BomUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BomUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    bomService = TestBed.inject(BomService);
    bomSourceService = TestBed.inject(BomSourceService);
    bomIntentionService = TestBed.inject(BomIntentionService);
    itemReferenceService = TestBed.inject(ItemReferenceService);
    impactMatrixService = TestBed.inject(ImpactMatrixService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call bomSource query and add missing value', () => {
      const bom: IBom = { id: 456 };
      const bomSource: IBomSource = { id: 69567 };
      bom.bomSource = bomSource;

      const bomSourceCollection: IBomSource[] = [{ id: 57903 }];
      jest.spyOn(bomSourceService, 'query').mockReturnValue(of(new HttpResponse({ body: bomSourceCollection })));
      const expectedCollection: IBomSource[] = [bomSource, ...bomSourceCollection];
      jest.spyOn(bomSourceService, 'addBomSourceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ bom });
      comp.ngOnInit();

      expect(bomSourceService.query).toHaveBeenCalled();
      expect(bomSourceService.addBomSourceToCollectionIfMissing).toHaveBeenCalledWith(bomSourceCollection, bomSource);
      expect(comp.bomSourcesCollection).toEqual(expectedCollection);
    });

    it('Should call bomIntention query and add missing value', () => {
      const bom: IBom = { id: 456 };
      const bomIntention: IBomIntention = { id: 43942 };
      bom.bomIntention = bomIntention;

      const bomIntentionCollection: IBomIntention[] = [{ id: 37068 }];
      jest.spyOn(bomIntentionService, 'query').mockReturnValue(of(new HttpResponse({ body: bomIntentionCollection })));
      const expectedCollection: IBomIntention[] = [bomIntention, ...bomIntentionCollection];
      jest.spyOn(bomIntentionService, 'addBomIntentionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ bom });
      comp.ngOnInit();

      expect(bomIntentionService.query).toHaveBeenCalled();
      expect(bomIntentionService.addBomIntentionToCollectionIfMissing).toHaveBeenCalledWith(bomIntentionCollection, bomIntention);
      expect(comp.bomIntentionsCollection).toEqual(expectedCollection);
    });

    it('Should call ItemReference query and add missing value', () => {
      const bom: IBom = { id: 456 };
      const itemReferences: IItemReference[] = [{ id: 97575 }];
      bom.itemReferences = itemReferences;

      const itemReferenceCollection: IItemReference[] = [{ id: 60790 }];
      jest.spyOn(itemReferenceService, 'query').mockReturnValue(of(new HttpResponse({ body: itemReferenceCollection })));
      const additionalItemReferences = [...itemReferences];
      const expectedCollection: IItemReference[] = [...additionalItemReferences, ...itemReferenceCollection];
      jest.spyOn(itemReferenceService, 'addItemReferenceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ bom });
      comp.ngOnInit();

      expect(itemReferenceService.query).toHaveBeenCalled();
      expect(itemReferenceService.addItemReferenceToCollectionIfMissing).toHaveBeenCalledWith(
        itemReferenceCollection,
        ...additionalItemReferences
      );
      expect(comp.itemReferencesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ImpactMatrix query and add missing value', () => {
      const bom: IBom = { id: 456 };
      const impactMatrix: IImpactMatrix = { id: 91145 };
      bom.impactMatrix = impactMatrix;

      const impactMatrixCollection: IImpactMatrix[] = [{ id: 84608 }];
      jest.spyOn(impactMatrixService, 'query').mockReturnValue(of(new HttpResponse({ body: impactMatrixCollection })));
      const additionalImpactMatrices = [impactMatrix];
      const expectedCollection: IImpactMatrix[] = [...additionalImpactMatrices, ...impactMatrixCollection];
      jest.spyOn(impactMatrixService, 'addImpactMatrixToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ bom });
      comp.ngOnInit();

      expect(impactMatrixService.query).toHaveBeenCalled();
      expect(impactMatrixService.addImpactMatrixToCollectionIfMissing).toHaveBeenCalledWith(
        impactMatrixCollection,
        ...additionalImpactMatrices
      );
      expect(comp.impactMatricesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const bom: IBom = { id: 456 };
      const bomSource: IBomSource = { id: 66982 };
      bom.bomSource = bomSource;
      const bomIntention: IBomIntention = { id: 79616 };
      bom.bomIntention = bomIntention;
      const itemReferences: IItemReference = { id: 88568 };
      bom.itemReferences = [itemReferences];
      const impactMatrix: IImpactMatrix = { id: 37406 };
      bom.impactMatrix = impactMatrix;

      activatedRoute.data = of({ bom });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(bom));
      expect(comp.bomSourcesCollection).toContain(bomSource);
      expect(comp.bomIntentionsCollection).toContain(bomIntention);
      expect(comp.itemReferencesSharedCollection).toContain(itemReferences);
      expect(comp.impactMatricesSharedCollection).toContain(impactMatrix);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Bom>>();
      const bom = { id: 123 };
      jest.spyOn(bomService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bom });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bom }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(bomService.update).toHaveBeenCalledWith(bom);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Bom>>();
      const bom = new Bom();
      jest.spyOn(bomService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bom });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bom }));
      saveSubject.complete();

      // THEN
      expect(bomService.create).toHaveBeenCalledWith(bom);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Bom>>();
      const bom = { id: 123 };
      jest.spyOn(bomService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bom });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(bomService.update).toHaveBeenCalledWith(bom);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackBomSourceById', () => {
      it('Should return tracked BomSource primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackBomSourceById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackBomIntentionById', () => {
      it('Should return tracked BomIntention primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackBomIntentionById(0, entity);
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
