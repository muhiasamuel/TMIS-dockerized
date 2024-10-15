import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpServiceService } from '../../services/http-service.service';
import { error, log } from 'console';
import { MatStepper } from '@angular/material/stepper';
import { StepperSelectionEvent } from '@angular/cdk/stepper';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-skills-assessment',
  templateUrl: './skills-assessment.component.html',
  styleUrls: ['./skills-assessment.component.scss']
})
export class SkillsAssessmentComponent implements OnInit {
  isLinear = true;
  skillForm: FormGroup;
  priorityForm: FormGroup;
  scarcityForm: FormGroup;
  costForm: FormGroup;
  fluidityForm: FormGroup;
  relevanceForm: FormGroup;
  availabilityForm: FormGroup;
  strategyForm: FormGroup;
  currentAvailability: FormGroup;
  authUser: any;
  average:number;
  currentState:string;
  isSubmiting:boolean = false;
  data:any;
  scores:any[] = [1,2,3,4,5];

  constructor(private fb: FormBuilder, private http:HttpServiceService, private snack: MatSnackBar, public dialogRef: MatDialogRef<SkillsAssessmentComponent>) {
    this.skillForm = this.fb.group({
      name: ['', Validators.required]
    });

    this.priorityForm = this.fb.group({
      priority: ['', [Validators.required, Validators.min(1), Validators.max(5)]]
    });
    
    this.scarcityForm = this.fb.group({
      scarcity: ['', [Validators.required, Validators.min(1), Validators.max(5)]]
    });

    this.costForm = this.fb.group({
      cost: ['', [Validators.required, Validators.min(1), Validators.max(5)]]
    });

    this.fluidityForm = this.fb.group({
      fluidity: ['', [Validators.required, Validators.min(1), Validators.max(5)]]
    });

    this.relevanceForm = this.fb.group({
      relevance: ['', [Validators.required, Validators.min(1), Validators.max(5)]]
    });

    this.availabilityForm = this.fb.group({
      availability: ['', Validators.required]
    });

    this.strategyForm = this.fb.group({
      strategy: ['', Validators.required]
    });

    this.currentAvailability = fb.group({
      current: ['', Validators.required]
    });

    console.log(1);
  }

  closeDialog(): void {
    this.dialogRef.close();
  }
  ngOnInit() {
   const user = sessionStorage.getItem("user");
    if (user) {
      this.authUser = JSON.parse(user)
    }
  }
  
  onStepChange(stepper: StepperSelectionEvent) {
    const aver = (this.scarcityForm.value.scarcity + this.priorityForm.value.priority + this.costForm.value.cost + this.fluidityForm.value.fluidity + this.relevanceForm.value.relevance )
    this.average = aver/5

    console.log( this.average);
  if (this.average <= 2.5) {
      this.currentState = "green"
      }else if(this.average > 2.5 && this.average < 3.5){
        this.currentState = "amber"
      }else{
        this.currentState = "red"
      }
      console.log(this.authUser);
      
  }
  onSubmit() {
    this.isSubmiting = true;
    if (!this.scarcityForm.value && !this.priorityForm.value && !this.costForm.value && !this.fluidityForm.value && !this.relevanceForm.value && !this.skillForm.value && !this.currentState) {
      this.snack.open("My friend, some values are missing. Goback!", "Close", {duration:3600})
    }else if(this.average >= 3.5){
      if(!this.strategyForm.value){
        alert("eerers")
        this.snack.open("Strategy if missing", "Close", {duration:3600})
      }else{
        const data = {
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

      this.http.createCriticalSkills(this.authUser.user.userId,data).subscribe(
        
        (res)=>{
      console.log('yoooo', res);
      },
      (error) => {
        console.error('ggggggggg', error);
        this.isSubmiting = false;
      },
      () => {
        this.snack.open('Skill assessment added sucessfully', 'Close', {duration: 3600});
        this.dialogRef.close()
        this.isSubmiting = false;
      })
      }
    }else{
      this.isSubmiting = true;
      const data = {
        "skillName": this.skillForm.value.name,
        "skillDescription": this.skillForm.value.name,
        "businessPriority": this.priorityForm.value.priority,
        "currentSkillState": this.currentState,
        "scarcityParameter": this.scarcityForm.value.scarcity,
        "marketFluidity": this.fluidityForm.value.fluidity,
        "developmentCostAndTimeCommitment": this.costForm.value.cost,
        "futureMarketAndTechRelevance": this.relevanceForm.value.relevance ,
        "averageRating": this.average          
    }

    this.http.createCriticalSkills(this.authUser.user.userId,data).subscribe(
      
      (res)=>{
    console.log('yoooo', res);
    },
    (error) => {
      console.error('ggggggggg', error);
      this.isSubmiting = false;
    },
    () => {
      this.dialogRef.close();
      this.snack.open('Skill assessment added sucessfully', 'Close', {duration: 3600});
      this.isSubmiting = false;
    })
         }
    }    



}


   