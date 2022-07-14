import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ReleasePackageService } from '../service/release-package.service';
import { IReleasePackage, ReleasePackage } from '../release-package.model';

import { ReleasePackageUpdateComponent } from './release-package-update.component';

describe('ReleasePackage Management Update Component', () => {
  let comp: ReleasePackageUpdateComponent;
  let fixture: ComponentFixture<ReleasePackageUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let releasePackageService: ReleasePackageService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ReleasePackageUpdateComponent],
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
      .overrideTemplate(ReleasePackageUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ReleasePackageUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    releasePackageService = TestBed.inject(ReleasePackageService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const releasePackage: IReleasePackage = { id: 456 };

      activatedRoute.data = of({ releasePackage });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(releasePackage));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ReleasePackage>>();
      const releasePackage = { id: 123 };
      jest.spyOn(releasePackageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ releasePackage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: releasePackage }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(releasePackageService.update).toHaveBeenCalledWith(releasePackage);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ReleasePackage>>();
      const releasePackage = new ReleasePackage();
      jest.spyOn(releasePackageService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ releasePackage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: releasePackage }));
      saveSubject.complete();

      // THEN
      expect(releasePackageService.create).toHaveBeenCalledWith(releasePackage);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ReleasePackage>>();
      const releasePackage = { id: 123 };
      jest.spyOn(releasePackageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ releasePackage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(releasePackageService.update).toHaveBeenCalledWith(releasePackage);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
