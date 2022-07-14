import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IImpactMatrix, ImpactMatrix } from '../impact-matrix.model';
import { ImpactMatrixService } from '../service/impact-matrix.service';

@Injectable({ providedIn: 'root' })
export class ImpactMatrixRoutingResolveService implements Resolve<IImpactMatrix> {
  constructor(protected service: ImpactMatrixService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IImpactMatrix> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((impactMatrix: HttpResponse<ImpactMatrix>) => {
          if (impactMatrix.body) {
            return of(impactMatrix.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ImpactMatrix());
  }
}
