import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDocumentSource, getDocumentSourceIdentifier } from '../document-source.model';

export type EntityResponseType = HttpResponse<IDocumentSource>;
export type EntityArrayResponseType = HttpResponse<IDocumentSource[]>;

@Injectable({ providedIn: 'root' })
export class DocumentSourceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/document-sources', 'jhipstersampleapplication');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(documentSource: IDocumentSource): Observable<EntityResponseType> {
    return this.http.post<IDocumentSource>(this.resourceUrl, documentSource, { observe: 'response' });
  }

  update(documentSource: IDocumentSource): Observable<EntityResponseType> {
    return this.http.put<IDocumentSource>(`${this.resourceUrl}/${getDocumentSourceIdentifier(documentSource) as number}`, documentSource, {
      observe: 'response',
    });
  }

  partialUpdate(documentSource: IDocumentSource): Observable<EntityResponseType> {
    return this.http.patch<IDocumentSource>(
      `${this.resourceUrl}/${getDocumentSourceIdentifier(documentSource) as number}`,
      documentSource,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDocumentSource>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDocumentSource[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDocumentSourceToCollectionIfMissing(
    documentSourceCollection: IDocumentSource[],
    ...documentSourcesToCheck: (IDocumentSource | null | undefined)[]
  ): IDocumentSource[] {
    const documentSources: IDocumentSource[] = documentSourcesToCheck.filter(isPresent);
    if (documentSources.length > 0) {
      const documentSourceCollectionIdentifiers = documentSourceCollection.map(
        documentSourceItem => getDocumentSourceIdentifier(documentSourceItem)!
      );
      const documentSourcesToAdd = documentSources.filter(documentSourceItem => {
        const documentSourceIdentifier = getDocumentSourceIdentifier(documentSourceItem);
        if (documentSourceIdentifier == null || documentSourceCollectionIdentifiers.includes(documentSourceIdentifier)) {
          return false;
        }
        documentSourceCollectionIdentifiers.push(documentSourceIdentifier);
        return true;
      });
      return [...documentSourcesToAdd, ...documentSourceCollection];
    }
    return documentSourceCollection;
  }
}
