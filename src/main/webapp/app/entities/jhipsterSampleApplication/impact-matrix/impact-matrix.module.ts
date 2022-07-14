import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ImpactMatrixComponent } from './list/impact-matrix.component';
import { ImpactMatrixDetailComponent } from './detail/impact-matrix-detail.component';
import { ImpactMatrixUpdateComponent } from './update/impact-matrix-update.component';
import { ImpactMatrixDeleteDialogComponent } from './delete/impact-matrix-delete-dialog.component';
import { ImpactMatrixRoutingModule } from './route/impact-matrix-routing.module';

@NgModule({
  imports: [SharedModule, ImpactMatrixRoutingModule],
  declarations: [ImpactMatrixComponent, ImpactMatrixDetailComponent, ImpactMatrixUpdateComponent, ImpactMatrixDeleteDialogComponent],
  entryComponents: [ImpactMatrixDeleteDialogComponent],
})
export class JhipsterSampleApplicationImpactMatrixModule {}
