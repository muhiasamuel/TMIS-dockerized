import { Component } from '@angular/core';
import { HttpServiceService } from '../../services/http-service.service';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-user-assessment',
  templateUrl: './user-assessment.component.html',
  styleUrl: './user-assessment.component.scss'
})
export class UserAssessmentComponent {
  activeButton: string = 'done';
  title = "Assess your potential" 
  dataToTranser = "Done"
  assessments: any[] = []
  systemUser: any
  greeting:string;
  constructor(private server: HttpServiceService, private http: HttpClient){}

  openDialog(){}

  ngOnInit(){
    this.setGreeting()
    this.systemUser = JSON.parse(localStorage.getItem("user"))
    console.log("ttodayyyyy",this.systemUser)
  }


  getUserDoneAssements(){
    const url = `${this.server.serverUrl}getDoneAssessments`
    const response = this.http.get(url);
    response.subscribe(
      (value: any) => {
        console.log(value)
        for(let i = 0; i < value.item.length; i++){
          if(value.item[i].managerId == this.systemUser.user.userId){
            this.assessments.push(value.item);
          }
        }
      },
      (error: any) => {
        console.log(error)
      }
    )
  }

  
  openDone(){
    this.activeButton = 'done';
    this.dataToTranser = "Done"
  }

  openNotDone(){
    this.activeButton = 'notDone';
    this.dataToTranser = "Not Done"

  }
  openAssessed(){
    this.activeButton = 'assessed';
    this.dataToTranser = "AssessedEmp"

  }


  setGreeting(): void {
    const currentHour = new Date().getHours();

    if (currentHour >= 5 && currentHour < 12) {
      this.greeting = 'Good morning';
    } else if (currentHour >= 12 && currentHour < 18) {
      this.greeting = 'Good afternoon';
    } else if (currentHour >= 18 && currentHour < 22) {
      this.greeting = 'Good evening';
    } else {
      this.greeting = 'Good night';
    }
  }


}
