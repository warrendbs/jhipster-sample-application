import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBomIntention, getBomIntentionIdentifier } from '../bom-intention.model';

export type EntityResponseType = HttpResponse<IBomIntention>;
export type EntityArrayResponseType = HttpResponse<IBomIntention[]>;

@Injectable({ providedIn: 'root' })
export class BomIntentionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/bom-intentions', 'jhipstersampleapplication');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(bomIntention: IBomIntention): Observable<EntityResponseType> {
    return this.http.post<IBomIntention>(this.resourceUrl, bomIntention, { observe: 'response' });
  }

  update(bomIntention: IBomIntention): Observable<EntityResponseType> {
    return this.http.put<IBomIntention>(`${this.resourceUrl}/${getBomIntentionIdentifier(bomIntention) as number}`, bomIntention, {
      observe: 'response',
    });
  }

  partialUpdate(bomIntention: IBomIntention): Observable<EntityResponseType> {
    return this.http.patch<IBomIntention>(`${this.resourceUrl}/${getBomIntentionIdentifier(bomIntention) as number}`, bomIntention, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBomIntention>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBomIntention[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBomIntentionToCollectionIfMissing(
    bomIntentionCollection: IBomIntention[],
    ...bomIntentionsToCheck: (IBomIntention | null | undefined)[]
  ): IBomIntention[] {
    const bomIntentions: IBomIntention[] = bomIntentionsToCheck.filter(isPresent);
    if (bomIntentions.length > 0) {
      const bomIntentionCollectionIdentifiers = bomIntentionCollection.map(
        bomIntentionItem => getBomIntentionIdentifier(bomIntentionItem)!
      );
      const bomIntentionsToAdd = bomIntentions.filter(bomIntentionItem => {
        const bomIntentionIdentifier = getBomIntentionIdentifier(bomIntentionItem);
        if (bomIntentionIdentifier == null || bomIntentionCollectionIdentifiers.includes(bomIntentionIdentifier)) {
          return false;
        }
        bomIntentionCollectionIdentifiers.push(bomIntentionIdentifier);
        return true;
      });
      return [...bomIntentionsToAdd, ...bomIntentionCollection];
    }
    return bomIntentionCollection;
  }
}
