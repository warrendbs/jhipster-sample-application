import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IImpactMatrix } from '../impact-matrix.model';
import { ImpactMatrixService } from '../service/impact-matrix.service';

@Component({
  templateUrl: './impact-matrix-delete-dialog.component.html',
})
export class ImpactMatrixDeleteDialogComponent {
  impactMatrix?: IImpactMatrix;

  constructor(protected impactMatrixService: ImpactMatrixService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.impactMatrixService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
