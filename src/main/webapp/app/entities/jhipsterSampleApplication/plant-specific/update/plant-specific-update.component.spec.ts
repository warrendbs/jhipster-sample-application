import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PlantSpecificService } from '../service/plant-specific.service';
import { IPlantSpecific, PlantSpecific } from '../plant-specific.model';

import { PlantSpecificUpdateComponent } from './plant-specific-update.component';

describe('PlantSpecific Management Update Component', () => {
  let comp: PlantSpecificUpdateComponent;
  let fixture: ComponentFixture<PlantSpecificUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let plantSpecificService: PlantSpecificService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PlantSpecificUpdateComponent],
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
      .overrideTemplate(PlantSpecificUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PlantSpecificUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    plantSpecificService = TestBed.inject(PlantSpecificService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const plantSpecific: IPlantSpecific = { id: 456 };

      activatedRoute.data = of({ plantSpecific });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(plantSpecific));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PlantSpecific>>();
      const plantSpecific = { id: 123 };
      jest.spyOn(plantSpecificService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ plantSpecific });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: plantSpecific }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(plantSpecificService.update).toHaveBeenCalledWith(plantSpecific);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PlantSpecific>>();
      const plantSpecific = new PlantSpecific();
      jest.spyOn(plantSpecificService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ plantSpecific });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: plantSpecific }));
      saveSubject.complete();

      // THEN
      expect(plantSpecificService.create).toHaveBeenCalledWith(plantSpecific);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PlantSpecific>>();
      const plantSpecific = { id: 123 };
      jest.spyOn(plantSpecificService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ plantSpecific });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(plantSpecificService.update).toHaveBeenCalledWith(plantSpecific);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
