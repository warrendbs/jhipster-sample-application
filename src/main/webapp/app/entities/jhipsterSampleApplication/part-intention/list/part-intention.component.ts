import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPartIntention } from '../part-intention.model';
import { PartIntentionService } from '../service/part-intention.service';
import { PartIntentionDeleteDialogComponent } from '../delete/part-intention-delete-dialog.component';

@Component({
  selector: 'jhi-part-intention',
  templateUrl: './part-intention.component.html',
})
export class PartIntentionComponent implements OnInit {
  partIntentions?: IPartIntention[];
  isLoading = false;

  constructor(protected partIntentionService: PartIntentionService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.partIntentionService.query().subscribe({
      next: (res: HttpResponse<IPartIntention[]>) => {
        this.isLoading = false;
        this.partIntentions = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IPartIntention): number {
    return item.id!;
  }

  delete(partIntention: IPartIntention): void {
    const modalRef = this.modalService.open(PartIntentionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.partIntention = partIntention;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
