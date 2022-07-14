import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IDocumentIntention, DocumentIntention } from '../document-intention.model';
import { DocumentIntentionService } from '../service/document-intention.service';

import { DocumentIntentionRoutingResolveService } from './document-intention-routing-resolve.service';

describe('DocumentIntention routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: DocumentIntentionRoutingResolveService;
  let service: DocumentIntentionService;
  let resultDocumentIntention: IDocumentIntention | undefined;

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
    routingResolveService = TestBed.inject(DocumentIntentionRoutingResolveService);
    service = TestBed.inject(DocumentIntentionService);
    resultDocumentIntention = undefined;
  });

  describe('resolve', () => {
    it('should return IDocumentIntention returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDocumentIntention = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDocumentIntention).toEqual({ id: 123 });
    });

    it('should return new IDocumentIntention if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDocumentIntention = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultDocumentIntention).toEqual(new DocumentIntention());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as DocumentIntention })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDocumentIntention = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDocumentIntention).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
