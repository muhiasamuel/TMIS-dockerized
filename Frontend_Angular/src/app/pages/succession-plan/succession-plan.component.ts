import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormArray, FormControl } from '@angular/forms';
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
  departments;
  authUser:any;
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
      this.authUser = JSON.parse(user)
    }
  
    
  
  }

  getDepartment() {
    this.http.getDepartments().subscribe({
      next: (res) => {
        console.log('yoohh', res.item)
        this.employees = res. item
      },
      error : (err) =>{
        console.log('error fetching the holders', err)
      },
      complete : () => {
        alert ('successful')
      },
    })
  }

  getSuccessionDrivers(){
    this.http.getDrivers().subscribe(
      (res) => {
        console.log(res);
        
      }
    )
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

  //getting departing

}

