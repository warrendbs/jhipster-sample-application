import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DocumentIntentionComponent } from './list/document-intention.component';
import { DocumentIntentionDetailComponent } from './detail/document-intention-detail.component';
import { DocumentIntentionUpdateComponent } from './update/document-intention-update.component';
import { DocumentIntentionDeleteDialogComponent } from './delete/document-intention-delete-dialog.component';
import { DocumentIntentionRoutingModule } from './route/document-intention-routing.module';

@NgModule({
  imports: [SharedModule, DocumentIntentionRoutingModule],
  declarations: [
    DocumentIntentionComponent,
    DocumentIntentionDetailComponent,
    DocumentIntentionUpdateComponent,
    DocumentIntentionDeleteDialogComponent,
  ],
  entryComponents: [DocumentIntentionDeleteDialogComponent],
})
export class JhipsterSampleApplicationDocumentIntentionModule {}
