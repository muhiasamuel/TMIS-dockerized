import { Component, Inject } from '@angular/core';
import { FormArray, FormBuilder, FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { HttpServiceService } from '../../../services/http-service.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-mvps-assessment',
  templateUrl: './mvps-assessment.component.html',
  styleUrl: './mvps-assessment.component.scss'
})
export class MvpsAssessmentComponent {

  RetentionStrategyFormGroup: FormGroup<any>;
  assessmentData: any;
  employeeId: number;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data,
    private dialogRef: MatDialogRef<MvpsAssessmentComponent>,
    public fb: FormBuilder,
    public http: HttpServiceService,
    public snackBar: MatSnackBar
  ) { }

  get mvpRetentionStrategiesFormArray() {
    return this.RetentionStrategyFormGroup.get('mvpRetentionStrategies') as FormArray;
  }
  ngOnInit(): void {
    //Called after the constructor, initializing input properties, and the first call to ngOnChanges.
    //Add 'implements OnInit' to the class.
    const data = this.data
    if (data) {
      this.assessmentData = data.assessment;
      this.employeeId = data.employeeId
      console.log("assessssssssss", data);
    }
    this.RetentionStrategyFormGroup = this.fb.group({
      // Define other form controls here
      mvpRetentionStrategies: this.fb.array([])
    });
  }

  addStrategy() {
    this.mvpRetentionStrategiesFormArray.push(this.fb.group({
      retentionStrategies: ''
    }));
  }

  removeStrategy(i: number) {
    this.mvpRetentionStrategiesFormArray.removeAt(i)
  }

  submit() {
    const strategies = this.mvpRetentionStrategiesFormArray.controls.map(control => control.value);
    const assessmentUrl = `${this.http.serverUrl}MVPs/assessment/create/${this.employeeId}`
    const strategiesUrl = `${this.http.serverUrl}MVPs/retention-strategies/create/${this.employeeId}`
    if (strategies) {
      this.http.postData(assessmentUrl, this.assessmentData).subscribe(
        ((res) => {
          console.log(res);
          
        }),
        ((error) => {
          this.snackBar.open(error.error.message, "Close", {duration:3600, verticalPosition:"top"})
          console.log("assess err", error);
          
        }),
        () => {
          this.http.postData(strategiesUrl, strategies).subscribe(
            ((res) => {
              console.log(res);
              
            }),
            ((error) => {
              this.snackBar.open(error.error.message, "Close", {duration:3600, verticalPosition:"top"})
              console.log("assess err", error);

            }),
            () => {
              this.snackBar.open("MVPs assessment data saved successfully", "Close", {duration:3600, verticalPosition:"top"})
              this.dialogRef.close()
            }
          )
        }
      )
    } else {
      this.snackBar.open("Strategies appears to be empty" , "Close", {duration:3600, verticalPosition:"top"})
    }

  }

}
