import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBomIntention } from '../bom-intention.model';

@Component({
  selector: 'jhi-bom-intention-detail',
  templateUrl: './bom-intention-detail.component.html',
})
export class BomIntentionDetailComponent implements OnInit {
  bomIntention: IBomIntention | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bomIntention }) => {
      this.bomIntention = bomIntention;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
