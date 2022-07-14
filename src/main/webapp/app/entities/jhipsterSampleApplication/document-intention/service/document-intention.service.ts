import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDocumentIntention, getDocumentIntentionIdentifier } from '../document-intention.model';

export type EntityResponseType = HttpResponse<IDocumentIntention>;
export type EntityArrayResponseType = HttpResponse<IDocumentIntention[]>;

@Injectable({ providedIn: 'root' })
export class DocumentIntentionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/document-intentions', 'jhipstersampleapplication');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(documentIntention: IDocumentIntention): Observable<EntityResponseType> {
    return this.http.post<IDocumentIntention>(this.resourceUrl, documentIntention, { observe: 'response' });
  }

  update(documentIntention: IDocumentIntention): Observable<EntityResponseType> {
    return this.http.put<IDocumentIntention>(
      `${this.resourceUrl}/${getDocumentIntentionIdentifier(documentIntention) as number}`,
      documentIntention,
      { observe: 'response' }
    );
  }

  partialUpdate(documentIntention: IDocumentIntention): Observable<EntityResponseType> {
    return this.http.patch<IDocumentIntention>(
      `${this.resourceUrl}/${getDocumentIntentionIdentifier(documentIntention) as number}`,
      documentIntention,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDocumentIntention>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDocumentIntention[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDocumentIntentionToCollectionIfMissing(
    documentIntentionCollection: IDocumentIntention[],
    ...documentIntentionsToCheck: (IDocumentIntention | null | undefined)[]
  ): IDocumentIntention[] {
    const documentIntentions: IDocumentIntention[] = documentIntentionsToCheck.filter(isPresent);
    if (documentIntentions.length > 0) {
      const documentIntentionCollectionIdentifiers = documentIntentionCollection.map(
        documentIntentionItem => getDocumentIntentionIdentifier(documentIntentionItem)!
      );
      const documentIntentionsToAdd = documentIntentions.filter(documentIntentionItem => {
        const documentIntentionIdentifier = getDocumentIntentionIdentifier(documentIntentionItem);
        if (documentIntentionIdentifier == null || documentIntentionCollectionIdentifiers.includes(documentIntentionIdentifier)) {
          return false;
        }
        documentIntentionCollectionIdentifiers.push(documentIntentionIdentifier);
        return true;
      });
      return [...documentIntentionsToAdd, ...documentIntentionCollection];
    }
    return documentIntentionCollection;
  }
}
