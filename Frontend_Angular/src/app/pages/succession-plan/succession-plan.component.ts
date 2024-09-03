import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormArray, FormControl } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { HttpServiceService } from '../../services/http-service.service';
import { ReadyNowDialogComponent } from '../../succession-plan/ready-now-dialog/ready-now-dialog.component';
import { ExternalSuccessorComponent } from './external-successor/external-successor.component';

@Component({
  selector: 'app-succession-plan',
  templateUrl: './succession-plan.component.html',
  styleUrls: ['./succession-plan.component.scss']
})
export class SuccessionPlanComponent implements OnInit {

  myForm: FormGroup;
  availableColumns = ['Ready Now (RN)', 'Ready in 1-2 Years (R1-2)', 'Ready in More Than 2 Years (R>2)', 'External Ready Successor'];
  showReadyNow = false;
  showReadyAfterTwoYears = false;
  showReadyMoreThanTwoYears = false;
  showExternalSuccessor = false;
  employees:any;
  drivers: any;
  departments: any;
  positions: any;
  positioHolder:any;


  constructor(
    private formBuilder: FormBuilder,
    private http: HttpServiceService,
    public dialog: MatDialog
  ) {}

  ngOnInit() {
    this.getSuccessionDrivers();
    this.getDepartments()
    this.getEmployees()
    this.myForm = this.formBuilder.group({
      departmentId: [''],
      driverId: [''],
      positionId: [''],
      currentRoleHolderId: [''],
      retentionRiskRating: [''],
      readyNow: this.formBuilder.array([]),
      readyAfterTwoYears: this.formBuilder.array([]),
      readyMoreThanTwoYears: this.formBuilder.array([]),
      externalSuccessor: this.formBuilder.array([]),
      developmentNeed: [''],
      proposedIntervention: ['']
    });

    const user = localStorage.getItem("user");
    if (user) {
      const authUser = JSON.parse(user);
    }
  }

  getSuccessionDrivers() {
    this.http.getDrivers().subscribe(
      (res) => {
        this.drivers = res.item
        console.log(res);
      }
    );
  }



  //get departments
  getDepartments(){
    this.http.getDepartments().subscribe(
      ((response)=> {
        console.log("deps", response.item);
        
        this.departments = response.item
      }),
      ((error) =>{
        console.error(error);        
      }),
      () => {}
    )
  }
  //filter selected department positions
  getPosition() {

    console.log("form", this.myForm.value.departmentId);
    const positions = this.departments.filter(item => item.depId === this.myForm.value.departmentId)
    this.positions = positions[0].departmentPositions
    console.log("positions", this.positions);
  }

  //filter employee who holds selected position
  getPositionHolder() {

    console.log("form", this.myForm.value.positionId);
    const holders = this.employees.filter(user => user.positionId === this.myForm.value.positionId)
    this.positioHolder = holders
    console.log("positions", this.positioHolder);
  }

  getEmployees(){
    this.http.getAllUsers().subscribe(
      ((response)=> {
        console.log("empl", response.item);
        
        this.employees = response.item
      }),
      ((error) =>{
        console.error(error);        
      }),
      () => {}
    )
  }

  getFormArray(arrayName: string): FormArray {
    return this.myForm.get(arrayName) as FormArray;
  }

  openReadyNowDialog(): void {
    const dialogRef = this.dialog.open(ReadyNowDialogComponent, {
      width: '600px',
      data: {
        data: this.getFormArray('readyNow'),
        employees:this.employees,
        department:this.departments
      }
      


      //data: null // Pass any initial data if needed
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        const formArray = this.getFormArray('readyNow');
        formArray.push(new FormControl(result));
      }
    });
  }

  openReadyTwoDialog(): void {
    const dialogRef = this.dialog.open(ReadyNowDialogComponent, {
      width: '600px',
      data:{
        data: this.getFormArray('readyAfterTwoYears'),
        employees:this.employees, 
        department:this.departments
        } 
      
      // Pass any initial data if needed
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        const formArray = this.getFormArray('readyAfterTwoYears');
        formArray.push(new FormControl(result));
      }
    });
  }

  openReadyMoreDialog(): void {
    const dialogRef = this.dialog.open(ReadyNowDialogComponent, {
      width: '600px',
      data:{data: this.getFormArray('readyMoreThanTwoYears'),
            employees:this.employees,
            department:this.departments  
      } // Pass any initial data if needed
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        const formArray = this.getFormArray('readyMoreThanTwoYears');
        formArray.push(new FormControl(result));
      }
    });
  }

  openExternalSuccessorDialog(): void {
    const dialogRef = this.dialog.open(ExternalSuccessorComponent, {
      width: '600px',
      data: null // Pass any initial data if needed
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        const formArray = this.getFormArray('externalSuccessor');
        formArray.push(new FormControl(result));
      }
    });
  }

  onSubmitted() {
    console.log(this.myForm.value);
    this.http.createSuccession(this.myForm.value).subscribe({
      next: res => {
        console.log("Success", res);
      },
      error: err => {
        console.log("Error", err);
      }
    });
  }

  onColumnSelect(column: string) {
    switch (column) {
      case 'Ready Now (RN)':
        this.showReadyNow = true;
        break;
      case 'Ready in 1-2 Years (R1-2)':
        this.showReadyAfterTwoYears = true;
        break;
      case 'Ready in More Than 2 Years (R>2)':
        this.showReadyMoreThanTwoYears = true;
        break;
      case 'External Ready Successor':
        this.showExternalSuccessor = true;
        break;
    }

    this.availableColumns = this.availableColumns.filter(item => item !== column);
  }

  onColumnRemove(column: string) {
    switch (column) {
      case 'Ready Now (RN)':
        this.showReadyNow = false;
        this.getFormArray('readyNow').clear();
        break;
      case 'Ready in 1-2 Years (R1-2)':
        this.showReadyAfterTwoYears = false;
        this.getFormArray('readytwo').clear();
        break;
      case 'Ready in More Than 2 Years (R>2)':
        this.showReadyMoreThanTwoYears = false;
        this.getFormArray('readyMore').clear();
        break;
      case 'External Ready Successor':
        this.showExternalSuccessor = false;
        this.getFormArray('externalSuccessor').clear();
        break;
    }

    this.availableColumns.push(column); // Add back to dropdown
  }
}
