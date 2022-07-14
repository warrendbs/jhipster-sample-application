import { IBom } from 'app/entities/jhipsterSampleApplication/bom/bom.model';
import { IDocument } from 'app/entities/jhipsterSampleApplication/document/document.model';
import { IPartIntention } from 'app/entities/jhipsterSampleApplication/part-intention/part-intention.model';

export interface IItemReference {
  id?: number;
  referenceId?: number | null;
  type?: string | null;
  boms?: IBom[] | null;
  documents?: IDocument[] | null;
  partIntentions?: IPartIntention[] | null;
}

export class ItemReference implements IItemReference {
  constructor(
    public id?: number,
    public referenceId?: number | null,
    public type?: string | null,
    public boms?: IBom[] | null,
    public documents?: IDocument[] | null,
    public partIntentions?: IPartIntention[] | null
  ) {}
}

export function getItemReferenceIdentifier(itemReference: IItemReference): number | undefined {
  return itemReference.id;
}
