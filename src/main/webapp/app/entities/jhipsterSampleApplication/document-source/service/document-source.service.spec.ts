import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDocumentSource, DocumentSource } from '../document-source.model';

import { DocumentSourceService } from './document-source.service';

describe('DocumentSource Service', () => {
  let service: DocumentSourceService;
  let httpMock: HttpTestingController;
  let elemDefault: IDocumentSource;
  let expectedResult: IDocumentSource | IDocumentSource[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DocumentSourceService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      description: 'AAAAAAA',
      changeIndicator: false,
      type: 'AAAAAAA',
      subtype: 'AAAAAAA',
      group: 'AAAAAAA',
      sheet: 'AAAAAAA',
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

    it('should create a DocumentSource', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new DocumentSource()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DocumentSource', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          description: 'BBBBBB',
          changeIndicator: true,
          type: 'BBBBBB',
          subtype: 'BBBBBB',
          group: 'BBBBBB',
          sheet: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DocumentSource', () => {
      const patchObject = Object.assign(
        {
          changeIndicator: true,
          subtype: 'BBBBBB',
          group: 'BBBBBB',
          sheet: 'BBBBBB',
        },
        new DocumentSource()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DocumentSource', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          description: 'BBBBBB',
          changeIndicator: true,
          type: 'BBBBBB',
          subtype: 'BBBBBB',
          group: 'BBBBBB',
          sheet: 'BBBBBB',
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

    it('should delete a DocumentSource', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDocumentSourceToCollectionIfMissing', () => {
      it('should add a DocumentSource to an empty array', () => {
        const documentSource: IDocumentSource = { id: 123 };
        expectedResult = service.addDocumentSourceToCollectionIfMissing([], documentSource);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(documentSource);
      });

      it('should not add a DocumentSource to an array that contains it', () => {
        const documentSource: IDocumentSource = { id: 123 };
        const documentSourceCollection: IDocumentSource[] = [
          {
            ...documentSource,
          },
          { id: 456 },
        ];
        expectedResult = service.addDocumentSourceToCollectionIfMissing(documentSourceCollection, documentSource);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DocumentSource to an array that doesn't contain it", () => {
        const documentSource: IDocumentSource = { id: 123 };
        const documentSourceCollection: IDocumentSource[] = [{ id: 456 }];
        expectedResult = service.addDocumentSourceToCollectionIfMissing(documentSourceCollection, documentSource);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(documentSource);
      });

      it('should add only unique DocumentSource to an array', () => {
        const documentSourceArray: IDocumentSource[] = [{ id: 123 }, { id: 456 }, { id: 2930 }];
        const documentSourceCollection: IDocumentSource[] = [{ id: 123 }];
        expectedResult = service.addDocumentSourceToCollectionIfMissing(documentSourceCollection, ...documentSourceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const documentSource: IDocumentSource = { id: 123 };
        const documentSource2: IDocumentSource = { id: 456 };
        expectedResult = service.addDocumentSourceToCollectionIfMissing([], documentSource, documentSource2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(documentSource);
        expect(expectedResult).toContain(documentSource2);
      });

      it('should accept null and undefined values', () => {
        const documentSource: IDocumentSource = { id: 123 };
        expectedResult = service.addDocumentSourceToCollectionIfMissing([], null, documentSource, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(documentSource);
      });

      it('should return initial array if no DocumentSource is added', () => {
        const documentSourceCollection: IDocumentSource[] = [{ id: 123 }];
        expectedResult = service.addDocumentSourceToCollectionIfMissing(documentSourceCollection, undefined, null);
        expect(expectedResult).toEqual(documentSourceCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
