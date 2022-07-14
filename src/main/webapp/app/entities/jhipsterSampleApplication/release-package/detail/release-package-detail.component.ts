import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IReleasePackage } from '../release-package.model';

@Component({
  selector: 'jhi-release-package-detail',
  templateUrl: './release-package-detail.component.html',
})
export class ReleasePackageDetailComponent implements OnInit {
  releasePackage: IReleasePackage | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ releasePackage }) => {
      this.releasePackage = releasePackage;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
