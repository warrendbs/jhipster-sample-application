import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPartIntention, PartIntention } from '../part-intention.model';
import { PartIntentionService } from '../service/part-intention.service';

@Injectable({ providedIn: 'root' })
export class PartIntentionRoutingResolveService implements Resolve<IPartIntention> {
  constructor(protected service: PartIntentionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPartIntention> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((partIntention: HttpResponse<PartIntention>) => {
          if (partIntention.body) {
            return of(partIntention.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PartIntention());
  }
}
