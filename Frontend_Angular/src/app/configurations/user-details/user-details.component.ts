import { Component, ViewChild, AfterViewInit } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { HttpServiceService } from '../../services/http-service.service';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-user-details',
  templateUrl: './user-details.component.html',
  styleUrls: ['./user-details.component.scss']
})
export class UserDetailsComponent {
  dataSource: MatTableDataSource<any>;
  displayedColumns: string[] = ['userId', 'userFullName', 'userEmail', 'roleName', 'departmentName', 'managerName', 'isEnabled', 'isLocked'];

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private http: HttpServiceService, private dialog: MatDialog) {}

  ngOnInit(): void {
    this.displayUser();
  }

  displayUser(): void {
    this.http.getUserDetails().subscribe(
      (res) => {
        this.dataSource = new MatTableDataSource(res.item);  // Wrap res.item in MatTableDataSource
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
        console.log('response', res);
      },
      (error) => {
        console.error('Error fetching user details:', error);
      }
    );
  }

  onFilterChange(event: KeyboardEvent): void{
    const inputElement = event.target as HTMLInputElement;
    const filterValue = inputElement.value.trim().toLowerCase();
    this.applyFilter(filterValue)
  }

  applyFilter(filterValue: string): void {
    this.dataSource.filter = filterValue;
  }

  toggleEnabled(element: any){
    console.log('eeee',element);
    
    if (element.isEnabled === false) {
      this.http.enableUserAcc(element.userId).subscribe(
        ((res)=>{
          this.ngOnInit()
          console.log(res);
          
        })
      )
    }else{
      this.http.disableUserAcc(element.userId).subscribe(
        ((res)=>{
          this.ngOnInit()
          console.log(res);
          
        })
      )
    }
  }
  toggleLocked(element: any){

    console.log("gg", element);
    if (element.isLocked === false) {
      this.http.lockUserAcc(element.userId).subscribe(
        ((res) => {
          this.ngOnInit()
          console.log(res);
          
        })
      )
    }else{
      this.http.unlockUseracc(element.userId).subscribe(
        ((res) => {
          this.ngOnInit()
          console.log(res);
          
        })
      )
    }
    

  }


}
