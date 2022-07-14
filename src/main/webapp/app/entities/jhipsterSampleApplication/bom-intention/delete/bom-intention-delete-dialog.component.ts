import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBomIntention } from '../bom-intention.model';
import { BomIntentionService } from '../service/bom-intention.service';

@Component({
  templateUrl: './bom-intention-delete-dialog.component.html',
})
export class BomIntentionDeleteDialogComponent {
  bomIntention?: IBomIntention;

  constructor(protected bomIntentionService: BomIntentionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.bomIntentionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
