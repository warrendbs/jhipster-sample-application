import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IReleasePackage, ReleasePackage } from '../release-package.model';
import { ReleasePackageService } from '../service/release-package.service';

@Injectable({ providedIn: 'root' })
export class ReleasePackageRoutingResolveService implements Resolve<IReleasePackage> {
  constructor(protected service: ReleasePackageService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IReleasePackage> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((releasePackage: HttpResponse<ReleasePackage>) => {
          if (releasePackage.body) {
            return of(releasePackage.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ReleasePackage());
  }
}
