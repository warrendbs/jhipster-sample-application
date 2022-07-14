import { IPartSource } from 'app/entities/jhipsterSampleApplication/part-source/part-source.model';
import { IPartIntention } from 'app/entities/jhipsterSampleApplication/part-intention/part-intention.model';

export interface IPlantSpecific {
  id?: number;
  objectDependancy?: string | null;
  refMaterial?: string | null;
  isDiscontinued?: boolean | null;
  partSources?: IPartSource[] | null;
  partIntentions?: IPartIntention[] | null;
}

export class PlantSpecific implements IPlantSpecific {
  constructor(
    public id?: number,
    public objectDependancy?: string | null,
    public refMaterial?: string | null,
    public isDiscontinued?: boolean | null,
    public partSources?: IPartSource[] | null,
    public partIntentions?: IPartIntention[] | null
  ) {
    this.isDiscontinued = this.isDiscontinued ?? false;
  }
}

export function getPlantSpecificIdentifier(plantSpecific: IPlantSpecific): number | undefined {
  return plantSpecific.id;
}
