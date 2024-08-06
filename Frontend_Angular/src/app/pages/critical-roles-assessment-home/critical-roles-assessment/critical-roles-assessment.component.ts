import {Component} from '@angular/core';
import {FormBuilder, Validators, FormsModule, ReactiveFormsModule, FormGroup, FormControl, FormArray} from '@angular/forms';
import {STEPPER_GLOBAL_OPTIONS, StepperSelectionEvent} from '@angular/cdk/stepper';

import { HttpServiceService } from '../../../services/http-service.service';
import { error, log } from 'console';
import { Dialog } from '@angular/cdk/dialog';
import { MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';

/**
 * @title Stepper that displays errors in the steps
 */
  @Component({
    selector: 'app-critical-roles-assessment',
    templateUrl: './critical-roles-assessment.component.html',
    styleUrl: './critical-roles-assessment.component.scss',
   
  })
  export class CriticalRolesAssessmentComponent {

    strategicImportanceFormGroup:FormGroup;
    roleNameFormGroup:FormGroup;
    revenueImpactFormGroup:FormGroup;
    vacancyRiskFormGroup:FormGroup;
    impactOnOperationFormGroup:FormGroup;
    skillExpirienceFormGroup:FormGroup;
    talentStrategyFormGroup:FormGroup;
    currentStateFormGroup:FormGroup;
    currentState:string
    authUser:any;
    strategy: any;
    isSubmitting:boolean = false;

    formData:any;
    isEditable = false;
    selectedScore!: string;
    average:number;
    scores: any[] = [1, 2, 3, 4, 5 ];

    ngOnInit() {
      const user = localStorage.getItem("user");
       if (user) {
         this.authUser = JSON.parse(user)
       }
     }
    // Form Groups for each step with relevant controls
    constructor(private http: HttpServiceService,private snack :MatSnackBar, private fb:FormBuilder, private dialogref:MatDialogRef<CriticalRolesAssessmentComponent>){
      this.roleNameFormGroup = this.fb.group ({
        roleName: ['',Validators.required]
      });
    
      this.strategicImportanceFormGroup  = this.fb.group ({
        strategicImportance: ['', [Validators.required, Validators.min(1), Validators.max(5)]]
      });
  
      this.revenueImpactFormGroup = this.fb.group({
        revenueImpact: ['', [Validators.required, Validators.min(1), Validators.max(5)]]
      });
  
      this.vacancyRiskFormGroup= this.fb.group({
        vacancyRisk: ['', [Validators.required, Validators.min(1), Validators.max(5)]]
      });
    
      this.impactOnOperationFormGroup = this.fb.group({
        impactOnOperation: ['', [Validators.required, Validators.min(1), Validators.max(5)]]
      });
  
      this.skillExpirienceFormGroup = this.fb.group({
        skillExpirience: ['', [Validators.required, Validators.min(1), Validators.max(5)]]
      });
      this.currentStateFormGroup = fb.group({
        currentState: ['']
      })
      this.talentStrategyFormGroup = this.fb.group({
        // Define other form controls here
        roleDevelopmentStrategies: this.fb.array([])
      });
  
    }

      get roleDevelopmentStrategiesFormArray() {
    return this.talentStrategyFormGroup.get('roleDevelopmentStrategies') as FormArray;
  }

  // Function to add a new strategy form control to the form array
  addStrategy() {
    this.roleDevelopmentStrategiesFormArray.push(this.fb.group({
      strategyName: ''
    }));
  }
  deleteStrategy(i:number){
    this.roleDevelopmentStrategiesFormArray.removeAt(i)
  }

    
    onStepChange(stepper: StepperSelectionEvent) {
      const strategicImportanceValue = this.strategicImportanceFormGroup?.get('strategicImportance')?.value;
      const revenueImpactValue = this.revenueImpactFormGroup?.get('revenueImpact')?.value;
      const vacancyRiskValue = this.vacancyRiskFormGroup?.get('vacancyRisk')?.value;
      const skillExperienceValue = this.skillExpirienceFormGroup?.get('skillExpirience')?.value;
      const impactOnOperationValue = this.impactOnOperationFormGroup?.get('impactOnOperation')?.value;
    
      const total = (strategicImportanceValue + revenueImpactValue + vacancyRiskValue + skillExperienceValue + impactOnOperationValue);
      this.average = total / 5;

      // if (this.average <= 2.5) {
      //   this.currentState = "green"
      //   }else if(this.average > 2.5 && this.average < 3.5){
      //     this.currentState = "amber"
      //   }else{
      //     this.currentState = "red"
      //   }

          const strategies = this.roleDevelopmentStrategiesFormArray.controls.map(control => control.value);
          console.log('Strategy Values:', strategies);
       
    
      console.log('Average:', this.average);
    }
    sam(){

    }
   async processValues() {
    
      const roleName = this.roleNameFormGroup?.get('roleName')?.value;
      const strategicImportanceValue = this.strategicImportanceFormGroup?.get('strategicImportance')?.value;
      const revenueImpactValue = this.revenueImpactFormGroup?.get('revenueImpact')?.value;
      const vacancyRiskValue = this.vacancyRiskFormGroup?.get('vacancyRisk')?.value;
      const skillExperienceValue = this.skillExpirienceFormGroup?.get('skillExpirience')?.value;
      const impactOnOperationValue = this.impactOnOperationFormGroup?.get('impactOnOperation')?.value;
      const talentStrategy = this.talentStrategyFormGroup?.get('talentStrategy')?.value;
      const state = this.currentStateFormGroup?.get('currentState')?.value

    // if(strategicImportanceValue && revenueImpactValue && vacancyRiskValue && impactOnOperationValue)
     // adding data to a form
     
     if (roleName && strategicImportanceValue && revenueImpactValue && impactOnOperationValue && skillExperienceValue && vacancyRiskValue && state) {
      let data = new FormData();
      const strategies = this.roleDevelopmentStrategiesFormArray.controls.map(control => control.value);
      this.strategy = this.roleDevelopmentStrategiesFormArray.controls.map(control => control.value);
      console.log(this.strategy);
      
      this.formData = {        
          "roleName": roleName,
          "currentState": state,
          "strategicImportance": strategicImportanceValue,
          "riskImpact": revenueImpactValue,
          "vacancyRisk": vacancyRiskValue,
          "impactOnOperation": impactOnOperationValue,
          "skillExperience": skillExperienceValue,
          "addedBy": this.authUser.userId,
          "averageRating": this.average,
          "roleDevelopmentStrategies":strategies
        
      }
      console.log("Samuel", this.formData);
      

      this.postRoleAssessment()
     }else{
      console.log('some values are missing!')
     }

    }
    postRoleAssessment() {
      this.isSubmitting = true;
      if (this.formData) {
       this.http.createRoleAssessment(this.authUser?.user?.userId, this.formData).subscribe(
        ((res)=>{
          console.log(res);
        }),
        ((e) =>{
          this.snack.open("There was an error adding your critical role", "Close", {duration:3600})
          this.isSubmitting = false
        }),
        () => {
          this.isSubmitting = false
          this.dialogref.close()
          this.snack.open("Critical role added successfully", "Close", {duration:3600})
        }
      ) 
      }
    }

    //dialog close
    dialogClose(){
      this.dialogref.close()
    }
}
