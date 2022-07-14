import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IContext } from '../context.model';

@Component({
  selector: 'jhi-context-detail',
  templateUrl: './context-detail.component.html',
})
export class ContextDetailComponent implements OnInit {
  context: IContext | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ context }) => {
      this.context = context;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
