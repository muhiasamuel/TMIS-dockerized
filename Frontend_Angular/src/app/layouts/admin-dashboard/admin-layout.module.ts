import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { AdminLayoutRoutes } from './admin-layout.routing';


import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { DashboardComponent } from '../../pages/dashboard/dashboard.component';
import { BarChartComponent } from '../../bar-chart/bar-chart.component';
import { PieChartComponent } from '../../bar-chart/pie-chart/pie-chart.component';

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(AdminLayoutRoutes),
    FormsModule,
    NgbModule
  ],
  declarations: [
    // DashboardComponent,
    // BarChartComponent,
    // PieChartComponent
  ]
})

export class AdminLayoutModule {}
