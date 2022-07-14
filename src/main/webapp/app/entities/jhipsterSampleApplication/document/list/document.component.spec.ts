import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { DocumentService } from '../service/document.service';

import { DocumentComponent } from './document.component';

describe('Document Management Component', () => {
  let comp: DocumentComponent;
  let fixture: ComponentFixture<DocumentComponent>;
  let service: DocumentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DocumentComponent],
    })
      .overrideTemplate(DocumentComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DocumentComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(DocumentService);

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
    expect(comp.documents?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
