import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BomIntentionComponent } from '../list/bom-intention.component';
import { BomIntentionDetailComponent } from '../detail/bom-intention-detail.component';
import { BomIntentionUpdateComponent } from '../update/bom-intention-update.component';
import { BomIntentionRoutingResolveService } from './bom-intention-routing-resolve.service';

const bomIntentionRoute: Routes = [
  {
    path: '',
    component: BomIntentionComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BomIntentionDetailComponent,
    resolve: {
      bomIntention: BomIntentionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BomIntentionUpdateComponent,
    resolve: {
      bomIntention: BomIntentionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BomIntentionUpdateComponent,
    resolve: {
      bomIntention: BomIntentionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(bomIntentionRoute)],
  exports: [RouterModule],
})
export class BomIntentionRoutingModule {}
