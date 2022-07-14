import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPartSource } from '../part-source.model';
import { PartSourceService } from '../service/part-source.service';

@Component({
  templateUrl: './part-source-delete-dialog.component.html',
})
export class PartSourceDeleteDialogComponent {
  partSource?: IPartSource;

  constructor(protected partSourceService: PartSourceService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.partSourceService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
