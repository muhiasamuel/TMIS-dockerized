import { Component, OnInit, ViewChild, AfterViewInit, inject } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { AddDepartmentComponent } from './add-department/add-department.component';
import { HttpServiceService } from '../../services/http-service.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-department',
  templateUrl: './department.component.html',
  styleUrls: ['./department.component.scss']
})
export class DepartmentComponent implements OnInit, AfterViewInit {
  departments: any[] = [];
  dataSource = new MatTableDataSource<any>();
  displayedColumns: string[] = ['index', 'depId', 'depName', 'positionName'];
  matDialog: MatDialog = inject(MatDialog);
  http: HttpServiceService = inject(HttpServiceService);
  snackbar: MatSnackBar = inject(MatSnackBar)

  @ViewChild(MatPaginator) paginator: MatPaginator;

  ngOnInit() {
    this.getDepartment();
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }

  openDep() {
    this.matDialog.open(AddDepartmentComponent, {
      width: '50vw'
    });
  }

  getDepartment() {
    this.http.getDepartments().subscribe({
      next: (res) =>{
        console.log('Departments data', res.item);
        this.departments = res.item;
         this.dataSource.data = this.departments;
      },
      error: (err) => {
        console.log('Error fetching departments', err);
      },
      complete: () => {
        // this.snackbar.open('Sucessful', 'close', {duration: 1000})
      }
    });
  }
}
