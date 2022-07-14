import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ImpactMatrixService } from '../service/impact-matrix.service';
import { IImpactMatrix, ImpactMatrix } from '../impact-matrix.model';

import { ImpactMatrixUpdateComponent } from './impact-matrix-update.component';

describe('ImpactMatrix Management Update Component', () => {
  let comp: ImpactMatrixUpdateComponent;
  let fixture: ComponentFixture<ImpactMatrixUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let impactMatrixService: ImpactMatrixService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ImpactMatrixUpdateComponent],
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
      .overrideTemplate(ImpactMatrixUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ImpactMatrixUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    impactMatrixService = TestBed.inject(ImpactMatrixService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const impactMatrix: IImpactMatrix = { id: 456 };

      activatedRoute.data = of({ impactMatrix });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(impactMatrix));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ImpactMatrix>>();
      const impactMatrix = { id: 123 };
      jest.spyOn(impactMatrixService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ impactMatrix });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: impactMatrix }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(impactMatrixService.update).toHaveBeenCalledWith(impactMatrix);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ImpactMatrix>>();
      const impactMatrix = new ImpactMatrix();
      jest.spyOn(impactMatrixService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ impactMatrix });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: impactMatrix }));
      saveSubject.complete();

      // THEN
      expect(impactMatrixService.create).toHaveBeenCalledWith(impactMatrix);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ImpactMatrix>>();
      const impactMatrix = { id: 123 };
      jest.spyOn(impactMatrixService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ impactMatrix });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(impactMatrixService.update).toHaveBeenCalledWith(impactMatrix);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
