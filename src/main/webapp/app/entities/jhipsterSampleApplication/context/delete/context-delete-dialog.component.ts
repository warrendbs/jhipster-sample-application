import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IContext } from '../context.model';
import { ContextService } from '../service/context.service';

@Component({
  templateUrl: './context-delete-dialog.component.html',
})
export class ContextDeleteDialogComponent {
  context?: IContext;

  constructor(protected contextService: ContextService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.contextService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
