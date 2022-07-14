import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IItemReference, ItemReference } from '../item-reference.model';
import { ItemReferenceService } from '../service/item-reference.service';

@Injectable({ providedIn: 'root' })
export class ItemReferenceRoutingResolveService implements Resolve<IItemReference> {
  constructor(protected service: ItemReferenceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IItemReference> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((itemReference: HttpResponse<ItemReference>) => {
          if (itemReference.body) {
            return of(itemReference.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ItemReference());
  }
}
