import { Component, Input, SimpleChanges } from '@angular/core';
import { HttpServiceService } from '../../services/http-service.service';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-intro-pge',
  templateUrl: './intro-pge.component.html',
  styleUrl: './intro-pge.component.scss'
})
export class IntroPgeComponent {

  @Input() data: any
  title = "Manager Assess" 
  status:any
  assessments: any[] = []
  assess: any[] = []
  systemUser: any
  message = ""
  constructor(private server: HttpServiceService, private http: HttpClient){}

  openDialog(){}

  ngOnInit(){
    this.systemUser = JSON.parse(localStorage.getItem("user"))
    //console.log(this.systemUser)
    //console.log(this.data)

    this.status = this.data
   this.getAssements()
   if(this.assess.length < 1){
    if(this.data == "UnAssessedMan"){
      this.message = "you currently dont have un-assessed employees"

    }else{
    this.message = "you currently dont have any assessed employees"

    }
   }

  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['data'] && changes['data'].previousValue != undefined) {
      this.data = changes['data'].currentValue
      console.log("changes",this.data)

      this.status = this.data
      if(this.assess.length < 1){
        if(this.data == "UnAssessedMan"){
          this.message = "you currently dont have un-assessed employees"
    
        }else{
        this.message = "you currently dont have any assessed employees"
    
        }
       }
      //this.ngOnInit()
    }else{
        this.data = 'UnAssessedMan'

    }
   this.getAssements()

  }

  getAssements(){
    let url = ``
    if(this.data == "UnassessedMan"){
      this.message = "you currently don't have anyone to assess"
      url = `${this.server.serverUrl}getManagerUnAssessedAssessments?manId=${this.systemUser.user.userId}`

    }else if(this.data == "AssessedMan"){
      this.message = "you have not assessed anyone"
      url = `${this.server.serverUrl}getManagerAssessedAssessments?manId=${this.systemUser.user.userId}`
    }
    
    const response = this.http.get(url);
    response.subscribe(
      (value: any) => {
        this.assess = []
        console.log("value",value)
        if(value != null){
          if(value.item != null){
            this.assess = value.item;
            console.log("assess",this.assess)
          }
        }
      },
      (error: any) => {
        console.error(error)
      }
    )
  }


}
