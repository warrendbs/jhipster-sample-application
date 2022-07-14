import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IImpactMatrix, ImpactMatrix } from '../impact-matrix.model';

import { ImpactMatrixService } from './impact-matrix.service';

describe('ImpactMatrix Service', () => {
  let service: ImpactMatrixService;
  let httpMock: HttpTestingController;
  let elemDefault: IImpactMatrix;
  let expectedResult: IImpactMatrix | IImpactMatrix[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ImpactMatrixService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      impactMatrixNumber: 0,
      status: 0,
      revision: 'AAAAAAA',
      reviser: 'AAAAAAA',
      revisionDescription: 'AAAAAAA',
      dateRevised: 'AAAAAAA',
      title: 'AAAAAAA',
      isAutoLayoutEnabled: false,
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

    it('should create a ImpactMatrix', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ImpactMatrix()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ImpactMatrix', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          impactMatrixNumber: 1,
          status: 1,
          revision: 'BBBBBB',
          reviser: 'BBBBBB',
          revisionDescription: 'BBBBBB',
          dateRevised: 'BBBBBB',
          title: 'BBBBBB',
          isAutoLayoutEnabled: true,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ImpactMatrix', () => {
      const patchObject = Object.assign(
        {
          impactMatrixNumber: 1,
          status: 1,
          reviser: 'BBBBBB',
          revisionDescription: 'BBBBBB',
        },
        new ImpactMatrix()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ImpactMatrix', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          impactMatrixNumber: 1,
          status: 1,
          revision: 'BBBBBB',
          reviser: 'BBBBBB',
          revisionDescription: 'BBBBBB',
          dateRevised: 'BBBBBB',
          title: 'BBBBBB',
          isAutoLayoutEnabled: true,
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

    it('should delete a ImpactMatrix', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addImpactMatrixToCollectionIfMissing', () => {
      it('should add a ImpactMatrix to an empty array', () => {
        const impactMatrix: IImpactMatrix = { id: 123 };
        expectedResult = service.addImpactMatrixToCollectionIfMissing([], impactMatrix);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(impactMatrix);
      });

      it('should not add a ImpactMatrix to an array that contains it', () => {
        const impactMatrix: IImpactMatrix = { id: 123 };
        const impactMatrixCollection: IImpactMatrix[] = [
          {
            ...impactMatrix,
          },
          { id: 456 },
        ];
        expectedResult = service.addImpactMatrixToCollectionIfMissing(impactMatrixCollection, impactMatrix);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ImpactMatrix to an array that doesn't contain it", () => {
        const impactMatrix: IImpactMatrix = { id: 123 };
        const impactMatrixCollection: IImpactMatrix[] = [{ id: 456 }];
        expectedResult = service.addImpactMatrixToCollectionIfMissing(impactMatrixCollection, impactMatrix);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(impactMatrix);
      });

      it('should add only unique ImpactMatrix to an array', () => {
        const impactMatrixArray: IImpactMatrix[] = [{ id: 123 }, { id: 456 }, { id: 41929 }];
        const impactMatrixCollection: IImpactMatrix[] = [{ id: 123 }];
        expectedResult = service.addImpactMatrixToCollectionIfMissing(impactMatrixCollection, ...impactMatrixArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const impactMatrix: IImpactMatrix = { id: 123 };
        const impactMatrix2: IImpactMatrix = { id: 456 };
        expectedResult = service.addImpactMatrixToCollectionIfMissing([], impactMatrix, impactMatrix2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(impactMatrix);
        expect(expectedResult).toContain(impactMatrix2);
      });

      it('should accept null and undefined values', () => {
        const impactMatrix: IImpactMatrix = { id: 123 };
        expectedResult = service.addImpactMatrixToCollectionIfMissing([], null, impactMatrix, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(impactMatrix);
      });

      it('should return initial array if no ImpactMatrix is added', () => {
        const impactMatrixCollection: IImpactMatrix[] = [{ id: 123 }];
        expectedResult = service.addImpactMatrixToCollectionIfMissing(impactMatrixCollection, undefined, null);
        expect(expectedResult).toEqual(impactMatrixCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
