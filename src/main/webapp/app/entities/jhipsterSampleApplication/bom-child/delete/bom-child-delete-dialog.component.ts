import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBomChild } from '../bom-child.model';
import { BomChildService } from '../service/bom-child.service';

@Component({
  templateUrl: './bom-child-delete-dialog.component.html',
})
export class BomChildDeleteDialogComponent {
  bomChild?: IBomChild;

  constructor(protected bomChildService: BomChildService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.bomChildService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
