import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IContext, Context } from '../context.model';
import { ContextService } from '../service/context.service';

@Injectable({ providedIn: 'root' })
export class ContextRoutingResolveService implements Resolve<IContext> {
  constructor(protected service: ContextService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IContext> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((context: HttpResponse<Context>) => {
          if (context.body) {
            return of(context.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Context());
  }
}
