import { Component, Inject, Input, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { HttpServiceService } from '../services/http-service.service';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

interface AssessmentStatus {
  managerScore: number | null;
  potentialAttributeName: string;
  userScore: number | null;
}

interface Assessment {
  assessmentId: number;
  assessmentName: string;
  managerAssessed: boolean;
  assessmentDescription: string;
  assessmentDate: string; // Format as 'yyyy-MM-dd'
  overallScore: number | null;
  assessmentStatuses: AssessmentStatus[];
}

@Component({
  selector: 'app-assessment-history-info',
  templateUrl: './assessment-history-info.component.html',
  styleUrls: ['./assessment-history-info.component.scss']
})
export class AssessmentHistoryInfoComponent implements OnInit {

  @Input() id!: number;

  assessments: Assessment[] = [];
  systemUser:any;
  totalAssessments = 0;
  highestPerformedAttribute: AssessmentStatus | null = null;
  highestPerformedAssessment: Assessment | null = null;

  constructor(
    private http: HttpClient,
     private server: HttpServiceService) { }

  ngOnInit(): void {
    this.systemUser = JSON.parse(localStorage.getItem('user')); // Get user data from local storage
    //this.systemUser.user.userId
    console.log('Received ID:', this.id);
    if (this.id) {
      this.loadAssessments(this.id);
    }else{
      this.loadAssessments(this.systemUser.user.userId);
    }
   
  }

  loadAssessments(id): void {
    const url = `${this.server.serverUrl}user/${id}/scoring-history`;
    this.http.get<{ item: Assessment[] }>(url).subscribe(response => {
      this.assessments = response.item;
      this.calculateStatistics();
    });
  }

  calculateStatistics(): void {
    this.totalAssessments = this.assessments.length;

    this.assessments.forEach(assessment => {
      // Check for the highest performed assessment
      if (!this.highestPerformedAssessment || 
          (assessment.overallScore !== null && 
           assessment.overallScore > (this.highestPerformedAssessment.overallScore || 0))) {
        this.highestPerformedAssessment = assessment;
      }

      // Check for the highest performed attribute
      assessment.assessmentStatuses.forEach(status => {
        if (!this.highestPerformedAttribute ||
            (status.managerScore !== null && 
             status.managerScore > (this.highestPerformedAttribute.managerScore || 0))) {
          this.highestPerformedAttribute = status;
        }
      });
    });
  }
}
