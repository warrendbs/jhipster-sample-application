import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IPlantSpecific, PlantSpecific } from '../plant-specific.model';
import { PlantSpecificService } from '../service/plant-specific.service';

import { PlantSpecificRoutingResolveService } from './plant-specific-routing-resolve.service';

describe('PlantSpecific routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: PlantSpecificRoutingResolveService;
  let service: PlantSpecificService;
  let resultPlantSpecific: IPlantSpecific | undefined;

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
    routingResolveService = TestBed.inject(PlantSpecificRoutingResolveService);
    service = TestBed.inject(PlantSpecificService);
    resultPlantSpecific = undefined;
  });

  describe('resolve', () => {
    it('should return IPlantSpecific returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPlantSpecific = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPlantSpecific).toEqual({ id: 123 });
    });

    it('should return new IPlantSpecific if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPlantSpecific = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPlantSpecific).toEqual(new PlantSpecific());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as PlantSpecific })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPlantSpecific = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPlantSpecific).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
