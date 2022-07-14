import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPartIntention, getPartIntentionIdentifier } from '../part-intention.model';

export type EntityResponseType = HttpResponse<IPartIntention>;
export type EntityArrayResponseType = HttpResponse<IPartIntention[]>;

@Injectable({ providedIn: 'root' })
export class PartIntentionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/part-intentions', 'jhipstersampleapplication');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(partIntention: IPartIntention): Observable<EntityResponseType> {
    return this.http.post<IPartIntention>(this.resourceUrl, partIntention, { observe: 'response' });
  }

  update(partIntention: IPartIntention): Observable<EntityResponseType> {
    return this.http.put<IPartIntention>(`${this.resourceUrl}/${getPartIntentionIdentifier(partIntention) as number}`, partIntention, {
      observe: 'response',
    });
  }

  partialUpdate(partIntention: IPartIntention): Observable<EntityResponseType> {
    return this.http.patch<IPartIntention>(`${this.resourceUrl}/${getPartIntentionIdentifier(partIntention) as number}`, partIntention, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPartIntention>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPartIntention[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPartIntentionToCollectionIfMissing(
    partIntentionCollection: IPartIntention[],
    ...partIntentionsToCheck: (IPartIntention | null | undefined)[]
  ): IPartIntention[] {
    const partIntentions: IPartIntention[] = partIntentionsToCheck.filter(isPresent);
    if (partIntentions.length > 0) {
      const partIntentionCollectionIdentifiers = partIntentionCollection.map(
        partIntentionItem => getPartIntentionIdentifier(partIntentionItem)!
      );
      const partIntentionsToAdd = partIntentions.filter(partIntentionItem => {
        const partIntentionIdentifier = getPartIntentionIdentifier(partIntentionItem);
        if (partIntentionIdentifier == null || partIntentionCollectionIdentifiers.includes(partIntentionIdentifier)) {
          return false;
        }
        partIntentionCollectionIdentifiers.push(partIntentionIdentifier);
        return true;
      });
      return [...partIntentionsToAdd, ...partIntentionCollection];
    }
    return partIntentionCollection;
  }
}
