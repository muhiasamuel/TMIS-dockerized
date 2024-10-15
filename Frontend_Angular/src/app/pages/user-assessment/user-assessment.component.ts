import { Component, inject, OnInit } from '@angular/core';
import { HttpServiceService } from '../../services/http-service.service';
import { HttpClient } from '@angular/common/http';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-user-assessment',
  templateUrl: './user-assessment.component.html',
  styleUrls: ['./user-assessment.component.scss']
})
export class UserAssessmentComponent implements OnInit {
  activeButton: string = 'notDone'; // Tracks which button is active
  title = 'Assess your potential';
  assessments: any[] = []; // Holds all assessments
  notDoneAssessments: any; // Holds not done assessments
  systemUser: any; // Holds current user data
  greeting: string; // Holds greeting message
  isLinear = true; // Set to true for linear stepper
  firstFormGroups: FormGroup[] = []; // Array to hold form groups for each assessment

  private _formBuilder = inject(FormBuilder);

  constructor(private server: HttpServiceService, private http: HttpClient, private snack:MatSnackBar) {}

  ngOnInit() {
    this.setGreeting(); // Set the greeting message
    this.systemUser = JSON.parse(sessionStorage.getItem('user')); // Get user data from local storage
    this.getNotDone(); // Fetch not done assessments
  }

  getNotDone() {
    this.server.getNotDoneAssesments(this.systemUser.user.userId).subscribe(
      (res) => {
        this.notDoneAssessments = res.item; // Store not done assessments
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
        assessmentId: [assessment.assessment.assessmentId], // Assessment ID
        attributes: this._formBuilder.array(
          assessment.assessment.potentialAttributes.map((attr) =>
            this._formBuilder.group({
              attributeName: [attr.attributeName], // Attribute name
              questions: this._formBuilder.array(
                attr.questions.map((q) =>
                  this._formBuilder.group({
                    assessmentQuestionId: [q.assessmentQuestionId], // Question ID
                    userId: [this.systemUser.user.userId], // User ID
                    choiceId: ['', Validators.required], // Initially empty, will be filled by the user
                    selfAssessed: [true], // Assuming self-assessment is true
                    choices: [q.choices] // Ensure choices are included
                  })
                )
              ),
            })
          )
        ),
      });
      this.firstFormGroups.push(formGroup); // Add the form group to the array
    });
  }

  getAttributeName(assessmentIndex: number, attrIndex: number): string {
    return this.firstFormGroups[assessmentIndex].get(['attributes', attrIndex, 'attributeName'])?.value;
  }

  getQuestionText(assessmentIndex: number, attrIndex: number, questionIndex: number): string {
    return this.notDoneAssessments[assessmentIndex].assessment.potentialAttributes[attrIndex].questions[questionIndex].assessmentQuestionDescription;
  }

  getChoices(assessmentIndex: number, attrIndex: number, qIndex: number) {
    const questions = this.firstFormGroups[assessmentIndex].get('attributes')['controls'][attrIndex].get('questions')['controls'];
    if (questions && questions[qIndex]) {
      const choices = questions[qIndex].get('choices')['value'];
      return choices ? choices.sort((a: any, b: any) => a.choiceValue - b.choiceValue) : []; // Sort choices in ascending order
    }
    return [];
  }

  submit() {
    const requestBody = this.firstFormGroups.map((formGroup) => ({
      assessmentId: formGroup.get('assessmentId')?.value, // Get assessment ID
      answers: formGroup.get('attributes')?.value.flatMap((attr) => 
        attr.questions.map((q) => ({
          assessmentQuestionId: q.assessmentQuestionId, // Include question ID
          userId: this.systemUser.user.userId, // Include user ID
          choiceId: q.choiceId, // Include selected choice ID
          selfAssessed: q.selfAssessed // Include self-assessed flag
        }))
      ), // Map questions to answers
    }));

    console.log('Request Body:', requestBody[0]);
    this.server.postUsersAnswers(requestBody[0]).subscribe(
      ((res)=>{
        console.log('response',res);
        this.snack.open("Assessment has been submitted successfully", "Close",{duration:3600, verticalPosition:"bottom"} )
      
      }),
      ((error)=>{
        console.log('error',error);
        
      }),
      ()=>{}
    )
    // Make your API call here with the requestBody
    // Example:
    // this.http.post(`${this.server.serverUrl}submitAssessment`, requestBody).subscribe(...);
  }

  openNotDone() {
    this.activeButton = 'notDone'; // Set active button
    this.getNotDone(); // Fetch not done assessments
  }

  openDone() {
    this.activeButton = 'done'; // Set active button
    this.getUserDoneAssessments(); // Fetch done assessments
  }

  openAssessed() {
    this.activeButton = 'assessed'; // Set active button
    //this.notDoneAssessments = ''; // Clear not done assessments
    // You can add logic here to fetch assessed assessments if needed
  }

  getUserDoneAssessments() {
    const url = `${this.server.serverUrl}getDoneAssessments`; // URL for done assessments
    this.http.get(url).subscribe(
      (value: any) => {
        console.log(value);
        this.ngOnInit()
        // Logic to filter or handle done assessments
      },
      (error: any) => {
        console.log(error);
      }
    );
  }

  setGreeting(): void {
    const currentHour = new Date().getHours(); // Get current hour
    // Set greeting message based on time of day
    if (currentHour >= 5 && currentHour < 12) {
      this.greeting = 'Good morning';
    } else if (currentHour >= 12 && currentHour < 18) {
      this.greeting = 'Good afternoon';
    } else if (currentHour >= 18 && currentHour < 22) {
      this.greeting = 'Good evening';
    } else {
      this.greeting = 'Good night';
    }
  }
}
