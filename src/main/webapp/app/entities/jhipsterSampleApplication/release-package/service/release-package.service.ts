import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IReleasePackage, getReleasePackageIdentifier } from '../release-package.model';

export type EntityResponseType = HttpResponse<IReleasePackage>;
export type EntityArrayResponseType = HttpResponse<IReleasePackage[]>;

@Injectable({ providedIn: 'root' })
export class ReleasePackageService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/release-packages', 'jhipstersampleapplication');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(releasePackage: IReleasePackage): Observable<EntityResponseType> {
    return this.http.post<IReleasePackage>(this.resourceUrl, releasePackage, { observe: 'response' });
  }

  update(releasePackage: IReleasePackage): Observable<EntityResponseType> {
    return this.http.put<IReleasePackage>(`${this.resourceUrl}/${getReleasePackageIdentifier(releasePackage) as number}`, releasePackage, {
      observe: 'response',
    });
  }

  partialUpdate(releasePackage: IReleasePackage): Observable<EntityResponseType> {
    return this.http.patch<IReleasePackage>(
      `${this.resourceUrl}/${getReleasePackageIdentifier(releasePackage) as number}`,
      releasePackage,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IReleasePackage>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IReleasePackage[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addReleasePackageToCollectionIfMissing(
    releasePackageCollection: IReleasePackage[],
    ...releasePackagesToCheck: (IReleasePackage | null | undefined)[]
  ): IReleasePackage[] {
    const releasePackages: IReleasePackage[] = releasePackagesToCheck.filter(isPresent);
    if (releasePackages.length > 0) {
      const releasePackageCollectionIdentifiers = releasePackageCollection.map(
        releasePackageItem => getReleasePackageIdentifier(releasePackageItem)!
      );
      const releasePackagesToAdd = releasePackages.filter(releasePackageItem => {
        const releasePackageIdentifier = getReleasePackageIdentifier(releasePackageItem);
        if (releasePackageIdentifier == null || releasePackageCollectionIdentifiers.includes(releasePackageIdentifier)) {
          return false;
        }
        releasePackageCollectionIdentifiers.push(releasePackageIdentifier);
        return true;
      });
      return [...releasePackagesToAdd, ...releasePackageCollection];
    }
    return releasePackageCollection;
  }
}
