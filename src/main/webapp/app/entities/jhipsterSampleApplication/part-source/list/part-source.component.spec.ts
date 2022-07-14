import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PartSourceService } from '../service/part-source.service';

import { PartSourceComponent } from './part-source.component';

describe('PartSource Management Component', () => {
  let comp: PartSourceComponent;
  let fixture: ComponentFixture<PartSourceComponent>;
  let service: PartSourceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PartSourceComponent],
    })
      .overrideTemplate(PartSourceComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PartSourceComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PartSourceService);

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
    expect(comp.partSources?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
