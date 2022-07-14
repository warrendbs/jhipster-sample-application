import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PartService } from '../service/part.service';
import { IPart, Part } from '../part.model';
import { IPartSource } from 'app/entities/jhipsterSampleApplication/part-source/part-source.model';
import { PartSourceService } from 'app/entities/jhipsterSampleApplication/part-source/service/part-source.service';
import { IPartIntention } from 'app/entities/jhipsterSampleApplication/part-intention/part-intention.model';
import { PartIntentionService } from 'app/entities/jhipsterSampleApplication/part-intention/service/part-intention.service';
import { IImpactMatrix } from 'app/entities/jhipsterSampleApplication/impact-matrix/impact-matrix.model';
import { ImpactMatrixService } from 'app/entities/jhipsterSampleApplication/impact-matrix/service/impact-matrix.service';

import { PartUpdateComponent } from './part-update.component';

describe('Part Management Update Component', () => {
  let comp: PartUpdateComponent;
  let fixture: ComponentFixture<PartUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let partService: PartService;
  let partSourceService: PartSourceService;
  let partIntentionService: PartIntentionService;
  let impactMatrixService: ImpactMatrixService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PartUpdateComponent],
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
      .overrideTemplate(PartUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PartUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    partService = TestBed.inject(PartService);
    partSourceService = TestBed.inject(PartSourceService);
    partIntentionService = TestBed.inject(PartIntentionService);
    impactMatrixService = TestBed.inject(ImpactMatrixService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call partSource query and add missing value', () => {
      const part: IPart = { id: 456 };
      const partSource: IPartSource = { id: 91998 };
      part.partSource = partSource;

      const partSourceCollection: IPartSource[] = [{ id: 69368 }];
      jest.spyOn(partSourceService, 'query').mockReturnValue(of(new HttpResponse({ body: partSourceCollection })));
      const expectedCollection: IPartSource[] = [partSource, ...partSourceCollection];
      jest.spyOn(partSourceService, 'addPartSourceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ part });
      comp.ngOnInit();

      expect(partSourceService.query).toHaveBeenCalled();
      expect(partSourceService.addPartSourceToCollectionIfMissing).toHaveBeenCalledWith(partSourceCollection, partSource);
      expect(comp.partSourcesCollection).toEqual(expectedCollection);
    });

    it('Should call partIntention query and add missing value', () => {
      const part: IPart = { id: 456 };
      const partIntention: IPartIntention = { id: 78486 };
      part.partIntention = partIntention;

      const partIntentionCollection: IPartIntention[] = [{ id: 20604 }];
      jest.spyOn(partIntentionService, 'query').mockReturnValue(of(new HttpResponse({ body: partIntentionCollection })));
      const expectedCollection: IPartIntention[] = [partIntention, ...partIntentionCollection];
      jest.spyOn(partIntentionService, 'addPartIntentionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ part });
      comp.ngOnInit();

      expect(partIntentionService.query).toHaveBeenCalled();
      expect(partIntentionService.addPartIntentionToCollectionIfMissing).toHaveBeenCalledWith(partIntentionCollection, partIntention);
      expect(comp.partIntentionsCollection).toEqual(expectedCollection);
    });

    it('Should call ImpactMatrix query and add missing value', () => {
      const part: IPart = { id: 456 };
      const impactMatrix: IImpactMatrix = { id: 16499 };
      part.impactMatrix = impactMatrix;

      const impactMatrixCollection: IImpactMatrix[] = [{ id: 11365 }];
      jest.spyOn(impactMatrixService, 'query').mockReturnValue(of(new HttpResponse({ body: impactMatrixCollection })));
      const additionalImpactMatrices = [impactMatrix];
      const expectedCollection: IImpactMatrix[] = [...additionalImpactMatrices, ...impactMatrixCollection];
      jest.spyOn(impactMatrixService, 'addImpactMatrixToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ part });
      comp.ngOnInit();

      expect(impactMatrixService.query).toHaveBeenCalled();
      expect(impactMatrixService.addImpactMatrixToCollectionIfMissing).toHaveBeenCalledWith(
        impactMatrixCollection,
        ...additionalImpactMatrices
      );
      expect(comp.impactMatricesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const part: IPart = { id: 456 };
      const partSource: IPartSource = { id: 49531 };
      part.partSource = partSource;
      const partIntention: IPartIntention = { id: 61210 };
      part.partIntention = partIntention;
      const impactMatrix: IImpactMatrix = { id: 55790 };
      part.impactMatrix = impactMatrix;

      activatedRoute.data = of({ part });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(part));
      expect(comp.partSourcesCollection).toContain(partSource);
      expect(comp.partIntentionsCollection).toContain(partIntention);
      expect(comp.impactMatricesSharedCollection).toContain(impactMatrix);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Part>>();
      const part = { id: 123 };
      jest.spyOn(partService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ part });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: part }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(partService.update).toHaveBeenCalledWith(part);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Part>>();
      const part = new Part();
      jest.spyOn(partService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ part });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: part }));
      saveSubject.complete();

      // THEN
      expect(partService.create).toHaveBeenCalledWith(part);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Part>>();
      const part = { id: 123 };
      jest.spyOn(partService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ part });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(partService.update).toHaveBeenCalledWith(part);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackPartSourceById', () => {
      it('Should return tracked PartSource primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPartSourceById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackPartIntentionById', () => {
      it('Should return tracked PartIntention primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPartIntentionById(0, entity);
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
});
