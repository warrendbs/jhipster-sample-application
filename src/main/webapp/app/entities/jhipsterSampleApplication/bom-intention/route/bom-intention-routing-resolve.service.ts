import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBomIntention, BomIntention } from '../bom-intention.model';
import { BomIntentionService } from '../service/bom-intention.service';

@Injectable({ providedIn: 'root' })
export class BomIntentionRoutingResolveService implements Resolve<IBomIntention> {
  constructor(protected service: BomIntentionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBomIntention> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((bomIntention: HttpResponse<BomIntention>) => {
          if (bomIntention.body) {
            return of(bomIntention.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new BomIntention());
  }
}
