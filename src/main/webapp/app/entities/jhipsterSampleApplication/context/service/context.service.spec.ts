import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IContext, Context } from '../context.model';

import { ContextService } from './context.service';

describe('Context Service', () => {
  let service: ContextService;
  let httpMock: HttpTestingController;
  let elemDefault: IContext;
  let expectedResult: IContext | IContext[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ContextService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      type: 'AAAAAAA',
      name: 'AAAAAAA',
      status: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Context', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Context()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Context', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          type: 'BBBBBB',
          name: 'BBBBBB',
          status: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Context', () => {
      const patchObject = Object.assign(
        {
          type: 'BBBBBB',
          name: 'BBBBBB',
          status: 1,
        },
        new Context()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Context', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          type: 'BBBBBB',
          name: 'BBBBBB',
          status: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Context', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addContextToCollectionIfMissing', () => {
      it('should add a Context to an empty array', () => {
        const context: IContext = { id: 123 };
        expectedResult = service.addContextToCollectionIfMissing([], context);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(context);
      });

      it('should not add a Context to an array that contains it', () => {
        const context: IContext = { id: 123 };
        const contextCollection: IContext[] = [
          {
            ...context,
          },
          { id: 456 },
        ];
        expectedResult = service.addContextToCollectionIfMissing(contextCollection, context);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Context to an array that doesn't contain it", () => {
        const context: IContext = { id: 123 };
        const contextCollection: IContext[] = [{ id: 456 }];
        expectedResult = service.addContextToCollectionIfMissing(contextCollection, context);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(context);
      });

      it('should add only unique Context to an array', () => {
        const contextArray: IContext[] = [{ id: 123 }, { id: 456 }, { id: 43328 }];
        const contextCollection: IContext[] = [{ id: 123 }];
        expectedResult = service.addContextToCollectionIfMissing(contextCollection, ...contextArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const context: IContext = { id: 123 };
        const context2: IContext = { id: 456 };
        expectedResult = service.addContextToCollectionIfMissing([], context, context2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(context);
        expect(expectedResult).toContain(context2);
      });

      it('should accept null and undefined values', () => {
        const context: IContext = { id: 123 };
        expectedResult = service.addContextToCollectionIfMissing([], null, context, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(context);
      });

      it('should return initial array if no Context is added', () => {
        const contextCollection: IContext[] = [{ id: 123 }];
        expectedResult = service.addContextToCollectionIfMissing(contextCollection, undefined, null);
        expect(expectedResult).toEqual(contextCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
