import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PlantSpecificDetailComponent } from './plant-specific-detail.component';

describe('PlantSpecific Management Detail Component', () => {
  let comp: PlantSpecificDetailComponent;
  let fixture: ComponentFixture<PlantSpecificDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PlantSpecificDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ plantSpecific: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PlantSpecificDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PlantSpecificDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load plantSpecific on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.plantSpecific).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
