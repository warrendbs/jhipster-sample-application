import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBomChild, BomChild } from '../bom-child.model';

import { BomChildService } from './bom-child.service';

describe('BomChild Service', () => {
  let service: BomChildService;
  let httpMock: HttpTestingController;
  let elemDefault: IBomChild;
  let expectedResult: IBomChild | IBomChild[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BomChildService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      productId: 'AAAAAAA',
      revision: 'AAAAAAA',
      quantity: 0,
      relationType: 'AAAAAAA',
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

    it('should create a BomChild', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new BomChild()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BomChild', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          productId: 'BBBBBB',
          revision: 'BBBBBB',
          quantity: 1,
          relationType: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a BomChild', () => {
      const patchObject = Object.assign(
        {
          productId: 'BBBBBB',
        },
        new BomChild()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BomChild', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          productId: 'BBBBBB',
          revision: 'BBBBBB',
          quantity: 1,
          relationType: 'BBBBBB',
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

    it('should delete a BomChild', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addBomChildToCollectionIfMissing', () => {
      it('should add a BomChild to an empty array', () => {
        const bomChild: IBomChild = { id: 123 };
        expectedResult = service.addBomChildToCollectionIfMissing([], bomChild);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bomChild);
      });

      it('should not add a BomChild to an array that contains it', () => {
        const bomChild: IBomChild = { id: 123 };
        const bomChildCollection: IBomChild[] = [
          {
            ...bomChild,
          },
          { id: 456 },
        ];
        expectedResult = service.addBomChildToCollectionIfMissing(bomChildCollection, bomChild);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BomChild to an array that doesn't contain it", () => {
        const bomChild: IBomChild = { id: 123 };
        const bomChildCollection: IBomChild[] = [{ id: 456 }];
        expectedResult = service.addBomChildToCollectionIfMissing(bomChildCollection, bomChild);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bomChild);
      });

      it('should add only unique BomChild to an array', () => {
        const bomChildArray: IBomChild[] = [{ id: 123 }, { id: 456 }, { id: 27799 }];
        const bomChildCollection: IBomChild[] = [{ id: 123 }];
        expectedResult = service.addBomChildToCollectionIfMissing(bomChildCollection, ...bomChildArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const bomChild: IBomChild = { id: 123 };
        const bomChild2: IBomChild = { id: 456 };
        expectedResult = service.addBomChildToCollectionIfMissing([], bomChild, bomChild2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bomChild);
        expect(expectedResult).toContain(bomChild2);
      });

      it('should accept null and undefined values', () => {
        const bomChild: IBomChild = { id: 123 };
        expectedResult = service.addBomChildToCollectionIfMissing([], null, bomChild, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bomChild);
      });

      it('should return initial array if no BomChild is added', () => {
        const bomChildCollection: IBomChild[] = [{ id: 123 }];
        expectedResult = service.addBomChildToCollectionIfMissing(bomChildCollection, undefined, null);
        expect(expectedResult).toEqual(bomChildCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
