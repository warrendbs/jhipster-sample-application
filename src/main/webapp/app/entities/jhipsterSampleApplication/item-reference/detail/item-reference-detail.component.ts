import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IItemReference } from '../item-reference.model';

@Component({
  selector: 'jhi-item-reference-detail',
  templateUrl: './item-reference-detail.component.html',
})
export class ItemReferenceDetailComponent implements OnInit {
  itemReference: IItemReference | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ itemReference }) => {
      this.itemReference = itemReference;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
