import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PlantSpecificService } from '../service/plant-specific.service';

import { PlantSpecificComponent } from './plant-specific.component';

describe('PlantSpecific Management Component', () => {
  let comp: PlantSpecificComponent;
  let fixture: ComponentFixture<PlantSpecificComponent>;
  let service: PlantSpecificService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PlantSpecificComponent],
    })
      .overrideTemplate(PlantSpecificComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PlantSpecificComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PlantSpecificService);

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
    expect(comp.plantSpecifics?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
