import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BomChildDetailComponent } from './bom-child-detail.component';

describe('BomChild Management Detail Component', () => {
  let comp: BomChildDetailComponent;
  let fixture: ComponentFixture<BomChildDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BomChildDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ bomChild: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(BomChildDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(BomChildDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load bomChild on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.bomChild).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
