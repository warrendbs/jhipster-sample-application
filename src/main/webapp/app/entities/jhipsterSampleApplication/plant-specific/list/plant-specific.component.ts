import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPlantSpecific } from '../plant-specific.model';
import { PlantSpecificService } from '../service/plant-specific.service';
import { PlantSpecificDeleteDialogComponent } from '../delete/plant-specific-delete-dialog.component';

@Component({
  selector: 'jhi-plant-specific',
  templateUrl: './plant-specific.component.html',
})
export class PlantSpecificComponent implements OnInit {
  plantSpecifics?: IPlantSpecific[];
  isLoading = false;

  constructor(protected plantSpecificService: PlantSpecificService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.plantSpecificService.query().subscribe({
      next: (res: HttpResponse<IPlantSpecific[]>) => {
        this.isLoading = false;
        this.plantSpecifics = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IPlantSpecific): number {
    return item.id!;
  }

  delete(plantSpecific: IPlantSpecific): void {
    const modalRef = this.modalService.open(PlantSpecificDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.plantSpecific = plantSpecific;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
