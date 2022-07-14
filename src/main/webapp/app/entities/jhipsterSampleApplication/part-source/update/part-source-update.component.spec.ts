import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PartSourceService } from '../service/part-source.service';
import { IPartSource, PartSource } from '../part-source.model';
import { IPlantSpecific } from 'app/entities/jhipsterSampleApplication/plant-specific/plant-specific.model';
import { PlantSpecificService } from 'app/entities/jhipsterSampleApplication/plant-specific/service/plant-specific.service';

import { PartSourceUpdateComponent } from './part-source-update.component';

describe('PartSource Management Update Component', () => {
  let comp: PartSourceUpdateComponent;
  let fixture: ComponentFixture<PartSourceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let partSourceService: PartSourceService;
  let plantSpecificService: PlantSpecificService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PartSourceUpdateComponent],
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
      .overrideTemplate(PartSourceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PartSourceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    partSourceService = TestBed.inject(PartSourceService);
    plantSpecificService = TestBed.inject(PlantSpecificService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call PlantSpecific query and add missing value', () => {
      const partSource: IPartSource = { id: 456 };
      const plantSpecifics: IPlantSpecific[] = [{ id: 68476 }];
      partSource.plantSpecifics = plantSpecifics;

      const plantSpecificCollection: IPlantSpecific[] = [{ id: 13017 }];
      jest.spyOn(plantSpecificService, 'query').mockReturnValue(of(new HttpResponse({ body: plantSpecificCollection })));
      const additionalPlantSpecifics = [...plantSpecifics];
      const expectedCollection: IPlantSpecific[] = [...additionalPlantSpecifics, ...plantSpecificCollection];
      jest.spyOn(plantSpecificService, 'addPlantSpecificToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ partSource });
      comp.ngOnInit();

      expect(plantSpecificService.query).toHaveBeenCalled();
      expect(plantSpecificService.addPlantSpecificToCollectionIfMissing).toHaveBeenCalledWith(
        plantSpecificCollection,
        ...additionalPlantSpecifics
      );
      expect(comp.plantSpecificsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const partSource: IPartSource = { id: 456 };
      const plantSpecifics: IPlantSpecific = { id: 55789 };
      partSource.plantSpecifics = [plantSpecifics];

      activatedRoute.data = of({ partSource });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(partSource));
      expect(comp.plantSpecificsSharedCollection).toContain(plantSpecifics);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PartSource>>();
      const partSource = { id: 123 };
      jest.spyOn(partSourceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partSource });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: partSource }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(partSourceService.update).toHaveBeenCalledWith(partSource);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PartSource>>();
      const partSource = new PartSource();
      jest.spyOn(partSourceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partSource });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: partSource }));
      saveSubject.complete();

      // THEN
      expect(partSourceService.create).toHaveBeenCalledWith(partSource);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PartSource>>();
      const partSource = { id: 123 };
      jest.spyOn(partSourceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partSource });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(partSourceService.update).toHaveBeenCalledWith(partSource);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackPlantSpecificById', () => {
      it('Should return tracked PlantSpecific primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPlantSpecificById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedPlantSpecific', () => {
      it('Should return option if no PlantSpecific is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedPlantSpecific(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected PlantSpecific for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedPlantSpecific(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this PlantSpecific is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedPlantSpecific(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
