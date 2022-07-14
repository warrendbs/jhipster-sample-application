import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PartIntentionService } from '../service/part-intention.service';
import { IPartIntention, PartIntention } from '../part-intention.model';
import { IPlantSpecific } from 'app/entities/jhipsterSampleApplication/plant-specific/plant-specific.model';
import { PlantSpecificService } from 'app/entities/jhipsterSampleApplication/plant-specific/service/plant-specific.service';
import { IItemReference } from 'app/entities/jhipsterSampleApplication/item-reference/item-reference.model';
import { ItemReferenceService } from 'app/entities/jhipsterSampleApplication/item-reference/service/item-reference.service';
import { IReleasePackage } from 'app/entities/jhipsterSampleApplication/release-package/release-package.model';
import { ReleasePackageService } from 'app/entities/jhipsterSampleApplication/release-package/service/release-package.service';

import { PartIntentionUpdateComponent } from './part-intention-update.component';

describe('PartIntention Management Update Component', () => {
  let comp: PartIntentionUpdateComponent;
  let fixture: ComponentFixture<PartIntentionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let partIntentionService: PartIntentionService;
  let plantSpecificService: PlantSpecificService;
  let itemReferenceService: ItemReferenceService;
  let releasePackageService: ReleasePackageService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PartIntentionUpdateComponent],
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
      .overrideTemplate(PartIntentionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PartIntentionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    partIntentionService = TestBed.inject(PartIntentionService);
    plantSpecificService = TestBed.inject(PlantSpecificService);
    itemReferenceService = TestBed.inject(ItemReferenceService);
    releasePackageService = TestBed.inject(ReleasePackageService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call PlantSpecific query and add missing value', () => {
      const partIntention: IPartIntention = { id: 456 };
      const plantSpecifics: IPlantSpecific[] = [{ id: 17933 }];
      partIntention.plantSpecifics = plantSpecifics;

      const plantSpecificCollection: IPlantSpecific[] = [{ id: 38307 }];
      jest.spyOn(plantSpecificService, 'query').mockReturnValue(of(new HttpResponse({ body: plantSpecificCollection })));
      const additionalPlantSpecifics = [...plantSpecifics];
      const expectedCollection: IPlantSpecific[] = [...additionalPlantSpecifics, ...plantSpecificCollection];
      jest.spyOn(plantSpecificService, 'addPlantSpecificToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ partIntention });
      comp.ngOnInit();

      expect(plantSpecificService.query).toHaveBeenCalled();
      expect(plantSpecificService.addPlantSpecificToCollectionIfMissing).toHaveBeenCalledWith(
        plantSpecificCollection,
        ...additionalPlantSpecifics
      );
      expect(comp.plantSpecificsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ItemReference query and add missing value', () => {
      const partIntention: IPartIntention = { id: 456 };
      const itemReferences: IItemReference[] = [{ id: 97819 }];
      partIntention.itemReferences = itemReferences;

      const itemReferenceCollection: IItemReference[] = [{ id: 87054 }];
      jest.spyOn(itemReferenceService, 'query').mockReturnValue(of(new HttpResponse({ body: itemReferenceCollection })));
      const additionalItemReferences = [...itemReferences];
      const expectedCollection: IItemReference[] = [...additionalItemReferences, ...itemReferenceCollection];
      jest.spyOn(itemReferenceService, 'addItemReferenceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ partIntention });
      comp.ngOnInit();

      expect(itemReferenceService.query).toHaveBeenCalled();
      expect(itemReferenceService.addItemReferenceToCollectionIfMissing).toHaveBeenCalledWith(
        itemReferenceCollection,
        ...additionalItemReferences
      );
      expect(comp.itemReferencesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ReleasePackage query and add missing value', () => {
      const partIntention: IPartIntention = { id: 456 };
      const releasePackages: IReleasePackage[] = [{ id: 86366 }];
      partIntention.releasePackages = releasePackages;

      const releasePackageCollection: IReleasePackage[] = [{ id: 62154 }];
      jest.spyOn(releasePackageService, 'query').mockReturnValue(of(new HttpResponse({ body: releasePackageCollection })));
      const additionalReleasePackages = [...releasePackages];
      const expectedCollection: IReleasePackage[] = [...additionalReleasePackages, ...releasePackageCollection];
      jest.spyOn(releasePackageService, 'addReleasePackageToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ partIntention });
      comp.ngOnInit();

      expect(releasePackageService.query).toHaveBeenCalled();
      expect(releasePackageService.addReleasePackageToCollectionIfMissing).toHaveBeenCalledWith(
        releasePackageCollection,
        ...additionalReleasePackages
      );
      expect(comp.releasePackagesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const partIntention: IPartIntention = { id: 456 };
      const plantSpecifics: IPlantSpecific = { id: 70830 };
      partIntention.plantSpecifics = [plantSpecifics];
      const itemReferences: IItemReference = { id: 99409 };
      partIntention.itemReferences = [itemReferences];
      const releasePackages: IReleasePackage = { id: 92001 };
      partIntention.releasePackages = [releasePackages];

      activatedRoute.data = of({ partIntention });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(partIntention));
      expect(comp.plantSpecificsSharedCollection).toContain(plantSpecifics);
      expect(comp.itemReferencesSharedCollection).toContain(itemReferences);
      expect(comp.releasePackagesSharedCollection).toContain(releasePackages);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PartIntention>>();
      const partIntention = { id: 123 };
      jest.spyOn(partIntentionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partIntention });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: partIntention }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(partIntentionService.update).toHaveBeenCalledWith(partIntention);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PartIntention>>();
      const partIntention = new PartIntention();
      jest.spyOn(partIntentionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partIntention });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: partIntention }));
      saveSubject.complete();

      // THEN
      expect(partIntentionService.create).toHaveBeenCalledWith(partIntention);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PartIntention>>();
      const partIntention = { id: 123 };
      jest.spyOn(partIntentionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ partIntention });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(partIntentionService.update).toHaveBeenCalledWith(partIntention);
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

    describe('trackItemReferenceById', () => {
      it('Should return tracked ItemReference primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackItemReferenceById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackReleasePackageById', () => {
      it('Should return tracked ReleasePackage primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackReleasePackageById(0, entity);
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

    describe('getSelectedReleasePackage', () => {
      it('Should return option if no ReleasePackage is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedReleasePackage(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected ReleasePackage for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedReleasePackage(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this ReleasePackage is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedReleasePackage(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
