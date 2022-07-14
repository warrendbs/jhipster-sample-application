import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPartSource, PartSource } from '../part-source.model';
import { PartSourceService } from '../service/part-source.service';

@Injectable({ providedIn: 'root' })
export class PartSourceRoutingResolveService implements Resolve<IPartSource> {
  constructor(protected service: PartSourceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPartSource> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((partSource: HttpResponse<PartSource>) => {
          if (partSource.body) {
            return of(partSource.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PartSource());
  }
}
