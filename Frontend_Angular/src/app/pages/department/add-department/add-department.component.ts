import { Component } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import moment from 'moment';
import { HttpServiceService } from '../../../services/http-service.service';
import { MatSnackBar, MatSnackBarAction,MatSnackBarActions,  MatSnackBarLabel,  MatSnackBarRef, } from '@angular/material/snack-bar';
import { MatDialogRef } from '@angular/material/dialog';



@Component({
  selector: 'app-add-department',
  templateUrl: './add-department.component.html',
  styleUrl: './add-department.component.scss'
})
export class AddDepartmentComponent {

  departmentForm: FormGroup
  data:any;  
  authUser:any;
  isSubmitting:boolean = false;

  constructor(private fb:FormBuilder, private http: HttpServiceService,private snackbar:MatSnackBar,public dialogRef: MatDialogRef< AddDepartmentComponent>){
    this.departmentForm = this.fb.group({ 
      departments: this.fb.array([]) ,  
    });  
  }
  


  ngOnInit(){
    const user = localStorage.getItem("user")
    if (user) {
      this.authUser = JSON.parse(user);
    }


  }

  departments() : FormArray {  
    return this.departmentForm.get("departments") as FormArray  
  }  

     
  newDepart(): FormGroup {  
    return this.fb.group({  
      departmentName: '',   
      positionName: '',  
      createdAt:moment().local()
    })  
  }  
  addDepartment() {  
    this.departments().push(this.newDepart());  
  }  
     
  
  removeDepartment(i:number) {  
    this.departments().removeAt(i);  
  }  
  onSubmit(){
    this.isSubmitting = true;
    if (!this.departmentForm.value.departments) {
      window.alert("please fill out all fields")
    }else{
      console.log("rooooooo",this.departmentForm.value.departments[0].departmentName)
      this.data = {
        depName: this.departmentForm.value.departments[0].departmentName,
        positionList: [
          {
            pname: this.departmentForm.value.departments[0].positionName
          }
        ]
      }
    
    //   console.log("january", this.data)
    //   this.http.createDepartment(this.data).subscribe((res) =>{
    //     console.log('december',res)
    //   },
    //    (err)=> {
    //     console.error("november",err)
    //    })
    
    // }}
    console.log("january", this.data)
      this.http.createDepartment(this.data).subscribe({
      next:(res)=>{
        console.log('december',res)
        this.isSubmitting= false;
      },
      error:(err)=> {
            console.error("november",err)
            this.isSubmitting=false;
   },
   complete:()=>{
    this.dialogRef.close()
    this.snackbar.open('Department added successfully', 'close', {duration:2000,
  verticalPosition: 'top', 
  panelClass: ['custom-snackbar'] 
})
   }

      })
    
    }}

   


  }
