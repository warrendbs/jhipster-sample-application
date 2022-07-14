import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBomSource, BomSource } from '../bom-source.model';

import { BomSourceService } from './bom-source.service';

describe('BomSource Service', () => {
  let service: BomSourceService;
  let httpMock: HttpTestingController;
  let elemDefault: IBomSource;
  let expectedResult: IBomSource | IBomSource[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BomSourceService);
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

    it('should create a BomSource', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new BomSource()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BomSource', () => {
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

    it('should partial update a BomSource', () => {
      const patchObject = Object.assign(
        {
          type: 'BBBBBB',
        },
        new BomSource()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BomSource', () => {
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

    it('should delete a BomSource', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addBomSourceToCollectionIfMissing', () => {
      it('should add a BomSource to an empty array', () => {
        const bomSource: IBomSource = { id: 123 };
        expectedResult = service.addBomSourceToCollectionIfMissing([], bomSource);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bomSource);
      });

      it('should not add a BomSource to an array that contains it', () => {
        const bomSource: IBomSource = { id: 123 };
        const bomSourceCollection: IBomSource[] = [
          {
            ...bomSource,
          },
          { id: 456 },
        ];
        expectedResult = service.addBomSourceToCollectionIfMissing(bomSourceCollection, bomSource);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BomSource to an array that doesn't contain it", () => {
        const bomSource: IBomSource = { id: 123 };
        const bomSourceCollection: IBomSource[] = [{ id: 456 }];
        expectedResult = service.addBomSourceToCollectionIfMissing(bomSourceCollection, bomSource);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bomSource);
      });

      it('should add only unique BomSource to an array', () => {
        const bomSourceArray: IBomSource[] = [{ id: 123 }, { id: 456 }, { id: 59884 }];
        const bomSourceCollection: IBomSource[] = [{ id: 123 }];
        expectedResult = service.addBomSourceToCollectionIfMissing(bomSourceCollection, ...bomSourceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const bomSource: IBomSource = { id: 123 };
        const bomSource2: IBomSource = { id: 456 };
        expectedResult = service.addBomSourceToCollectionIfMissing([], bomSource, bomSource2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bomSource);
        expect(expectedResult).toContain(bomSource2);
      });

      it('should accept null and undefined values', () => {
        const bomSource: IBomSource = { id: 123 };
        expectedResult = service.addBomSourceToCollectionIfMissing([], null, bomSource, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bomSource);
      });

      it('should return initial array if no BomSource is added', () => {
        const bomSourceCollection: IBomSource[] = [{ id: 123 }];
        expectedResult = service.addBomSourceToCollectionIfMissing(bomSourceCollection, undefined, null);
        expect(expectedResult).toEqual(bomSourceCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
