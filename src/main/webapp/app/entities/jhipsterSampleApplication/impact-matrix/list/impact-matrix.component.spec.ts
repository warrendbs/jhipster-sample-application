import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ImpactMatrixService } from '../service/impact-matrix.service';

import { ImpactMatrixComponent } from './impact-matrix.component';

describe('ImpactMatrix Management Component', () => {
  let comp: ImpactMatrixComponent;
  let fixture: ComponentFixture<ImpactMatrixComponent>;
  let service: ImpactMatrixService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ImpactMatrixComponent],
    })
      .overrideTemplate(ImpactMatrixComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ImpactMatrixComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ImpactMatrixService);

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
    expect(comp.impactMatrices?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
