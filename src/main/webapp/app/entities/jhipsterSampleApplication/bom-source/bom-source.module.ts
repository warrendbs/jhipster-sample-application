import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BomSourceComponent } from './list/bom-source.component';
import { BomSourceDetailComponent } from './detail/bom-source-detail.component';
import { BomSourceUpdateComponent } from './update/bom-source-update.component';
import { BomSourceDeleteDialogComponent } from './delete/bom-source-delete-dialog.component';
import { BomSourceRoutingModule } from './route/bom-source-routing.module';

@NgModule({
  imports: [SharedModule, BomSourceRoutingModule],
  declarations: [BomSourceComponent, BomSourceDetailComponent, BomSourceUpdateComponent, BomSourceDeleteDialogComponent],
  entryComponents: [BomSourceDeleteDialogComponent],
})
export class JhipsterSampleApplicationBomSourceModule {}
