import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DocumentIntentionService } from '../service/document-intention.service';
import { IDocumentIntention, DocumentIntention } from '../document-intention.model';
import { IReleasePackage } from 'app/entities/jhipsterSampleApplication/release-package/release-package.model';
import { ReleasePackageService } from 'app/entities/jhipsterSampleApplication/release-package/service/release-package.service';

import { DocumentIntentionUpdateComponent } from './document-intention-update.component';

describe('DocumentIntention Management Update Component', () => {
  let comp: DocumentIntentionUpdateComponent;
  let fixture: ComponentFixture<DocumentIntentionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let documentIntentionService: DocumentIntentionService;
  let releasePackageService: ReleasePackageService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DocumentIntentionUpdateComponent],
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
      .overrideTemplate(DocumentIntentionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DocumentIntentionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    documentIntentionService = TestBed.inject(DocumentIntentionService);
    releasePackageService = TestBed.inject(ReleasePackageService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ReleasePackage query and add missing value', () => {
      const documentIntention: IDocumentIntention = { id: 456 };
      const releasePackages: IReleasePackage[] = [{ id: 3523 }];
      documentIntention.releasePackages = releasePackages;

      const releasePackageCollection: IReleasePackage[] = [{ id: 78819 }];
      jest.spyOn(releasePackageService, 'query').mockReturnValue(of(new HttpResponse({ body: releasePackageCollection })));
      const additionalReleasePackages = [...releasePackages];
      const expectedCollection: IReleasePackage[] = [...additionalReleasePackages, ...releasePackageCollection];
      jest.spyOn(releasePackageService, 'addReleasePackageToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ documentIntention });
      comp.ngOnInit();

      expect(releasePackageService.query).toHaveBeenCalled();
      expect(releasePackageService.addReleasePackageToCollectionIfMissing).toHaveBeenCalledWith(
        releasePackageCollection,
        ...additionalReleasePackages
      );
      expect(comp.releasePackagesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const documentIntention: IDocumentIntention = { id: 456 };
      const releasePackages: IReleasePackage = { id: 38136 };
      documentIntention.releasePackages = [releasePackages];

      activatedRoute.data = of({ documentIntention });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(documentIntention));
      expect(comp.releasePackagesSharedCollection).toContain(releasePackages);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DocumentIntention>>();
      const documentIntention = { id: 123 };
      jest.spyOn(documentIntentionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentIntention });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentIntention }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(documentIntentionService.update).toHaveBeenCalledWith(documentIntention);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DocumentIntention>>();
      const documentIntention = new DocumentIntention();
      jest.spyOn(documentIntentionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentIntention });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentIntention }));
      saveSubject.complete();

      // THEN
      expect(documentIntentionService.create).toHaveBeenCalledWith(documentIntention);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DocumentIntention>>();
      const documentIntention = { id: 123 };
      jest.spyOn(documentIntentionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentIntention });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(documentIntentionService.update).toHaveBeenCalledWith(documentIntention);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackReleasePackageById', () => {
      it('Should return tracked ReleasePackage primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackReleasePackageById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
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
