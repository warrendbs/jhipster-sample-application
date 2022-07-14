import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ContextDetailComponent } from './context-detail.component';

describe('Context Management Detail Component', () => {
  let comp: ContextDetailComponent;
  let fixture: ComponentFixture<ContextDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ContextDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ context: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ContextDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ContextDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load context on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.context).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
