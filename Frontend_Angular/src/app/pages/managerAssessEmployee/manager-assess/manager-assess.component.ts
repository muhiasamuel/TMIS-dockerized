import { Component, OnInit, ViewChild } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { HttpServiceService } from '../../../services/http-service.service';

// Define the ApiResponse interface
interface ApiResponse {
  item: AssessmentStatusDto[];
  status: number;
  message: string;
}

export interface AssessmentStatusDto {
  assessmentName: string;
  assessmentId:number;
  assessmentDescription: string;
  assessmentExpiry: string;
  assessmentStatuses: UserAssessment[];
}

export interface UserAssessment {
  userId: number;
  username: string;
  userFullName: string;
  pf: string;
  selfAssessed: boolean;
  managerAssessed: boolean;
}

@Component({
  selector: 'app-manager-assess',
  templateUrl: './manager-assess.component.html',
  styleUrls: ['./manager-assess.component.scss']
})
export class ManagerAssessComponent implements OnInit {
  greeting = "Welcome to the Manager Assessments Page!";
  title = "Manager Assess";
  systemUser: any;
  assessmentInfo: AssessmentStatusDto | null = null;
  dataSource = new MatTableDataSource<UserAssessment>([]);
  displayedColumns: string[] = ['userId', 'username', 'userFullName', 'pf', 'selfAssessed', 'managerAssessed', "Actions"];

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(private http: HttpClient, private server: HttpServiceService) { }

  ngOnInit(): void {
    this.systemUser = JSON.parse(localStorage.getItem('user') || '{}');
    this.fetchUserAssessments();
  }

  fetchUserAssessments(): void {
    const url = `${this.server.serverUrl}${this.systemUser.user.userId}/assessment-status`;
    this.http.get<ApiResponse>(url).subscribe(response => {
      if (response.status === 200) {
        // Flatten the assessmentStatuses into a single array for the table
        const flattenedData = response.item.flatMap(assessment => assessment.assessmentStatuses);

        // Set assessment info and data source
        this.assessmentInfo = response.item[0]; // Assuming there's at least one assessment
        this.dataSource.data = flattenedData;
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      } else {
        console.error('Error fetching data:', response.message);
      }
    }, error => {
      console.error('HTTP Error:', error);
    });
  }

  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value.trim().toLowerCase();
    this.dataSource.filter = filterValue;
  }
}
