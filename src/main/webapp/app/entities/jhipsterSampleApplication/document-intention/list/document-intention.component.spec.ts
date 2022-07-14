import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { DocumentIntentionService } from '../service/document-intention.service';

import { DocumentIntentionComponent } from './document-intention.component';

describe('DocumentIntention Management Component', () => {
  let comp: DocumentIntentionComponent;
  let fixture: ComponentFixture<DocumentIntentionComponent>;
  let service: DocumentIntentionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DocumentIntentionComponent],
    })
      .overrideTemplate(DocumentIntentionComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DocumentIntentionComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(DocumentIntentionService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.documentIntentions?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
