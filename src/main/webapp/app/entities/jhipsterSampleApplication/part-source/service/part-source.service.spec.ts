import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPartSource, PartSource } from '../part-source.model';

import { PartSourceService } from './part-source.service';

describe('PartSource Service', () => {
  let service: PartSourceService;
  let httpMock: HttpTestingController;
  let elemDefault: IPartSource;
  let expectedResult: IPartSource | IPartSource[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PartSourceService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      productId: 0,
      revision: 'AAAAAAA',
      name: 'AAAAAAA',
      description: 'AAAAAAA',
      vqi: 'AAAAAAA',
      procurementType: 'AAAAAAA',
      materialType: 'AAAAAAA',
      serialNumberProfile: 'AAAAAAA',
      criticalConfigurationItemIndicator: false,
      regularPartIndicator: 'AAAAAAA',
      historyIndicator: 'AAAAAAA',
      crossPlantStatus: 'AAAAAAA',
      crossPlantStatusToBe: 'AAAAAAA',
      toolPackCategory: 'AAAAAAA',
      tcChangeControl: false,
      sapChangeControl: false,
      allowBomRestructuring: false,
      unitOfMeasure: 'AAAAAAA',
      itemUsage: 'AAAAAAA',
      isPhantom: false,
      failureRate: 'AAAAAAA',
      inHouseProductionTime: 0,
      slAbcCode: 'AAAAAAA',
      productionPlant: 'AAAAAAA',
      limitedDriving12Nc: 'AAAAAAA',
      limitedDriving12Ncflag: 'AAAAAAA',
      multiPlant: 'AAAAAAA',
      type: 'AAAAAAA',
      successorPartId: 0,
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

    it('should create a PartSource', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new PartSource()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PartSource', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          productId: 1,
          revision: 'BBBBBB',
          name: 'BBBBBB',
          description: 'BBBBBB',
          vqi: 'BBBBBB',
          procurementType: 'BBBBBB',
          materialType: 'BBBBBB',
          serialNumberProfile: 'BBBBBB',
          criticalConfigurationItemIndicator: true,
          regularPartIndicator: 'BBBBBB',
          historyIndicator: 'BBBBBB',
          crossPlantStatus: 'BBBBBB',
          crossPlantStatusToBe: 'BBBBBB',
          toolPackCategory: 'BBBBBB',
          tcChangeControl: true,
          sapChangeControl: true,
          allowBomRestructuring: true,
          unitOfMeasure: 'BBBBBB',
          itemUsage: 'BBBBBB',
          isPhantom: true,
          failureRate: 'BBBBBB',
          inHouseProductionTime: 1,
          slAbcCode: 'BBBBBB',
          productionPlant: 'BBBBBB',
          limitedDriving12Nc: 'BBBBBB',
          limitedDriving12Ncflag: 'BBBBBB',
          multiPlant: 'BBBBBB',
          type: 'BBBBBB',
          successorPartId: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PartSource', () => {
      const patchObject = Object.assign(
        {
          productId: 1,
          revision: 'BBBBBB',
          name: 'BBBBBB',
          description: 'BBBBBB',
          vqi: 'BBBBBB',
          materialType: 'BBBBBB',
          criticalConfigurationItemIndicator: true,
          regularPartIndicator: 'BBBBBB',
          historyIndicator: 'BBBBBB',
          crossPlantStatus: 'BBBBBB',
          crossPlantStatusToBe: 'BBBBBB',
          toolPackCategory: 'BBBBBB',
          allowBomRestructuring: true,
          unitOfMeasure: 'BBBBBB',
          itemUsage: 'BBBBBB',
          isPhantom: true,
          type: 'BBBBBB',
        },
        new PartSource()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PartSource', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          productId: 1,
          revision: 'BBBBBB',
          name: 'BBBBBB',
          description: 'BBBBBB',
          vqi: 'BBBBBB',
          procurementType: 'BBBBBB',
          materialType: 'BBBBBB',
          serialNumberProfile: 'BBBBBB',
          criticalConfigurationItemIndicator: true,
          regularPartIndicator: 'BBBBBB',
          historyIndicator: 'BBBBBB',
          crossPlantStatus: 'BBBBBB',
          crossPlantStatusToBe: 'BBBBBB',
          toolPackCategory: 'BBBBBB',
          tcChangeControl: true,
          sapChangeControl: true,
          allowBomRestructuring: true,
          unitOfMeasure: 'BBBBBB',
          itemUsage: 'BBBBBB',
          isPhantom: true,
          failureRate: 'BBBBBB',
          inHouseProductionTime: 1,
          slAbcCode: 'BBBBBB',
          productionPlant: 'BBBBBB',
          limitedDriving12Nc: 'BBBBBB',
          limitedDriving12Ncflag: 'BBBBBB',
          multiPlant: 'BBBBBB',
          type: 'BBBBBB',
          successorPartId: 1,
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

    it('should delete a PartSource', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPartSourceToCollectionIfMissing', () => {
      it('should add a PartSource to an empty array', () => {
        const partSource: IPartSource = { id: 123 };
        expectedResult = service.addPartSourceToCollectionIfMissing([], partSource);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(partSource);
      });

      it('should not add a PartSource to an array that contains it', () => {
        const partSource: IPartSource = { id: 123 };
        const partSourceCollection: IPartSource[] = [
          {
            ...partSource,
          },
          { id: 456 },
        ];
        expectedResult = service.addPartSourceToCollectionIfMissing(partSourceCollection, partSource);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PartSource to an array that doesn't contain it", () => {
        const partSource: IPartSource = { id: 123 };
        const partSourceCollection: IPartSource[] = [{ id: 456 }];
        expectedResult = service.addPartSourceToCollectionIfMissing(partSourceCollection, partSource);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(partSource);
      });

      it('should add only unique PartSource to an array', () => {
        const partSourceArray: IPartSource[] = [{ id: 123 }, { id: 456 }, { id: 85003 }];
        const partSourceCollection: IPartSource[] = [{ id: 123 }];
        expectedResult = service.addPartSourceToCollectionIfMissing(partSourceCollection, ...partSourceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const partSource: IPartSource = { id: 123 };
        const partSource2: IPartSource = { id: 456 };
        expectedResult = service.addPartSourceToCollectionIfMissing([], partSource, partSource2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(partSource);
        expect(expectedResult).toContain(partSource2);
      });

      it('should accept null and undefined values', () => {
        const partSource: IPartSource = { id: 123 };
        expectedResult = service.addPartSourceToCollectionIfMissing([], null, partSource, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(partSource);
      });

      it('should return initial array if no PartSource is added', () => {
        const partSourceCollection: IPartSource[] = [{ id: 123 }];
        expectedResult = service.addPartSourceToCollectionIfMissing(partSourceCollection, undefined, null);
        expect(expectedResult).toEqual(partSourceCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
