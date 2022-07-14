import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BomChildComponent } from './list/bom-child.component';
import { BomChildDetailComponent } from './detail/bom-child-detail.component';
import { BomChildUpdateComponent } from './update/bom-child-update.component';
import { BomChildDeleteDialogComponent } from './delete/bom-child-delete-dialog.component';
import { BomChildRoutingModule } from './route/bom-child-routing.module';

@NgModule({
  imports: [SharedModule, BomChildRoutingModule],
  declarations: [BomChildComponent, BomChildDetailComponent, BomChildUpdateComponent, BomChildDeleteDialogComponent],
  entryComponents: [BomChildDeleteDialogComponent],
})
export class JhipsterSampleApplicationBomChildModule {}
