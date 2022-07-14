import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PartIntentionComponent } from './list/part-intention.component';
import { PartIntentionDetailComponent } from './detail/part-intention-detail.component';
import { PartIntentionUpdateComponent } from './update/part-intention-update.component';
import { PartIntentionDeleteDialogComponent } from './delete/part-intention-delete-dialog.component';
import { PartIntentionRoutingModule } from './route/part-intention-routing.module';

@NgModule({
  imports: [SharedModule, PartIntentionRoutingModule],
  declarations: [PartIntentionComponent, PartIntentionDetailComponent, PartIntentionUpdateComponent, PartIntentionDeleteDialogComponent],
  entryComponents: [PartIntentionDeleteDialogComponent],
})
export class JhipsterSampleApplicationPartIntentionModule {}
