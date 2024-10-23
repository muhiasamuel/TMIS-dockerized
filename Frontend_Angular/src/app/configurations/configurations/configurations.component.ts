import { Component, ViewChild } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { CreateEmployeeComponent } from '../../create-employee/create-employee.component';
import { UserDetailsComponent } from '../user-details/user-details.component';

@Component({
  selector: 'app-configurations',
  templateUrl: './configurations.component.html',
  styleUrls: ['./configurations.component.scss']
})
export class ConfigurationsComponent {
  showButtons: boolean = false; // Property to track button visibility
  @ViewChild(UserDetailsComponent) childComponent!: UserDetailsComponent; // Get reference to the child

  employeesData: any[] = [];

  constructor(    private dialog: MatDialog){}
  toggleButtons(): void {
    this.showButtons = !this.showButtons; // Toggle button visibility
  }

  handleEmployeesData(data: any[]) {
    this.employeesData = data; // Capture the data emitted by the child
    console.log('Received data from child:', this.employeesData);
  }

  addEmployee(role:string){
    this.showButtons = false
    const dialogRef: MatDialogRef<CreateEmployeeComponent> = this.dialog.open(CreateEmployeeComponent, {
      width: '48%',
      height: '60%',
      data:{
        role,
        managers:this.employeesData
      }
    });
  }
}
