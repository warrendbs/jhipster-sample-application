import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BomDetailComponent } from './bom-detail.component';

describe('Bom Management Detail Component', () => {
  let comp: BomDetailComponent;
  let fixture: ComponentFixture<BomDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BomDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ bom: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(BomDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(BomDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load bom on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.bom).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
