import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PartIntentionComponent } from '../list/part-intention.component';
import { PartIntentionDetailComponent } from '../detail/part-intention-detail.component';
import { PartIntentionUpdateComponent } from '../update/part-intention-update.component';
import { PartIntentionRoutingResolveService } from './part-intention-routing-resolve.service';

const partIntentionRoute: Routes = [
  {
    path: '',
    component: PartIntentionComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PartIntentionDetailComponent,
    resolve: {
      partIntention: PartIntentionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PartIntentionUpdateComponent,
    resolve: {
      partIntention: PartIntentionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PartIntentionUpdateComponent,
    resolve: {
      partIntention: PartIntentionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(partIntentionRoute)],
  exports: [RouterModule],
})
export class PartIntentionRoutingModule {}
