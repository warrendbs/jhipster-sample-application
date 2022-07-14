import { IContext } from 'app/entities/jhipsterSampleApplication/context/context.model';
import { IBom } from 'app/entities/jhipsterSampleApplication/bom/bom.model';
import { IDocument } from 'app/entities/jhipsterSampleApplication/document/document.model';
import { IPart } from 'app/entities/jhipsterSampleApplication/part/part.model';

export interface IImpactMatrix {
  id?: number;
  impactMatrixNumber?: number | null;
  status?: number | null;
  revision?: string | null;
  reviser?: string | null;
  revisionDescription?: string | null;
  dateRevised?: string | null;
  title?: string | null;
  isAutoLayoutEnabled?: boolean | null;
  contexts?: IContext[] | null;
  boms?: IBom[] | null;
  documents?: IDocument[] | null;
  parts?: IPart[] | null;
}

export class ImpactMatrix implements IImpactMatrix {
  constructor(
    public id?: number,
    public impactMatrixNumber?: number | null,
    public status?: number | null,
    public revision?: string | null,
    public reviser?: string | null,
    public revisionDescription?: string | null,
    public dateRevised?: string | null,
    public title?: string | null,
    public isAutoLayoutEnabled?: boolean | null,
    public contexts?: IContext[] | null,
    public boms?: IBom[] | null,
    public documents?: IDocument[] | null,
    public parts?: IPart[] | null
  ) {
    this.isAutoLayoutEnabled = this.isAutoLayoutEnabled ?? false;
  }
}

export function getImpactMatrixIdentifier(impactMatrix: IImpactMatrix): number | undefined {
  return impactMatrix.id;
}
