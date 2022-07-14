import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BomChildComponent } from '../list/bom-child.component';
import { BomChildDetailComponent } from '../detail/bom-child-detail.component';
import { BomChildUpdateComponent } from '../update/bom-child-update.component';
import { BomChildRoutingResolveService } from './bom-child-routing-resolve.service';

const bomChildRoute: Routes = [
  {
    path: '',
    component: BomChildComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BomChildDetailComponent,
    resolve: {
      bomChild: BomChildRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BomChildUpdateComponent,
    resolve: {
      bomChild: BomChildRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BomChildUpdateComponent,
    resolve: {
      bomChild: BomChildRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(bomChildRoute)],
  exports: [RouterModule],
})
export class BomChildRoutingModule {}
