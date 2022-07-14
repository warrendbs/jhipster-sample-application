import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { BomService } from '../service/bom.service';

import { BomComponent } from './bom.component';

describe('Bom Management Component', () => {
  let comp: BomComponent;
  let fixture: ComponentFixture<BomComponent>;
  let service: BomService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [BomComponent],
    })
      .overrideTemplate(BomComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BomComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(BomService);

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
    expect(comp.boms?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
