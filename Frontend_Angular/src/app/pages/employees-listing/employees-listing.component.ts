import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-employees-listing',
  templateUrl: './employees-listing.component.html',
  styleUrl: './employees-listing.component.scss'
})
export class EmployeesListingComponent {

  managerEmployees:any
 randomNumber = Math.floor(10000 + Math.random() * 9000);
  constructor(private dialogref: MatDialogRef<EmployeesListingComponent>, 
    @Inject (MAT_DIALOG_DATA) public data
  ){}

  ngOnInit(): void {
    //Called after the constructor, initializing input properties, and the first call to ngOnChanges.
    //Add 'implements OnInit' to the class.
    const employees = this.data.data
    console.log(employees);
    if (employees) {
      
   
    const empData = []
    const item = employees.forEach(e => {
      const data = {
        ...e,
        pfNo:Math. floor(10000 + Math.random() * 9000)
      }
      
      empData.push(data)
      
    })

    this.managerEmployees = empData
  }
  }

  dialogClose(){
    this.dialogref.close()
  }

}
