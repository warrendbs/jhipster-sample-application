import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PartSourceComponent } from './list/part-source.component';
import { PartSourceDetailComponent } from './detail/part-source-detail.component';
import { PartSourceUpdateComponent } from './update/part-source-update.component';
import { PartSourceDeleteDialogComponent } from './delete/part-source-delete-dialog.component';
import { PartSourceRoutingModule } from './route/part-source-routing.module';

@NgModule({
  imports: [SharedModule, PartSourceRoutingModule],
  declarations: [PartSourceComponent, PartSourceDetailComponent, PartSourceUpdateComponent, PartSourceDeleteDialogComponent],
  entryComponents: [PartSourceDeleteDialogComponent],
})
export class JhipsterSampleApplicationPartSourceModule {}
