import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PlantSpecificComponent } from './list/plant-specific.component';
import { PlantSpecificDetailComponent } from './detail/plant-specific-detail.component';
import { PlantSpecificUpdateComponent } from './update/plant-specific-update.component';
import { PlantSpecificDeleteDialogComponent } from './delete/plant-specific-delete-dialog.component';
import { PlantSpecificRoutingModule } from './route/plant-specific-routing.module';

@NgModule({
  imports: [SharedModule, PlantSpecificRoutingModule],
  declarations: [PlantSpecificComponent, PlantSpecificDetailComponent, PlantSpecificUpdateComponent, PlantSpecificDeleteDialogComponent],
  entryComponents: [PlantSpecificDeleteDialogComponent],
})
export class JhipsterSampleApplicationPlantSpecificModule {}
