import { IReleasePackage } from 'app/entities/jhipsterSampleApplication/release-package/release-package.model';
import { IDocument } from 'app/entities/jhipsterSampleApplication/document/document.model';

export interface IDocumentIntention {
  id?: number;
  name?: string | null;
  description?: string | null;
  changeIndicator?: boolean | null;
  type?: string | null;
  subtype?: string | null;
  group?: string | null;
  sheet?: string | null;
  releasePackages?: IReleasePackage[] | null;
  document?: IDocument | null;
}

export class DocumentIntention implements IDocumentIntention {
  constructor(
    public id?: number,
    public name?: string | null,
    public description?: string | null,
    public changeIndicator?: boolean | null,
    public type?: string | null,
    public subtype?: string | null,
    public group?: string | null,
    public sheet?: string | null,
    public releasePackages?: IReleasePackage[] | null,
    public document?: IDocument | null
  ) {
    this.changeIndicator = this.changeIndicator ?? false;
  }
}

export function getDocumentIntentionIdentifier(documentIntention: IDocumentIntention): number | undefined {
  return documentIntention.id;
}
