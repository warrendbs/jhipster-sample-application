import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ContextService } from '../service/context.service';

import { ContextComponent } from './context.component';

describe('Context Management Component', () => {
  let comp: ContextComponent;
  let fixture: ComponentFixture<ContextComponent>;
  let service: ContextService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ContextComponent],
    })
      .overrideTemplate(ContextComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ContextComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ContextService);

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
    expect(comp.contexts?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
