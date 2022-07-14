import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DocumentSourceComponent } from '../list/document-source.component';
import { DocumentSourceDetailComponent } from '../detail/document-source-detail.component';
import { DocumentSourceUpdateComponent } from '../update/document-source-update.component';
import { DocumentSourceRoutingResolveService } from './document-source-routing-resolve.service';

const documentSourceRoute: Routes = [
  {
    path: '',
    component: DocumentSourceComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DocumentSourceDetailComponent,
    resolve: {
      documentSource: DocumentSourceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DocumentSourceUpdateComponent,
    resolve: {
      documentSource: DocumentSourceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DocumentSourceUpdateComponent,
    resolve: {
      documentSource: DocumentSourceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(documentSourceRoute)],
  exports: [RouterModule],
})
export class DocumentSourceRoutingModule {}
