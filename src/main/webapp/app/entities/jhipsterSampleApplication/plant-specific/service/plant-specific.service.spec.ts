import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPlantSpecific, PlantSpecific } from '../plant-specific.model';

import { PlantSpecificService } from './plant-specific.service';

describe('PlantSpecific Service', () => {
  let service: PlantSpecificService;
  let httpMock: HttpTestingController;
  let elemDefault: IPlantSpecific;
  let expectedResult: IPlantSpecific | IPlantSpecific[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PlantSpecificService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      objectDependancy: 'AAAAAAA',
      refMaterial: 'AAAAAAA',
      isDiscontinued: false,
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

    it('should create a PlantSpecific', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new PlantSpecific()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PlantSpecific', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          objectDependancy: 'BBBBBB',
          refMaterial: 'BBBBBB',
          isDiscontinued: true,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PlantSpecific', () => {
      const patchObject = Object.assign(
        {
          objectDependancy: 'BBBBBB',
          isDiscontinued: true,
        },
        new PlantSpecific()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PlantSpecific', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          objectDependancy: 'BBBBBB',
          refMaterial: 'BBBBBB',
          isDiscontinued: true,
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

    it('should delete a PlantSpecific', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPlantSpecificToCollectionIfMissing', () => {
      it('should add a PlantSpecific to an empty array', () => {
        const plantSpecific: IPlantSpecific = { id: 123 };
        expectedResult = service.addPlantSpecificToCollectionIfMissing([], plantSpecific);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(plantSpecific);
      });

      it('should not add a PlantSpecific to an array that contains it', () => {
        const plantSpecific: IPlantSpecific = { id: 123 };
        const plantSpecificCollection: IPlantSpecific[] = [
          {
            ...plantSpecific,
          },
          { id: 456 },
        ];
        expectedResult = service.addPlantSpecificToCollectionIfMissing(plantSpecificCollection, plantSpecific);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PlantSpecific to an array that doesn't contain it", () => {
        const plantSpecific: IPlantSpecific = { id: 123 };
        const plantSpecificCollection: IPlantSpecific[] = [{ id: 456 }];
        expectedResult = service.addPlantSpecificToCollectionIfMissing(plantSpecificCollection, plantSpecific);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(plantSpecific);
      });

      it('should add only unique PlantSpecific to an array', () => {
        const plantSpecificArray: IPlantSpecific[] = [{ id: 123 }, { id: 456 }, { id: 40054 }];
        const plantSpecificCollection: IPlantSpecific[] = [{ id: 123 }];
        expectedResult = service.addPlantSpecificToCollectionIfMissing(plantSpecificCollection, ...plantSpecificArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const plantSpecific: IPlantSpecific = { id: 123 };
        const plantSpecific2: IPlantSpecific = { id: 456 };
        expectedResult = service.addPlantSpecificToCollectionIfMissing([], plantSpecific, plantSpecific2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(plantSpecific);
        expect(expectedResult).toContain(plantSpecific2);
      });

      it('should accept null and undefined values', () => {
        const plantSpecific: IPlantSpecific = { id: 123 };
        expectedResult = service.addPlantSpecificToCollectionIfMissing([], null, plantSpecific, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(plantSpecific);
      });

      it('should return initial array if no PlantSpecific is added', () => {
        const plantSpecificCollection: IPlantSpecific[] = [{ id: 123 }];
        expectedResult = service.addPlantSpecificToCollectionIfMissing(plantSpecificCollection, undefined, null);
        expect(expectedResult).toEqual(plantSpecificCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
