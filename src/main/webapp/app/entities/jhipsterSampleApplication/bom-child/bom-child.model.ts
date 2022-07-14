import { IBomSource } from 'app/entities/jhipsterSampleApplication/bom-source/bom-source.model';
import { IBomIntention } from 'app/entities/jhipsterSampleApplication/bom-intention/bom-intention.model';

export interface IBomChild {
  id?: number;
  productId?: string | null;
  revision?: string | null;
  quantity?: number | null;
  relationType?: string | null;
  bomSources?: IBomSource[] | null;
  bomIntentions?: IBomIntention[] | null;
}

export class BomChild implements IBomChild {
  constructor(
    public id?: number,
    public productId?: string | null,
    public revision?: string | null,
    public quantity?: number | null,
    public relationType?: string | null,
    public bomSources?: IBomSource[] | null,
    public bomIntentions?: IBomIntention[] | null
  ) {}
}

export function getBomChildIdentifier(bomChild: IBomChild): number | undefined {
  return bomChild.id;
}
