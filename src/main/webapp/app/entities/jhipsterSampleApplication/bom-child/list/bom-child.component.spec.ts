import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { BomChildService } from '../service/bom-child.service';

import { BomChildComponent } from './bom-child.component';

describe('BomChild Management Component', () => {
  let comp: BomChildComponent;
  let fixture: ComponentFixture<BomChildComponent>;
  let service: BomChildService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [BomChildComponent],
    })
      .overrideTemplate(BomChildComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BomChildComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(BomChildService);

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
    expect(comp.bomChildren?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
