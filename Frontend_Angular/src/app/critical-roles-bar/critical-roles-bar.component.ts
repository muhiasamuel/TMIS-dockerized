import { DOCUMENT } from '@angular/common';
import { Component, Inject, Input } from '@angular/core';
import { Chart, Colors, ChartOptions } from 'chart.js';
import { HttpServiceService } from '../services/http-service.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-critical-roles-bar',
  templateUrl: './critical-roles-bar.component.html',
  styleUrls: ['./critical-roles-bar.component.scss'] // Note: Changed 'styleUrl' to 'styleUrls'
})
export class CriticalRolesBarComponent {

  @Input() criticalSkills: any[] = [];
  @Input() items: any[] = [];
  labelList: string[] = [];
  colorCode: string[] = [];
  totalCountList: string[] = [];
  chart1: any;
  authUser: any;

  constructor(
    @Inject(DOCUMENT) private document: Document,
    private http: HttpServiceService,
    private snack: MatSnackBar
  ) { }

  ngOnDestroy(): void {
    if (this.chart1) {
      this.chart1.destroy();
    }
  }

  ngOnInit(): void {
    const user = localStorage.getItem("user");
    if (user) {
      this.authUser = JSON.parse(user);
    }

    this.getAllCriticalRoles();
  }

  ngAfterViewInit(): void { }

  getAllCriticalRoles() {
    this.http.getCriticalRoles(this.authUser.user.userId).subscribe(
      (res) => {
        console.log("critiv", res.item);
        const criticalRoles = res.item;

        if (criticalRoles) {
          criticalRoles.forEach((e: any) => {
            console.log(e);
            if (e.averageRating >= 3.5) {
              this.calculateAndPushData(
                e.averageRating || 0,
                e.roleName,
                e.currentState
              );
            }
          });
          this.setupChart(this.labelList, this.totalCountList, this.colorCode);
        }
      },
      (error) => {
        this.snack.open(error.error.message, "Close", { duration: 3600 });
      },
      () => { }
    );
  }

  calculateAndPushData(total: any, label: string, state:string) {
    this.totalCountList.push(total);
    this.labelList.push(label);
    this.colorCode.push(state)
  }

  private setupChart(labelList: string[], totalCountList: string[], colorCode:string[]): void {
    let ctx;

    // Destroy existing chart if it exists
    if (this.chart1) {
      this.chart1.destroy();
      ctx = null;
    }

    ctx = document.getElementById('barChart1') as HTMLCanvasElement;
    Chart.register(Colors); // Register the plugin before creating the chart

    const chartOptions: ChartOptions = {
      responsive: true,
      maintainAspectRatio: true,
      indexAxis: 'y',
      scales: {
        x: {
          ticks: {
            autoSkip: true,
            maxRotation: 45, // Adjust the maximum rotation for labels
            minRotation: 0,  // Adjust the minimum rotation for labels
          }
        },
        y: {
          beginAtZero: true
        }
      },
      plugins: {
        legend: {
          display: true,
        }
      }
    };

    this.chart1 = new Chart(ctx, {
      type: 'bar',
      data: {
        labels: labelList,        
        datasets: [
          {
            data: totalCountList,
            label: 'Critical Roles',
            borderWidth: 1,
            //backgroundColor: colorCode
            backgroundColor: [
              'rgba(248, 41, 43, 0.5)',
              'rgba(229, 189, 12, 0.5)',
              'rgba(40, 170, 45, 0.5)'
            ],
            borderColor: [
              'rgba(248, 41, 43, 1)',
              'rgba(229, 189, 12, 1)',
              'rgba(40, 170, 45, 1)'
            ],
          }
          
        ],
      },
      options: chartOptions,
      
    });
  }
}
