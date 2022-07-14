import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DocumentIntentionComponent } from '../list/document-intention.component';
import { DocumentIntentionDetailComponent } from '../detail/document-intention-detail.component';
import { DocumentIntentionUpdateComponent } from '../update/document-intention-update.component';
import { DocumentIntentionRoutingResolveService } from './document-intention-routing-resolve.service';

const documentIntentionRoute: Routes = [
  {
    path: '',
    component: DocumentIntentionComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DocumentIntentionDetailComponent,
    resolve: {
      documentIntention: DocumentIntentionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DocumentIntentionUpdateComponent,
    resolve: {
      documentIntention: DocumentIntentionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DocumentIntentionUpdateComponent,
    resolve: {
      documentIntention: DocumentIntentionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(documentIntentionRoute)],
  exports: [RouterModule],
})
export class DocumentIntentionRoutingModule {}
