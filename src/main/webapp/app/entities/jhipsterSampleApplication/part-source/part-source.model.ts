import { IPlantSpecific } from 'app/entities/jhipsterSampleApplication/plant-specific/plant-specific.model';
import { IPart } from 'app/entities/jhipsterSampleApplication/part/part.model';

export interface IPartSource {
  id?: number;
  productId?: number | null;
  revision?: string | null;
  name?: string | null;
  description?: string | null;
  vqi?: string | null;
  procurementType?: string | null;
  materialType?: string | null;
  serialNumberProfile?: string | null;
  criticalConfigurationItemIndicator?: boolean | null;
  regularPartIndicator?: string | null;
  historyIndicator?: string | null;
  crossPlantStatus?: string | null;
  crossPlantStatusToBe?: string | null;
  toolPackCategory?: string | null;
  tcChangeControl?: boolean | null;
  sapChangeControl?: boolean | null;
  allowBomRestructuring?: boolean | null;
  unitOfMeasure?: string | null;
  itemUsage?: string | null;
  isPhantom?: boolean | null;
  failureRate?: string | null;
  inHouseProductionTime?: number | null;
  slAbcCode?: string | null;
  productionPlant?: string | null;
  limitedDriving12Nc?: string | null;
  limitedDriving12Ncflag?: string | null;
  multiPlant?: string | null;
  type?: string | null;
  successorPartId?: number | null;
  plantSpecifics?: IPlantSpecific[] | null;
  part?: IPart | null;
}

export class PartSource implements IPartSource {
  constructor(
    public id?: number,
    public productId?: number | null,
    public revision?: string | null,
    public name?: string | null,
    public description?: string | null,
    public vqi?: string | null,
    public procurementType?: string | null,
    public materialType?: string | null,
    public serialNumberProfile?: string | null,
    public criticalConfigurationItemIndicator?: boolean | null,
    public regularPartIndicator?: string | null,
    public historyIndicator?: string | null,
    public crossPlantStatus?: string | null,
    public crossPlantStatusToBe?: string | null,
    public toolPackCategory?: string | null,
    public tcChangeControl?: boolean | null,
    public sapChangeControl?: boolean | null,
    public allowBomRestructuring?: boolean | null,
    public unitOfMeasure?: string | null,
    public itemUsage?: string | null,
    public isPhantom?: boolean | null,
    public failureRate?: string | null,
    public inHouseProductionTime?: number | null,
    public slAbcCode?: string | null,
    public productionPlant?: string | null,
    public limitedDriving12Nc?: string | null,
    public limitedDriving12Ncflag?: string | null,
    public multiPlant?: string | null,
    public type?: string | null,
    public successorPartId?: number | null,
    public plantSpecifics?: IPlantSpecific[] | null,
    public part?: IPart | null
  ) {
    this.criticalConfigurationItemIndicator = this.criticalConfigurationItemIndicator ?? false;
    this.tcChangeControl = this.tcChangeControl ?? false;
    this.sapChangeControl = this.sapChangeControl ?? false;
    this.allowBomRestructuring = this.allowBomRestructuring ?? false;
    this.isPhantom = this.isPhantom ?? false;
  }
}

export function getPartSourceIdentifier(partSource: IPartSource): number | undefined {
  return partSource.id;
}
