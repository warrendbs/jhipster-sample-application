import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IItemReference } from '../item-reference.model';
import { ItemReferenceService } from '../service/item-reference.service';

@Component({
  templateUrl: './item-reference-delete-dialog.component.html',
})
export class ItemReferenceDeleteDialogComponent {
  itemReference?: IItemReference;

  constructor(protected itemReferenceService: ItemReferenceService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.itemReferenceService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
