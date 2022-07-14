import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBomSource } from '../bom-source.model';
import { BomSourceService } from '../service/bom-source.service';
import { BomSourceDeleteDialogComponent } from '../delete/bom-source-delete-dialog.component';

@Component({
  selector: 'jhi-bom-source',
  templateUrl: './bom-source.component.html',
})
export class BomSourceComponent implements OnInit {
  bomSources?: IBomSource[];
  isLoading = false;

  constructor(protected bomSourceService: BomSourceService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.bomSourceService.query().subscribe({
      next: (res: HttpResponse<IBomSource[]>) => {
        this.isLoading = false;
        this.bomSources = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IBomSource): number {
    return item.id!;
  }

  delete(bomSource: IBomSource): void {
    const modalRef = this.modalService.open(BomSourceDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.bomSource = bomSource;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
