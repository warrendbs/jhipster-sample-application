import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPartIntention } from '../part-intention.model';

@Component({
  selector: 'jhi-part-intention-detail',
  templateUrl: './part-intention-detail.component.html',
})
export class PartIntentionDetailComponent implements OnInit {
  partIntention: IPartIntention | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ partIntention }) => {
      this.partIntention = partIntention;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
