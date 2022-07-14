import { IDocument } from 'app/entities/jhipsterSampleApplication/document/document.model';

export interface IDocumentSource {
  id?: number;
  name?: string | null;
  description?: string | null;
  changeIndicator?: boolean | null;
  type?: string | null;
  subtype?: string | null;
  group?: string | null;
  sheet?: string | null;
  document?: IDocument | null;
}

export class DocumentSource implements IDocumentSource {
  constructor(
    public id?: number,
    public name?: string | null,
    public description?: string | null,
    public changeIndicator?: boolean | null,
    public type?: string | null,
    public subtype?: string | null,
    public group?: string | null,
    public sheet?: string | null,
    public document?: IDocument | null
  ) {
    this.changeIndicator = this.changeIndicator ?? false;
  }
}

export function getDocumentSourceIdentifier(documentSource: IDocumentSource): number | undefined {
  return documentSource.id;
}
