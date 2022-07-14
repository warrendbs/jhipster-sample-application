import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBomChild } from '../bom-child.model';
import { BomChildService } from '../service/bom-child.service';
import { BomChildDeleteDialogComponent } from '../delete/bom-child-delete-dialog.component';

@Component({
  selector: 'jhi-bom-child',
  templateUrl: './bom-child.component.html',
})
export class BomChildComponent implements OnInit {
  bomChildren?: IBomChild[];
  isLoading = false;

  constructor(protected bomChildService: BomChildService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.bomChildService.query().subscribe({
      next: (res: HttpResponse<IBomChild[]>) => {
        this.isLoading = false;
        this.bomChildren = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IBomChild): number {
    return item.id!;
  }

  delete(bomChild: IBomChild): void {
    const modalRef = this.modalService.open(BomChildDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.bomChild = bomChild;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
