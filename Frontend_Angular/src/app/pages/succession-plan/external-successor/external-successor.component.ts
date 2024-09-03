import { HttpClient } from '@angular/common/http';
import { Component, Inject } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-external-successor',
  templateUrl: './external-successor.component.html',
  styleUrl: './external-successor.component.scss'
})
export class ExternalSuccessorComponent {

  externalSuccessorForm: FormGroup;

  constructor(
    public dialogRef: MatDialogRef<ExternalSuccessorComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private formBuilder: FormBuilder
  ) {
    this.externalSuccessorForm = this.formBuilder.group({
      name: [data?.name || ''],
      contactInfo: [data?.contactInfo || ''],
      currentPosition: [data?.currentPosition || ''],
      currentCompany: [data?.currentCompany || ''],
      reasonForSelection: [data?.reasonForSelection || ''],
      expectedStartDate: [data?.expectedStartDate || ''],
    });
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  onSaveClick(): void {
    this.dialogRef.close(this.externalSuccessorForm.value);
  }
}

