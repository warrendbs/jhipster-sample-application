import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'impact-matrix',
        data: { pageTitle: 'ImpactMatrices' },
        loadChildren: () =>
          import('./jhipsterSampleApplication/impact-matrix/impact-matrix.module').then(m => m.JhipsterSampleApplicationImpactMatrixModule),
      },
      {
        path: 'context',
        data: { pageTitle: 'Contexts' },
        loadChildren: () =>
          import('./jhipsterSampleApplication/context/context.module').then(m => m.JhipsterSampleApplicationContextModule),
      },
      {
        path: 'bom',
        data: { pageTitle: 'Boms' },
        loadChildren: () => import('./jhipsterSampleApplication/bom/bom.module').then(m => m.JhipsterSampleApplicationBomModule),
      },
      {
        path: 'bom-source',
        data: { pageTitle: 'BomSources' },
        loadChildren: () =>
          import('./jhipsterSampleApplication/bom-source/bom-source.module').then(m => m.JhipsterSampleApplicationBomSourceModule),
      },
      {
        path: 'bom-intention',
        data: { pageTitle: 'BomIntentions' },
        loadChildren: () =>
          import('./jhipsterSampleApplication/bom-intention/bom-intention.module').then(m => m.JhipsterSampleApplicationBomIntentionModule),
      },
      {
        path: 'bom-child',
        data: { pageTitle: 'BomChildren' },
        loadChildren: () =>
          import('./jhipsterSampleApplication/bom-child/bom-child.module').then(m => m.JhipsterSampleApplicationBomChildModule),
      },
      {
        path: 'item-reference',
        data: { pageTitle: 'ItemReferences' },
        loadChildren: () =>
          import('./jhipsterSampleApplication/item-reference/item-reference.module').then(
            m => m.JhipsterSampleApplicationItemReferenceModule
          ),
      },
      {
        path: 'release-package',
        data: { pageTitle: 'ReleasePackages' },
        loadChildren: () =>
          import('./jhipsterSampleApplication/release-package/release-package.module').then(
            m => m.JhipsterSampleApplicationReleasePackageModule
          ),
      },
      {
        path: 'document-type',
        data: { pageTitle: 'DocumentTypes' },
        loadChildren: () =>
          import('./jhipsterSampleApplication/document-type/document-type.module').then(m => m.JhipsterSampleApplicationDocumentTypeModule),
      },
      {
        path: 'document',
        data: { pageTitle: 'Documents' },
        loadChildren: () =>
          import('./jhipsterSampleApplication/document/document.module').then(m => m.JhipsterSampleApplicationDocumentModule),
      },
      {
        path: 'document-source',
        data: { pageTitle: 'DocumentSources' },
        loadChildren: () =>
          import('./jhipsterSampleApplication/document-source/document-source.module').then(
            m => m.JhipsterSampleApplicationDocumentSourceModule
          ),
      },
      {
        path: 'document-intention',
        data: { pageTitle: 'DocumentIntentions' },
        loadChildren: () =>
          import('./jhipsterSampleApplication/document-intention/document-intention.module').then(
            m => m.JhipsterSampleApplicationDocumentIntentionModule
          ),
      },
      {
        path: 'part',
        data: { pageTitle: 'Parts' },
        loadChildren: () => import('./jhipsterSampleApplication/part/part.module').then(m => m.JhipsterSampleApplicationPartModule),
      },
      {
        path: 'part-source',
        data: { pageTitle: 'PartSources' },
        loadChildren: () =>
          import('./jhipsterSampleApplication/part-source/part-source.module').then(m => m.JhipsterSampleApplicationPartSourceModule),
      },
      {
        path: 'plant-specific',
        data: { pageTitle: 'PlantSpecifics' },
        loadChildren: () =>
          import('./jhipsterSampleApplication/plant-specific/plant-specific.module').then(
            m => m.JhipsterSampleApplicationPlantSpecificModule
          ),
      },
      {
        path: 'part-intention',
        data: { pageTitle: 'PartIntentions' },
        loadChildren: () =>
          import('./jhipsterSampleApplication/part-intention/part-intention.module').then(
            m => m.JhipsterSampleApplicationPartIntentionModule
          ),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
