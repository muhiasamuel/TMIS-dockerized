import { Component, AfterViewInit, OnDestroy, HostListener } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Chart, ChartConfiguration } from 'chart.js';
import { HttpServiceService } from '../../services/http-service.service';

@Component({
  selector: 'app-assessment-line-graph',
  templateUrl: './assessment-line-graph.component.html',
  styleUrls: ['./assessment-line-graph.component.scss']
})
export class AssessmentLineGraphComponent implements AfterViewInit, OnDestroy {
  chart: Chart<'line'>;
  assessments: any[] = [];
  lineChartData: ChartConfiguration<'line'>['data'] = {
    labels: [],
    datasets: []
  };
  lineChartOptions: ChartConfiguration<'line'>['options'] = {
    responsive: true,
    maintainAspectRatio: true,
    scales: {
      x: {
        title: {
          display: true,
          text: 'Assessment Date'
        },
        ticks: {
          autoSkip: true,
          maxTicksLimit: 10
        }
      },
      y: {
        title: {
          display: true,
          text: 'Score'
        },
        min:2,
        beginAtZero: false,
        ticks: {
          stepSize: 0.5
        }
      }
    },
    plugins: {
      tooltip: {
        enabled: true,
        mode: 'index',
        intersect: false,
        callbacks: {
          label: (context) => {
            const label = context.dataset.label || '';
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



  private hasRendered = false;
  private resizeTimeout: any;
  systemUser: any;

  constructor(private http: HttpClient, private server: HttpServiceService) {}

  ngAfterViewInit(): void {
    this.systemUser = JSON.parse(localStorage.getItem("user"));
    const userId = this.systemUser.user.userId
    this.loadAssessments(userId); // Assume user ID 2 for this example
  }

  ngOnDestroy(): void {
    if (this.chart) {
      this.chart.destroy();
    }
  
  }

  loadAssessments(id: number): void {
    const url = `${this.server.serverUrl}user/${id}/scoring-history`;
    this.http.get<{ item: any[] }>(url).subscribe(response => {
      this.assessments = response.item;
      this.prepareChartData();
      
    });
  }

  prepareChartData(): void {
    const attributeData = new Map<string, { dates: string[], scores: number[] }>();

    this.assessments.forEach(assessment => {
      const assessmentDate = `${assessment.assessmentDate[0]}-${assessment.assessmentDate[1]}-${assessment.assessmentDate[2]}`;
      
      assessment.assessmentStatuses.forEach(status => {
        if (!attributeData.has(status.potentialAttributeName)) {
          attributeData.set(status.potentialAttributeName, { dates: [], scores: [] });
        }

        const data = attributeData.get(status.potentialAttributeName);
        data.dates.push(assessmentDate);
        data.scores.push(status.userScore);
      });
    });

    this.lineChartData.labels = Array.from(attributeData.values())[0]?.dates || [];

    this.lineChartData.datasets = Array.from(attributeData.entries()).map(([attributeName, data], index) => {
      return {
        data: data.scores,
        label: attributeName,
        fill: false,
        borderColor: `hsl(${index * 70}, 70%, 50%)`,
        backgroundColor: `hsl(${index * 70}, 70%, 50%)`,
        tension: 0.5
      };
    });

    this.createLineChart();
  }

 

  createLineChart(): void {
    if (this.chart) {
      this.chart.destroy();
    }

    this.chart = new Chart("AssessmentLineChart", {
      type: 'line',
      data: this.lineChartData,
      options: this.lineChartOptions
    });

    setTimeout(() => {
      if (!this.hasRendered) {
        this.chart.resize();
        this.chart.update();
        this.hasRendered = true;
      }
    }, 100);
  }


  @HostListener('window:resize', ['$event'])
  onResize(event) {
    clearTimeout(this.resizeTimeout);
    this.resizeTimeout = setTimeout(() => {
      if (this.chart && this.hasRendered) {
        this.chart.resize();
        this.chart.update(); // Ensure the chart size is updated correctly
      }
   
    }, 200); // Debounce resize by 200ms
  }
}
