import { IBomChild } from 'app/entities/jhipsterSampleApplication/bom-child/bom-child.model';
import { IBom } from 'app/entities/jhipsterSampleApplication/bom/bom.model';

export interface IBomSource {
  id?: number;
  type?: string | null;
  bomChildren?: IBomChild[] | null;
  bom?: IBom | null;
}

export class BomSource implements IBomSource {
  constructor(public id?: number, public type?: string | null, public bomChildren?: IBomChild[] | null, public bom?: IBom | null) {}
}

export function getBomSourceIdentifier(bomSource: IBomSource): number | undefined {
  return bomSource.id;
}
