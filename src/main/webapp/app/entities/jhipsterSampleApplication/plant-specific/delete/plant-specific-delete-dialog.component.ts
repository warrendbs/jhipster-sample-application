import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPlantSpecific } from '../plant-specific.model';
import { PlantSpecificService } from '../service/plant-specific.service';

@Component({
  templateUrl: './plant-specific-delete-dialog.component.html',
})
export class PlantSpecificDeleteDialogComponent {
  plantSpecific?: IPlantSpecific;

  constructor(protected plantSpecificService: PlantSpecificService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.plantSpecificService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
