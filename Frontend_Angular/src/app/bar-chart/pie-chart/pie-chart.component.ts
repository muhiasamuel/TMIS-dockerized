import { Component, HostListener, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Chart, ChartConfiguration } from 'chart.js';
import { HttpServiceService } from '../../services/http-service.service';

@Component({
  selector: 'app-pie-chart',
  templateUrl: './pie-chart.component.html',
  styleUrls: ['./pie-chart.component.scss']
})
export class PieChartComponent implements OnInit {
  pieChart: Chart<'pie'>;
  assessments: any[] = [];
  pieChartData: ChartConfiguration<'pie'>['data'] = {
    labels: [],
    datasets: [{
      data: [],
      backgroundColor: []
    }]
  };
  pieChartOptions: ChartConfiguration<'pie'>['options'] = {
    responsive: true,
    maintainAspectRatio: true, // Allow chart to adjust to container
    plugins: {
      tooltip: {
        enabled: true,
        callbacks: {
          label: (context) => {
            const label = context.label || '';
            const value = context.raw;
            return `${label}: ${value}`;
          }
        }
      },
      legend: {
        display: true,
        position: 'top'
      }
    }
  };

  private resizeTimeout: any;

  constructor(private http: HttpClient, private server: HttpServiceService) {}

  ngOnInit(): void {
    this.loadAssessments(2);
  }

  loadAssessments(id: number): void {
    const url = `${this.server.serverUrl}user/${id}/scoring-history`;
    this.http.get<{ item: any[] }>(url).subscribe(response => {
      this.assessments = response.item;
      this.preparePieChartData();
    });
  }

  preparePieChartData(): void {
    const scores = this.assessments.reduce((acc, assessment) => {
      assessment.assessmentStatuses.forEach(status => {
        if (!acc[status.potentialAttributeName]) {
          acc[status.potentialAttributeName] = 0;
        }
        acc[status.potentialAttributeName] += status.userScore;
      });
      return acc;
    }, {} as { [key: string]: number });

    this.pieChartData.labels = Object.keys(scores);
    this.pieChartData.datasets[0].data = Object.values(scores);
    this.pieChartData.datasets[0].backgroundColor = Object.keys(scores).map((_, index) => `hsl(${index * 70}, 70%, 50%)`);

    this.createPieChart();
  }

  createPieChart(): void {
    if (this.pieChart) {
      this.pieChart.destroy();
    }

    this.pieChart = new Chart('AssessmentPieChart', {
      type: 'pie',
      data: this.pieChartData,
      options: this.pieChartOptions
    });
  }

  @HostListener('window:resize', ['$event'])
  onResize(): void {
    if (this.pieChart) {
      clearTimeout(this.resizeTimeout);
      this.resizeTimeout = setTimeout(() => {
        this.pieChart.resize(); // Ensure chart resizes correctly
      }, 200); // Debounce resize by 200ms
    }
  }
}
