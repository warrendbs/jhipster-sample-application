import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BomChildService } from '../service/bom-child.service';
import { IBomChild, BomChild } from '../bom-child.model';

import { BomChildUpdateComponent } from './bom-child-update.component';

describe('BomChild Management Update Component', () => {
  let comp: BomChildUpdateComponent;
  let fixture: ComponentFixture<BomChildUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let bomChildService: BomChildService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BomChildUpdateComponent],
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
      .overrideTemplate(BomChildUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BomChildUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    bomChildService = TestBed.inject(BomChildService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const bomChild: IBomChild = { id: 456 };

      activatedRoute.data = of({ bomChild });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(bomChild));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BomChild>>();
      const bomChild = { id: 123 };
      jest.spyOn(bomChildService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bomChild });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bomChild }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(bomChildService.update).toHaveBeenCalledWith(bomChild);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BomChild>>();
      const bomChild = new BomChild();
      jest.spyOn(bomChildService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bomChild });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bomChild }));
      saveSubject.complete();

      // THEN
      expect(bomChildService.create).toHaveBeenCalledWith(bomChild);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BomChild>>();
      const bomChild = { id: 123 };
      jest.spyOn(bomChildService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bomChild });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(bomChildService.update).toHaveBeenCalledWith(bomChild);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
