import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BomSourceService } from '../service/bom-source.service';
import { IBomSource, BomSource } from '../bom-source.model';
import { IBomChild } from 'app/entities/jhipsterSampleApplication/bom-child/bom-child.model';
import { BomChildService } from 'app/entities/jhipsterSampleApplication/bom-child/service/bom-child.service';

import { BomSourceUpdateComponent } from './bom-source-update.component';

describe('BomSource Management Update Component', () => {
  let comp: BomSourceUpdateComponent;
  let fixture: ComponentFixture<BomSourceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let bomSourceService: BomSourceService;
  let bomChildService: BomChildService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BomSourceUpdateComponent],
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
      .overrideTemplate(BomSourceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BomSourceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    bomSourceService = TestBed.inject(BomSourceService);
    bomChildService = TestBed.inject(BomChildService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call BomChild query and add missing value', () => {
      const bomSource: IBomSource = { id: 456 };
      const bomChildren: IBomChild[] = [{ id: 73162 }];
      bomSource.bomChildren = bomChildren;

      const bomChildCollection: IBomChild[] = [{ id: 89936 }];
      jest.spyOn(bomChildService, 'query').mockReturnValue(of(new HttpResponse({ body: bomChildCollection })));
      const additionalBomChildren = [...bomChildren];
      const expectedCollection: IBomChild[] = [...additionalBomChildren, ...bomChildCollection];
      jest.spyOn(bomChildService, 'addBomChildToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ bomSource });
      comp.ngOnInit();

      expect(bomChildService.query).toHaveBeenCalled();
      expect(bomChildService.addBomChildToCollectionIfMissing).toHaveBeenCalledWith(bomChildCollection, ...additionalBomChildren);
      expect(comp.bomChildrenSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const bomSource: IBomSource = { id: 456 };
      const bomChildren: IBomChild = { id: 17183 };
      bomSource.bomChildren = [bomChildren];

      activatedRoute.data = of({ bomSource });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(bomSource));
      expect(comp.bomChildrenSharedCollection).toContain(bomChildren);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BomSource>>();
      const bomSource = { id: 123 };
      jest.spyOn(bomSourceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bomSource });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bomSource }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(bomSourceService.update).toHaveBeenCalledWith(bomSource);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BomSource>>();
      const bomSource = new BomSource();
      jest.spyOn(bomSourceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bomSource });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bomSource }));
      saveSubject.complete();

      // THEN
      expect(bomSourceService.create).toHaveBeenCalledWith(bomSource);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BomSource>>();
      const bomSource = { id: 123 };
      jest.spyOn(bomSourceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bomSource });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(bomSourceService.update).toHaveBeenCalledWith(bomSource);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackBomChildById', () => {
      it('Should return tracked BomChild primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackBomChildById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedBomChild', () => {
      it('Should return option if no BomChild is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedBomChild(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected BomChild for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedBomChild(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this BomChild is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedBomChild(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
