import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBom } from '../bom.model';
import { BomService } from '../service/bom.service';

@Component({
  templateUrl: './bom-delete-dialog.component.html',
})
export class BomDeleteDialogComponent {
  bom?: IBom;

  constructor(protected bomService: BomService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.bomService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
