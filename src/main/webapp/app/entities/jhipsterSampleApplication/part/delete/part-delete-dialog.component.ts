import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPart } from '../part.model';
import { PartService } from '../service/part.service';

@Component({
  templateUrl: './part-delete-dialog.component.html',
})
export class PartDeleteDialogComponent {
  part?: IPart;

  constructor(protected partService: PartService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.partService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
