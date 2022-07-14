import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DocumentSourceService } from '../service/document-source.service';
import { IDocumentSource, DocumentSource } from '../document-source.model';

import { DocumentSourceUpdateComponent } from './document-source-update.component';

describe('DocumentSource Management Update Component', () => {
  let comp: DocumentSourceUpdateComponent;
  let fixture: ComponentFixture<DocumentSourceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let documentSourceService: DocumentSourceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DocumentSourceUpdateComponent],
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
      .overrideTemplate(DocumentSourceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DocumentSourceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    documentSourceService = TestBed.inject(DocumentSourceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const documentSource: IDocumentSource = { id: 456 };

      activatedRoute.data = of({ documentSource });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(documentSource));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DocumentSource>>();
      const documentSource = { id: 123 };
      jest.spyOn(documentSourceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentSource });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentSource }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(documentSourceService.update).toHaveBeenCalledWith(documentSource);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DocumentSource>>();
      const documentSource = new DocumentSource();
      jest.spyOn(documentSourceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentSource });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentSource }));
      saveSubject.complete();

      // THEN
      expect(documentSourceService.create).toHaveBeenCalledWith(documentSource);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DocumentSource>>();
      const documentSource = { id: 123 };
      jest.spyOn(documentSourceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentSource });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(documentSourceService.update).toHaveBeenCalledWith(documentSource);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
