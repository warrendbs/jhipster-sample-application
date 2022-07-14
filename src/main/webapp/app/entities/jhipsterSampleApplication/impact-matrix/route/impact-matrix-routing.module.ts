import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ImpactMatrixComponent } from '../list/impact-matrix.component';
import { ImpactMatrixDetailComponent } from '../detail/impact-matrix-detail.component';
import { ImpactMatrixUpdateComponent } from '../update/impact-matrix-update.component';
import { ImpactMatrixRoutingResolveService } from './impact-matrix-routing-resolve.service';

const impactMatrixRoute: Routes = [
  {
    path: '',
    component: ImpactMatrixComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ImpactMatrixDetailComponent,
    resolve: {
      impactMatrix: ImpactMatrixRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ImpactMatrixUpdateComponent,
    resolve: {
      impactMatrix: ImpactMatrixRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ImpactMatrixUpdateComponent,
    resolve: {
      impactMatrix: ImpactMatrixRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(impactMatrixRoute)],
  exports: [RouterModule],
})
export class ImpactMatrixRoutingModule {}
