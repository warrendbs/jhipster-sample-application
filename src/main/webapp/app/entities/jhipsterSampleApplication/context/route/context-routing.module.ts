import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ContextComponent } from '../list/context.component';
import { ContextDetailComponent } from '../detail/context-detail.component';
import { ContextUpdateComponent } from '../update/context-update.component';
import { ContextRoutingResolveService } from './context-routing-resolve.service';

const contextRoute: Routes = [
  {
    path: '',
    component: ContextComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ContextDetailComponent,
    resolve: {
      context: ContextRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ContextUpdateComponent,
    resolve: {
      context: ContextRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ContextUpdateComponent,
    resolve: {
      context: ContextRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(contextRoute)],
  exports: [RouterModule],
})
export class ContextRoutingModule {}
