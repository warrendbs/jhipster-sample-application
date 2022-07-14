import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IImpactMatrix, getImpactMatrixIdentifier } from '../impact-matrix.model';

export type EntityResponseType = HttpResponse<IImpactMatrix>;
export type EntityArrayResponseType = HttpResponse<IImpactMatrix[]>;

@Injectable({ providedIn: 'root' })
export class ImpactMatrixService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/impact-matrices', 'jhipstersampleapplication');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(impactMatrix: IImpactMatrix): Observable<EntityResponseType> {
    return this.http.post<IImpactMatrix>(this.resourceUrl, impactMatrix, { observe: 'response' });
  }

  update(impactMatrix: IImpactMatrix): Observable<EntityResponseType> {
    return this.http.put<IImpactMatrix>(`${this.resourceUrl}/${getImpactMatrixIdentifier(impactMatrix) as number}`, impactMatrix, {
      observe: 'response',
    });
  }

  partialUpdate(impactMatrix: IImpactMatrix): Observable<EntityResponseType> {
    return this.http.patch<IImpactMatrix>(`${this.resourceUrl}/${getImpactMatrixIdentifier(impactMatrix) as number}`, impactMatrix, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IImpactMatrix>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IImpactMatrix[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addImpactMatrixToCollectionIfMissing(
    impactMatrixCollection: IImpactMatrix[],
    ...impactMatricesToCheck: (IImpactMatrix | null | undefined)[]
  ): IImpactMatrix[] {
    const impactMatrices: IImpactMatrix[] = impactMatricesToCheck.filter(isPresent);
    if (impactMatrices.length > 0) {
      const impactMatrixCollectionIdentifiers = impactMatrixCollection.map(
        impactMatrixItem => getImpactMatrixIdentifier(impactMatrixItem)!
      );
      const impactMatricesToAdd = impactMatrices.filter(impactMatrixItem => {
        const impactMatrixIdentifier = getImpactMatrixIdentifier(impactMatrixItem);
        if (impactMatrixIdentifier == null || impactMatrixCollectionIdentifiers.includes(impactMatrixIdentifier)) {
          return false;
        }
        impactMatrixCollectionIdentifiers.push(impactMatrixIdentifier);
        return true;
      });
      return [...impactMatricesToAdd, ...impactMatrixCollection];
    }
    return impactMatrixCollection;
  }
}
