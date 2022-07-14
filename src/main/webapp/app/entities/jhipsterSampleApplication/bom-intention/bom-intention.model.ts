import { IBomChild } from 'app/entities/jhipsterSampleApplication/bom-child/bom-child.model';
import { IReleasePackage } from 'app/entities/jhipsterSampleApplication/release-package/release-package.model';
import { IBom } from 'app/entities/jhipsterSampleApplication/bom/bom.model';

export interface IBomIntention {
  id?: number;
  type?: string | null;
  bomChildren?: IBomChild[] | null;
  releasePackages?: IReleasePackage[] | null;
  bom?: IBom | null;
}

export class BomIntention implements IBomIntention {
  constructor(
    public id?: number,
    public type?: string | null,
    public bomChildren?: IBomChild[] | null,
    public releasePackages?: IReleasePackage[] | null,
    public bom?: IBom | null
  ) {}
}

export function getBomIntentionIdentifier(bomIntention: IBomIntention): number | undefined {
  return bomIntention.id;
}
