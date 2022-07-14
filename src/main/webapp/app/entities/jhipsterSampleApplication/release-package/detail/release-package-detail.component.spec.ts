import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ReleasePackageDetailComponent } from './release-package-detail.component';

describe('ReleasePackage Management Detail Component', () => {
  let comp: ReleasePackageDetailComponent;
  let fixture: ComponentFixture<ReleasePackageDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ReleasePackageDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ releasePackage: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ReleasePackageDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ReleasePackageDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load releasePackage on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.releasePackage).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
