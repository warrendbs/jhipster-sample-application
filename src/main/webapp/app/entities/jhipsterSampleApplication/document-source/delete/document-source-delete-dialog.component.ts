import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDocumentSource } from '../document-source.model';
import { DocumentSourceService } from '../service/document-source.service';

@Component({
  templateUrl: './document-source-delete-dialog.component.html',
})
export class DocumentSourceDeleteDialogComponent {
  documentSource?: IDocumentSource;

  constructor(protected documentSourceService: DocumentSourceService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.documentSourceService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
