import { Component, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { HttpServiceService } from '../../services/http-service.service';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrl: './users.component.scss'
})
export class UsersComponent {
  totalAttributes: any[] = []
  employeeName: any
  systemUser:any
  userId:any
  managerId:any
  dataSource: any
  displayedColumns: any[] = ['Potential Attribute', 'userType', 'username', 'userFullName']

  constructor(private route:ActivatedRoute, private server: HttpServiceService, private http: HttpClient){}
  
  ngOnInit(){

    this.route.params.subscribe(params => {
      this.userId = params['id']; // Access the 'id' parameter from the URL
      console.log('Test ID:', this.userId);
    });

      this.systemUser = JSON.parse(localStorage.getItem("user"))
        if(this.systemUser.user.role.id == 1 && this.userId != this.systemUser.user.userId){
            this.managerId = this.systemUser.user.userId
            //console.log(this.systemUser)
        }else if(this.systemUser.user.role.id == 2){
            this.managerId = this.systemUser.user.manager.userId
        }
      console.log('Test ID2:', this.managerId);

    this.getUserIdentificationMatrix(this.userId, this.managerId);
  }


 getUserIdentificationMatrix(userId:any, managerId: any){
  const url = `${this.server.serverUrl}getUserPotentialMatrix?userId=${userId}&managerId=${managerId}`
  const response = this.http.get(url)

  response.subscribe(
    (value: any) => {
      console.log("response",value)

      if(value != null){
        if(value.item != null){
          this.dataSource = value.item.pattributes
          this.totalAttributes = value.item.pattributes
          this.employeeName = value.item.userName
          console.log("response 2",this.dataSource)
        }
      }

    },
    (error: any) => {

    }
  )

 }

}
