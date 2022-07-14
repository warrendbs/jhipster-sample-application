import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBom } from '../bom.model';
import { BomService } from '../service/bom.service';
import { BomDeleteDialogComponent } from '../delete/bom-delete-dialog.component';

@Component({
  selector: 'jhi-bom',
  templateUrl: './bom.component.html',
})
export class BomComponent implements OnInit {
  boms?: IBom[];
  isLoading = false;

  constructor(protected bomService: BomService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.bomService.query().subscribe({
      next: (res: HttpResponse<IBom[]>) => {
        this.isLoading = false;
        this.boms = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IBom): number {
    return item.id!;
  }

  delete(bom: IBom): void {
    const modalRef = this.modalService.open(BomDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.bom = bom;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
