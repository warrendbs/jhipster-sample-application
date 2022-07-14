import { IBomSource } from 'app/entities/jhipsterSampleApplication/bom-source/bom-source.model';
import { IBomIntention } from 'app/entities/jhipsterSampleApplication/bom-intention/bom-intention.model';
import { IItemReference } from 'app/entities/jhipsterSampleApplication/item-reference/item-reference.model';
import { IImpactMatrix } from 'app/entities/jhipsterSampleApplication/impact-matrix/impact-matrix.model';

export interface IBom {
  id?: number;
  status?: number | null;
  bomSource?: IBomSource | null;
  bomIntention?: IBomIntention | null;
  itemReferences?: IItemReference[] | null;
  impactMatrix?: IImpactMatrix | null;
}

export class Bom implements IBom {
  constructor(
    public id?: number,
    public status?: number | null,
    public bomSource?: IBomSource | null,
    public bomIntention?: IBomIntention | null,
    public itemReferences?: IItemReference[] | null,
    public impactMatrix?: IImpactMatrix | null
  ) {}
}

export function getBomIdentifier(bom: IBom): number | undefined {
  return bom.id;
}
