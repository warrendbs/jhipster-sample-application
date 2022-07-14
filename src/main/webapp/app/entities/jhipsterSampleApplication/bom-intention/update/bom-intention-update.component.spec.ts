import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BomIntentionService } from '../service/bom-intention.service';
import { IBomIntention, BomIntention } from '../bom-intention.model';
import { IBomChild } from 'app/entities/jhipsterSampleApplication/bom-child/bom-child.model';
import { BomChildService } from 'app/entities/jhipsterSampleApplication/bom-child/service/bom-child.service';
import { IReleasePackage } from 'app/entities/jhipsterSampleApplication/release-package/release-package.model';
import { ReleasePackageService } from 'app/entities/jhipsterSampleApplication/release-package/service/release-package.service';

import { BomIntentionUpdateComponent } from './bom-intention-update.component';

describe('BomIntention Management Update Component', () => {
  let comp: BomIntentionUpdateComponent;
  let fixture: ComponentFixture<BomIntentionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let bomIntentionService: BomIntentionService;
  let bomChildService: BomChildService;
  let releasePackageService: ReleasePackageService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BomIntentionUpdateComponent],
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
      .overrideTemplate(BomIntentionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BomIntentionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    bomIntentionService = TestBed.inject(BomIntentionService);
    bomChildService = TestBed.inject(BomChildService);
    releasePackageService = TestBed.inject(ReleasePackageService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call BomChild query and add missing value', () => {
      const bomIntention: IBomIntention = { id: 456 };
      const bomChildren: IBomChild[] = [{ id: 34278 }];
      bomIntention.bomChildren = bomChildren;

      const bomChildCollection: IBomChild[] = [{ id: 11810 }];
      jest.spyOn(bomChildService, 'query').mockReturnValue(of(new HttpResponse({ body: bomChildCollection })));
      const additionalBomChildren = [...bomChildren];
      const expectedCollection: IBomChild[] = [...additionalBomChildren, ...bomChildCollection];
      jest.spyOn(bomChildService, 'addBomChildToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ bomIntention });
      comp.ngOnInit();

      expect(bomChildService.query).toHaveBeenCalled();
      expect(bomChildService.addBomChildToCollectionIfMissing).toHaveBeenCalledWith(bomChildCollection, ...additionalBomChildren);
      expect(comp.bomChildrenSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ReleasePackage query and add missing value', () => {
      const bomIntention: IBomIntention = { id: 456 };
      const releasePackages: IReleasePackage[] = [{ id: 27421 }];
      bomIntention.releasePackages = releasePackages;

      const releasePackageCollection: IReleasePackage[] = [{ id: 27616 }];
      jest.spyOn(releasePackageService, 'query').mockReturnValue(of(new HttpResponse({ body: releasePackageCollection })));
      const additionalReleasePackages = [...releasePackages];
      const expectedCollection: IReleasePackage[] = [...additionalReleasePackages, ...releasePackageCollection];
      jest.spyOn(releasePackageService, 'addReleasePackageToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ bomIntention });
      comp.ngOnInit();

      expect(releasePackageService.query).toHaveBeenCalled();
      expect(releasePackageService.addReleasePackageToCollectionIfMissing).toHaveBeenCalledWith(
        releasePackageCollection,
        ...additionalReleasePackages
      );
      expect(comp.releasePackagesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const bomIntention: IBomIntention = { id: 456 };
      const bomChildren: IBomChild = { id: 66574 };
      bomIntention.bomChildren = [bomChildren];
      const releasePackages: IReleasePackage = { id: 25448 };
      bomIntention.releasePackages = [releasePackages];

      activatedRoute.data = of({ bomIntention });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(bomIntention));
      expect(comp.bomChildrenSharedCollection).toContain(bomChildren);
      expect(comp.releasePackagesSharedCollection).toContain(releasePackages);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BomIntention>>();
      const bomIntention = { id: 123 };
      jest.spyOn(bomIntentionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bomIntention });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bomIntention }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(bomIntentionService.update).toHaveBeenCalledWith(bomIntention);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BomIntention>>();
      const bomIntention = new BomIntention();
      jest.spyOn(bomIntentionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bomIntention });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bomIntention }));
      saveSubject.complete();

      // THEN
      expect(bomIntentionService.create).toHaveBeenCalledWith(bomIntention);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<BomIntention>>();
      const bomIntention = { id: 123 };
      jest.spyOn(bomIntentionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bomIntention });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(bomIntentionService.update).toHaveBeenCalledWith(bomIntention);
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

    describe('trackReleasePackageById', () => {
      it('Should return tracked ReleasePackage primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackReleasePackageById(0, entity);
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
