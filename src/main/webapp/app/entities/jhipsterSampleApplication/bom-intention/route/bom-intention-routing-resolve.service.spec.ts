import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IBomIntention, BomIntention } from '../bom-intention.model';
import { BomIntentionService } from '../service/bom-intention.service';

import { BomIntentionRoutingResolveService } from './bom-intention-routing-resolve.service';

describe('BomIntention routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: BomIntentionRoutingResolveService;
  let service: BomIntentionService;
  let resultBomIntention: IBomIntention | undefined;

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
    routingResolveService = TestBed.inject(BomIntentionRoutingResolveService);
    service = TestBed.inject(BomIntentionService);
    resultBomIntention = undefined;
  });

  describe('resolve', () => {
    it('should return IBomIntention returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultBomIntention = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultBomIntention).toEqual({ id: 123 });
    });

    it('should return new IBomIntention if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultBomIntention = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultBomIntention).toEqual(new BomIntention());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as BomIntention })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultBomIntention = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultBomIntention).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
