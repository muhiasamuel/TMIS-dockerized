import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpServiceService } from '../../../services/http-service.service';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-transfer-employee',
  templateUrl: './transfer-employee.component.html',
  styleUrl: './transfer-employee.component.scss'
})
export class TransferEmployeeComponent {

  editForm: FormGroup

 constructor( 
  private fb:FormBuilder, 
  private http:HttpServiceService,
  public dialogRef: MatDialogRef<TransferEmployeeComponent>,
  @Inject(MAT_DIALOG_DATA) public data: any
){}

ngOnInit():void{
 this.editForm = this.fb.group({
  userFullName: [this.data.userFullName, Validators.required],
      pf: [this.data.pf, Validators.required],
      username: [this.data.username, Validators.required],
      email: [this.data.email, [Validators.required, Validators.email]],
      roleId: [this.data.roleId, Validators.required],
      departmentId: [this.data.departmentId, Validators.required],
      positionId: [this.data.positionId, Validators.required],
      locked: [this.data.locked],
      enabled: [this.data.enabled]
 })
}
onSave():void{
  if(this.editForm.valid){
    const updatedData = this.editForm.value;

  }
}
onCancel(): void {
  this.dialogRef.close();
}
}
