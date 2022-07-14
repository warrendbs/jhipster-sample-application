import { IImpactMatrix } from 'app/entities/jhipsterSampleApplication/impact-matrix/impact-matrix.model';

export interface IContext {
  id?: number;
  type?: string | null;
  name?: string | null;
  status?: number | null;
  impactMatrix?: IImpactMatrix | null;
}

export class Context implements IContext {
  constructor(
    public id?: number,
    public type?: string | null,
    public name?: string | null,
    public status?: number | null,
    public impactMatrix?: IImpactMatrix | null
  ) {}
}

export function getContextIdentifier(context: IContext): number | undefined {
  return context.id;
}
