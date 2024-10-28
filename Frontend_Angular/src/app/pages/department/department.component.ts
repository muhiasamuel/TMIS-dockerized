import { Component, OnInit, inject } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { HttpServiceService } from '../../services/http-service.service';
import { AddDepartmentComponent } from './add-department/add-department.component';

interface Position {
  positionName: string;
}

interface Department {
  depName: string;
  departmentPositions: Position[];
  showAll?: boolean;
  displayLimit?: number;
}

@Component({
  selector: 'app-department',
  templateUrl: './department.component.html',
  styleUrls: ['./department.component.scss']
})
export class DepartmentComponent implements OnInit {
  departments: Department[] = [];
  defaultDisplayLimit = 2;

  matDialog: MatDialog = inject(MatDialog);
  http: HttpServiceService = inject(HttpServiceService);

  ngOnInit() {
    this.getDepartment();
  }

  // Toggle between showing all positions or a limited number
  toggleView(department: Department) {
    department.showAll = !department.showAll;
    department.displayLimit = department.showAll ? department.departmentPositions.length : this.defaultDisplayLimit;
  }

  openDep() {
    this.matDialog.open(AddDepartmentComponent, {
      width: '50vw'
    });
  }

  getDepartment() {
    this.http.getDepartments().subscribe({
      next: (res) => {
        this.departments = res.item.map((dep: Department) => ({
          ...dep,
          showAll: false,
          displayLimit: this.defaultDisplayLimit
        }));
      },
      error: (err) => console.log('Error fetching departments', err)
    });
  }
}
