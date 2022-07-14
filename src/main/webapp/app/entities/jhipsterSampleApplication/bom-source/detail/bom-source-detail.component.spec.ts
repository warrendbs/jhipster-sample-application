import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BomSourceDetailComponent } from './bom-source-detail.component';

describe('BomSource Management Detail Component', () => {
  let comp: BomSourceDetailComponent;
  let fixture: ComponentFixture<BomSourceDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BomSourceDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ bomSource: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(BomSourceDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(BomSourceDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load bomSource on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.bomSource).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
