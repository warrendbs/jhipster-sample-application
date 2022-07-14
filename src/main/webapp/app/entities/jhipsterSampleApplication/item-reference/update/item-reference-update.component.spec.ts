import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ItemReferenceService } from '../service/item-reference.service';
import { IItemReference, ItemReference } from '../item-reference.model';

import { ItemReferenceUpdateComponent } from './item-reference-update.component';

describe('ItemReference Management Update Component', () => {
  let comp: ItemReferenceUpdateComponent;
  let fixture: ComponentFixture<ItemReferenceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let itemReferenceService: ItemReferenceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ItemReferenceUpdateComponent],
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
      .overrideTemplate(ItemReferenceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ItemReferenceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    itemReferenceService = TestBed.inject(ItemReferenceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const itemReference: IItemReference = { id: 456 };

      activatedRoute.data = of({ itemReference });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(itemReference));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ItemReference>>();
      const itemReference = { id: 123 };
      jest.spyOn(itemReferenceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ itemReference });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: itemReference }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(itemReferenceService.update).toHaveBeenCalledWith(itemReference);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ItemReference>>();
      const itemReference = new ItemReference();
      jest.spyOn(itemReferenceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ itemReference });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: itemReference }));
      saveSubject.complete();

      // THEN
      expect(itemReferenceService.create).toHaveBeenCalledWith(itemReference);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ItemReference>>();
      const itemReference = { id: 123 };
      jest.spyOn(itemReferenceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ itemReference });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(itemReferenceService.update).toHaveBeenCalledWith(itemReference);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
