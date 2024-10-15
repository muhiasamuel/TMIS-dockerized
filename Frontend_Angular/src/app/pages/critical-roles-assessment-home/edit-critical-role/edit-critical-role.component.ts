import { StepperSelectionEvent } from '@angular/cdk/stepper';
import { Component, Inject } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { HttpServiceService } from '../../../services/http-service.service';
import { of, switchMap } from 'rxjs';

@Component({
  selector: 'app-edit-critical-role',
  templateUrl: './edit-critical-role.component.html',
  styleUrl: './edit-critical-role.component.scss'
})
export class EditCriticalRoleComponent {

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
  dataToEdit:any
  criticalRole: any;
  constructor(@Inject(MAT_DIALOG_DATA) public data, 
  private snack:MatSnackBar,
  private http: HttpServiceService,
  private fb: FormBuilder,
  private dialogref:MatDialogRef<EditCriticalRoleComponent>){
  }

    get roleDevelopmentStrategiesFormArray() {
  return this.talentStrategyFormGroup.get('roleDevelopmentStrategies') as FormArray;
}

  ngOnInit(): void {
    const user = sessionStorage.getItem("user")
    if (user) {
      this.authUser = JSON.parse(user);
    }
    if (this.data) {
      this.dataToEdit = this.data.role
    }    
    console.log("sam", this.dataToEdit);
    
    //Called after the constructor, initializing input properties, and the first call to ngOnChanges.
    //Add 'implements OnInit' to the class.
    this.getCriticalRoleDetails()
    this.roleNameFormGroup = this.fb.group ({
      roleName: [this.dataToEdit.roleName, Validators.required]
    });
    console.log("12345678",this.criticalRole);
    
  
    this.strategicImportanceFormGroup  = this.fb.group ({
      strategicImportance: [Number(this.dataToEdit.strategicImportance), [Validators.required, Validators.min(1), Validators.max(5)]]
    });

    this.revenueImpactFormGroup = this.fb.group({
      revenueImpact: [Number(this.dataToEdit.riskImpact), [Validators.required, Validators.min(1), Validators.max(5)]]
    });

    this.vacancyRiskFormGroup= this.fb.group({
      vacancyRisk: [Number(this.dataToEdit.vacancyRisk), [Validators.required, Validators.min(1), Validators.max(5)]]
    });
  
    this.impactOnOperationFormGroup = this.fb.group({
      impactOnOperation: [Number(this.dataToEdit.impactOnOperation), [Validators.required, Validators.min(1), Validators.max(5)]]
    });
    this.currentStateFormGroup = this.fb.group({
      currentState: [this.dataToEdit.currentState]
    })

    this.skillExpirienceFormGroup = this.fb.group({
      skillExpirience: [Number(this.dataToEdit.skillExperience), [Validators.required, Validators.min(1), Validators.max(5)]]
    });

    this.talentStrategyFormGroup = this.fb.group({
      // Define other form controls here
      roleDevelopmentStrategies: this.fb.array([])
    });

  }

  ngAfterViewInit(): void {
    //Called after ngAfterContentInit when the component's view has been initialized. Applies to components only.
    //Add 'implements AfterViewInit' to the class.
    
  }

  //get Criticalrole by id
// Function to add a new strategy form control to the form array
addStrategy() {
  this.roleDevelopmentStrategiesFormArray.push(this.fb.group({
    strategyName: ''
  }));
}

deleteStrategy(i: number) {
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
        "addedBy": this.authUser.user.userId,
        "averageRating": this.average,
        "roleDevelopmentStrategies":strategies
      
    }
    console.log("Samuel", this.formData);
    

    this.editRoleAssessment();
   // this.postRoleAssessment()
   }else{
    console.log('some values are missing!')
   }

  }
  editRoleAssessment() {
    this.isSubmitting = true;
    if (this.formData) {
     this.http.criticalRolesEdit(this.authUser.user.userId,this.dataToEdit.criticalRoleId, this.formData).subscribe(
      ((res)=>{
        console.log(res);
      }),
      ((e) =>{
        this.snack.open("There was an error adding your critical role", "Close", {duration:3600})
      }),
      () => {
        this.isSubmitting = false
        this.dialogref.close()
        this.snack.open("Critical role updated successfully", "Close", {duration:3600})
      }
    ) 
    }
  }

  //dialog close
  dialogClose(){
    this.dialogref.close()
  }
  //get critical ole details + startegies
  getCriticalRoleDetails(){
    this.http.getCriticalRoleByID(this.dataToEdit.criticalRoleId).pipe(
      switchMap(response => {
       this.criticalRole  = response.item.strategies

        const roleDevelopmentStrategiesFormArray = this.talentStrategyFormGroup.get('roleDevelopmentStrategies') as FormArray;
        this.criticalRole.forEach(strategy => {
          roleDevelopmentStrategiesFormArray.push(this.fb.group({
            strategyName: strategy.strategyName // Assuming each strategy object has a property called 'strategyName'
          }));
        });
        console.log("qwert",this.criticalRole);


        
        this.talentStrategyFormGroup = this.fb.group({
          roleDevelopmentStrategies: this.fb.array([])
        });
        // // Initialize form controls here using this.criticalRole
        // this.roleNameFormGroup = this.fb.group ({
        //   roleName: [this.criticalRole.roleName, Validators.required]
        // });
        // this.strategicImportanceFormGroup = this.fb.group ({
        //   strategicImportance: [Number(this.criticalRole.strategicImportance), [Validators.required, Validators.min(1), Validators.max(5)]]
        // });
        // Initialize other form controls similarly
        return of(null);  // or any observable you might want to return
      })
    )
    .subscribe(),
      ((error) =>{
        this.snack.open("an error occurred viewing this critical role", "close", {duration:3600})
      }),
      ()=>{

      }
    
  }
}

