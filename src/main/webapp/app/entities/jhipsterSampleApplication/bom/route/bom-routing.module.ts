import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BomComponent } from '../list/bom.component';
import { BomDetailComponent } from '../detail/bom-detail.component';
import { BomUpdateComponent } from '../update/bom-update.component';
import { BomRoutingResolveService } from './bom-routing-resolve.service';

const bomRoute: Routes = [
  {
    path: '',
    component: BomComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BomDetailComponent,
    resolve: {
      bom: BomRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BomUpdateComponent,
    resolve: {
      bom: BomRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BomUpdateComponent,
    resolve: {
      bom: BomRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(bomRoute)],
  exports: [RouterModule],
})
export class BomRoutingModule {}
