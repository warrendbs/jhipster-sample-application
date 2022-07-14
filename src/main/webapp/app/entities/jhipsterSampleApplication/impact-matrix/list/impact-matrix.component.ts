import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IImpactMatrix } from '../impact-matrix.model';
import { ImpactMatrixService } from '../service/impact-matrix.service';
import { ImpactMatrixDeleteDialogComponent } from '../delete/impact-matrix-delete-dialog.component';

@Component({
  selector: 'jhi-impact-matrix',
  templateUrl: './impact-matrix.component.html',
})
export class ImpactMatrixComponent implements OnInit {
  impactMatrices?: IImpactMatrix[];
  isLoading = false;

  constructor(protected impactMatrixService: ImpactMatrixService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.impactMatrixService.query().subscribe({
      next: (res: HttpResponse<IImpactMatrix[]>) => {
        this.isLoading = false;
        this.impactMatrices = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IImpactMatrix): number {
    return item.id!;
  }

  delete(impactMatrix: IImpactMatrix): void {
    const modalRef = this.modalService.open(ImpactMatrixDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.impactMatrix = impactMatrix;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
