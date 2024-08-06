import { Component, OnInit } from '@angular/core';
import { HttpServiceService } from '../../../services/http-service.service';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-manager-assess',
  templateUrl: './manager-assess.component.html',
  styleUrl: './manager-assess.component.scss'
})
export class ManagerAssessComponent implements OnInit{
  title = "Manager Assess" 
  dataToTranser = "All"
  assessments: any[] = []
  systemUser: any
  constructor(private server: HttpServiceService, private http: HttpClient){}

  openDialog(){}

  ngOnInit(){
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


  openUnseen(){
    this.dataToTranser = "UnassessedMan"
  }

  openSeen(){
    this.dataToTranser = "AssessedMan"

  }
}
