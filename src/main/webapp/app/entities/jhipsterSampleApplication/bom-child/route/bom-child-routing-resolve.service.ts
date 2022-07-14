import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBomChild, BomChild } from '../bom-child.model';
import { BomChildService } from '../service/bom-child.service';

@Injectable({ providedIn: 'root' })
export class BomChildRoutingResolveService implements Resolve<IBomChild> {
  constructor(protected service: BomChildService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBomChild> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((bomChild: HttpResponse<BomChild>) => {
          if (bomChild.body) {
            return of(bomChild.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new BomChild());
  }
}
