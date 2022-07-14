import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IDocumentSource, DocumentSource } from '../document-source.model';
import { DocumentSourceService } from '../service/document-source.service';

import { DocumentSourceRoutingResolveService } from './document-source-routing-resolve.service';

describe('DocumentSource routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: DocumentSourceRoutingResolveService;
  let service: DocumentSourceService;
  let resultDocumentSource: IDocumentSource | undefined;

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
    routingResolveService = TestBed.inject(DocumentSourceRoutingResolveService);
    service = TestBed.inject(DocumentSourceService);
    resultDocumentSource = undefined;
  });

  describe('resolve', () => {
    it('should return IDocumentSource returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDocumentSource = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDocumentSource).toEqual({ id: 123 });
    });

    it('should return new IDocumentSource if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDocumentSource = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultDocumentSource).toEqual(new DocumentSource());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as DocumentSource })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDocumentSource = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDocumentSource).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
