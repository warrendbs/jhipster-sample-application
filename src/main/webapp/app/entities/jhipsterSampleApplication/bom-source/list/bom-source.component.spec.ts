import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { BomSourceService } from '../service/bom-source.service';

import { BomSourceComponent } from './bom-source.component';

describe('BomSource Management Component', () => {
  let comp: BomSourceComponent;
  let fixture: ComponentFixture<BomSourceComponent>;
  let service: BomSourceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [BomSourceComponent],
    })
      .overrideTemplate(BomSourceComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BomSourceComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(BomSourceService);

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
    expect(comp.bomSources?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
