import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { HttpServiceService } from '../../../services/http-service.service';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { Router } from '@angular/router';
import { CreateEmployeeComponent } from '../../../create-employee/create-employee.component';
import { AddIndividualPerformanceComponent } from './add-individual-performance/add-individual-performance.component';
import { MatTableDataSource } from '@angular/material/table';

@Component({
  selector: 'app-my-teams-profile',
  templateUrl: './my-teams-profile.component.html',
  styleUrls: ['./my-teams-profile.component.scss']
})
export class MyTeamsProfileComponent implements OnInit {

  managerEmployees: any[] = [];
  performances: any[] = [];
  authUser: any;
  employees: any[] = [];
  noOfEmployees: number = 0;
  performance:any[] = [];
  selectedFile: File | null = null;
  uploading: boolean = false;
  selectedMethod: string | null = null;
  selectedPerformanceType: string | null = null;
  displayedColumns: string[] = ['id', 'employeeName', 'year', 'performanceMetric'];
  // dataSource: any[] = [];

  dataSource = new MatTableDataSource<any, MatPaginator>;

  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(
    public http: HttpServiceService,
    private dialog: MatDialog,
    private snack: MatSnackBar,
    private router: Router
  ) {}

  ngOnInit(): void {
    const user = localStorage.getItem("user");
    if (user) {
      this.authUser = JSON.parse(user);
    }
    this.getAllEmployees();
    this.displayPerformance();
    this.dataSource.paginator = this.paginator;
  }

  // Method to handle selection of assessment type (Excel or Single Employee)
  select(selected: string): void {
    this.selectedMethod = selected;
  }

  // Method to handle selection of performance type (Excel or Single Employee)
  selectPerformance(selected: string): void {
    this.selectedPerformanceType = selected;
  }

  // Handles file selection for both employee and performance uploads
  onFileSelected(event: any): void {
    this.selectedFile = event.target.files[0];
  }

  // Upload employee file as Excel
  onUpload(): void {
    if (!this.selectedFile) {
      this.snack.open('No file selected.', 'Close', { duration: 2000 });
      return;
    }

    this.uploading = true;
    const formData = new FormData();
    formData.append('file', this.selectedFile);

    this.http.uploadEmployees(formData, this.authUser.user.userId).subscribe(
      response => {
        this.snack.open('File uploaded successfully', 'Close', { duration: 2000 });
        this.uploading = false;
        this.getAllEmployees();  // Refresh the employee list after upload
      },
      error => {
        console.error('Error uploading file:', error);
        this.snack.open('Error uploading file', 'Close', { duration: 2000 });
        this.uploading = false;
      }
    );
  }

  // Upload performance file as Excel
  performanceUpload(): void {
    if (!this.selectedFile) {
      this.snack.open('No file selected.', 'Close', { duration: 2000 });
      return;
    }

    this.uploading = true;
    const formData = new FormData();
    formData.append('file', this.selectedFile);

    this.http.uploadPerformance(formData).subscribe(
      response => {
        if (response.status === 200) {
          this.snack.open('Performance uploaded successfully', 'Close', { duration: 2000 });
          this.uploading = false;
        } else {
          this.snack.open(response.message, 'Close', { duration: 20000 });
          this.uploading = false;
        }
       
        // Any additional logic for updating UI after performance upload
      },
      error => {
        console.error('Error uploading performance file:', error);
        this.snack.open('Error uploading performance file', 'Close', { duration: 2000 });
        this.uploading = false;
      }
    );
  }

  // Fetch all employees for the manager
  getAllEmployees(): void {
    this.http.getAllEmployees(this.authUser.user.userId).subscribe(
      res => {
        this.employees = res.item || [];
        this.noOfEmployees = this.employees.length;

        // Add random PF numbers for demo purposes
        this.managerEmployees = this.employees.map(e => ({
          ...e,
          pfNo: Math.floor(10000 + Math.random() * 9000)
        }));
      },
      error => {
        this.snack.open(error.error.message, 'Close', { duration: 3600 });
      }
    );
  }

  displayPerformance(){
    this.http.getPerformance(this.authUser.user.userId).subscribe(
      ((res)=>{
        this.performance = res.item
        this.dataSource = new MatTableDataSource(this.performance);

        console.log('responseeee',res);
        
      }),
      ((error)=>{
        
      }),
      ()=>{}
    )
  }

  // Navigate to the employee profile page
  navigateToUserProfile(userId: string): void {
    this.router.navigate(['/profile'], { queryParams: { userId } });
  }

  // Open dialog to create a new employee
  openCreateEmployeeDialogue(): void {
    const dialogRef: MatDialogRef<CreateEmployeeComponent> = this.dialog.open(CreateEmployeeComponent, {
      width: '40%',
      height: '80%'
    });

    // Refresh employee list after dialog is closed
    dialogRef.afterClosed().subscribe(() => {
      this.getAllEmployees();
    });
  }

  // Open dialog to add performance for a single employee
  openAddPerformanceDialogue(): void {
    const dialogRef: MatDialogRef<AddIndividualPerformanceComponent> = this.dialog.open(AddIndividualPerformanceComponent, {
      width: '45%',
      height: '60%',
      data: { emp: this.employees }
    });

    // Add logic to refresh data or handle the result if needed
    dialogRef.afterClosed().subscribe(() => {
      this.displayPerformance();
    });
  }
}
