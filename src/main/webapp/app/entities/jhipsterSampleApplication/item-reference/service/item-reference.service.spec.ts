import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IItemReference, ItemReference } from '../item-reference.model';

import { ItemReferenceService } from './item-reference.service';

describe('ItemReference Service', () => {
  let service: ItemReferenceService;
  let httpMock: HttpTestingController;
  let elemDefault: IItemReference;
  let expectedResult: IItemReference | IItemReference[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ItemReferenceService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      referenceId: 0,
      type: 'AAAAAAA',
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

    it('should create a ItemReference', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ItemReference()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ItemReference', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          referenceId: 1,
          type: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ItemReference', () => {
      const patchObject = Object.assign(
        {
          type: 'BBBBBB',
        },
        new ItemReference()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ItemReference', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          referenceId: 1,
          type: 'BBBBBB',
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

    it('should delete a ItemReference', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addItemReferenceToCollectionIfMissing', () => {
      it('should add a ItemReference to an empty array', () => {
        const itemReference: IItemReference = { id: 123 };
        expectedResult = service.addItemReferenceToCollectionIfMissing([], itemReference);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(itemReference);
      });

      it('should not add a ItemReference to an array that contains it', () => {
        const itemReference: IItemReference = { id: 123 };
        const itemReferenceCollection: IItemReference[] = [
          {
            ...itemReference,
          },
          { id: 456 },
        ];
        expectedResult = service.addItemReferenceToCollectionIfMissing(itemReferenceCollection, itemReference);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ItemReference to an array that doesn't contain it", () => {
        const itemReference: IItemReference = { id: 123 };
        const itemReferenceCollection: IItemReference[] = [{ id: 456 }];
        expectedResult = service.addItemReferenceToCollectionIfMissing(itemReferenceCollection, itemReference);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(itemReference);
      });

      it('should add only unique ItemReference to an array', () => {
        const itemReferenceArray: IItemReference[] = [{ id: 123 }, { id: 456 }, { id: 62553 }];
        const itemReferenceCollection: IItemReference[] = [{ id: 123 }];
        expectedResult = service.addItemReferenceToCollectionIfMissing(itemReferenceCollection, ...itemReferenceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const itemReference: IItemReference = { id: 123 };
        const itemReference2: IItemReference = { id: 456 };
        expectedResult = service.addItemReferenceToCollectionIfMissing([], itemReference, itemReference2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(itemReference);
        expect(expectedResult).toContain(itemReference2);
      });

      it('should accept null and undefined values', () => {
        const itemReference: IItemReference = { id: 123 };
        expectedResult = service.addItemReferenceToCollectionIfMissing([], null, itemReference, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(itemReference);
      });

      it('should return initial array if no ItemReference is added', () => {
        const itemReferenceCollection: IItemReference[] = [{ id: 123 }];
        expectedResult = service.addItemReferenceToCollectionIfMissing(itemReferenceCollection, undefined, null);
        expect(expectedResult).toEqual(itemReferenceCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
