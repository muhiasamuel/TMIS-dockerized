import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { HttpServiceService } from '../services/http-service.service';
import { MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-create-employee',
  templateUrl: './create-employee.component.html',
  styleUrl: './create-employee.component.scss'
})
export class CreateEmployeeComponent {
  createEmployeeForm: FormGroup;
  manager:any;
  managerId: number;
  loading = false; // Variable to track loading state
  positions:any;
  roles: any;
  department: any;

  constructor(
    public dialogRef: MatDialogRef<CreateEmployeeComponent>,
    private snackBar: MatSnackBar,
    private http: HttpClient, private service: HttpServiceService){}

  ngOnInit(): void{
    const user = sessionStorage.getItem("user")
    if (user) {
      this.manager = JSON.parse(user);
    }

    

    this.createEmployeeForm= new FormGroup({
      userFullName: new FormControl('', Validators.required),
      pf: new FormControl('', Validators.required),
      username: new FormControl('', Validators.required),
      email: new FormControl('', [Validators.required, Validators.email]),
      roleId: new FormControl(0, Validators.required),
      positionId: new FormControl(0, Validators.required),
      departmentId: new FormControl(0, Validators.required)
    });

    this.getDepartments();
    this.getRole();
  }

  getRole(): void {
    const url = `${this.service.serverUrl}roles/getRoles`;
    this.http.get<{item: {id: number, roleName: string} []}>(url).subscribe(
      response =>{
        this.roles =response.item;
        console.log("qwert", response.item);
        
      },
      error =>{
        console.error('Error fetching roles',error);
      }
    );
  }

  getPosition() {

    console.log("form", this.createEmployeeForm.value.department);
    const positions = this.department.filter(item => item.depId === this.createEmployeeForm.value.departmentId)
    this.positions = positions[0].departmentPositions
    console.log("positions", this.positions);
    
   
  }
  getDepartments(){
    this.service.getDepartments().subscribe(
      ((res) =>{
        this.department=res.item
      }),
      ((error) =>{
        console.error('Error fetching departments',error);
        
      }),
      ()=>{}
    )
  }


  onSubmit(): void{
      this.loading = true; 
      const data = {
        ...this.createEmployeeForm.value,
        "locked": false,
        "enabled": true
      }

      console.log("data",data);
      
      const url=`${this.service.serverUrl}users/create-employee/${this.manager.user.userId}`;
      this.http.post(url, data).subscribe(
        response =>{
          console.log('employee created succesfully',response); 
          this.loading = false;
          this.dialogRef.close() 

            // Show success snackbar
      this.snackBar.open("Employee created successfully!", "Close", {
        duration: 3000, // 3 seconds
        verticalPosition: "bottom",
        horizontalPosition: "center",
      });
        },
        error =>{
          console.error('There was an error!',error)
          this.loading = false; 

           // Show error snackbar
      this.snackBar.open("Error creating employee. Please try again.", "Close", {
        duration: 3000, // 3 seconds
        verticalPosition: "bottom",
        horizontalPosition: "center",
      });

        }
      );
    
  }

  closeDialog(): void {
    this.dialogRef.close();
  }
}
