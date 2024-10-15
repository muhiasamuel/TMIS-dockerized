import { Component, Input, SimpleChanges, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MatTableDataSource } from '@angular/material/table';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';

import { ViewDialogComponent } from './components/view-dialog/view-dialog.component';
import { DialogViewComponent } from '../dialog-view/dialog-view.component';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { HttpServiceService } from '../../services/http-service.service';
import { HttpClient } from '@angular/common/http';
import { DoneByComponent } from './done-by/done-by.component';





export interface IEmployeeData{
  userId: number;
  userType: number;
  username: string;
  userFullName: string;
  action: string;
}

@Component({
  selector: 'app-asess-my-team',
  templateUrl:'./asess-my-team.component.html',
  styleUrl: './asess-my-team.component.scss',
  
})

export class AsessMyTeamComponent {
  title: any
  doneBy = "Done by"
  description: any
  systemUser:any
  assId: any
  userDoneAssess: any[] = []
  userNotDoneAssess: any[] = []

  dataSource: any
  displayedColumns: any[] = ['userId', 'userType', 'username', 'userFullName', 'action']

  constructor(private router: Router,private route: ActivatedRoute, public dialog: MatDialog, private http:HttpClient, private server: HttpServiceService) {
    
  }
  @ViewChild(MatPaginator,{static: false})
   paginator!: MatPaginator;

   ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }

  ngOnInit(){
    this.systemUser = JSON.parse(sessionStorage.getItem("user"))
    this.route.params.subscribe(params => {
      this.assId = params['id']; // Access the 'id' parameter from the URL
      console.log('Test assID:', this.assId);
      console.log('Manager ID:', this.systemUser.user.userId);
    })
    console.log('Test assID:', this.assId);
    console.log('Manager ID:', this.systemUser.userId);


    this.getUserDoneAssessment();
    console.log("yooooo",this.userDoneAssess)

  }

  showDialog(){
    this.dialog.open(DialogViewComponent,{
    })
  }

  // method to open the dialog box
  openDialog(employee): void {
    this.dialog.open(ViewDialogComponent,{
      width: '60%',height: '70%',
      data:{
        emp:employee
      }
    });
  }

    

  // Method to apply filters to the table
  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  getUserDoneAssessment(){
    const url = `${this.server.serverUrl}doneAssessmentDetails?assId=${this.assId}&managerId=${this.systemUser.user.userId}`
    const response = this.http.get(url);

    response.subscribe(
      (value: any) => {
        console.log(value)
        this.title = value.item.assessmentName
        this.description = value.item.assessmentDescription
        for(let i = 0; i < value.item.doneBy.length; i++){
          const emp: IEmployeeData = {
            userId: value.item.doneBy[i].userId,
            userType: value.item.doneBy[i].userType,
            username: value.item.doneBy[i].username,
            userFullName: value.item.doneBy[i].userFullName,
            action: 'view'
          } 
          this.userDoneAssess.push(emp)
        }
        if(value.item.notDoneBy.length){
          for(let i = 0; i < value.item.notDoneBy.length; i++){
            const emp: IEmployeeData = {
              userId: value.item.notDoneBy[i].userId,
              userType: value.item.notDoneBy[i].userType,
              username: value.item.notDoneBy[i].username,
              userFullName: value.item.notDoneBy[i].userFullName,
              action: 'view'
            } 
            this.userNotDoneAssess.push(emp)
          }
        }
        
    this.dataSource = new MatTableDataSource<IEmployeeData>(this.userDoneAssess);

        console.log("user Done assessments",this.userDoneAssess)
      },
      (error: any) => {
          console.log(error)
      }
    )
  }

  showNotDoneBy(){
    this.doneBy = "Not done by"
    this.dataSource = new MatTableDataSource<IEmployeeData>(this.userNotDoneAssess);
  }
  showDoneBy(){
    this.doneBy = "Done by"
    this.dataSource = new MatTableDataSource<IEmployeeData>(this.userDoneAssess);
  }

  // method to handle button click
  goToAssessMyPotential(employeeId:number): void {
    console.log('Button clicked!')
    this.router.navigate(['/assess-my-employee', employeeId, this.assId] ); // Navigate to AssessMyPotentialComponent
    
  }
}
