import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPartIntention } from '../part-intention.model';
import { PartIntentionService } from '../service/part-intention.service';

@Component({
  templateUrl: './part-intention-delete-dialog.component.html',
})
export class PartIntentionDeleteDialogComponent {
  partIntention?: IPartIntention;

  constructor(protected partIntentionService: PartIntentionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.partIntentionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
