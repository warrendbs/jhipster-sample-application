import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBom, Bom } from '../bom.model';
import { BomService } from '../service/bom.service';

@Injectable({ providedIn: 'root' })
export class BomRoutingResolveService implements Resolve<IBom> {
  constructor(protected service: BomService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBom> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((bom: HttpResponse<Bom>) => {
          if (bom.body) {
            return of(bom.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Bom());
  }
}
