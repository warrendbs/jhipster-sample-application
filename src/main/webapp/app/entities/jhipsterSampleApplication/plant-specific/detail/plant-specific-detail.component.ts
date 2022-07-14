import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPlantSpecific } from '../plant-specific.model';

@Component({
  selector: 'jhi-plant-specific-detail',
  templateUrl: './plant-specific-detail.component.html',
})
export class PlantSpecificDetailComponent implements OnInit {
  plantSpecific: IPlantSpecific | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ plantSpecific }) => {
      this.plantSpecific = plantSpecific;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
