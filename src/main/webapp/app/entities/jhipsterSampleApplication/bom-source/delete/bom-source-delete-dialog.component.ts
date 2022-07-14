import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBomSource } from '../bom-source.model';
import { BomSourceService } from '../service/bom-source.service';

@Component({
  templateUrl: './bom-source-delete-dialog.component.html',
})
export class BomSourceDeleteDialogComponent {
  bomSource?: IBomSource;

  constructor(protected bomSourceService: BomSourceService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.bomSourceService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
