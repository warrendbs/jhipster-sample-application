import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ImpactMatrixDetailComponent } from './impact-matrix-detail.component';

describe('ImpactMatrix Management Detail Component', () => {
  let comp: ImpactMatrixDetailComponent;
  let fixture: ComponentFixture<ImpactMatrixDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ImpactMatrixDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ impactMatrix: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ImpactMatrixDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ImpactMatrixDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load impactMatrix on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.impactMatrix).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
