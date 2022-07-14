import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IItemReference } from '../item-reference.model';
import { ItemReferenceService } from '../service/item-reference.service';
import { ItemReferenceDeleteDialogComponent } from '../delete/item-reference-delete-dialog.component';

@Component({
  selector: 'jhi-item-reference',
  templateUrl: './item-reference.component.html',
})
export class ItemReferenceComponent implements OnInit {
  itemReferences?: IItemReference[];
  isLoading = false;

  constructor(protected itemReferenceService: ItemReferenceService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.itemReferenceService.query().subscribe({
      next: (res: HttpResponse<IItemReference[]>) => {
        this.isLoading = false;
        this.itemReferences = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IItemReference): number {
    return item.id!;
  }

  delete(itemReference: IItemReference): void {
    const modalRef = this.modalService.open(ItemReferenceDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.itemReference = itemReference;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
