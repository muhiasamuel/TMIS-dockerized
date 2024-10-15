import { HttpClient } from '@angular/common/http';
import { Component, Input, SimpleChanges } from '@angular/core';
import { HttpServiceService } from '../../../services/http-service.service';

@Component({
  selector: 'app-user-done-assessments',
  templateUrl: './user-done-assessments.component.html',
  styleUrl: './user-done-assessments.component.scss'
})
export class UserDoneAssessmentsComponent {
  @Input() data: any
  title = "Manager Assess" 
  status:any
  assessments: any[] = []
  assess: any[] = []
  systemUser: any
  message = ""
  isloading: boolean = false;
  constructor(private server: HttpServiceService, private http: HttpClient){}

  openDialog(){}

  ngOnInit(){
    this.systemUser = JSON.parse(sessionStorage.getItem("user"))
    console.log(this.systemUser)
    console.log("data being initialised",this.data)

    this.status = this.data
   this.getAssements(this.data)
   if(this.assess.length < 1){
      this.message = "you have not done any assessments"

   }

  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['data'] && changes['data'].previousValue != undefined) {
      this.data = changes['data'].currentValue
      console.log("changes",this.data)

      this.status = this.data
      this.getAssements(changes['data'].currentValue)
    }else{
      this.data = 'Done'
    }
  }

  getAssements(data: any){
    this.isloading = true;
    let url = ``
    
     if(data == "Done"){
      this.message = "you have not attempted any assessment"
      this.status = data
      url = `${this.server.serverUrl}getUserDoneAssessment?empId=${this.systemUser.user.userId}&manId=${this.systemUser.user.manager.userId}`
    }else if(data == "Not Done"){
      this.message = "there are no assessments ready. please wait."
      this.status = data
      url = `${this.server.serverUrl}getNotDoneEmpAssess?empId=${this.systemUser.user.userId}&manId=${this.systemUser.user.manager.userId}`
    }else if(data == "AssessedEmp"){
      this.message = "you have no assessed assessments"
      this.status = data
      url = `${this.server.serverUrl}getUserAssessedAssessments?manId=${this.systemUser.user.manager.userId}&empId=${this.systemUser.user.userId}`
    }
    
    const response = this.http.get(url);
    response.subscribe(
      (value: any) => {
        this.isloading = false;
        this.assess = []
        if(value != null){
          if(value.item != null){
            this.assess = value.item;
            console.log("value",this.assess)
          }
        }
        console.log("current assess",this.assess)
      },
      (error: any) => {
        this.isloading = false;
        console.error();
        (error)
      }
    )
  }

}
