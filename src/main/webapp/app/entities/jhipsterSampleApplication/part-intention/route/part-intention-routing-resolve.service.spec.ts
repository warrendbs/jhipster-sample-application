import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IPartIntention, PartIntention } from '../part-intention.model';
import { PartIntentionService } from '../service/part-intention.service';

import { PartIntentionRoutingResolveService } from './part-intention-routing-resolve.service';

describe('PartIntention routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: PartIntentionRoutingResolveService;
  let service: PartIntentionService;
  let resultPartIntention: IPartIntention | undefined;

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
    routingResolveService = TestBed.inject(PartIntentionRoutingResolveService);
    service = TestBed.inject(PartIntentionService);
    resultPartIntention = undefined;
  });

  describe('resolve', () => {
    it('should return IPartIntention returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPartIntention = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPartIntention).toEqual({ id: 123 });
    });

    it('should return new IPartIntention if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPartIntention = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPartIntention).toEqual(new PartIntention());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as PartIntention })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPartIntention = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPartIntention).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
