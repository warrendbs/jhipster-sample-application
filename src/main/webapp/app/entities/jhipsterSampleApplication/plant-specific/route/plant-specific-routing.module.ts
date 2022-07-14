import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PlantSpecificComponent } from '../list/plant-specific.component';
import { PlantSpecificDetailComponent } from '../detail/plant-specific-detail.component';
import { PlantSpecificUpdateComponent } from '../update/plant-specific-update.component';
import { PlantSpecificRoutingResolveService } from './plant-specific-routing-resolve.service';

const plantSpecificRoute: Routes = [
  {
    path: '',
    component: PlantSpecificComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PlantSpecificDetailComponent,
    resolve: {
      plantSpecific: PlantSpecificRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PlantSpecificUpdateComponent,
    resolve: {
      plantSpecific: PlantSpecificRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PlantSpecificUpdateComponent,
    resolve: {
      plantSpecific: PlantSpecificRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(plantSpecificRoute)],
  exports: [RouterModule],
})
export class PlantSpecificRoutingModule {}
