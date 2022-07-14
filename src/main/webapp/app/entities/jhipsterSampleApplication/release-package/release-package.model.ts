import { IBomIntention } from 'app/entities/jhipsterSampleApplication/bom-intention/bom-intention.model';
import { IDocumentIntention } from 'app/entities/jhipsterSampleApplication/document-intention/document-intention.model';
import { IPartIntention } from 'app/entities/jhipsterSampleApplication/part-intention/part-intention.model';

export interface IReleasePackage {
  id?: number;
  title?: string | null;
  releasePackageNumber?: string | null;
  releasePackageTitle?: string | null;
  status?: string | null;
  ecn?: string | null;
  bomIntentions?: IBomIntention[] | null;
  documentIntentions?: IDocumentIntention[] | null;
  partIntentions?: IPartIntention[] | null;
}

export class ReleasePackage implements IReleasePackage {
  constructor(
    public id?: number,
    public title?: string | null,
    public releasePackageNumber?: string | null,
    public releasePackageTitle?: string | null,
    public status?: string | null,
    public ecn?: string | null,
    public bomIntentions?: IBomIntention[] | null,
    public documentIntentions?: IDocumentIntention[] | null,
    public partIntentions?: IPartIntention[] | null
  ) {}
}

export function getReleasePackageIdentifier(releasePackage: IReleasePackage): number | undefined {
  return releasePackage.id;
}
