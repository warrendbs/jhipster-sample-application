import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BomSourceComponent } from '../list/bom-source.component';
import { BomSourceDetailComponent } from '../detail/bom-source-detail.component';
import { BomSourceUpdateComponent } from '../update/bom-source-update.component';
import { BomSourceRoutingResolveService } from './bom-source-routing-resolve.service';

const bomSourceRoute: Routes = [
  {
    path: '',
    component: BomSourceComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BomSourceDetailComponent,
    resolve: {
      bomSource: BomSourceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BomSourceUpdateComponent,
    resolve: {
      bomSource: BomSourceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BomSourceUpdateComponent,
    resolve: {
      bomSource: BomSourceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(bomSourceRoute)],
  exports: [RouterModule],
})
export class BomSourceRoutingModule {}
