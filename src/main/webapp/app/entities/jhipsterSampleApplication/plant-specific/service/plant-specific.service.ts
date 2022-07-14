import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPlantSpecific, getPlantSpecificIdentifier } from '../plant-specific.model';

export type EntityResponseType = HttpResponse<IPlantSpecific>;
export type EntityArrayResponseType = HttpResponse<IPlantSpecific[]>;

@Injectable({ providedIn: 'root' })
export class PlantSpecificService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/plant-specifics', 'jhipstersampleapplication');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(plantSpecific: IPlantSpecific): Observable<EntityResponseType> {
    return this.http.post<IPlantSpecific>(this.resourceUrl, plantSpecific, { observe: 'response' });
  }

  update(plantSpecific: IPlantSpecific): Observable<EntityResponseType> {
    return this.http.put<IPlantSpecific>(`${this.resourceUrl}/${getPlantSpecificIdentifier(plantSpecific) as number}`, plantSpecific, {
      observe: 'response',
    });
  }

  partialUpdate(plantSpecific: IPlantSpecific): Observable<EntityResponseType> {
    return this.http.patch<IPlantSpecific>(`${this.resourceUrl}/${getPlantSpecificIdentifier(plantSpecific) as number}`, plantSpecific, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPlantSpecific>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPlantSpecific[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPlantSpecificToCollectionIfMissing(
    plantSpecificCollection: IPlantSpecific[],
    ...plantSpecificsToCheck: (IPlantSpecific | null | undefined)[]
  ): IPlantSpecific[] {
    const plantSpecifics: IPlantSpecific[] = plantSpecificsToCheck.filter(isPresent);
    if (plantSpecifics.length > 0) {
      const plantSpecificCollectionIdentifiers = plantSpecificCollection.map(
        plantSpecificItem => getPlantSpecificIdentifier(plantSpecificItem)!
      );
      const plantSpecificsToAdd = plantSpecifics.filter(plantSpecificItem => {
        const plantSpecificIdentifier = getPlantSpecificIdentifier(plantSpecificItem);
        if (plantSpecificIdentifier == null || plantSpecificCollectionIdentifiers.includes(plantSpecificIdentifier)) {
          return false;
        }
        plantSpecificCollectionIdentifiers.push(plantSpecificIdentifier);
        return true;
      });
      return [...plantSpecificsToAdd, ...plantSpecificCollection];
    }
    return plantSpecificCollection;
  }
}
