import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPartIntention, PartIntention } from '../part-intention.model';

import { PartIntentionService } from './part-intention.service';

describe('PartIntention Service', () => {
  let service: PartIntentionService;
  let httpMock: HttpTestingController;
  let elemDefault: IPartIntention;
  let expectedResult: IPartIntention | IPartIntention[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PartIntentionService);
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

    it('should create a PartIntention', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new PartIntention()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PartIntention', () => {
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

    it('should partial update a PartIntention', () => {
      const patchObject = Object.assign(
        {
          productId: 1,
          revision: 'BBBBBB',
          vqi: 'BBBBBB',
          historyIndicator: 'BBBBBB',
          crossPlantStatusToBe: 'BBBBBB',
          toolPackCategory: 'BBBBBB',
          allowBomRestructuring: true,
          itemUsage: 'BBBBBB',
          isPhantom: true,
          failureRate: 'BBBBBB',
          inHouseProductionTime: 1,
          slAbcCode: 'BBBBBB',
          limitedDriving12Ncflag: 'BBBBBB',
          multiPlant: 'BBBBBB',
          type: 'BBBBBB',
          successorPartId: 1,
        },
        new PartIntention()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PartIntention', () => {
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

    it('should delete a PartIntention', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPartIntentionToCollectionIfMissing', () => {
      it('should add a PartIntention to an empty array', () => {
        const partIntention: IPartIntention = { id: 123 };
        expectedResult = service.addPartIntentionToCollectionIfMissing([], partIntention);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(partIntention);
      });

      it('should not add a PartIntention to an array that contains it', () => {
        const partIntention: IPartIntention = { id: 123 };
        const partIntentionCollection: IPartIntention[] = [
          {
            ...partIntention,
          },
          { id: 456 },
        ];
        expectedResult = service.addPartIntentionToCollectionIfMissing(partIntentionCollection, partIntention);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PartIntention to an array that doesn't contain it", () => {
        const partIntention: IPartIntention = { id: 123 };
        const partIntentionCollection: IPartIntention[] = [{ id: 456 }];
        expectedResult = service.addPartIntentionToCollectionIfMissing(partIntentionCollection, partIntention);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(partIntention);
      });

      it('should add only unique PartIntention to an array', () => {
        const partIntentionArray: IPartIntention[] = [{ id: 123 }, { id: 456 }, { id: 94403 }];
        const partIntentionCollection: IPartIntention[] = [{ id: 123 }];
        expectedResult = service.addPartIntentionToCollectionIfMissing(partIntentionCollection, ...partIntentionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const partIntention: IPartIntention = { id: 123 };
        const partIntention2: IPartIntention = { id: 456 };
        expectedResult = service.addPartIntentionToCollectionIfMissing([], partIntention, partIntention2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(partIntention);
        expect(expectedResult).toContain(partIntention2);
      });

      it('should accept null and undefined values', () => {
        const partIntention: IPartIntention = { id: 123 };
        expectedResult = service.addPartIntentionToCollectionIfMissing([], null, partIntention, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(partIntention);
      });

      it('should return initial array if no PartIntention is added', () => {
        const partIntentionCollection: IPartIntention[] = [{ id: 123 }];
        expectedResult = service.addPartIntentionToCollectionIfMissing(partIntentionCollection, undefined, null);
        expect(expectedResult).toEqual(partIntentionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
