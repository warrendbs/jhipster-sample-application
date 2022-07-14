import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PartSourceComponent } from '../list/part-source.component';
import { PartSourceDetailComponent } from '../detail/part-source-detail.component';
import { PartSourceUpdateComponent } from '../update/part-source-update.component';
import { PartSourceRoutingResolveService } from './part-source-routing-resolve.service';

const partSourceRoute: Routes = [
  {
    path: '',
    component: PartSourceComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PartSourceDetailComponent,
    resolve: {
      partSource: PartSourceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PartSourceUpdateComponent,
    resolve: {
      partSource: PartSourceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PartSourceUpdateComponent,
    resolve: {
      partSource: PartSourceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(partSourceRoute)],
  exports: [RouterModule],
})
export class PartSourceRoutingModule {}
