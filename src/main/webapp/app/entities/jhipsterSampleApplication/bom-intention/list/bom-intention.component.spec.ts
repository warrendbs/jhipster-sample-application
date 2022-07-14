import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { BomIntentionService } from '../service/bom-intention.service';

import { BomIntentionComponent } from './bom-intention.component';

describe('BomIntention Management Component', () => {
  let comp: BomIntentionComponent;
  let fixture: ComponentFixture<BomIntentionComponent>;
  let service: BomIntentionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [BomIntentionComponent],
    })
      .overrideTemplate(BomIntentionComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BomIntentionComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(BomIntentionService);

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
    expect(comp.bomIntentions?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
