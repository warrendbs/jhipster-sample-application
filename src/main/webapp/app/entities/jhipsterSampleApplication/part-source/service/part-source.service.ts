import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPartSource, getPartSourceIdentifier } from '../part-source.model';

export type EntityResponseType = HttpResponse<IPartSource>;
export type EntityArrayResponseType = HttpResponse<IPartSource[]>;

@Injectable({ providedIn: 'root' })
export class PartSourceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/part-sources', 'jhipstersampleapplication');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(partSource: IPartSource): Observable<EntityResponseType> {
    return this.http.post<IPartSource>(this.resourceUrl, partSource, { observe: 'response' });
  }

  update(partSource: IPartSource): Observable<EntityResponseType> {
    return this.http.put<IPartSource>(`${this.resourceUrl}/${getPartSourceIdentifier(partSource) as number}`, partSource, {
      observe: 'response',
    });
  }

  partialUpdate(partSource: IPartSource): Observable<EntityResponseType> {
    return this.http.patch<IPartSource>(`${this.resourceUrl}/${getPartSourceIdentifier(partSource) as number}`, partSource, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPartSource>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPartSource[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPartSourceToCollectionIfMissing(
    partSourceCollection: IPartSource[],
    ...partSourcesToCheck: (IPartSource | null | undefined)[]
  ): IPartSource[] {
    const partSources: IPartSource[] = partSourcesToCheck.filter(isPresent);
    if (partSources.length > 0) {
      const partSourceCollectionIdentifiers = partSourceCollection.map(partSourceItem => getPartSourceIdentifier(partSourceItem)!);
      const partSourcesToAdd = partSources.filter(partSourceItem => {
        const partSourceIdentifier = getPartSourceIdentifier(partSourceItem);
        if (partSourceIdentifier == null || partSourceCollectionIdentifiers.includes(partSourceIdentifier)) {
          return false;
        }
        partSourceCollectionIdentifiers.push(partSourceIdentifier);
        return true;
      });
      return [...partSourcesToAdd, ...partSourceCollection];
    }
    return partSourceCollection;
  }
}
