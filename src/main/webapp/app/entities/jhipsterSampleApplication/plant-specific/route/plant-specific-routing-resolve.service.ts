import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPlantSpecific, PlantSpecific } from '../plant-specific.model';
import { PlantSpecificService } from '../service/plant-specific.service';

@Injectable({ providedIn: 'root' })
export class PlantSpecificRoutingResolveService implements Resolve<IPlantSpecific> {
  constructor(protected service: PlantSpecificService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPlantSpecific> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((plantSpecific: HttpResponse<PlantSpecific>) => {
          if (plantSpecific.body) {
            return of(plantSpecific.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PlantSpecific());
  }
}
