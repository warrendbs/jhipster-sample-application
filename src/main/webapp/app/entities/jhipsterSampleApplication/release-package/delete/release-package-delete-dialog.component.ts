import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IReleasePackage } from '../release-package.model';
import { ReleasePackageService } from '../service/release-package.service';

@Component({
  templateUrl: './release-package-delete-dialog.component.html',
})
export class ReleasePackageDeleteDialogComponent {
  releasePackage?: IReleasePackage;

  constructor(protected releasePackageService: ReleasePackageService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.releasePackageService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
