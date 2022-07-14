import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DocumentSourceComponent } from './list/document-source.component';
import { DocumentSourceDetailComponent } from './detail/document-source-detail.component';
import { DocumentSourceUpdateComponent } from './update/document-source-update.component';
import { DocumentSourceDeleteDialogComponent } from './delete/document-source-delete-dialog.component';
import { DocumentSourceRoutingModule } from './route/document-source-routing.module';

@NgModule({
  imports: [SharedModule, DocumentSourceRoutingModule],
  declarations: [
    DocumentSourceComponent,
    DocumentSourceDetailComponent,
    DocumentSourceUpdateComponent,
    DocumentSourceDeleteDialogComponent,
  ],
  entryComponents: [DocumentSourceDeleteDialogComponent],
})
export class JhipsterSampleApplicationDocumentSourceModule {}
