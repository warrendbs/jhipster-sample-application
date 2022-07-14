import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBom } from '../bom.model';

@Component({
  selector: 'jhi-bom-detail',
  templateUrl: './bom-detail.component.html',
})
export class BomDetailComponent implements OnInit {
  bom: IBom | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bom }) => {
      this.bom = bom;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
