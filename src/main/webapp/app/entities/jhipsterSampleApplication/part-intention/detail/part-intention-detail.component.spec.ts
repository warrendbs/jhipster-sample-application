import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PartIntentionDetailComponent } from './part-intention-detail.component';

describe('PartIntention Management Detail Component', () => {
  let comp: PartIntentionDetailComponent;
  let fixture: ComponentFixture<PartIntentionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PartIntentionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ partIntention: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PartIntentionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PartIntentionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load partIntention on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.partIntention).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
