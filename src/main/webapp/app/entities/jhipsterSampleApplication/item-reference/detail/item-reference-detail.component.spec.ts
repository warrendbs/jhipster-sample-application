import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ItemReferenceDetailComponent } from './item-reference-detail.component';

describe('ItemReference Management Detail Component', () => {
  let comp: ItemReferenceDetailComponent;
  let fixture: ComponentFixture<ItemReferenceDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ItemReferenceDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ itemReference: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ItemReferenceDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ItemReferenceDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load itemReference on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.itemReference).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
