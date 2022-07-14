import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IItemReference, getItemReferenceIdentifier } from '../item-reference.model';

export type EntityResponseType = HttpResponse<IItemReference>;
export type EntityArrayResponseType = HttpResponse<IItemReference[]>;

@Injectable({ providedIn: 'root' })
export class ItemReferenceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/item-references', 'jhipstersampleapplication');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(itemReference: IItemReference): Observable<EntityResponseType> {
    return this.http.post<IItemReference>(this.resourceUrl, itemReference, { observe: 'response' });
  }

  update(itemReference: IItemReference): Observable<EntityResponseType> {
    return this.http.put<IItemReference>(`${this.resourceUrl}/${getItemReferenceIdentifier(itemReference) as number}`, itemReference, {
      observe: 'response',
    });
  }

  partialUpdate(itemReference: IItemReference): Observable<EntityResponseType> {
    return this.http.patch<IItemReference>(`${this.resourceUrl}/${getItemReferenceIdentifier(itemReference) as number}`, itemReference, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IItemReference>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IItemReference[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addItemReferenceToCollectionIfMissing(
    itemReferenceCollection: IItemReference[],
    ...itemReferencesToCheck: (IItemReference | null | undefined)[]
  ): IItemReference[] {
    const itemReferences: IItemReference[] = itemReferencesToCheck.filter(isPresent);
    if (itemReferences.length > 0) {
      const itemReferenceCollectionIdentifiers = itemReferenceCollection.map(
        itemReferenceItem => getItemReferenceIdentifier(itemReferenceItem)!
      );
      const itemReferencesToAdd = itemReferences.filter(itemReferenceItem => {
        const itemReferenceIdentifier = getItemReferenceIdentifier(itemReferenceItem);
        if (itemReferenceIdentifier == null || itemReferenceCollectionIdentifiers.includes(itemReferenceIdentifier)) {
          return false;
        }
        itemReferenceCollectionIdentifiers.push(itemReferenceIdentifier);
        return true;
      });
      return [...itemReferencesToAdd, ...itemReferenceCollection];
    }
    return itemReferenceCollection;
  }
}
