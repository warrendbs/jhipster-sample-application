import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IReleasePackage, ReleasePackage } from '../release-package.model';
import { ReleasePackageService } from '../service/release-package.service';

import { ReleasePackageRoutingResolveService } from './release-package-routing-resolve.service';

describe('ReleasePackage routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ReleasePackageRoutingResolveService;
  let service: ReleasePackageService;
  let resultReleasePackage: IReleasePackage | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(ReleasePackageRoutingResolveService);
    service = TestBed.inject(ReleasePackageService);
    resultReleasePackage = undefined;
  });

  describe('resolve', () => {
    it('should return IReleasePackage returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultReleasePackage = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultReleasePackage).toEqual({ id: 123 });
    });

    it('should return new IReleasePackage if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultReleasePackage = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultReleasePackage).toEqual(new ReleasePackage());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ReleasePackage })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultReleasePackage = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultReleasePackage).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
