import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ReleasePackageComponent } from './list/release-package.component';
import { ReleasePackageDetailComponent } from './detail/release-package-detail.component';
import { ReleasePackageUpdateComponent } from './update/release-package-update.component';
import { ReleasePackageDeleteDialogComponent } from './delete/release-package-delete-dialog.component';
import { ReleasePackageRoutingModule } from './route/release-package-routing.module';

@NgModule({
  imports: [SharedModule, ReleasePackageRoutingModule],
  declarations: [
    ReleasePackageComponent,
    ReleasePackageDetailComponent,
    ReleasePackageUpdateComponent,
    ReleasePackageDeleteDialogComponent,
  ],
  entryComponents: [ReleasePackageDeleteDialogComponent],
})
export class JhipsterSampleApplicationReleasePackageModule {}
