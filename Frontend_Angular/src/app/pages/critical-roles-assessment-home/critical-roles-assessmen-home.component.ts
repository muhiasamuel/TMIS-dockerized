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
import { Router } from '@angular/router';
import { Observable, tap, forkJoin } from 'rxjs';

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
succeededRoles:any;
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
    "Actions",
   "SuccessionStatus",
   "ActionsOnSuccession"
];

dataSource: MatTableDataSource<UserData>;

@ViewChild(MatPaginator) paginator: MatPaginator;
@ViewChild(MatSort) sort: MatSort;
  constructor(private dialog : MatDialog,
    private router: Router,
     private http: HttpServiceService, private snack: MatSnackBar){}
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
//check roles for succession
getRolesSuccessionStatus(): Observable<any> {
  return this.http.checkRolesSuccessionStatus().pipe(
    tap((res) => {
      this.succeededRoles = res.item;
      console.log("Roles fetched:", res.item);
    })
  );
}

// Get manager added critical roles
getManagerCriticalRoles() {
  // Use forkJoin to wait until both observables complete
  forkJoin([
    this.getRolesSuccessionStatus(),
    this.http.getCriticalRoles(this.authUser.user.userId)
  ]).subscribe(
    ([rolesStatusResponse, criticalRolesResponse]) => {
      // Ensure criticalRolesResponse.item is defined and an array before proceeding
      if (!criticalRolesResponse || !criticalRolesResponse.item || !Array.isArray(criticalRolesResponse.item)) {
        console.error('Critical roles response is not valid:', criticalRolesResponse);
        this.snack.open('No roles found', 'Close', { duration: 3600 });
        return;
      }

      const sortedRoles = criticalRolesResponse.item.sort((a, b) => b.criticalRoleId - a.criticalRoleId);

      let critical: any[] = [];
      let medium: any[] = [];
      let low: any[] = [];
      let items: any[] = [];

      // Create a map of roleId and its data (successionStatus and planId)
      const roleSuccessionMap = new Map<number, { successionStatus: string, planId: number }>();
      if (this.succeededRoles) {
        this.succeededRoles.forEach((role: any) => {
          roleSuccessionMap.set(role.roleId, { successionStatus: role.successionStatus, planId: role.planId });
        });
      } else {
        console.error('succeededRoles is not defined or empty');
      }

      // Iterate through sorted critical roles and match with the roleSuccessionMap
      sortedRoles.forEach((element) => {
        let roleData = roleSuccessionMap.get(element.criticalRoleId);
        let successionStatus = roleData ? roleData.successionStatus : 'not mapped';
        let planId = roleData ? roleData.planId : null;

        if (element.averageRating >= 3.5) {
          element = {
            ...element,
            levelClassification: 'critical',
            successionStatus: successionStatus,
            planId: planId
          };
          critical.push(element);
        } else if (element.averageRating >= 2.5 && element.averageRating < 3.5) {
          element = {
            ...element,
            levelClassification: 'medium',
            successionStatus: successionStatus,
            planId: planId
          };
          medium.push(element);
        } else {
          element = {
            ...element,
            levelClassification: 'low',
            successionStatus: successionStatus,
            planId: planId
          };
          low.push(element);
        }

        items.push(element);
      });

      // Update class properties with the new data
      this.criticalLevel = critical;
      this.mediumCritical = medium;
      this.LowCritical = low;
      this.userData = items;

      // Check if critical is non-empty before setting dataSource
      if (critical.length > 0) {
        this.dataSource = new MatTableDataSource(critical);
      } else {
        this.dataSource = new MatTableDataSource([]);
        console.warn('No critical roles found');
      }

      console.log('Processed Items:', items);
    },
    (error) => {
      this.snack.open('An error occurred while getting critical skills', 'Close', { duration: 3600 });
    },
    () => {
      // Ensure dataSource is not undefined before setting paginator and sort
      if (this.dataSource) {
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      } else {
        console.error('DataSource is undefined, unable to set paginator and sort');
      }
    }
  );
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

  onEdit(row: any): void {
    // Logic to handle editing a succession plan
    console.log('Edit Succession Plan for:', row);
    this.router.navigate(['/view/plan'], { queryParams: { planId: row } });
  }
  
  onView(row: any): void {
    // Logic to handle viewing the details of a succession plan
    this.router.navigate(['/view/plan'], { queryParams: { planId: row } });
  }
  
  onAdd(row: any): void {
    // Logic to handle adding a new succession plan
    console.log('Add Succession Plan for:', row);
  }
  
}
 