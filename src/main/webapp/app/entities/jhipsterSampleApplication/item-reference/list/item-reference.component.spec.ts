import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ItemReferenceService } from '../service/item-reference.service';

import { ItemReferenceComponent } from './item-reference.component';

describe('ItemReference Management Component', () => {
  let comp: ItemReferenceComponent;
  let fixture: ComponentFixture<ItemReferenceComponent>;
  let service: ItemReferenceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ItemReferenceComponent],
    })
      .overrideTemplate(ItemReferenceComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ItemReferenceComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ItemReferenceService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.itemReferences?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
