import { IDocumentSource } from 'app/entities/jhipsterSampleApplication/document-source/document-source.model';
import { IDocumentIntention } from 'app/entities/jhipsterSampleApplication/document-intention/document-intention.model';
import { IItemReference } from 'app/entities/jhipsterSampleApplication/item-reference/item-reference.model';
import { IImpactMatrix } from 'app/entities/jhipsterSampleApplication/impact-matrix/impact-matrix.model';
import { IDocumentType } from 'app/entities/jhipsterSampleApplication/document-type/document-type.model';

export interface IDocument {
  id?: number;
  title?: string | null;
  status?: string | null;
  documentSource?: IDocumentSource | null;
  documentIntention?: IDocumentIntention | null;
  itemReferences?: IItemReference[] | null;
  impactMatrix?: IImpactMatrix | null;
  documentType?: IDocumentType | null;
}

export class Document implements IDocument {
  constructor(
    public id?: number,
    public title?: string | null,
    public status?: string | null,
    public documentSource?: IDocumentSource | null,
    public documentIntention?: IDocumentIntention | null,
    public itemReferences?: IItemReference[] | null,
    public impactMatrix?: IImpactMatrix | null,
    public documentType?: IDocumentType | null
  ) {}
}

export function getDocumentIdentifier(document: IDocument): number | undefined {
  return document.id;
}
