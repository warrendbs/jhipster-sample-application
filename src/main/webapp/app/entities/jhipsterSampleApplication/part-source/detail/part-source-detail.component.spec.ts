import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PartSourceDetailComponent } from './part-source-detail.component';

describe('PartSource Management Detail Component', () => {
  let comp: PartSourceDetailComponent;
  let fixture: ComponentFixture<PartSourceDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PartSourceDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ partSource: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PartSourceDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PartSourceDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load partSource on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.partSource).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
