import { HttpClient } from '@angular/common/http';
import { Component, inject, OnInit } from '@angular/core';
import { HttpServiceService } from '../../../services/http-service.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';

export interface Answer {
  userId: any
  choiceId: any
  managerId: any
  questionId: any
}

@Component({
  selector: 'app-manager-assess-employee',
  templateUrl: './manager-assess-employee.component.html',
  styleUrl: './manager-assess-employee.component.scss'
})
export class ManagerAssessEmployeeComponent implements OnInit {
  activeButton: string = 'notDone'; // Tracks which button is active
  title = 'Assess your potential';
  assessments: any[] = []; // Holds all assessments
  notDoneAssessments: any; // Holds not done assessments
  systemUser: any; // Holds current user data
  greeting: string; // Holds greeting message
  isLinear = true; // Set to true for linear stepper
  firstFormGroups: FormGroup[] = []; // Array to hold form groups for each assessment
  userId: string | null = null;
  assessmentId: string | null = null;
  userToAssess: any

  private _formBuilder = inject(FormBuilder);

  constructor(private server: HttpServiceService,
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient, private snack: MatSnackBar) { }

  ngOnInit() { // Set the greeting message
    this.systemUser = JSON.parse(localStorage.getItem('user')); // Get user data from local storage
    this.getNotDone(); // Fetch not done assessments
    this.route.paramMap.subscribe(params => {
      this.userId = params.get('id');
      this.assessmentId = params.get('assId');
    });

    this.getEmployeeInfo()
  }

  getNotDone() {
    this.server.getActiveAssessments().subscribe(
      (res) => {
        this.notDoneAssessments = res.item; // Store not done assessments
        console.log("hello", res.item);
        
        this.setupFormGroups(); // Setup form groups after receiving assessments
      },
      (error) => {
        console.log('error', error);
      }
    );
  }

  setupFormGroups() {
    this.firstFormGroups = []; // Reset the array
    this.notDoneAssessments.forEach((assessment) => {
      const formGroup = this._formBuilder.group({
        assessmentId: [assessment.assessmentId],
        potentialAttributes: this._formBuilder.array(
          assessment.potentialAttributes.map((attr: any) =>
            this._formBuilder.group({
              attributeId: [attr.attributeId],
              attributeName: [attr.attributeName],
              questions: this._formBuilder.array(
                attr.questions.map((q: any) =>
                  this._formBuilder.group({
                    assessmentQuestionId: [q.assessmentQuestionId],
                    assessmentQuestionDescription: [q.assessmentQuestionDescription], // Ensure this field is included
                    choices: this._formBuilder.array( // Initialize choices as a FormArray
                      q.choices.map((choice: any) => this._formBuilder.group({
                        choiceId: [choice.choiceId],
                        choiceValue: [choice.choiceValue]
                      }))
                    ),
                    choiceId: ['', Validators.required] // Default value for selected choice
                  })
                )
              )
            })
          )
        )
      });
      this.firstFormGroups.push(formGroup); // Add the form group to the array
    });
  }

  getAttributeName(assessmentIndex: number, attrIndex: number): string {
    return this.firstFormGroups[assessmentIndex].get(['potentialAttributes', attrIndex, 'attributeName'])?.value;
  }

  getQuestionText(assessmentIndex: number, attrIndex: number, questionIndex: number): string {
    return this.firstFormGroups[assessmentIndex].get(['potentialAttributes', attrIndex, 'questions', questionIndex, 'assessmentQuestionDescription'])?.value;
  }

  getChoices(assessmentIndex: number, attrIndex: number, qIndex: number) {
    const choicesArray = this.firstFormGroups[assessmentIndex].get(['potentialAttributes', attrIndex, 'questions', qIndex, 'choices']) as FormArray;
    return choicesArray ? choicesArray.controls : [];
  }

  submit() {
    const requestBody = this.firstFormGroups.map((formGroup) => ({
      managerId: this.systemUser.user.userId,
      assessmentId: formGroup.get('assessmentId')?.value,
      answers: formGroup.get('potentialAttributes')?.value.flatMap((attr: any) =>
        attr.questions.map((q: any) => ({
          assessmentQuestionId: q.assessmentQuestionId,
          userId: this.userId,
          choiceId: q.choiceId,
          managerAssessed: true
        }))
      )
    }));

    console.log('Request Body:', requestBody[0]);
    this.server.postManagerAnswers(requestBody[0]).subscribe(
      (res) => {
        console.log('response', res);
        this.snack.open("Assessment has been submitted successfully", "Close", { duration: 3600, verticalPosition: "bottom" });
        this.router.navigate(['/assess-my-team']).then(() => {
          window.location.reload();
        });
      },
      (error) => {
        console.log('error', error);
      }
    );
  }
  getEmployeeInfo() {
    this.server.getEmployeeById(this.userId).subscribe(
      (response: any) => {
        console.log("assesed", response.item);
        this.userToAssess = response.item

      },
      (error: any) => { },

    )
  }

  getUserDoneAssessments() {
    const url = `${this.server.serverUrl}getDoneAssessments`; // URL for done assessments
    this.http.get(url).subscribe(
      (value: any) => {
        console.log(value);
        // Logic to filter or handle done assessments
      },
      (error: any) => {
        console.log(error);
      }
    );
  }

}