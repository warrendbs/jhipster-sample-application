import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDocumentIntention } from '../document-intention.model';

@Component({
  selector: 'jhi-document-intention-detail',
  templateUrl: './document-intention-detail.component.html',
})
export class DocumentIntentionDetailComponent implements OnInit {
  documentIntention: IDocumentIntention | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documentIntention }) => {
      this.documentIntention = documentIntention;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
