import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ReleasePackageComponent } from '../list/release-package.component';
import { ReleasePackageDetailComponent } from '../detail/release-package-detail.component';
import { ReleasePackageUpdateComponent } from '../update/release-package-update.component';
import { ReleasePackageRoutingResolveService } from './release-package-routing-resolve.service';

const releasePackageRoute: Routes = [
  {
    path: '',
    component: ReleasePackageComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ReleasePackageDetailComponent,
    resolve: {
      releasePackage: ReleasePackageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ReleasePackageUpdateComponent,
    resolve: {
      releasePackage: ReleasePackageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ReleasePackageUpdateComponent,
    resolve: {
      releasePackage: ReleasePackageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(releasePackageRoute)],
  exports: [RouterModule],
})
export class ReleasePackageRoutingModule {}
