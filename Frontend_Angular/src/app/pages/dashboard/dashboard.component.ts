
import { Component, OnInit } from '@angular/core';
import Chart from 'chart.js/auto';
import { HttpServiceService } from '../../services/http-service.service';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { EmployeesListingComponent } from '../employees-listing/employees-listing.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SkillsAssessmentComponent } from '../skills-assessment/skills-assessment.component';



@Component({
    moduleId: module.id,
    templateUrl:'./dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})

export class DashboardComponent implements OnInit{
  authUser:any;
  employees:any;
  noOfEmployees:number;
  criticalSkills:any[] = [];
  items:any[] = [];
  noOfCriticalSkills:any;
  constructor(
    public http: HttpServiceService,
    private dialog: MatDialog,
    private snack: MatSnackBar
  ){}

  ngOnInit(): void {
    const user = localStorage.getItem("user")
    if (user) {
      this.authUser = JSON.parse(user)
    }

    this.getAllEmployees()
    this.getAllCriticalSkills()
    //this.getAllCriticalRoles()
  }
getAllEmployees(){
    this.http.getAllEmployees(this.authUser.user.userId).subscribe(
      ((res) =>{
        this.employees = res.item;
        this.noOfEmployees = res?.item?.length;
      }),
    ((error) =>{
      this.snack.open(error.error.message, "Close", {duration:3600})
    }),
    () => {}
    )
}

//critical skills
getAllCriticalSkills(){
  this.http.getCriticalSkills(this.authUser.user.userId).subscribe(
    ((res)=>{
      console.log("roooooo", res);
      this.criticalSkills = res.item;
      localStorage.setItem("criticalSkills", JSON.stringify(res.item))
      const i = res.item?.filter(c=> c.averageRating >= 3.5)
      this.noOfCriticalSkills = i?.length
      console.log();
      
      
    }),
    ((error)=>{
      this.snack.open(error.error.message, "Close", {duration:3600})
    }),
    () =>{}
  )
}
// //critical roles
// getAllCriticalRoles(){
//   this.http.getCriticalRoles(this.authUser.userId).subscribe(
//     ((res)=>{
//       console.log("critiv", res.item);
      
//       const data = res.item;
//       localStorage.setItem("criticalRoles", JSON.stringify(res.item))
//       const count = res.item.length;
//     }),
//     ((error)=>{
//       this.snack.open(error.error.message, "Close", {duration:3600})
//     }),
//     () =>{}
//   )
// }


employeesDetail(){
  this.dialog.open(EmployeesListingComponent, {
    width:"80%",
    height:"70%",
    position:{
       right:"4em" 
    },
    data:{
      data:this.employees
    }

  })
}
  
}
