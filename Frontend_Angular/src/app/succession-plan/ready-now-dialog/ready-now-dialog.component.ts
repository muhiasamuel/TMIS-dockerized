import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormArray } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-ready-now-dialog',
  templateUrl: './ready-now-dialog.component.html',
  styleUrl: './ready-now-dialog.component.scss'
})
export class ReadyNowDialogComponent {
  readyNowForm: FormGroup;
  names = ['Muthui', 'Beth', 'Charles', 'Steve'];

  constructor(
    private formBuilder: FormBuilder,
    public dialogRef: MatDialogRef<ReadyNowDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) { }

  ngOnInit(): void {
    this.readyNowForm = this.formBuilder.group({
      name: [this.data.name || ''],
      readinessLevel: [this.data.readinessLevel || ''],
      proposedInterventions: this.formBuilder.array(
        this.data.proposedInterventions?.map(intervention => this.createInterventionGroup(intervention)) || []
      ),
      developmentNeeds: this.formBuilder.array(
        this.data.developmentNeeds?.map(need => this.createDevelopmentNeedGroup(need)) || []
      )
    });
  }

  createInterventionGroup(intervention): FormGroup {
    return this.formBuilder.group({
      type: [intervention.type],
      description: [intervention.description],
      status: [intervention.status],
      startDate: [intervention.startDate],
      endDate: [intervention.endDate]
    });
  }

  createDevelopmentNeedGroup(need): FormGroup {
    return this.formBuilder.group({
      needType: [need.needType],
      description: [need.description]
    });
  }

  get proposedInterventions(): FormArray {
    return this.readyNowForm.get('proposedInterventions') as FormArray;
  }

  get developmentNeeds(): FormArray {
    return this.readyNowForm.get('developmentNeeds') as FormArray;
  }

  addIntervention(): void {
    this.proposedInterventions.push(this.createInterventionGroup({ type: '', description: '', status: '', startDate: '', endDate: '' }));
  }

  addDevelopmentNeed(): void {
    this.developmentNeeds.push(this.createDevelopmentNeedGroup({ needType: '', description: '' }));
  }

  removeIntervention(index: number): void {
    this.proposedInterventions.removeAt(index);
  }

  removeDevelopmentNeed(index: number): void {
    this.developmentNeeds.removeAt(index);
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  onSaveClick(): void {
    this.dialogRef.close(this.readyNowForm.value);
  }
}

