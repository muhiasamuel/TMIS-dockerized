import { Component, OnInit } from '@angular/core';
import { HttpServiceService } from '../../../services/http-service.service';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-manager-assess',
  templateUrl: './manager-assess.component.html',
  styleUrl: './manager-assess.component.scss'
})
export class ManagerAssessComponent implements OnInit{

  
  greeting: string; // Holds greeting message
  title = "Manager Assess" 
  dataToTranser = "All"
  assessments: any[] = []
  systemUser: any
managerEmployees: any;
  constructor(private server: HttpServiceService, private http: HttpClient){}

  openDialog(){}

  ngOnInit(){
    this.setGreeting(); // Set the greeting message
    this.systemUser = JSON.parse(localStorage.getItem("user"))
  }


  getUserDoneAssements(){
    const url = `${this.server.serverUrl}/getDoneAssessments`
    const response = this.http.get(url);
    response.subscribe(
      (value: any) => {
        console.log(value)
        for(let i = 0; i < value.item.length; i++){
          if(value.item[i].managerId == this.systemUser.userId){
            this.assessments.push(value.item);
          }
        }
      },
      (error: any) => {
        console.log(error)
      }
    )
  }


  setGreeting(): void {
    const currentHour = new Date().getHours(); // Get current hour
    // Set greeting message based on time of day
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
