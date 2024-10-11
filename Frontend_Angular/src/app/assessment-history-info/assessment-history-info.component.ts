import { Component, Inject, Injector, Input, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { HttpServiceService } from '../services/http-service.service';
import { MAT_DIALOG_DATA, MatDialogRef,MatDialog } from '@angular/material/dialog';

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
  isDisabled = true;
  alldata:Assessment[] = [];
  currentPage: number = 1;
  itemsPerPage: number = 4;
  totalPages: number = 1;
  assessments: Assessment[] = [];
  systemUser:any;
  totalAssessments = 0;
  highestPerformedAttribute: AssessmentStatus | null = null;
  highestPerformedAssessment: Assessment | null = null;

  private dialogRef: MatDialogRef<AssessmentHistoryInfoComponent, any>;

  constructor(
    private injector: Injector,
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
      console.log(response.item);
      
      this.assessments = response.item;
      this.alldata = response.item
      this.calculateStatistics();
      this.updatePagination()
    });
  }
  closeDialog() {
    const dialog = this.injector.get(MatDialog);
    dialog.closeAll();
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

  updatePagination(): void {
    this.totalPages = Math.ceil(this.alldata.length / this.itemsPerPage);
    this.paginate();
  }

  //pagination
  paginate(): void {
    const start = (this.currentPage - 1) * this.itemsPerPage;
    const end = start + this.itemsPerPage;
    this.assessments = this.alldata.slice(start, end);
  }
  prevPage(): void {
    if (this.currentPage > 1) {
      this.currentPage--;
      this.paginate();
    }
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
      this.paginate();
    }
  }
}
