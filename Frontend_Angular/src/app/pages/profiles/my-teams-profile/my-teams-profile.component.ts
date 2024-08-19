import { Component } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { HttpServiceService } from '../../../services/http-service.service';
import {MatPaginatorModule} from '@angular/material/paginator';
import { Router } from '@angular/router';
import { CreateEmployeeComponent } from '../../../create-employee/create-employee.component';

@Component({
  selector: 'app-my-teams-profile',
  templateUrl: './my-teams-profile.component.html',
  styleUrl: './my-teams-profile.component.scss'
  
})
export class MyTeamsProfileComponent {

  managerEmployees:any
  randomNumber = Math.floor(10000 + Math.random() * 9000);
  dialogref: any;
  data: any;
  authUser:any;
  employees:any;
  noOfEmployees:number;
  selectedFile: File;
  uploading: boolean = false;
  
  constructor(
    public http: HttpServiceService,
    private dialog: MatDialog,
    private snack: MatSnackBar,
    private router:Router
  ){}
 
   ngOnInit(): void {
     //Called after the constructor, initializing input properties, and the first call to ngOnChanges.
     //Add 'implements OnInit' to the class.
     const user = localStorage.getItem("user")
    if (user) {
      this.authUser = JSON.parse(user)
    }

    this.getAllEmployees()
    
  
   }

   onFileSelected(event: any): void {
    this.selectedFile = event.target.files[0];
}

  onUpload(): void {
    this.uploading = true
      if (!this.selectedFile) {
          console.error('No file selected.');
          return;
      }
      const formData = new FormData();
      formData.append('file', this.selectedFile);

      this.http.uploadEmployees(formData, this.authUser.user.userId).subscribe(
          response => {
              console.log('File uploaded successfully:', response);
              // Handle success
              this.uploading = false
              this.getAllEmployees();
          },
          error => {
              console.error('Error uploading file:', error);
              // Handle error
              this.uploading = false
          }
      );
  }

   getAllEmployees(){
    this.http.getAllEmployees(this.authUser.user.userId).subscribe(
      ((res) =>{
        this.employees = res.item;
        console.log("emp", this.employees);
        
        this.noOfEmployees = res?.item?.length;
      }),
    ((error) =>{
      this.snack.open(error.error.message, "Close", {duration:3600})
    }),
    () => {
      const empData = []
      const item = this.employees.forEach(e => {
        const data = {
          ...e,
          pfNo:Math. floor(10000 + Math.random() * 9000)
        }
        
        empData.push(data)
        
      })
  
      this.managerEmployees = empData
    }

    )
}

navigateToUserProfile(userId: string) {
  this.router.navigate(['/profile'], { queryParams: { userId: userId } });
}

openCreateEmployeeDialogue():void{
  const dialogRef:MatDialogRef<CreateEmployeeComponent> = this.dialog.open(CreateEmployeeComponent, {
    width:"40%",
    height:"80%",
  });
  
  // Method to get data to udate dynamically when the dialog is closed
  dialogRef.afterClosed().subscribe( 
    ((result) =>{
      this.getAllEmployees()
    })
  )
  
}
   
}
 
  



