import { StepperSelectionEvent } from '@angular/cdk/stepper';
import { Component, Inject, Injectable, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { HttpServiceService } from '../../services/http-service.service';
import { of, switchMap } from 'rxjs';

@Component({
  selector: 'app-edit-critical-skill',
  templateUrl: './edit-critical-skill.component.html',
  styleUrl: './edit-critical-skill.component.scss'
})
export class EditCriticalSkillComponent implements OnInit{
  skillForm: FormGroup;
  priorityForm: FormGroup;
  scarcityForm: FormGroup;
  costForm: FormGroup;
  fluidityForm: FormGroup;
  relevanceForm: FormGroup;
  availabilityForm: FormGroup;
  strategyForm: FormGroup;
  currentAvailability: FormGroup;

  isLinear = true;
  authUser: any;
  strategy:any;
  currentState:string;
  isSubmiting:boolean = false;
  
  formData:any;
  average:number;
  data:any;
  scores:any[] = [1,2,3,4,5];
  criticalSkill:any;
  skillToEdit: any;

  constructor(private fb: FormBuilder, private http:HttpServiceService, private snack: MatSnackBar, public dialogRef:MatDialogRef<EditCriticalSkillComponent>,
    @Inject(MAT_DIALOG_DATA) public row: any,) {
      
  
  }

  closeDialog() {
    this.dialogRef.close();
  }

  get skillDevelopmentStrategiesFormArray(){
    return this.strategyForm.get('skillDevelopmentStrategies')
  }

  ngOnInit() {
   const user = localStorage.getItem("user");
    if (user) {
      this.authUser = JSON.parse(user)
    }

    if(this.row) {
      console.log('data',this.row);
      
      this.skillToEdit = this.row.roleEdit
    }
    console.log('weeeeeee', this.skillToEdit);
    
    this.initializeForms()
  }

  initializeForms(){
    this.skillForm = this.fb.group({
      name: [this.skillToEdit.skillName, Validators.required]
    });

    this.priorityForm = this.fb.group({
      priority: [Number(this.skillToEdit.businessPriority), [Validators.required, Validators.min(1), Validators.max(5)]]
    });
    
    this.scarcityForm = this.fb.group({
      scarcity: [Number(this.skillToEdit.scarcityParameter), [Validators.required, Validators.min(1), Validators.max(5)]]
    });

    this.costForm = this.fb.group({
      cost: [Number(this.skillToEdit.developmentCostAndTimeCommitment), [Validators.required, Validators.min(1), Validators.max(5)]]
    });

    this.fluidityForm = this.fb.group({
      fluidity: [Number (this.skillToEdit.marketFluidity), [Validators.required, Validators.min(1), Validators.max(5)]]
    });

    this.relevanceForm = this.fb.group({
      relevance: [Number(this.skillToEdit.futureMarketAndTechRelevance), [Validators.required, Validators.min(1), Validators.max(5)]]
    });

    this.availabilityForm = this.fb.group({
      availability: [(this.skillToEdit.availability), Validators.required]
    });

    this.strategyForm = this.fb.group({
      strategy: [this.skillToEdit.skillDevelopmentStrategy, Validators.required]
    });
    this.currentAvailability = this.fb.group({
      current: [this.skillToEdit.currentSkillState, Validators.required]
    })
  }
 
  onStepChange(stepper: StepperSelectionEvent) {
    const priorityFormValue = this.priorityForm?.get('priority')?.value;
    const scarcityFormValue = this.scarcityForm?.get('scarcity')?.value;
    const costFormValue = this.costForm?.get('cost')?.value;
    const fluidityFormValue = this.fluidityForm?.get('fluidity')?.value;
    const relevanceFormValue = this.relevanceForm?.get('relevance')?.value;
    

    const total = (priorityFormValue +scarcityFormValue + costFormValue + fluidityFormValue + relevanceFormValue);
    this.average = total/5
     const strategies = this.strategyForm?.get("strategy").value
    //const strategies =this.skillDevelopmentStrategiesFormArray.controls.map(control =>control.value);
    console.log( this.average);
  if (this.average <= 2.5) {
      this.currentState = "green"
      }else if(this.average > 2.5 && this.average < 3.5){
        this.currentState = "amber"
      }else{
        this.currentState = "red"
      }
      
  }

  processSkills(){
    const skillName = this.skillForm.value.name;
    const PriorityValue = this.priorityForm.value.priority;
    const scarcityValue = this.scarcityForm.value.scarcity;
    const costValue = this.costForm.value.cost;
    const fluidityValue = this.fluidityForm?.value.fluidity;
    const relevanceValue = this.relevanceForm.value.relevance;
    const availabilityValue = this.availabilityForm.value.availability;
    const strategyValue = this.strategyForm.value.strategy;
    const currentAvailabilityValue = this.currentAvailability.value.current;

    
 

    console.log('formData',this.formData);
    
    console.log("all", skillName , PriorityValue , scarcityValue , costValue , fluidityValue , relevanceValue, strategyValue ,currentAvailabilityValue);

    if (skillName && PriorityValue && scarcityValue && costValue && fluidityValue && relevanceValue && strategyValue && currentAvailabilityValue){
      
      let row = new FormData();
      // const strategies =this.skillDevelopmentStrategiesFormArray.controls.map(control =>control.value);
      // this.strategy = this.skillDevelopmentStrategiesFormArray.control.map(control => control.value);
      // console.log(this.strategy);
      this.formData = {
        "skillName": this.skillForm.value.name,
        "skillDescription": this.skillForm.value.name,
        "skillDevelopmentStrategy": this.strategyForm.value.strategy,
        "businessPriority": this.priorityForm.value.priority,
        "currentSkillState": this.currentAvailability.value.current,
        "scarcityParameter": this.scarcityForm.value.scarcity,
        "marketFluidity": this.fluidityForm.value.fluidity,
        "developmentCostAndTimeCommitment": this.costForm.value.cost,
        "futureMarketAndTechRelevance": this.relevanceForm.value.relevance ,
        "averageRating": this.average  
    }


    this.editSkill()
  }else{
    console.log('some values are missing!')
  }
}
  editSkill() {
    console.log('new data',this.formData);
    this.isSubmiting = true;
    if (this.formData){
      this.http.updateCriticalSkill(this.authUser.user.userId, this.skillToEdit.skillAssessmentId,this.formData).subscribe(
        ((res)=>{
          console.log(res);

        }),
        ((error)=>{
          console.log(error);
          
        }),
        ()=>{
          this.isSubmiting=false
          this.dialogRef.close()
          this.snack.open('Critical Skill Updated', 'close',{duration:3600})
        }
      )
    }
  }

   //dialog close
   dialogClose(){
    this.dialogRef.close()
  }

  getCriticalSkill(){
    this.http.getCriticalSkill(this.skillToEdit.skillId).subscribe(
      ((res)=>{
        console.log("skills",res);
        this.criticalSkill= res.item
      }),
      ((error)=>{
        console.log("error",error);
        
      }),
      ()=>{}
    )
  }
}
