import { Component, Inject } from '@angular/core';
import { FormGroup, FormBuilder, FormArray } from '@angular/forms';
import { Output, EventEmitter } from '@angular/core';
import { HttpServiceService } from '../../../services/http-service.service';
import moment, { duration } from 'moment';
import {
  MatSnackBar,
  MatSnackBarAction,
  MatSnackBarActions,
  MatSnackBarLabel,
  MatSnackBarRef,
} from '@angular/material/snack-bar';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { log } from 'node:console';


@Component({
  selector: 'app-hipos-interventions',
  templateUrl: './hipos-interventions.component.html',
  styleUrl: './hipos-interventions.component.scss'
})
export class HiposInterventionsComponent {
  issubmitting:boolean = false
  name = 'Angular';  
  data:any;  
  authUser:any;
  interventionForm: FormGroup;
  employeeId:number;  
  potentialRoles:any;
  @Output() newItemEvent = new EventEmitter<boolean>();
  
     
  constructor(
    @Inject(MAT_DIALOG_DATA) public info,
    private fb:FormBuilder,
    public dialogRef: MatDialogRef<HiposInterventionsComponent >, 
    private httpService: HttpServiceService,
    private sb: MatSnackBar) {  
     
    this.interventionForm = this.fb.group({ 
      interventions: this.fb.array([]) ,  
    });  
  }  

  ngOnInit(): void {

    const data = this.info
    if (data) {
      this.employeeId = data.employeeId,
      this.potentialRoles= data.employeeData
    }

    const user = localStorage.getItem("user")
    if (user) {
      this.authUser = JSON.parse(user);
    }
    //Called after the constructor, initializing input properties, and the first call to ngOnChanges.
    //Add 'implements OnInit' to the class.
    
  }
    
  interventions() : FormArray {  
    return this.interventionForm.get("interventions") as FormArray  
  }  
     
  newAtrr(): FormGroup {  
    return this.fb.group({  
      developmentInterventions: '',  
      howToAchieveInterventions:   '', 
    })  
  }  
     
  addIntervention() {  
    this.interventions().push(this.newAtrr());  
  }  
     
  removeinterventions(i:number) {  
    this. interventions().removeAt(i);  
  }  
     
  onSubmit() {   
  const data = this.interventionForm.value.interventions;
  console.log(data); 
  console.log("next roles", this.potentialRoles);
  console.log("user",this.employeeId);
  
  
  this.httpService.createDevelopmentInterventions(this.employeeId, data).subscribe(
    (res) => {
      console.log('Response:', res);
     
    },
    (error) => {
      console.error('Error:', error);  
    },
    
    () => {
      this.httpService.createPotentialNextRole(this.employeeId, this.potentialRoles).subscribe(
        ((res) => {
          console.log(res);          
        }),
        ((error) =>{
          console.log(error.error.message);          
        }),
        () => {
          this.dialogRef.close()
          this.sb.open("Intervention added successfully",  'Close', { duration: 2000 })
          this.issubmitting = false         
        }
      )

      

  
      

    }
  );
   
  }  
}
