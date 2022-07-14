import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BomComponent } from './list/bom.component';
import { BomDetailComponent } from './detail/bom-detail.component';
import { BomUpdateComponent } from './update/bom-update.component';
import { BomDeleteDialogComponent } from './delete/bom-delete-dialog.component';
import { BomRoutingModule } from './route/bom-routing.module';

@NgModule({
  imports: [SharedModule, BomRoutingModule],
  declarations: [BomComponent, BomDetailComponent, BomUpdateComponent, BomDeleteDialogComponent],
  entryComponents: [BomDeleteDialogComponent],
})
export class JhipsterSampleApplicationBomModule {}
