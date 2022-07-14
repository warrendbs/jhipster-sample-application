import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDocumentIntention } from '../document-intention.model';
import { DocumentIntentionService } from '../service/document-intention.service';

@Component({
  templateUrl: './document-intention-delete-dialog.component.html',
})
export class DocumentIntentionDeleteDialogComponent {
  documentIntention?: IDocumentIntention;

  constructor(protected documentIntentionService: DocumentIntentionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.documentIntentionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
