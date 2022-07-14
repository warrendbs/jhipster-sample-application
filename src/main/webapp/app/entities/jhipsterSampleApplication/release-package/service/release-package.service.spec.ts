import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IReleasePackage, ReleasePackage } from '../release-package.model';

import { ReleasePackageService } from './release-package.service';

describe('ReleasePackage Service', () => {
  let service: ReleasePackageService;
  let httpMock: HttpTestingController;
  let elemDefault: IReleasePackage;
  let expectedResult: IReleasePackage | IReleasePackage[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ReleasePackageService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      title: 'AAAAAAA',
      releasePackageNumber: 'AAAAAAA',
      releasePackageTitle: 'AAAAAAA',
      status: 'AAAAAAA',
      ecn: 'AAAAAAA',
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

    it('should create a ReleasePackage', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ReleasePackage()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ReleasePackage', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          title: 'BBBBBB',
          releasePackageNumber: 'BBBBBB',
          releasePackageTitle: 'BBBBBB',
          status: 'BBBBBB',
          ecn: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ReleasePackage', () => {
      const patchObject = Object.assign(
        {
          title: 'BBBBBB',
          releasePackageTitle: 'BBBBBB',
          ecn: 'BBBBBB',
        },
        new ReleasePackage()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ReleasePackage', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          title: 'BBBBBB',
          releasePackageNumber: 'BBBBBB',
          releasePackageTitle: 'BBBBBB',
          status: 'BBBBBB',
          ecn: 'BBBBBB',
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

    it('should delete a ReleasePackage', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addReleasePackageToCollectionIfMissing', () => {
      it('should add a ReleasePackage to an empty array', () => {
        const releasePackage: IReleasePackage = { id: 123 };
        expectedResult = service.addReleasePackageToCollectionIfMissing([], releasePackage);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(releasePackage);
      });

      it('should not add a ReleasePackage to an array that contains it', () => {
        const releasePackage: IReleasePackage = { id: 123 };
        const releasePackageCollection: IReleasePackage[] = [
          {
            ...releasePackage,
          },
          { id: 456 },
        ];
        expectedResult = service.addReleasePackageToCollectionIfMissing(releasePackageCollection, releasePackage);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ReleasePackage to an array that doesn't contain it", () => {
        const releasePackage: IReleasePackage = { id: 123 };
        const releasePackageCollection: IReleasePackage[] = [{ id: 456 }];
        expectedResult = service.addReleasePackageToCollectionIfMissing(releasePackageCollection, releasePackage);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(releasePackage);
      });

      it('should add only unique ReleasePackage to an array', () => {
        const releasePackageArray: IReleasePackage[] = [{ id: 123 }, { id: 456 }, { id: 56952 }];
        const releasePackageCollection: IReleasePackage[] = [{ id: 123 }];
        expectedResult = service.addReleasePackageToCollectionIfMissing(releasePackageCollection, ...releasePackageArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const releasePackage: IReleasePackage = { id: 123 };
        const releasePackage2: IReleasePackage = { id: 456 };
        expectedResult = service.addReleasePackageToCollectionIfMissing([], releasePackage, releasePackage2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(releasePackage);
        expect(expectedResult).toContain(releasePackage2);
      });

      it('should accept null and undefined values', () => {
        const releasePackage: IReleasePackage = { id: 123 };
        expectedResult = service.addReleasePackageToCollectionIfMissing([], null, releasePackage, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(releasePackage);
      });

      it('should return initial array if no ReleasePackage is added', () => {
        const releasePackageCollection: IReleasePackage[] = [{ id: 123 }];
        expectedResult = service.addReleasePackageToCollectionIfMissing(releasePackageCollection, undefined, null);
        expect(expectedResult).toEqual(releasePackageCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
