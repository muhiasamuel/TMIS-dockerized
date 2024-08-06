import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { HttpServiceService } from '../../../services/http-service.service';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';

export interface Answer{
  userId: any
  choiceId: any
  managerId: any
  questionId: any
}

@Component({
  selector: 'app-manager-assess-employee',
  templateUrl: './manager-assess-employee.component.html',
  styleUrl: './manager-assess-employee.component.scss'
})
export class ManagerAssessEmployeeComponent implements OnInit{
  title = "Manager assess employee"
  assId: any
  empId: any
  systemUser: any
  assName: any
  assDescription: any
  employeeName: any
  userDoneQuestions: any[] = []
  assessmentQuestions: any[] = []
  questionForm = this._formBuilder.group({
      
  })

  constructor(private _formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private snack:MatSnackBar,
     private http: HttpClient, private server: HttpServiceService){}

  ngOnInit(): void {
    //getting the manager id from local storage.
    this.systemUser = JSON.parse(localStorage.getItem("user"))

    //getting the userId from query params
    this.route.params.subscribe(params => {
      this.assId = params['assId']; 
      this.empId = params['id']// Access the 'id' parameter from the URL
      console.log('Assessment ID:', this.assId);
      console.log('Employee ID:', this.empId);
    })
    this.questionForm = this._formBuilder.group({
      
    })
    this.getUserAssesment()
  }

  openDialog(){}

  getUserAssesment(){
    const url = `${this.server.serverUrl}employeeAss?assId=${this.assId}&empId=${this.empId}`
    const response = this.http.get(url)

    response.subscribe(
      (value: any) => {
        this.userDoneQuestions = value.item.user.assQuestionDtoList
        this.employeeName = value.item.user.userFullName
        this.assName = value.item.assName
        this.assDescription = value.item.assDescription
        this.assessmentQuestions = value.item.assQuestions
        this.userDoneQuestions.forEach((quest: any)=>{
          this.questionForm.addControl(
            `${quest.questionId}`, this._formBuilder.control('', Validators.required)
          )
        })
        
        console.log(value)
      },
      (error: any) => {
        console.log(error)

      }
    )
  }

  submitMangerAssessment(){
    //console.log(this.questionForm.value)
    //console.log("value selected of 1",this.questionForm.value[1].choiceId)

    if(this.questionForm.valid){
      const answers: Answer[] = []

      for(let i = 0; i < this.userDoneQuestions.length; i++){
        const questionId = this.userDoneQuestions[i].questionId
        //console.log("Current question id",questionId)

        const cId = this.questionForm.value[questionId].choiceId
        const answer: Answer = {
          userId: this.empId,
          choiceId: cId,
          managerId: this.systemUser.user.userId,
          questionId: questionId 
        }
        answers.push(answer)
      }

      //updating the manager answers
      const url = `${this.server.serverUrl}managerUpdateChoice?assId=${this.assId}&manId=${this.systemUser.user.userId}&empId=${this.empId}`
      const response = this.http.post(url, answers)

      response.subscribe(
        (response: any) => {
          this.snack.open(`you have successfully asessed${this.employeeName}`)
          //if the message is successful please do something
          console.log(response)
        },
        (error: any) => {
          console.log(error)

        }
      )
      
    }else{
      const errorParagraph = document.getElementById('error')
      errorParagraph.innerHTML = "Please answer all the questions."
      console.log("Form doesn't contain all answers")

    }
  }

}
