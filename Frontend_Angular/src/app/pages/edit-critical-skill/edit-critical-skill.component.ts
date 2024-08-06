import { StepperSelectionEvent } from '@angular/cdk/stepper';
import { Component, Inject, Injectable, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { HttpServiceService } from '../../services/http-service.service';

@Component({
  selector: 'app-edit-critical-skill',
  templateUrl: './edit-critical-skill.component.html',
  styleUrl: './edit-critical-skill.component.scss'
})
export class EditCriticalSkillComponent implements OnInit{
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
  dialogRef: any;
  skillToEdit: any;

  constructor(private fb: FormBuilder, private http:HttpServiceService, private snack: MatSnackBar, public diologRef:MatDialogRef<EditCriticalSkillComponent>,
    @Inject(MAT_DIALOG_DATA) public row: any,) {
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
      this.currentAvailability = this.fb.group({
        current: ['', Validators.required]
      })
  
      console.log(1);
    // this.skillForm = this.fb.group({

    //   name: [this.skillToEdit.skillName, Validators.required]
    // });

    // this.priorityForm = this.fb.group({
    //   priority: [this.skillToEdit.businessPriority, [Validators.required, Validators.min(1), Validators.max(5)]]
    // });
    
    // this.scarcityForm = this.fb.group({
    //   scarcity: [this.skillToEdit.scarcityParameter, [Validators.required, Validators.min(1), Validators.max(5)]]
    // });

    // this.costForm = this.fb.group({
    //   cost: [this.skillToEdit.developmentCostAndTimeCommitment, [Validators.required, Validators.min(1), Validators.max(5)]]
    // });

    // this.fluidityForm = this.fb.group({
    //   fluidity: [this.skillToEdit.marketFluidity, [Validators.required, Validators.min(1), Validators.max(5)]]
    // });

    // this.relevanceForm = this.fb.group({
    //   relevance: [this.skillToEdit.futureMarketAndTechRelevance, [Validators.required, Validators.min(1), Validators.max(5)]]
    // });

    // this.availabilityForm = this.fb.group({
    //   availability: [this.skillToEdit.currentSkillState, Validators.required]
    // });

    // this.strategyForm = this.fb.group({
    //   strategy: [this.skillToEdit.skillDevelopmentStrategy, Validators.required]
    // });

    console.log(1);
  }

  closeDialog() {
    this.diologRef.close();
  }
  ngOnInit() {
   const user = localStorage.getItem("user");
    if (user) {
      this.authUser = JSON.parse(user)
    }

    if(this.row) {
      this.skillToEdit = this.row.roleEdit
    }

    console.log('weeeeeee', this.skillToEdit);
    this.skillForm.patchValue({
     name: this.skillToEdit.skillName
    });
     this.priorityForm.patchValue({
       priority: this.skillToEdit.businessPriority
     });

    this.scarcityForm.patchValue({
      scarcity: this.skillToEdit.scarcityParameter
    });
    this.costForm.patchValue({
      cost: this.skillToEdit.developmentCostAndTimeCommitment
    });
    console.log('yesssssss', this.skillToEdit.developmentCostAndTimeCommitment);

   
    this.fluidityForm.patchValue({
      fluidity: this.skillToEdit.marketFluidity
    });
    this.relevanceForm.patchValue({
      relevance: this.skillToEdit.futureMarketAndTechRelevance.value
    });
    this.availabilityForm.patchValue({
      availability: this.skillToEdit.currentSkillState
    });
    this.strategyForm.patchValue({
      stragegy:this.skillToEdit.skillDevelopmentStrategy
    });

    this.currentAvailability.patchValue({
      current: this.skillToEdit.currentSkillState
    })

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
      
  }

   onEditing() {

     this.data = {
      "skillName": this.skillForm.value.name,
      "skillDescription": this.skillForm.value.name,
      "skillDevelopmentStrategy": this.strategyForm.value.strategy,
      "businessPriority": this.priorityForm.value,
      "currentSkillState": this.currentAvailability.value.current,
      "scarcityParameter": this.scarcityForm.value.scarcity,
      "marketFluidity": this.fluidityForm.value.fluidity,
      "developmentCostAndTimeCommitment": this.costForm.value.cost,
      "futureMarketAndTechRelevance": this.relevanceForm.value.relevance ,
      "averageRating": this.average          
  }

  console.log(this.priorityForm.value,);
    this.editSkill();

  }

  

  editSkill() {
    this.data = {
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

  console.log('priority form',this.priorityForm.value.priority,);
    console.log('deeeeeee',this.skillToEdit.skillAssessmentId,this.authUser.userId );
      this.http.updateCriticalSkill(this.skillToEdit.skillAssessmentId,this.authUser.user.userId, this.data ).subscribe(
      ((res) =>{
        console.log("Martinelli", res);
      }),
    ((error)=>{
      console.error("gabriel",error)
    }),
  ()=>{alert('successful')})
  }
 
}
