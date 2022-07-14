import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBomSource } from '../bom-source.model';

@Component({
  selector: 'jhi-bom-source-detail',
  templateUrl: './bom-source-detail.component.html',
})
export class BomSourceDetailComponent implements OnInit {
  bomSource: IBomSource | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bomSource }) => {
      this.bomSource = bomSource;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
