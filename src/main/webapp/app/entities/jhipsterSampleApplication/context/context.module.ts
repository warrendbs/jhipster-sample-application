import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ContextComponent } from './list/context.component';
import { ContextDetailComponent } from './detail/context-detail.component';
import { ContextUpdateComponent } from './update/context-update.component';
import { ContextDeleteDialogComponent } from './delete/context-delete-dialog.component';
import { ContextRoutingModule } from './route/context-routing.module';

@NgModule({
  imports: [SharedModule, ContextRoutingModule],
  declarations: [ContextComponent, ContextDetailComponent, ContextUpdateComponent, ContextDeleteDialogComponent],
  entryComponents: [ContextDeleteDialogComponent],
})
export class JhipsterSampleApplicationContextModule {}
