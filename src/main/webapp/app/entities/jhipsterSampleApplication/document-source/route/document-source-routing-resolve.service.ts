import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDocumentSource, DocumentSource } from '../document-source.model';
import { DocumentSourceService } from '../service/document-source.service';

@Injectable({ providedIn: 'root' })
export class DocumentSourceRoutingResolveService implements Resolve<IDocumentSource> {
  constructor(protected service: DocumentSourceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDocumentSource> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((documentSource: HttpResponse<DocumentSource>) => {
          if (documentSource.body) {
            return of(documentSource.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DocumentSource());
  }
}
