import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IImpactMatrix } from '../impact-matrix.model';

@Component({
  selector: 'jhi-impact-matrix-detail',
  templateUrl: './impact-matrix-detail.component.html',
})
export class ImpactMatrixDetailComponent implements OnInit {
  impactMatrix: IImpactMatrix | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ impactMatrix }) => {
      this.impactMatrix = impactMatrix;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
