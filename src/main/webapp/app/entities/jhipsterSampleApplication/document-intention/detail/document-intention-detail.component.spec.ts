import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DocumentIntentionDetailComponent } from './document-intention-detail.component';

describe('DocumentIntention Management Detail Component', () => {
  let comp: DocumentIntentionDetailComponent;
  let fixture: ComponentFixture<DocumentIntentionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DocumentIntentionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ documentIntention: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DocumentIntentionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DocumentIntentionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load documentIntention on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.documentIntention).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
