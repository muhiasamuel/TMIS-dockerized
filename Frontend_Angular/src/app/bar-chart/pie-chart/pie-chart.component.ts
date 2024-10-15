import { Component, EventEmitter, HostListener, OnInit, Output } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Chart, ChartConfiguration } from 'chart.js';
import { HttpServiceService } from '../../services/http-service.service';

@Component({
  selector: 'app-pie-chart',
  templateUrl: './pie-chart.component.html',
  styleUrls: ['./pie-chart.component.scss']
})
export class PieChartComponent implements OnInit {
  @Output() assessmentsChange = new EventEmitter<any[]>();
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
    maintainAspectRatio: true,
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
  systemUser:any
  constructor(private http: HttpClient, private server: HttpServiceService) {}

  ngOnInit(): void {
    this.systemUser = JSON.parse(sessionStorage.getItem("user"));

    const userId = this.systemUser.user.userId
    this.loadAssessments(userId); // Assume user ID 2 for this example
  }

  loadAssessments(id: number): void {
    const url = `${this.server.serverUrl}user/${id}/scoring-history`;
    this.http.get<{ item: any[] }>(url).subscribe(response => {
      this.assessments = response.item;
      console.log("assessments", response.item);

      // Emit the assessments data to the parent
      this.assessmentsChange.emit(this.assessments);

      this.preparePieChartData();
    });
  }

  preparePieChartData(): void {
    // Aggregate overallScores for each assessment
    const scores = this.assessments.reduce((acc, assessment) => {
      if (!acc[assessment.assessmentName]) {
        acc[assessment.assessmentName] = 0;
      }
      acc[assessment.assessmentName] += assessment.overallScore;
      return acc;
    }, {} as { [key: string]: number });

    this.pieChartData.labels = Object.keys(scores);
    this.pieChartData.datasets[0].data = Object.values(scores);
    this.pieChartData.datasets[0].backgroundColor = Object.keys(scores).map((_, index) => `hsla(${index * 70}, 70%, 30%, 0.8)`);

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
