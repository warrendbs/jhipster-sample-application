import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ReleasePackageService } from '../service/release-package.service';

import { ReleasePackageComponent } from './release-package.component';

describe('ReleasePackage Management Component', () => {
  let comp: ReleasePackageComponent;
  let fixture: ComponentFixture<ReleasePackageComponent>;
  let service: ReleasePackageService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ReleasePackageComponent],
    })
      .overrideTemplate(ReleasePackageComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ReleasePackageComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ReleasePackageService);

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
    expect(comp.releasePackages?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
