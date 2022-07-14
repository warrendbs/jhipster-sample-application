import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ItemReferenceComponent } from '../list/item-reference.component';
import { ItemReferenceDetailComponent } from '../detail/item-reference-detail.component';
import { ItemReferenceUpdateComponent } from '../update/item-reference-update.component';
import { ItemReferenceRoutingResolveService } from './item-reference-routing-resolve.service';

const itemReferenceRoute: Routes = [
  {
    path: '',
    component: ItemReferenceComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ItemReferenceDetailComponent,
    resolve: {
      itemReference: ItemReferenceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ItemReferenceUpdateComponent,
    resolve: {
      itemReference: ItemReferenceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ItemReferenceUpdateComponent,
    resolve: {
      itemReference: ItemReferenceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(itemReferenceRoute)],
  exports: [RouterModule],
})
export class ItemReferenceRoutingModule {}
