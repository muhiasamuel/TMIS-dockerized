
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
  receivedAssessments: any[] = [];

  onAssessmentsChange(assessments: any[]): void {
    this.receivedAssessments = assessments;
    console.log('Assessments received from child:', this.receivedAssessments);
  }
  authUser:any;
  employees:any;
  noOfEmployees:number;
  criticalSkills:any[] = [];
  items:any[] = [];
  noOfCriticalSkills:any;
  permission: any;
  constructor(
    public http: HttpServiceService,
    private dialog: MatDialog,
    private snack: MatSnackBar
  ){}

  ngOnInit(): void {
    const user = sessionStorage.getItem("user")
    if (user) {
    
      
      this.authUser = JSON.parse(user)
      console.log("user", this.authUser);
    }

    this.getAllEmployees()
    this.getAllCriticalSkills()
    //this.getAllCriticalRoles()
    this.categorizePermissions(this.authUser.permissions)
  }
 categorizePermissions(permissions) {
    const categorized = {};
    const permissionsMap = {};


    permissions.forEach(permission => {
      this.permission = permission
        const { resource } = permission;
        permissionsMap[permission.permissionName] = permission;

        
        if (!categorized[resource]) {
            categorized[resource] = [];
        }
        
        categorized[resource].push(permission);
    });
    this.permission = permissionsMap
    console.log(this.permission);
    

    return categorized;
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
      sessionStorage.setItem("criticalSkills", JSON.stringify(res.item))
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
//       sessionStorage.setItem("criticalRoles", JSON.stringify(res.item))
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
