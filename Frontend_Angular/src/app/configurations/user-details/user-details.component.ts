import { Component, ViewChild, AfterViewInit } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { HttpServiceService } from '../../services/http-service.service';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmPromptComponent } from '../../pages/confirm-prompt/confirm-prompt.component';
import { FormControl } from '@angular/forms';
import { map, Observable, startWith } from 'rxjs';

@Component({
  selector: 'app-user-details',
  templateUrl: './user-details.component.html',
  styleUrls: ['./user-details.component.scss']
})
export class UserDetailsComponent {
openDialogBox() {
throw new Error('Method not implemented.');
}
  dataSource: MatTableDataSource<any>;
  displayedColumns: string[] = ['userId', 'userFullName', 'userEmail', 'roleName', 'departmentName', 'managerName', 'isEnabled', 'isLocked', 'transfer'];
  status:string="";
  globalFilterValue: string = '';
  departmentFilterValue: string = '';
  managerFilterValue: string = '';
  roleNameFilterValue: string = '';
  editingUserId: number | null = null; // To keep track of which user is being edited
  employeesData:any;
  stateCtrl = new FormControl('');
  managerId:any;
  filteredStates: Observable<any[]>;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private http: HttpServiceService, private dialog: MatDialog) {
    this.filteredStates = this.stateCtrl.valueChanges.pipe(
      startWith(''),
      map(employee => (employee ? this._filterManagers(employee) : this.employeesData.slice())),
    );

      }

  ngOnInit(): void {
    this.displayUser();
  }
  private _filterManagers(value: string): any[] {
    console.log("val", value);
    
    this.managerId = this.employeesData.filter((item) => item.pf === value)[0]

    console.log(this.managerId);
    
    const filterValue = value.toLowerCase();
    return this.employeesData?.filter(manager =>
      manager.pf.toLowerCase().includes(filterValue) || 
      manager.userFullName.toLowerCase().includes(filterValue)
    );
    
  }

  displayUser(): void {
    this.http.getUserDetails().subscribe(
      (res) => {
        
        this.employeesData = res.item.filter((employee) => employee.roleName === "TopManager")
        this.dataSource = new MatTableDataSource(res.item);  // Wrap res.item in MatTableDataSource
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
        this.applyFilter(); // Call applyFilter to initialize filtering
        console.log('response', res);
      },
      (error) => {

        console.error('Error fetching user details:', error);
      }
    );
  }


  editUser(userId: number): void {
    this.editingUserId = userId; // Set the user ID to be edited
  }

  saveChanges(element: any): void {

    if (element.roleName === "TopManager") {
      const url = `${this.http.serverUrl}users/management/transfer-manager/${element.userId}/manager/${this.managerId.userId}`
      this.http.patchData(url,{}).subscribe(
        ((res) => {
          alert(res.message)
          this.ngOnInit()
        })
      )
    }else{
      const url = `${this.http.serverUrl}users/management/transfer/${element.userId}/manager/${this.managerId.userId}`
      this.http.patchData(url,{}).subscribe(
        ((res) => {
          alert(res.message)
          this.ngOnInit()
        })
      )
    }
    
    console.log('New Role Name:', element.userId, "new Manager", this.stateCtrl); // Do something with the new value

    // Here you can make an HTTP request to save the changes
    // this.http.updateUserRole(element.userId, element.newRoleName).subscribe(/* Handle response */);

    this.editingUserId = null; // Reset the editing user ID
  }

  cancelEdit(): void {
    this.editingUserId = null; // Reset the editing user ID without saving
  }


  applyFilter(): void {
    this.dataSource.filterPredicate = (data: any) => {
        const globalMatch = this.globalFilterValue
            ? Object.values(data).some(value =>
                value.toString().toLowerCase().includes(this.globalFilterValue)
            )
            : true;

        const departmentMatch = this.departmentFilterValue
            ? data.departmentName.toLowerCase().includes(this.departmentFilterValue)
            : true;

        const managerMatch = this.managerFilterValue
            ? data.managerName.toLowerCase().includes(this.managerFilterValue)
            : true;

        const roleNameMatch = this.roleNameFilterValue
            ? data.roleName.toLowerCase().includes(this.roleNameFilterValue)
            : true;

        return globalMatch && departmentMatch && managerMatch && roleNameMatch;
    };

    // Trigger filtering by changing the filter property
    this.dataSource.filter = JSON.stringify({
        global: this.globalFilterValue,
        department: this.departmentFilterValue,
        manager: this.managerFilterValue,
        roleName: this.roleNameFilterValue
    });
}


  onFilterChange(event: KeyboardEvent): void {
    const inputElement = event.target as HTMLInputElement;
    this.globalFilterValue = inputElement.value.trim().toLowerCase();
    this.applyFilter(); // Call applyFilter to update the table
  }

  applyDepartmentFilter(event: KeyboardEvent): void {
    const inputElement = event.target as HTMLInputElement;
    this.departmentFilterValue = inputElement.value.trim().toLowerCase();
    this.applyFilter(); // Call applyFilter to update the table
  }

  applyManagerFilter(event: KeyboardEvent): void {
    const inputElement = event.target as HTMLInputElement;
    this.managerFilterValue = inputElement.value.trim().toLowerCase();
    this.applyFilter(); // Call applyFilter to update the table
  }

  applyRoleNameFilter(event: KeyboardEvent): void {
    const inputElement = event.target as HTMLInputElement;
    this.roleNameFilterValue = inputElement.value.trim().toLowerCase();
    this.applyFilter(); // Call applyFilter to update the table
  }
  


  toggleEnabled(element: any){
    if (element.isEnabled === false) {
      this.status = "enable"
    } else {
      this.status = "disable"
    }

    const dialogRef = this.dialog.open(ConfirmPromptComponent, {
      width: '500px',
      data: `Are you sure you want to ${this.status}? ${element.userFullName} account ??`
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        console.log('eeee',element);
    
        if (element.isEnabled === false) {
          this.http.enableUserAcc(element.userId).subscribe(
            ((res)=>{
              this.ngOnInit()
              console.log(res);
              
            })
          )
        }else{
          this.http.disableUserAcc(element.userId).subscribe(
            ((res)=>{
              this.ngOnInit()
              console.log(res);
              
            })
          )
        }
      }
      else{

      }})
    

  }
  toggleLocked(element: any){

    if (element.isLocked === false) {
      this.status = "Lock"
    } else {
      this.status = "Unlock"
    }
    const dialogRef = this.dialog.open(ConfirmPromptComponent, {
      width: '500px',
      data: `Are you sure you want to ${this.status} ${element.userFullName} account ??`
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        // User clicked OK
        if (element.isLocked === false) {
          this.http.lockUserAcc(element.userId).subscribe(
            ((res) => {
              this.ngOnInit()
              console.log(res);
              
            })
          )
        }else{
          this.http.unlockUseracc(element.userId).subscribe(
            ((res) => {
              this.ngOnInit()
              console.log(res);
              
            })
          )
        }
      } else {
        // User clicked Cancel
        console.log('Action cancelled');
      }
    });
    console.log("gg", element);

  }

  //approve/cancel action
    // Method to open the confirmation dialog
    openConfirmDialog(action: string) {
      
    }
}
