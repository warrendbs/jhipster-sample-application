import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDocument } from '../document.model';
import { DocumentService } from '../service/document.service';
import { DocumentDeleteDialogComponent } from '../delete/document-delete-dialog.component';

@Component({
  selector: 'jhi-document',
  templateUrl: './document.component.html',
})
export class DocumentComponent implements OnInit {
  documents?: IDocument[];
  isLoading = false;

  constructor(protected documentService: DocumentService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.documentService.query().subscribe({
      next: (res: HttpResponse<IDocument[]>) => {
        this.isLoading = false;
        this.documents = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IDocument): number {
    return item.id!;
  }

  delete(document: IDocument): void {
    const modalRef = this.modalService.open(DocumentDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.document = document;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
