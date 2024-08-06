import { Component } from '@angular/core';
import { AttributesComponent } from '../attributes/attributes.component';
import { ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { MatDialog } from '@angular/material/dialog';
import { HttpServiceService } from '../../services/http-service.service';
import { AddPotentialDescriptorComponent } from '../add-potential-descriptor/add-potential-descriptor.component';
import { AddAssessmentQuestionsComponent } from '../add-assessment-questions/add-assessment-questions.component';

@Component({
  selector: 'app-single-attribute',
  templateUrl: './single-attribute.component.html',
  styleUrl: './single-attribute.component.scss'
})
export class SingleAttributeComponent {
  attribute = {
    "name":"",
    "description":""
  }
  systemUser: any
  pAttsAssess: any[] = []
  currentAtt: any
  att: any
  atts: any
  title: any
  attId:any

  constructor(private dialog: MatDialog,
     private route: ActivatedRoute, 
     private server: HttpServiceService,
    private http: HttpClient){
    //this.getUserId()
    
  }
  ngOnInit(){
    this.systemUser = JSON.parse(localStorage.getItem("user"))
    this.route.params.subscribe(params => {
      this.attId = params['id']; // Access the 'id' parameter from the URL
      console.log('Test ID:', this.attId);
    });
    this.getAssessments(this.attId);
  }

  openDialog(){
  

    this.dialog.open(AddAssessmentQuestionsComponent ,{
      position: {
        
        right: '3em' 
      },
      width:"85%",
      height: '85%' })
  }

  getAssessments(attId:any){
    const url = `${this.server.serverUrl}getAttribute?attId=${attId}`
    console.log(url)
    const res = this.http.get(url)

    res.subscribe(
      (data:any )=> {
        this.attribute.name = data.item.potentialAttributeName
        this.attribute.description = data.item.potentialAttributeDescription
        this.pAttsAssess = data.item.assessments
        this.title = `${data.item.potentialAttributeName} assessments`
        console.log("Attribute questions",data)
      },
      (error: any) =>{
        console.log("Attribute questions errors",error)
      }
    )
  }

}
