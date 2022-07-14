import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBom, getBomIdentifier } from '../bom.model';

export type EntityResponseType = HttpResponse<IBom>;
export type EntityArrayResponseType = HttpResponse<IBom[]>;

@Injectable({ providedIn: 'root' })
export class BomService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/boms', 'jhipstersampleapplication');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(bom: IBom): Observable<EntityResponseType> {
    return this.http.post<IBom>(this.resourceUrl, bom, { observe: 'response' });
  }

  update(bom: IBom): Observable<EntityResponseType> {
    return this.http.put<IBom>(`${this.resourceUrl}/${getBomIdentifier(bom) as number}`, bom, { observe: 'response' });
  }

  partialUpdate(bom: IBom): Observable<EntityResponseType> {
    return this.http.patch<IBom>(`${this.resourceUrl}/${getBomIdentifier(bom) as number}`, bom, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBom>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBom[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBomToCollectionIfMissing(bomCollection: IBom[], ...bomsToCheck: (IBom | null | undefined)[]): IBom[] {
    const boms: IBom[] = bomsToCheck.filter(isPresent);
    if (boms.length > 0) {
      const bomCollectionIdentifiers = bomCollection.map(bomItem => getBomIdentifier(bomItem)!);
      const bomsToAdd = boms.filter(bomItem => {
        const bomIdentifier = getBomIdentifier(bomItem);
        if (bomIdentifier == null || bomCollectionIdentifiers.includes(bomIdentifier)) {
          return false;
        }
        bomCollectionIdentifiers.push(bomIdentifier);
        return true;
      });
      return [...bomsToAdd, ...bomCollection];
    }
    return bomCollection;
  }
}
