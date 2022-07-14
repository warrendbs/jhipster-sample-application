import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBomSource, BomSource } from '../bom-source.model';
import { BomSourceService } from '../service/bom-source.service';

@Injectable({ providedIn: 'root' })
export class BomSourceRoutingResolveService implements Resolve<IBomSource> {
  constructor(protected service: BomSourceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBomSource> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((bomSource: HttpResponse<BomSource>) => {
          if (bomSource.body) {
            return of(bomSource.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new BomSource());
  }
}
