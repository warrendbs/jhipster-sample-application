import { IPartSource } from 'app/entities/jhipsterSampleApplication/part-source/part-source.model';
import { IPartIntention } from 'app/entities/jhipsterSampleApplication/part-intention/part-intention.model';
import { IImpactMatrix } from 'app/entities/jhipsterSampleApplication/impact-matrix/impact-matrix.model';

export interface IPart {
  id?: number;
  status?: string | null;
  changeIndication?: boolean | null;
  isParentPartBomChanged?: boolean | null;
  partSource?: IPartSource | null;
  partIntention?: IPartIntention | null;
  impactMatrix?: IImpactMatrix | null;
}

export class Part implements IPart {
  constructor(
    public id?: number,
    public status?: string | null,
    public changeIndication?: boolean | null,
    public isParentPartBomChanged?: boolean | null,
    public partSource?: IPartSource | null,
    public partIntention?: IPartIntention | null,
    public impactMatrix?: IImpactMatrix | null
  ) {
    this.changeIndication = this.changeIndication ?? false;
    this.isParentPartBomChanged = this.isParentPartBomChanged ?? false;
  }
}

export function getPartIdentifier(part: IPart): number | undefined {
  return part.id;
}
