import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBomChild } from '../bom-child.model';

@Component({
  selector: 'jhi-bom-child-detail',
  templateUrl: './bom-child-detail.component.html',
})
export class BomChildDetailComponent implements OnInit {
  bomChild: IBomChild | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bomChild }) => {
      this.bomChild = bomChild;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
