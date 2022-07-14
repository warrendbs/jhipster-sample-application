import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBom, Bom } from '../bom.model';

import { BomService } from './bom.service';

describe('Bom Service', () => {
  let service: BomService;
  let httpMock: HttpTestingController;
  let elemDefault: IBom;
  let expectedResult: IBom | IBom[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BomService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
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

    it('should create a Bom', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Bom()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Bom', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
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

    it('should partial update a Bom', () => {
      const patchObject = Object.assign(
        {
          status: 1,
        },
        new Bom()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Bom', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
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

    it('should delete a Bom', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addBomToCollectionIfMissing', () => {
      it('should add a Bom to an empty array', () => {
        const bom: IBom = { id: 123 };
        expectedResult = service.addBomToCollectionIfMissing([], bom);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bom);
      });

      it('should not add a Bom to an array that contains it', () => {
        const bom: IBom = { id: 123 };
        const bomCollection: IBom[] = [
          {
            ...bom,
          },
          { id: 456 },
        ];
        expectedResult = service.addBomToCollectionIfMissing(bomCollection, bom);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Bom to an array that doesn't contain it", () => {
        const bom: IBom = { id: 123 };
        const bomCollection: IBom[] = [{ id: 456 }];
        expectedResult = service.addBomToCollectionIfMissing(bomCollection, bom);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bom);
      });

      it('should add only unique Bom to an array', () => {
        const bomArray: IBom[] = [{ id: 123 }, { id: 456 }, { id: 77915 }];
        const bomCollection: IBom[] = [{ id: 123 }];
        expectedResult = service.addBomToCollectionIfMissing(bomCollection, ...bomArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const bom: IBom = { id: 123 };
        const bom2: IBom = { id: 456 };
        expectedResult = service.addBomToCollectionIfMissing([], bom, bom2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bom);
        expect(expectedResult).toContain(bom2);
      });

      it('should accept null and undefined values', () => {
        const bom: IBom = { id: 123 };
        expectedResult = service.addBomToCollectionIfMissing([], null, bom, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bom);
      });

      it('should return initial array if no Bom is added', () => {
        const bomCollection: IBom[] = [{ id: 123 }];
        expectedResult = service.addBomToCollectionIfMissing(bomCollection, undefined, null);
        expect(expectedResult).toEqual(bomCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
