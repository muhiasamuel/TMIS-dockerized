import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { HttpServiceService } from '../services/http-service.service';

interface AssessmentStatus {
  managerScore: number | null;
  potentialAttributeName: string;
  userScore: number | null;
}

interface Assessment {
  assessmentId: number;
  assessmentName: string;
  assessmentDescription: string;
  assessmentDate: string; // Format as 'yyyy-MM-dd'
  overallScore: number | null;
  assessmentStatuses: AssessmentStatus[];
}
@Component({
  selector: 'app-assessment-history-info',
  templateUrl: './assessment-history-info.component.html',
  styleUrl: './assessment-history-info.component.scss'
})
export class AssessmentHistoryInfoComponent {

  assessments: Assessment[] = [];

  constructor(private http: HttpClient, private server:HttpServiceService) { }

  ngOnInit(): void {
    this.loadAssessments();
  }

  loadAssessments(): void {
    //http://localhost:8080/v1/api/user/1/scoring-history
    const url = `${this.server.serverUrl}user/2/scoring-history`
    this.http.get<{ item: Assessment[] }>(url).subscribe(response => {
      this.assessments = response.item;
    });
  }
}
