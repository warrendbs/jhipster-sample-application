import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPartSource } from '../part-source.model';

@Component({
  selector: 'jhi-part-source-detail',
  templateUrl: './part-source-detail.component.html',
})
export class PartSourceDetailComponent implements OnInit {
  partSource: IPartSource | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ partSource }) => {
      this.partSource = partSource;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
