import { Component } from '@angular/core';
import { HttpServiceService } from '../../services/http-service.service';
import { HttpClient } from '@angular/common/http';
import { Dialog } from '@angular/cdk/dialog';
import { AttributesComponent } from '../attributes/attributes.component';
import { AddPotentialDescriptorComponent } from '../add-potential-descriptor/add-potential-descriptor.component';
import { AddAttributeComponent } from '../add-attribute/add-attribute.component';
import {  Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { log } from 'console';
import { AddAssessmentQuestionsComponent } from '../add-assessment-questions/add-assessment-questions.component';
import { url } from 'inspector';

@Component({
  selector: 'app-potential-attributes',
  templateUrl: './potential-attributes.component.html',
  styleUrl: './potential-attributes.component.scss'
})
export class PotentialAttributesComponent {
  systemUser: any
  userRoleId: any
  status:boolean= false;
  title = "Potential Attributes"
  name="Assesements"
  potentialAttribute: any[] = []
  Assesement: any[] = []
  Assesements: any[] = []
  

  constructor(private route: Router, private http: HttpServiceService, private httpClient: HttpClient, private dialog: MatDialog){}
  ngOnInit(){
    this.systemUser = JSON.parse(localStorage.getItem("user"))
    console.log(this.systemUser)
    if(this.systemUser != null){
      this.userRoleId = this.systemUser.user.role.id
      if(this.systemUser.user.role.id == 1){
        this.getManagerAttributes(this.systemUser.user.userId)
      }else{
        this.getManagerAttributes(this.systemUser.user.manager?.userId)

      }

    }else{
      this.route.navigate([''])
    }
    this.getAssesements()
  }

  getManagerAttributes(managerId: any){
    //http://192.168.2.21:8080/getManagerAttributes?managerId=1

    const url = `${this.http.serverUrl}getManagerAttributes?managerId=${managerId}`
    console.log("url",url)

    const res = this.httpClient.get(url)

    res.subscribe(
      (response: any) => {
        console.log("Api response", response)
        this.potentialAttribute = response.item
      },

      (error: any) => {
        console.log("Api response", error)

      }
    )

  }

  getAssesements(){
    this.http.getAllAssesements().subscribe(
    ((res)=>{
      this.Assesement = res.item
      console.log('assessements',this.Assesement);
      const i = this.Assesement.forEach((assessment) => {
        console.log(assessment);
        
        if (assessment.status == "Active") {
          this.status = true
        }
      })
    }),
    ((error)=>{
      console.log(error);
      
    }),
    ()=>{}
    )
  }

  openDialog(){
    // this.dialog.open(AddAttributeComponent)
      this.dialog.open(AddPotentialDescriptorComponent,{
       width: "50vw"
      })
  }

  openAssesementDialog(){
    this.dialog.open(AddAssessmentQuestionsComponent,{
      width:"60vw",
      position:{
        right:"20em"
      } 
    
    })
  }

}
