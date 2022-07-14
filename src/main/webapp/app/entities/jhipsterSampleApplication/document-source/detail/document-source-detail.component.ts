import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDocumentSource } from '../document-source.model';

@Component({
  selector: 'jhi-document-source-detail',
  templateUrl: './document-source-detail.component.html',
})
export class DocumentSourceDetailComponent implements OnInit {
  documentSource: IDocumentSource | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documentSource }) => {
      this.documentSource = documentSource;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
