import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IContext, getContextIdentifier } from '../context.model';

export type EntityResponseType = HttpResponse<IContext>;
export type EntityArrayResponseType = HttpResponse<IContext[]>;

@Injectable({ providedIn: 'root' })
export class ContextService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/contexts', 'jhipstersampleapplication');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(context: IContext): Observable<EntityResponseType> {
    return this.http.post<IContext>(this.resourceUrl, context, { observe: 'response' });
  }

  update(context: IContext): Observable<EntityResponseType> {
    return this.http.put<IContext>(`${this.resourceUrl}/${getContextIdentifier(context) as number}`, context, { observe: 'response' });
  }

  partialUpdate(context: IContext): Observable<EntityResponseType> {
    return this.http.patch<IContext>(`${this.resourceUrl}/${getContextIdentifier(context) as number}`, context, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IContext>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IContext[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addContextToCollectionIfMissing(contextCollection: IContext[], ...contextsToCheck: (IContext | null | undefined)[]): IContext[] {
    const contexts: IContext[] = contextsToCheck.filter(isPresent);
    if (contexts.length > 0) {
      const contextCollectionIdentifiers = contextCollection.map(contextItem => getContextIdentifier(contextItem)!);
      const contextsToAdd = contexts.filter(contextItem => {
        const contextIdentifier = getContextIdentifier(contextItem);
        if (contextIdentifier == null || contextCollectionIdentifiers.includes(contextIdentifier)) {
          return false;
        }
        contextCollectionIdentifiers.push(contextIdentifier);
        return true;
      });
      return [...contextsToAdd, ...contextCollection];
    }
    return contextCollection;
  }
}
