import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DocumentSourceDetailComponent } from './document-source-detail.component';

describe('DocumentSource Management Detail Component', () => {
  let comp: DocumentSourceDetailComponent;
  let fixture: ComponentFixture<DocumentSourceDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DocumentSourceDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ documentSource: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DocumentSourceDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DocumentSourceDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load documentSource on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.documentSource).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
