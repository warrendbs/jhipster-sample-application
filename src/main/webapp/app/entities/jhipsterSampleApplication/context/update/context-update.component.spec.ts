import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ContextService } from '../service/context.service';
import { IContext, Context } from '../context.model';
import { IImpactMatrix } from 'app/entities/jhipsterSampleApplication/impact-matrix/impact-matrix.model';
import { ImpactMatrixService } from 'app/entities/jhipsterSampleApplication/impact-matrix/service/impact-matrix.service';

import { ContextUpdateComponent } from './context-update.component';

describe('Context Management Update Component', () => {
  let comp: ContextUpdateComponent;
  let fixture: ComponentFixture<ContextUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let contextService: ContextService;
  let impactMatrixService: ImpactMatrixService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ContextUpdateComponent],
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
      .overrideTemplate(ContextUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ContextUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    contextService = TestBed.inject(ContextService);
    impactMatrixService = TestBed.inject(ImpactMatrixService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ImpactMatrix query and add missing value', () => {
      const context: IContext = { id: 456 };
      const impactMatrix: IImpactMatrix = { id: 67920 };
      context.impactMatrix = impactMatrix;

      const impactMatrixCollection: IImpactMatrix[] = [{ id: 84715 }];
      jest.spyOn(impactMatrixService, 'query').mockReturnValue(of(new HttpResponse({ body: impactMatrixCollection })));
      const additionalImpactMatrices = [impactMatrix];
      const expectedCollection: IImpactMatrix[] = [...additionalImpactMatrices, ...impactMatrixCollection];
      jest.spyOn(impactMatrixService, 'addImpactMatrixToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ context });
      comp.ngOnInit();

      expect(impactMatrixService.query).toHaveBeenCalled();
      expect(impactMatrixService.addImpactMatrixToCollectionIfMissing).toHaveBeenCalledWith(
        impactMatrixCollection,
        ...additionalImpactMatrices
      );
      expect(comp.impactMatricesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const context: IContext = { id: 456 };
      const impactMatrix: IImpactMatrix = { id: 49998 };
      context.impactMatrix = impactMatrix;

      activatedRoute.data = of({ context });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(context));
      expect(comp.impactMatricesSharedCollection).toContain(impactMatrix);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Context>>();
      const context = { id: 123 };
      jest.spyOn(contextService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ context });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: context }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(contextService.update).toHaveBeenCalledWith(context);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Context>>();
      const context = new Context();
      jest.spyOn(contextService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ context });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: context }));
      saveSubject.complete();

      // THEN
      expect(contextService.create).toHaveBeenCalledWith(context);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Context>>();
      const context = { id: 123 };
      jest.spyOn(contextService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ context });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(contextService.update).toHaveBeenCalledWith(context);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackImpactMatrixById', () => {
      it('Should return tracked ImpactMatrix primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackImpactMatrixById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
