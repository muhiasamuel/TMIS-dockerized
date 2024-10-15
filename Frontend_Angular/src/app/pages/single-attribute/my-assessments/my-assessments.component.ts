import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpServiceService } from '../../../services/http-service.service';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-my-assessments',
  templateUrl: './my-assessments.component.html',
  styleUrl: './my-assessments.component.scss'
})
export class MyAssessmentsComponent {
  title = "Assessments"
  systemUser: any
  assId: any
  managerId: any
  assessment: any
  pageAssessment: any
  managerName: any

  constructor(private route: ActivatedRoute, private router: Router,
    private server: HttpServiceService,
   private http: HttpClient){}
  ngOnInit(){
    this.systemUser = JSON.parse(sessionStorage.getItem("user"))
    this.route.params.subscribe(params => {
      this.assId = params['id']; // Access the 'id' parameter from the URL
      console.log('Test ID:', this.assId);
    });

    if(this.systemUser.user.role.id == 1){
      this.managerId = this.systemUser.user.userId
      this.managerName = this.systemUser.user.userFullName
    }else{
      this.managerId = this.systemUser.user.manager.userId
      this.managerName = this.systemUser.user.manager.userFullName
    }

    this.getAssessment(this.assId)
    this.getPageAssessment(this.assId, this.managerId)

    //console.log("assessment",this.assessment)
    //console.log("page assessment",this.pageAssessment)
  }
  openDialog(){}

  dateOf(date: any):any{
    console.log(date)
    if(date != null && date.length == 3){
      const year = date[0]
      const month = date[1]
      const day = date[2]
  
      const correctDate = `${day}/${month}/${year}`
      return correctDate
    }
    return `00/00/00`
  }

  getAssessment(assId: any) {
    const url = `${this.server.serverUrl}getAssess?assId=${assId}`
    const response = this.http.get(url)
    response.subscribe(
      (value: any) => {
        
        if(value != null){
          if(value.item != null){
            this.assessment = value.item
            console.log("assessment",this.assessment)
          }
        }
        
      },
      (error: any) => {
        console.log("assessment error",error)

      }
    )
  }

  getPageAssessment(assId: any, manId: any) {
    const url = `${this.server.serverUrl}getPageAssessment?assId=${assId}&manId=${manId}`
    const response = this.http.get(url)
    response.subscribe(
      (value: any) => {
        
        if(value != null){
          if(value.item != null){
            this.pageAssessment = value.item
            console.log("page assessment",this.pageAssessment)
          }
        }
        
      },
      (error: any) => {
        console.log("page assessment error",this.assessment)

      }
    )
  }

  goBack(){
    this.router.navigate(['/singleAtt',this.assId ])
  }

}


