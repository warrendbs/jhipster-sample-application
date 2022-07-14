import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBomIntention, BomIntention } from '../bom-intention.model';

import { BomIntentionService } from './bom-intention.service';

describe('BomIntention Service', () => {
  let service: BomIntentionService;
  let httpMock: HttpTestingController;
  let elemDefault: IBomIntention;
  let expectedResult: IBomIntention | IBomIntention[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BomIntentionService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
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

    it('should create a BomIntention', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new BomIntention()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BomIntention', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
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

    it('should partial update a BomIntention', () => {
      const patchObject = Object.assign(
        {
          type: 'BBBBBB',
        },
        new BomIntention()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BomIntention', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
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

    it('should delete a BomIntention', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addBomIntentionToCollectionIfMissing', () => {
      it('should add a BomIntention to an empty array', () => {
        const bomIntention: IBomIntention = { id: 123 };
        expectedResult = service.addBomIntentionToCollectionIfMissing([], bomIntention);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bomIntention);
      });

      it('should not add a BomIntention to an array that contains it', () => {
        const bomIntention: IBomIntention = { id: 123 };
        const bomIntentionCollection: IBomIntention[] = [
          {
            ...bomIntention,
          },
          { id: 456 },
        ];
        expectedResult = service.addBomIntentionToCollectionIfMissing(bomIntentionCollection, bomIntention);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BomIntention to an array that doesn't contain it", () => {
        const bomIntention: IBomIntention = { id: 123 };
        const bomIntentionCollection: IBomIntention[] = [{ id: 456 }];
        expectedResult = service.addBomIntentionToCollectionIfMissing(bomIntentionCollection, bomIntention);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bomIntention);
      });

      it('should add only unique BomIntention to an array', () => {
        const bomIntentionArray: IBomIntention[] = [{ id: 123 }, { id: 456 }, { id: 29078 }];
        const bomIntentionCollection: IBomIntention[] = [{ id: 123 }];
        expectedResult = service.addBomIntentionToCollectionIfMissing(bomIntentionCollection, ...bomIntentionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const bomIntention: IBomIntention = { id: 123 };
        const bomIntention2: IBomIntention = { id: 456 };
        expectedResult = service.addBomIntentionToCollectionIfMissing([], bomIntention, bomIntention2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bomIntention);
        expect(expectedResult).toContain(bomIntention2);
      });

      it('should accept null and undefined values', () => {
        const bomIntention: IBomIntention = { id: 123 };
        expectedResult = service.addBomIntentionToCollectionIfMissing([], null, bomIntention, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bomIntention);
      });

      it('should return initial array if no BomIntention is added', () => {
        const bomIntentionCollection: IBomIntention[] = [{ id: 123 }];
        expectedResult = service.addBomIntentionToCollectionIfMissing(bomIntentionCollection, undefined, null);
        expect(expectedResult).toEqual(bomIntentionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
