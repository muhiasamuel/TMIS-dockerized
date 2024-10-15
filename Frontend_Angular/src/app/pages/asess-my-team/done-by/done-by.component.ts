import { Dialog } from '@angular/cdk/dialog';
import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { HttpServiceService } from '../../../services/http-service.service';

export interface Answer{
  userId: any
  choiceId: any
  managerId: any
  questionId: any
}

@Component({
  selector: 'app-done-by',
  templateUrl: './done-by.component.html',
  styleUrl: './done-by.component.scss'
})
export class DoneByComponent {
goToAssessedAttributes() {
throw new Error('Method not implemented.');
}
  title = "Assessments"
  answers: any[] = []
  value: any[] = []
  questionIds: any[] = []
  systemUser: any
  user:any;
  assId: any
  startQuiz: Boolean = true
  assQuestions: any[] = []
  managerId:any
  pageStatus: any

  
  assignment = {
    "assessmentName":"",
    "assessmentDescription":""
  }

  questionForm = this._formBuilder.group({
      
  })

  names: any[] = []
  answerErrors: any[] = []

  
  

  isLinear = true;
notComplete: boolean = true;
userResponse: any;

  constructor(private _formBuilder: FormBuilder, private route: ActivatedRoute,
    private dialog: Dialog,
    private http: HttpClient, 
    private server: HttpServiceService) {}

  ngOnInit(){
    this.user = JSON.parse(sessionStorage.getItem("user"))
    this.systemUser = this.user.user
    this.route.params.subscribe(params => {
      this.assId = params['id']; // Access the 'id' parameter from the URL
      this.pageStatus = params['status']
      console.log('Test ID:', this.assId);
      console.log('Test status:', this.pageStatus);

    });
    this.getAssessment(this.assId)
    this.title=this.assignment.assessmentName
    
   // console.log("question ids",this.questionIds)
   // console.log("user-manager",this.systemUser.manager.userId)


    //this.getThem()
   // this.checkAss(this.value)
    
  }

  getThem(){
    const url = `${this.server.serverUrl}userAssessment?assId=${this.assId}&userId=${this.systemUser.user.userId}`
    const res = this.http.get(url)

    res.subscribe(
      (response: any) => {
        for(let i = 0; i < response.item.length; i++){
        console.log("res",)
        if(response.item[i].assessmentId == this.assId && response.item[i].employeeId == this.systemUser.user.userId){
          this.value.push(response.item[i])
        }
        }
        console.log("value",this.value)
        
      },
      (error: any) => {
          console.log(error)
      }
    )
  }

  checkAss(value:any[]){
    console.log("first",value.length)

    for(let i = 0; i < value.length; i++){
      console.log( `iteration -> ${i} assessmentId: ${value[i].assessmentId} -> myId: ${this.assId}`);
      console.log( `iteration -> ${i} employeeId: ${value[i].employeeId} -> userId: ${this.systemUser.user.userId}`);


        if(value[i].assessmentId == this.assId && value[i].employeeId == this.systemUser.user.userId){
            this.answers.push(value[i])
        }
      }

      console.log("answers",this.answers);
  }

  getAssessment(id:any){
    const url = `${this.server.serverUrl}getAssess?assId=${id}`
    const res = this.http.get(url)

    res.subscribe(
      (response: any) =>{
        this.assignment = response.item
        this.title = `${this.assignment.assessmentName}`
        this.assQuestions = response.item.assessmentQuestions
        this.assQuestions.forEach((question: any) => {
          this.questionIds.push(question.assessmentQuestionId)
          this.names.push(question.assessmentQuestionId)
          this.questionForm.addControl(
            `${question.assessmentQuestionId}`, this._formBuilder.control([''])
          );
        });
        
        console.log("Assignment Response", this.assQuestions)

      },
      (error: any) =>{
        console.log("Assignment Response", error)
      }
    )
  }

  startAssignment(){
    // this.dialog.open(TimerDialogComponent,{
    //   width: '25%'
    // })

    this.startQuiz = false
  }

  collectUserInput(){
    //checking if the user has answered all the questions
    this.answerErrors = []
    for(let i = 0; i < this.names.length; i++){
      if(this.questionForm.get(`${this.names[i]}`).value == ''){
        this.answerErrors.push(`Please answer -> Question ${i +1}`)
      }
    }
    if(this.answerErrors.length == 0){
      const answers: Answer[] = []
      for (let index = 0; index < this.assQuestions.length; index++) {
        const element = this.assQuestions[index];
        const formControlName = this.names[index]
        const questionId = this.questionIds[index]
        const choiceId = this.questionForm.value[formControlName].choiceId
       // console.log("question id",questionId)
       // console.log("choice id", choiceId)
       // console.log("user id",this.systemUser.userId)
  
        const answer =  {
          "userId" : this.systemUser.user.userId,
          "choiceId" : choiceId,
          "managerId":0,
          "questionId": questionId
        }
        console.log("answer",answer)
        answers.push(answer)
      }
  
      if(answers.length == this.assQuestions.length && answers.length > 0){
        const url = `${this.server.serverUrl}addAnswers?assId=${this.assId}&manId=${this.systemUser.user.manager.userId}`
       // let url =''
        const response = this.http.post(url, answers)
        response.subscribe(
          (res: any) => {
            console.log(res)
          },
          (error: any) => {
            console.error(error)
          }
        )
  
      }
  
    }

  }
}
