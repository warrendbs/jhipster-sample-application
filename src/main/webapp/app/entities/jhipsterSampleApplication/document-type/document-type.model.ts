import { IDocument } from 'app/entities/jhipsterSampleApplication/document/document.model';

export interface IDocumentType {
  id?: number;
  desctiption?: string | null;
  documents?: IDocument[] | null;
}

export class DocumentType implements IDocumentType {
  constructor(public id?: number, public desctiption?: string | null, public documents?: IDocument[] | null) {}
}

export function getDocumentTypeIdentifier(documentType: IDocumentType): number | undefined {
  return documentType.id;
}
