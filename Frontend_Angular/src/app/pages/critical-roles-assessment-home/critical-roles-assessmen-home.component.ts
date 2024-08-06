import { Dialog } from '@angular/cdk/dialog';
import { Component, ViewChild } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { CriticalRolesAssessmentComponent } from './critical-roles-assessment/critical-roles-assessment.component';
import { HttpServiceService } from '../../services/http-service.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { log } from 'console';
import { ViewCriticalRoleComponent } from './view-critical-role/view-critical-role.component';
import { ViewDialogComponent } from '../asess-my-team/components/view-dialog/view-dialog.component';
import { EditCriticalRoleComponent } from './edit-critical-role/edit-critical-role.component';
import { AddStrategiesComponent } from './add-strategies/add-strategies.component';

export interface UserData {
  roleName:string
  currentState: string,
  averageRating: string,
  levelClassification:string,

}
@Component({
  selector: 'app-critical-roles-assessmen-home',
  templateUrl: './critical-roles-assessmen-home.component.html',
  styleUrl: './critical-roles-assessmen-home.component.scss'
})
export class CriticalRolesAssessmenHomeComponent {

crititicalRoles:any;
mediumCritical:any;
LowCritical:any;
criticalLevel:any;
  authUser:any;
  userData:any[] = []
  data:any;
  displayedColumns: string[] = [
    'index',
    "roleName",
    "averageRating",
    "levelClassification",
    "currentState",
    "Actions"
   
];

dataSource: MatTableDataSource<UserData>;

@ViewChild(MatPaginator) paginator: MatPaginator;
@ViewChild(MatSort) sort: MatSort;
  constructor(private dialog : MatDialog, private http: HttpServiceService, private snack: MatSnackBar){}
  addCriticalRoles(){
   const dialogRef:MatDialogRef<CriticalRolesAssessmentComponent> = this.dialog.open(CriticalRolesAssessmentComponent,{
      width:"90%",
      height:"90%",
      position:{
        right:"25px",
        bottom:"20px"
      }

    })
    dialogRef.afterClosed().subscribe(
      ((res) =>{
        this.ngOnInit()

      }))
  }

  ngOnInit(): void {
    const user = localStorage.getItem("user");
    if (user) {
      this.authUser = JSON.parse(user)
    }

    
    //Called after the constructor, initializing input properties, and the first call to ngOnChanges.
    //Add 'implements OnInit' to the class.
    this.getManagerCriticalRoles()
  }
  print(item:string) {
    let printContents = document.getElementById(item).innerHTML;
    let originalContents = document.body.innerHTML;
    
    document.body.innerHTML = printContents;
    window.print();
    document.body.innerHTML = originalContents;
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }
  //actions
  onViewClick(data:any) {
    this.dialog.open(ViewCriticalRoleComponent, {
      width:"80%",
      height:"65%",
      position:{
        right:"20px"
      },
      data:{
        criticalRole:data
      }

    })
    }
    onEditClick(row: any) {
     const dialogRef:MatDialogRef<EditCriticalRoleComponent> =  this.dialog.open(EditCriticalRoleComponent, {
        width:"85%",
        height:"90%",
        position:{
          right:"2em",
          bottom:"30px"
        },
        data:{
          role:row
        }
      })

      dialogRef.afterClosed().subscribe(
        ((res) =>{
          this.ngOnInit()
        })
      )
    
    }
    onAddStrategiesClick(row:any) {
    this.dialog.open(AddStrategiesComponent,{
      width:"80%",
      height:"65%",
      position:{
        right:"6em"
      },
      data:{
        role:row
      }
    }

    )
    }

  //get manager added critical roles
getManagerCriticalRoles(){
  this.http.getCriticalRoles(this.authUser.user.userId).subscribe(
    ((response) =>{
      console.log(response);
      const myData = response.item.sort((a, b) => b.criticalRoleId - a.criticalRoleId);
      let critical = []
      let medium: any[] = [];
      let low: any[] = [];
      let items: any[] = []
      const sorted = response.item.sort((a,b) => b.criticalRoleId - a.criticalRoleId)
      if (sorted) {
        const dataSort = sorted.forEach(element =>{       
          if (element.averageRating >= 3.5) {
            this.crititicalRoles = element
            element = {
              ...element,
              levelClassification: "critical"
            }
            critical.push(element)
            this.criticalLevel = "critical"
          } else if(element.averageRating >= 2.5 && element.averageRating < 3.5) {
            element = {
              ...element,
              levelClassification: "medium"
            }
            this.criticalLevel = "medium"
            this.mediumCritical = element
            medium.push(element)
            
          }else{
            this.LowCritical = element
            element = {
              ...element,
              levelClassification: "low"
            }
            low.push(element)
            this.criticalLevel = "low"
          }
          this.data = {
            ...element,
            levelClassification: this.criticalLevel
          }
          items.push(this.data)
        })
      }

      this.criticalLevel = critical
      this.mediumCritical = medium
      this.LowCritical = low
      this.userData = items;
      this.dataSource = new MatTableDataSource(critical);



      console.log("assa", items);
      

    }),
    ((e) =>{
      this.snack.open("An error occured while getting critical skills", "Close", {duration:3600})
    }),
    ()=>{
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
    }
  )
}

//pdf
  downloadPdf(type: string){
    this.http.getCriticalRoleReport(type).subscribe((blob: Blob) => {
      // Create a URL for the blob
      const url = window.URL.createObjectURL(blob);
      // Create a link element
      const a = document.createElement('a');
      // Set the href attribute of the link to the URL of the blob
      a.href = url;
      // Set the download attribute of the link to specify the filename
      a.download = 'report.pdf';
      // Append the link to the body
      document.body.appendChild(a);
      // Click the link to trigger the download
      a.click();
      // Remove the link from the body
      document.body.removeChild(a);
      // Revoke the URL to release the resources
      window.URL.revokeObjectURL(url);
    });
  }

}
 