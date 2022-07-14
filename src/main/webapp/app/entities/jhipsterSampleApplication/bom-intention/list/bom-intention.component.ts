import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBomIntention } from '../bom-intention.model';
import { BomIntentionService } from '../service/bom-intention.service';
import { BomIntentionDeleteDialogComponent } from '../delete/bom-intention-delete-dialog.component';

@Component({
  selector: 'jhi-bom-intention',
  templateUrl: './bom-intention.component.html',
})
export class BomIntentionComponent implements OnInit {
  bomIntentions?: IBomIntention[];
  isLoading = false;

  constructor(protected bomIntentionService: BomIntentionService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.bomIntentionService.query().subscribe({
      next: (res: HttpResponse<IBomIntention[]>) => {
        this.isLoading = false;
        this.bomIntentions = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IBomIntention): number {
    return item.id!;
  }

  delete(bomIntention: IBomIntention): void {
    const modalRef = this.modalService.open(BomIntentionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.bomIntention = bomIntention;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
