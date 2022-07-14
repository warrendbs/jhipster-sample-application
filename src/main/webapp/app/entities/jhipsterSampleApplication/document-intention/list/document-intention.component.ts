import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDocumentIntention } from '../document-intention.model';
import { DocumentIntentionService } from '../service/document-intention.service';
import { DocumentIntentionDeleteDialogComponent } from '../delete/document-intention-delete-dialog.component';

@Component({
  selector: 'jhi-document-intention',
  templateUrl: './document-intention.component.html',
})
export class DocumentIntentionComponent implements OnInit {
  documentIntentions?: IDocumentIntention[];
  isLoading = false;

  constructor(protected documentIntentionService: DocumentIntentionService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.documentIntentionService.query().subscribe({
      next: (res: HttpResponse<IDocumentIntention[]>) => {
        this.isLoading = false;
        this.documentIntentions = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IDocumentIntention): number {
    return item.id!;
  }

  delete(documentIntention: IDocumentIntention): void {
    const modalRef = this.modalService.open(DocumentIntentionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.documentIntention = documentIntention;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
