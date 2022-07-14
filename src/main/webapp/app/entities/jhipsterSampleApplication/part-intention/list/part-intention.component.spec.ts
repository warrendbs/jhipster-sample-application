import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PartIntentionService } from '../service/part-intention.service';

import { PartIntentionComponent } from './part-intention.component';

describe('PartIntention Management Component', () => {
  let comp: PartIntentionComponent;
  let fixture: ComponentFixture<PartIntentionComponent>;
  let service: PartIntentionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PartIntentionComponent],
    })
      .overrideTemplate(PartIntentionComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PartIntentionComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PartIntentionService);

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
    expect(comp.partIntentions?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
