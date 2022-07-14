import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPlantSpecific, PlantSpecific } from '../plant-specific.model';
import { PlantSpecificService } from '../service/plant-specific.service';

@Component({
  selector: 'jhi-plant-specific-update',
  templateUrl: './plant-specific-update.component.html',
})
export class PlantSpecificUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    objectDependancy: [],
    refMaterial: [],
    isDiscontinued: [],
  });

  constructor(protected plantSpecificService: PlantSpecificService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ plantSpecific }) => {
      this.updateForm(plantSpecific);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const plantSpecific = this.createFromForm();
    if (plantSpecific.id !== undefined) {
      this.subscribeToSaveResponse(this.plantSpecificService.update(plantSpecific));
    } else {
      this.subscribeToSaveResponse(this.plantSpecificService.create(plantSpecific));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlantSpecific>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(plantSpecific: IPlantSpecific): void {
    this.editForm.patchValue({
      id: plantSpecific.id,
      objectDependancy: plantSpecific.objectDependancy,
      refMaterial: plantSpecific.refMaterial,
      isDiscontinued: plantSpecific.isDiscontinued,
    });
  }

  protected createFromForm(): IPlantSpecific {
    return {
      ...new PlantSpecific(),
      id: this.editForm.get(['id'])!.value,
      objectDependancy: this.editForm.get(['objectDependancy'])!.value,
      refMaterial: this.editForm.get(['refMaterial'])!.value,
      isDiscontinued: this.editForm.get(['isDiscontinued'])!.value,
    };
  }
}
