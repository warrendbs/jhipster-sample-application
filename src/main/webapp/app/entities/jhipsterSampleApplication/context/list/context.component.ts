import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IContext } from '../context.model';
import { ContextService } from '../service/context.service';
import { ContextDeleteDialogComponent } from '../delete/context-delete-dialog.component';

@Component({
  selector: 'jhi-context',
  templateUrl: './context.component.html',
})
export class ContextComponent implements OnInit {
  contexts?: IContext[];
  isLoading = false;

  constructor(protected contextService: ContextService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.contextService.query().subscribe({
      next: (res: HttpResponse<IContext[]>) => {
        this.isLoading = false;
        this.contexts = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IContext): number {
    return item.id!;
  }

  delete(context: IContext): void {
    const modalRef = this.modalService.open(ContextDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.context = context;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
