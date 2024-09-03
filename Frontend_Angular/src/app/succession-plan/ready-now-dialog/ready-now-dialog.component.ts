import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormArray } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-ready-now-dialog',
  templateUrl: './ready-now-dialog.component.html',
  styleUrls: ['./ready-now-dialog.component.scss'] // Corrected to styleUrls
})
export class ReadyNowDialogComponent implements OnInit { // Added OnInit to the class declaration
  readyNowForm: FormGroup;
  departments:any;
  positions:any;
  employees:any
  department:any
  position:any

  names:any;

  constructor(
    private formBuilder: FormBuilder,
    public dialogRef: MatDialogRef<ReadyNowDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {}

  ngOnInit(): void {
    this.employees = this.data.employees
    this.departments = this.data.department
    console.log('depatmenssss', this.departments);
    
    console.log("emps yooooooooo",this.employees);
    
    this.readyNowForm = this.formBuilder.group({
      userId: [this.data.name || ''],
      department:[''],
      position:[''],
      readinessLevel: [this.data.readinessLevel || ''],
      proposedInterventions: this.formBuilder.array(
        this.data.proposedInterventions?.map(intervention => this.createInterventionGroup(intervention)) || []
      ),
      developmentNeeds: this.formBuilder.array(
        this.data.developmentNeeds?.map(need => this.createDevelopmentNeedGroup(need)) || []
      )
    });
  }
  
    //filter selected department positions
    getPosition() {

      console.log("form", this.readyNowForm.value.department);
      const positions = this.departments.filter(item => item.depId === this.readyNowForm.value.department)
      this.positions = positions[0].departmentPositions
      console.log("positions", this.positions);
    }
  
    //filter employee who holds selected position
    getPositionHolder() {
  
      console.log("form", this.readyNowForm.value.position);
      const holders = this.employees.filter(user => user.positionId === this.readyNowForm.value.position)
      this.names = holders
      console.log("positions", this.names);
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
    const data = {
      "userId": this.readyNowForm.value.userId,
      "readinessLevel": this.readyNowForm.value.readinessLevel,
      "proposedInterventions":this.readyNowForm.value.proposedInterventions,
      "developmentNeeds":this.readyNowForm.value.developmentNeeds 
    }
    console.log("sam-data", data);
    
    this.dialogRef.close(data);
  }
}
