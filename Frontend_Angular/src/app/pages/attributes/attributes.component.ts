import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { HttpServiceService } from '../../services/http-service.service';
import { Dialog } from '@angular/cdk/dialog';
import { TimerDialogComponent } from '../timer-dialog/timer-dialog.component';
import { FormGroup } from '@angular/forms';
import { ActivatedRoute, Route } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';

export interface Answer{
  userId: any
  choiceId: any
  managerId: any
  questionId: any
  
}

@Component({
  selector: 'app-attributes',
  templateUrl: './attributes.component.html',
  styleUrl: './attributes.component.scss'
})
export class AttributesComponent {
  systemUser: any
  assId: any
  empId: any
  assQuestions: any[] = []
  managerId:any
  managerName: any
  managerAverage: any
  employeeName: any
  employeeAverage: any

  
  assignment = {
    "endDate":"",
    "assessmentName":"",
    "assessmentDescription":""
  }



  constructor(private _formBuilder: FormBuilder, private route: ActivatedRoute,
    private dialog: Dialog,
    private http: HttpClient,
    private snack:MatSnackBar,
    private server: HttpServiceService) {}

  ngOnInit(){
    this.systemUser = JSON.parse(localStorage.getItem("user"))
    this.route.params.subscribe(params => {
      this.assId = params['assId']; // Access the 'id' parameter from the URL
      this.empId = params['id']
      console.log('Test ID:', this.assId);
    });
    this.getAssessedAssessmentDetails(this.assId, this.systemUser.user.userId, this.empId)
    console.log("ass quests", this.assQuestions)
    

    
  }
  getAssessedAssessmentDetails(assId: any, managerId: any, empId: any) {
    const url = `${this.server.serverUrl}assessedAssessmentDetails?assId=${assId}&managerId=${managerId}&empId=${empId}`
    const response = this.http.get(url)

    response.subscribe(
      (value: any) => {
        console.log(value)
        this.assignment.assessmentName = value.item.assName
        this.assignment.assessmentDescription = value.item.assDescription
        this.assQuestions = value.item.doneBy.assQuestionDtoList
        this.managerName = value.item.doneBy.man.userFullName
        this.managerAverage = value.item.doneBy.managerAverageScore
        this.employeeName = value.item.doneBy.userFullName
        this.employeeAverage = value.item.doneBy.averageScore
        
      },
      (error: any) => {
        console.log(error)

      }
    )
  }
}
