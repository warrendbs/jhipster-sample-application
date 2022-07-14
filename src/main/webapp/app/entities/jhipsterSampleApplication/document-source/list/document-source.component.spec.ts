import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { DocumentSourceService } from '../service/document-source.service';

import { DocumentSourceComponent } from './document-source.component';

describe('DocumentSource Management Component', () => {
  let comp: DocumentSourceComponent;
  let fixture: ComponentFixture<DocumentSourceComponent>;
  let service: DocumentSourceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DocumentSourceComponent],
    })
      .overrideTemplate(DocumentSourceComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DocumentSourceComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(DocumentSourceService);

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
    expect(comp.documentSources?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
