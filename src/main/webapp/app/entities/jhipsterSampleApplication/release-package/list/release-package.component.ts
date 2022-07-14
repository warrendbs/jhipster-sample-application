import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IReleasePackage } from '../release-package.model';
import { ReleasePackageService } from '../service/release-package.service';
import { ReleasePackageDeleteDialogComponent } from '../delete/release-package-delete-dialog.component';

@Component({
  selector: 'jhi-release-package',
  templateUrl: './release-package.component.html',
})
export class ReleasePackageComponent implements OnInit {
  releasePackages?: IReleasePackage[];
  isLoading = false;

  constructor(protected releasePackageService: ReleasePackageService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.releasePackageService.query().subscribe({
      next: (res: HttpResponse<IReleasePackage[]>) => {
        this.isLoading = false;
        this.releasePackages = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IReleasePackage): number {
    return item.id!;
  }

  delete(releasePackage: IReleasePackage): void {
    const modalRef = this.modalService.open(ReleasePackageDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.releasePackage = releasePackage;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
