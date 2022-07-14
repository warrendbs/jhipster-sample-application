import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDocumentSource } from '../document-source.model';
import { DocumentSourceService } from '../service/document-source.service';
import { DocumentSourceDeleteDialogComponent } from '../delete/document-source-delete-dialog.component';

@Component({
  selector: 'jhi-document-source',
  templateUrl: './document-source.component.html',
})
export class DocumentSourceComponent implements OnInit {
  documentSources?: IDocumentSource[];
  isLoading = false;

  constructor(protected documentSourceService: DocumentSourceService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.documentSourceService.query().subscribe({
      next: (res: HttpResponse<IDocumentSource[]>) => {
        this.isLoading = false;
        this.documentSources = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IDocumentSource): number {
    return item.id!;
  }

  delete(documentSource: IDocumentSource): void {
    const modalRef = this.modalService.open(DocumentSourceDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.documentSource = documentSource;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
