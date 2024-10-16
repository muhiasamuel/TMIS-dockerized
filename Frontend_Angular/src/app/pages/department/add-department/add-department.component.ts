import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import moment from 'moment';
import { HttpServiceService } from '../../../services/http-service.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-add-department',
  templateUrl: './add-department.component.html',
  styleUrls: ['./add-department.component.scss']
})
export class AddDepartmentComponent implements OnInit {

  departmentForm: FormGroup;
  data: any;  
  authUser: any;
  isSubmitting: boolean = false;

  constructor(
    private fb: FormBuilder,
    private http: HttpServiceService,
    private snackbar: MatSnackBar,
    public dialogRef: MatDialogRef<AddDepartmentComponent>
  ) {
    this.departmentForm = this.fb.group({
      depName: [''],
      positionList: this.fb.array([])

    });
  }

  ngOnInit() {
    const user = localStorage.getItem("user");
    if (user) {
      this.authUser = JSON.parse(user);
    }
   // this.addDepartment(); // Initialize with one department
  }

  // Getter for departments FormArray
  // departments(): FormArray {
  //   return this.departmentForm.get('departments') as FormArray;
  // }

  // Create a new department FormGroup
  // newDepart(): FormGroup {
  //   return this.fb.group({
  //     departmentName: ['', Validators.required],
  //     createdAt: [moment().local()]
  //   });
  // }

  // Create a new position FormGroup
  newPosition(): FormGroup {
    return this.fb.group({
      pname: ['', Validators.required]
    });
  }

  // Add a new department
  // addDepartment() {
  //   this.departments().push(this.newDepart());
  // }

  // // Remove a department
  // removeDepartment(index: number) {
  //   this.departments().removeAt(index);
  // }

  // Add a new position to a specific department
  addPosition() {
    this.getPositionList().push(this.newPosition())
  }

  // Remove a position from a specific department
  removePosition(index: number) {
   this.getPositionList().removeAt(index)
   
  }

  getPositionList(): FormArray {
    return this.departmentForm.get('positionList') as FormArray;
  }

  // Handle form submission
  onSubmit() {
    console.log('Form Valiggggggg',this.departmentForm);
    // console.log('Form Valid:', this.departmentForm.valid);
    console.log('Form Errors:', this.departmentForm.errors);
    this.isSubmitting = true;
    if (!this.departmentForm.valid) {

      window.alert("Please fill out all fields");
      this.isSubmitting = false;
      return;
    }

    const formValue = this.departmentForm.value;
    const departmentsData = {
      depName: formValue.depName,
      positionList: formValue.positionList.map(position => ({
        pname: position.pname
      }))
    };

    console.log("gracii", departmentsData);
    
    this.http.createDepartment(departmentsData).subscribe({
      next: (res) => {
        console.log('Success', res);
        this.snackbar.open('Department added successfully', 'Close', { duration: 2000, verticalPosition: 'top', panelClass: ['custom-snackbar'] });
        this.dialogRef.close();
      },
      error: (err) => {
        console.error('Error', err);
        this.isSubmitting = false;
      },
      complete: () => {
        this.isSubmitting = false;
      }
    });
  }
}
