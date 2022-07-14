import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDocumentIntention, DocumentIntention } from '../document-intention.model';
import { DocumentIntentionService } from '../service/document-intention.service';

@Injectable({ providedIn: 'root' })
export class DocumentIntentionRoutingResolveService implements Resolve<IDocumentIntention> {
  constructor(protected service: DocumentIntentionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDocumentIntention> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((documentIntention: HttpResponse<DocumentIntention>) => {
          if (documentIntention.body) {
            return of(documentIntention.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DocumentIntention());
  }
}
