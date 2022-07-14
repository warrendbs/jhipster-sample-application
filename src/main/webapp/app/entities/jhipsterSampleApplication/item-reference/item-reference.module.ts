import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ItemReferenceComponent } from './list/item-reference.component';
import { ItemReferenceDetailComponent } from './detail/item-reference-detail.component';
import { ItemReferenceUpdateComponent } from './update/item-reference-update.component';
import { ItemReferenceDeleteDialogComponent } from './delete/item-reference-delete-dialog.component';
import { ItemReferenceRoutingModule } from './route/item-reference-routing.module';

@NgModule({
  imports: [SharedModule, ItemReferenceRoutingModule],
  declarations: [ItemReferenceComponent, ItemReferenceDetailComponent, ItemReferenceUpdateComponent, ItemReferenceDeleteDialogComponent],
  entryComponents: [ItemReferenceDeleteDialogComponent],
})
export class JhipsterSampleApplicationItemReferenceModule {}
