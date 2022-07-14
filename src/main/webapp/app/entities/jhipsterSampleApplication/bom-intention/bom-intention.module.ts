import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BomIntentionComponent } from './list/bom-intention.component';
import { BomIntentionDetailComponent } from './detail/bom-intention-detail.component';
import { BomIntentionUpdateComponent } from './update/bom-intention-update.component';
import { BomIntentionDeleteDialogComponent } from './delete/bom-intention-delete-dialog.component';
import { BomIntentionRoutingModule } from './route/bom-intention-routing.module';

@NgModule({
  imports: [SharedModule, BomIntentionRoutingModule],
  declarations: [BomIntentionComponent, BomIntentionDetailComponent, BomIntentionUpdateComponent, BomIntentionDeleteDialogComponent],
  entryComponents: [BomIntentionDeleteDialogComponent],
})
export class JhipsterSampleApplicationBomIntentionModule {}
