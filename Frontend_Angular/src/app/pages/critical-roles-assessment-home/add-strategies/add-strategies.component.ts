import { Component, Inject, inject } from '@angular/core';
import { FormArray, FormBuilder, FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { HttpServiceService } from '../../../services/http-service.service';
import { MatSnackBar } from '@angular/material/snack-bar';


@Component({
  selector: 'app-add-strategies',
  templateUrl: './add-strategies.component.html',
  styleUrl: './add-strategies.component.scss'
})
export class AddStrategiesComponent {


authUser : any
talentStrategyFormGroup: FormGroup<any>;
stepper: any;
  constructor(private fb:FormBuilder, private dialogRef: MatDialogRef<AddStrategiesComponent>,
     @Inject(MAT_DIALOG_DATA) private data, private http:HttpServiceService, private snack: MatSnackBar){
    
  }
  get roleDevelopmentStrategiesFormArray() {
    return this.talentStrategyFormGroup.get('roleDevelopmentStrategies') as FormArray;
  }

  ngOnInit(): void {
    //Called after the constructor, initializing input properties, and the first call to ngOnChanges.
    //Add 'implements OnInit' to the class.
    console.log(this.data);
    const user = localStorage.getItem("user");
    if (user) {
      this.authUser = JSON.parse(user);
    }
    this.talentStrategyFormGroup = this.fb.group({
      // Define other form controls here
      roleDevelopmentStrategies: this.fb.array([])
    });
  }

  addStrategy() {
    this.roleDevelopmentStrategiesFormArray.push(this.fb.group({
      strategyName: ''
    }));
  }

  removeStrategy(i: number) {
    this.roleDevelopmentStrategiesFormArray.removeAt(i)
    }

    onsubmit(){
      const strategies = this.roleDevelopmentStrategiesFormArray.controls.map(control => control.value);
      if (this.talentStrategyFormGroup.value.roleDevelopmentStrategies == "" || this.talentStrategyFormGroup.value.roleDevelopmentStrategies == null) {
        this.snack.open("atleast one strategy is required","Close",{duration:3600})
      } else {
        this.http.addStrategiesToRole(this.data.role,this.authUser.user.userId,strategies).subscribe(
          ((res) =>{
            console.log(res);
            
          }),
          ((e)=>{
            this.snack.open(e.message, "close",{duration:3600})
          }),
          ()=>{
            this.dialogRef.close()
            this.snack.open("strategies added successfully","close",{duration:3600})
          }
        )
      }
      console.log(strategies);
      
    }

}
