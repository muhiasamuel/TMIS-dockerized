import { Component, OnInit, ViewChild } from '@angular/core';
import { FormGroup, FormArray, FormControl,Validators, FormBuilder } from '@angular/forms';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { AddPotentialDescriptorComponent } from '../add-potential-descriptor/add-potential-descriptor.component';
import {BreakpointObserver} from '@angular/cdk/layout';
import {StepperOrientation, MatStepperModule, MatStepper} from '@angular/material/stepper';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import { provideNativeDateAdapter } from '@angular/material/core';
import { HttpServiceService } from '../../services/http-service.service';
import moment from 'moment';
import { log } from 'console';
import { MatSnackBar } from '@angular/material/snack-bar';
@Component({
  selector: 'app-add-assessment-questions',
  templateUrl: './add-assessment-questions.component.html',
  styleUrl: './add-assessment-questions.component.scss',
  providers: [provideNativeDateAdapter()],
}) 
export class AddAssessmentQuestionsComponent implements OnInit{
  assessmentAttributes: any;
  assessments:any[] = [];
  user:any;
  targetGroup = ["ALL"]
  attributes: any ;
  assessmentData:any
  myVariable: string | null | undefined;
  assessmentForm:FormGroup = new FormGroup({
    target:new FormControl(""),
    assessmentName:new FormControl(""),
    assessmentDescription:new FormControl(""),
    endDate : new FormControl(new Date().toISOString()),
    // potentialAttributeId:new FormControl(""),


  })
  // questionForm: FormGroup = new FormGroup({
  //   questionList: new FormArray([this.getQuestionFields()]),
  // });
 
  stepperOrientation: Observable<StepperOrientation>;  
     
  @ViewChild(MatStepper) stepper!: MatStepper;
  constructor(private fb:FormBuilder,
    private dialog:MatDialogRef<AddAssessmentQuestionsComponent>, 
    private http: HttpServiceService,
    private snackbar: MatSnackBar,
    breakpointObserver: BreakpointObserver,) {  
      this.stepperOrientation = breakpointObserver
      .observe('(min-width: 800px)')
      .pipe(map(({matches}) => (matches ? 'horizontal' : 'vertical')));
  
  } 

  ngOnInit(): void {
    const user = localStorage.getItem("user")
    if (user) {
      this.user = JSON.parse(user)
      console.log(this.user);
      
    }
   // this.getAssessmentAttribute()
  }
 
  //adding assessments and assessment questions
  submitAssesementDetails(){

    let targetData = this.assessmentForm.value
    const formattedEndDate = moment(targetData.endDate).format('YYYY-MM-DD');
    targetData = {
      ...targetData,
      assessmentName: this.assessmentForm.value.assessmentName + ' ' + " ON " + ' ' + moment().format('YYYY-MM-DD hh:mm A'),
      endDate: formattedEndDate,
      createdAt: moment().format('YYYY-MM-DD')
    };

   //this.addAssessmentQuestions(targetData,serverData)


    console.log("target data",targetData);
    
    
    this.http.postAssesement(targetData, this.user.user.userId).subscribe(
      ((res) =>{
        console.log("");
        let assessments = res.item.assessments;
        console.log("assess",res);
        
      }),
      ((error) =>{
        this.snackbar.open(error.error.message, "Close", {duration: 3000})
        console.error(error);
        
      }),
      ()=>{
        this.snackbar.open("Assesment added sucessfully", "Close", {duration: 3000})
        this.dialog.close()
      }  
    
    )}


//getting attributes, assessment and assessment questions

closeDialog(){
  this.dialog.close()
}
}
