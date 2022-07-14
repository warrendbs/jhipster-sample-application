import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPartSource } from '../part-source.model';
import { PartSourceService } from '../service/part-source.service';
import { PartSourceDeleteDialogComponent } from '../delete/part-source-delete-dialog.component';

@Component({
  selector: 'jhi-part-source',
  templateUrl: './part-source.component.html',
})
export class PartSourceComponent implements OnInit {
  partSources?: IPartSource[];
  isLoading = false;

  constructor(protected partSourceService: PartSourceService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.partSourceService.query().subscribe({
      next: (res: HttpResponse<IPartSource[]>) => {
        this.isLoading = false;
        this.partSources = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IPartSource): number {
    return item.id!;
  }

  delete(partSource: IPartSource): void {
    const modalRef = this.modalService.open(PartSourceDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.partSource = partSource;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
