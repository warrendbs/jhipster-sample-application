import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBomChild, getBomChildIdentifier } from '../bom-child.model';

export type EntityResponseType = HttpResponse<IBomChild>;
export type EntityArrayResponseType = HttpResponse<IBomChild[]>;

@Injectable({ providedIn: 'root' })
export class BomChildService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/bom-children', 'jhipstersampleapplication');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(bomChild: IBomChild): Observable<EntityResponseType> {
    return this.http.post<IBomChild>(this.resourceUrl, bomChild, { observe: 'response' });
  }

  update(bomChild: IBomChild): Observable<EntityResponseType> {
    return this.http.put<IBomChild>(`${this.resourceUrl}/${getBomChildIdentifier(bomChild) as number}`, bomChild, { observe: 'response' });
  }

  partialUpdate(bomChild: IBomChild): Observable<EntityResponseType> {
    return this.http.patch<IBomChild>(`${this.resourceUrl}/${getBomChildIdentifier(bomChild) as number}`, bomChild, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBomChild>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBomChild[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBomChildToCollectionIfMissing(bomChildCollection: IBomChild[], ...bomChildrenToCheck: (IBomChild | null | undefined)[]): IBomChild[] {
    const bomChildren: IBomChild[] = bomChildrenToCheck.filter(isPresent);
    if (bomChildren.length > 0) {
      const bomChildCollectionIdentifiers = bomChildCollection.map(bomChildItem => getBomChildIdentifier(bomChildItem)!);
      const bomChildrenToAdd = bomChildren.filter(bomChildItem => {
        const bomChildIdentifier = getBomChildIdentifier(bomChildItem);
        if (bomChildIdentifier == null || bomChildCollectionIdentifiers.includes(bomChildIdentifier)) {
          return false;
        }
        bomChildCollectionIdentifiers.push(bomChildIdentifier);
        return true;
      });
      return [...bomChildrenToAdd, ...bomChildCollection];
    }
    return bomChildCollection;
  }
}
