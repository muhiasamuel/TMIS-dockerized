import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { HttpServiceService } from '../../../services/http-service.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-succession-display',
  templateUrl: './succession-display.component.html',
  styleUrls: ['./succession-display.component.scss']
})
export class SuccessionDisplayComponent implements OnInit {


displayedColumns: string[] = ['departmentName', 'driverName', 'positionName', 'currentRoleHolderName', 'riskRating', 'actions'];


  dataSource!: MatTableDataSource<any>;
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(private service: HttpServiceService, private router:Router) { }

  ngOnInit(): void {
    this.loadSuccessionPlans();
  }
  loadSuccessionPlans(): void {
    this.service.getAllSuccessionPlans().subscribe(
      (data) => {
        // Remove duplicate plans based on planId
        const uniquePlans = data.item.reduce((accumulator, current) => {
          if (!accumulator.some(plan => plan.planId === current.planId)) {
            accumulator.push(current);
          }
          return accumulator;
        }, []);
  
        // Log the unique plans for verification
        console.log(uniquePlans);
  
        // Set the filtered unique plans to the data source
        this.dataSource = new MatTableDataSource(uniquePlans);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      });
  }
  

  // Apply filter to the table
  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  getRiskRatingClass(riskRating: string): string {
    switch (riskRating.toLowerCase()) {
      case 'high':
        return 'red';
      case 'medium':
        return 'amber';
      case 'low':
        return 'green';
      default:
        return '';
    }
  }

// SuccessionDisplayComponent.ts
getHeatMapClass(heatMap: string | undefined): string {
  // Default to an empty string or a default class if heatMap is undefined
  const heatMapValue = heatMap ? heatMap.toLowerCase() : 'default-class';

  switch (heatMapValue) {
    case 'high':
      return 'heatmap-high';
    case 'medium':
      return 'heatmap-medium';
    case 'low':
      return 'heatmap-low';
    default:
      return 'heatmap-default'; // Provide a default class or handle unexpected values
  }
}

  

  editRow(row: any) {
   // row.editing = true;
    this.router.navigate(['/view/plan'], { queryParams: { planId: row } });
  }

  viewRow(row: any) {
    this.router.navigate(['/view/plan'], { queryParams: { planId: row } });
  }
  saveRow(row: any): void {
    row.editing = false;
    console.log(row);
    
    // Optionally send updated data to the server here
    // this.service.updateCriticalSkill(row).subscribe(
    //   response => {
    //     console.log('Update successful', response);
    //   },
    //   error => {
    //     console.error('Update failed', error);
    //   }
    // );
  }

  cancelEdit(row: any): void {
    row.editing = false;
    // Optionally reload data from the server to revert changes
    this.loadSuccessionPlans();
  }
}
