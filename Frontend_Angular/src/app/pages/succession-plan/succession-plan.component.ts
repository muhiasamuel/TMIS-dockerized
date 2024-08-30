import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { HttpServiceService } from '../../services/http-service.service';
import { ReadyNowDialogComponent } from '../../succession-plan/ready-now-dialog/ready-now-dialog.component';

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
  employees;

  constructor(
    private formBuilder: FormBuilder,
    private http: HttpServiceService,
    public dialog: MatDialog
  ) {}

  ngOnInit() {
    this.getSuccessionDrivers();
    this.myForm = this.formBuilder.group({
      departments: [''],
      SuccessionDriver: [''],
      keyRole: [''],
      currentHolder: [''],
      retentionRisk: [''],
      readyNow: [''],
      readytwo: [''],
      readyMore: [''],
      externalSuccessor: [''],
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
        console.log(res);
      }
    );
  }

  openReadyNowDialog(): void {
    const dialogRef = this.dialog.open(ReadyNowDialogComponent, {
      width: '600px',
      data: this.myForm.get('readyNow').value
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.myForm.get('readyNow').setValue(result);
      }
    });
  }

  openReadyTwoDialog(): void {
    const dialogRef = this.dialog.open(ReadyNowDialogComponent, {
      width: '600px',
      data: this.myForm.get('readytwo').value
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.myForm.get('readytwo').setValue(result);
      }
    });
  }

  openReadyMoreDialog(): void {
    const dialogRef = this.dialog.open(ReadyNowDialogComponent, {
      width: '600px',
      data: this.myForm.get('readyMore').value
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.myForm.get('readyMore').setValue(result);
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
        this.myForm.get('readyNow').reset();

        break;
      case 'Ready in 1-2 Years (R1-2)':
        this.showReadyAfterTwoYears = false;
        this.myForm.get('readytwo').reset();
        break;
      case 'Ready in More Than 2 Years (R>2)':
        this.showReadyMoreThanTwoYears = false;
        this.myForm.get('readyMore').reset();
        break;
      case 'External Ready Successor':
        this.showExternalSuccessor = false;
        this.myForm.get('externalSuccessor').reset();
        break;
    }
  
    this.availableColumns.push(column); // Add back to dropdown
  }
  
  
}
