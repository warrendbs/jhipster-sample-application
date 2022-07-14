import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBomSource, getBomSourceIdentifier } from '../bom-source.model';

export type EntityResponseType = HttpResponse<IBomSource>;
export type EntityArrayResponseType = HttpResponse<IBomSource[]>;

@Injectable({ providedIn: 'root' })
export class BomSourceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/bom-sources', 'jhipstersampleapplication');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(bomSource: IBomSource): Observable<EntityResponseType> {
    return this.http.post<IBomSource>(this.resourceUrl, bomSource, { observe: 'response' });
  }

  update(bomSource: IBomSource): Observable<EntityResponseType> {
    return this.http.put<IBomSource>(`${this.resourceUrl}/${getBomSourceIdentifier(bomSource) as number}`, bomSource, {
      observe: 'response',
    });
  }

  partialUpdate(bomSource: IBomSource): Observable<EntityResponseType> {
    return this.http.patch<IBomSource>(`${this.resourceUrl}/${getBomSourceIdentifier(bomSource) as number}`, bomSource, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBomSource>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBomSource[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBomSourceToCollectionIfMissing(
    bomSourceCollection: IBomSource[],
    ...bomSourcesToCheck: (IBomSource | null | undefined)[]
  ): IBomSource[] {
    const bomSources: IBomSource[] = bomSourcesToCheck.filter(isPresent);
    if (bomSources.length > 0) {
      const bomSourceCollectionIdentifiers = bomSourceCollection.map(bomSourceItem => getBomSourceIdentifier(bomSourceItem)!);
      const bomSourcesToAdd = bomSources.filter(bomSourceItem => {
        const bomSourceIdentifier = getBomSourceIdentifier(bomSourceItem);
        if (bomSourceIdentifier == null || bomSourceCollectionIdentifiers.includes(bomSourceIdentifier)) {
          return false;
        }
        bomSourceCollectionIdentifiers.push(bomSourceIdentifier);
        return true;
      });
      return [...bomSourcesToAdd, ...bomSourceCollection];
    }
    return bomSourceCollection;
  }
}
