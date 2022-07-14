import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDocumentIntention, DocumentIntention } from '../document-intention.model';

import { DocumentIntentionService } from './document-intention.service';

describe('DocumentIntention Service', () => {
  let service: DocumentIntentionService;
  let httpMock: HttpTestingController;
  let elemDefault: IDocumentIntention;
  let expectedResult: IDocumentIntention | IDocumentIntention[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DocumentIntentionService);
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

    it('should create a DocumentIntention', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new DocumentIntention()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DocumentIntention', () => {
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

    it('should partial update a DocumentIntention', () => {
      const patchObject = Object.assign(
        {
          changeIndicator: true,
          type: 'BBBBBB',
          subtype: 'BBBBBB',
          group: 'BBBBBB',
          sheet: 'BBBBBB',
        },
        new DocumentIntention()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DocumentIntention', () => {
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

    it('should delete a DocumentIntention', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDocumentIntentionToCollectionIfMissing', () => {
      it('should add a DocumentIntention to an empty array', () => {
        const documentIntention: IDocumentIntention = { id: 123 };
        expectedResult = service.addDocumentIntentionToCollectionIfMissing([], documentIntention);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(documentIntention);
      });

      it('should not add a DocumentIntention to an array that contains it', () => {
        const documentIntention: IDocumentIntention = { id: 123 };
        const documentIntentionCollection: IDocumentIntention[] = [
          {
            ...documentIntention,
          },
          { id: 456 },
        ];
        expectedResult = service.addDocumentIntentionToCollectionIfMissing(documentIntentionCollection, documentIntention);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DocumentIntention to an array that doesn't contain it", () => {
        const documentIntention: IDocumentIntention = { id: 123 };
        const documentIntentionCollection: IDocumentIntention[] = [{ id: 456 }];
        expectedResult = service.addDocumentIntentionToCollectionIfMissing(documentIntentionCollection, documentIntention);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(documentIntention);
      });

      it('should add only unique DocumentIntention to an array', () => {
        const documentIntentionArray: IDocumentIntention[] = [{ id: 123 }, { id: 456 }, { id: 61183 }];
        const documentIntentionCollection: IDocumentIntention[] = [{ id: 123 }];
        expectedResult = service.addDocumentIntentionToCollectionIfMissing(documentIntentionCollection, ...documentIntentionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const documentIntention: IDocumentIntention = { id: 123 };
        const documentIntention2: IDocumentIntention = { id: 456 };
        expectedResult = service.addDocumentIntentionToCollectionIfMissing([], documentIntention, documentIntention2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(documentIntention);
        expect(expectedResult).toContain(documentIntention2);
      });

      it('should accept null and undefined values', () => {
        const documentIntention: IDocumentIntention = { id: 123 };
        expectedResult = service.addDocumentIntentionToCollectionIfMissing([], null, documentIntention, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(documentIntention);
      });

      it('should return initial array if no DocumentIntention is added', () => {
        const documentIntentionCollection: IDocumentIntention[] = [{ id: 123 }];
        expectedResult = service.addDocumentIntentionToCollectionIfMissing(documentIntentionCollection, undefined, null);
        expect(expectedResult).toEqual(documentIntentionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
