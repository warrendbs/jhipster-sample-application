import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BomIntentionDetailComponent } from './bom-intention-detail.component';

describe('BomIntention Management Detail Component', () => {
  let comp: BomIntentionDetailComponent;
  let fixture: ComponentFixture<BomIntentionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BomIntentionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ bomIntention: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(BomIntentionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(BomIntentionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load bomIntention on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.bomIntention).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
