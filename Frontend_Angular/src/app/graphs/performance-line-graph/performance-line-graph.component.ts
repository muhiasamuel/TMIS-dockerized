import { HttpClient } from '@angular/common/http';
import { Component, HostListener } from '@angular/core';
import { Chart, ChartConfiguration } from 'chart.js';
import { HttpServiceService } from '../../services/http-service.service';

@Component({
  selector: 'app-performance-line-graph',
  templateUrl: './performance-line-graph.component.html',
  styleUrl: './performance-line-graph.component.scss'
})
export class PerformanceLineGraphComponent {
  chart: Chart<'line'>;
  performanceData: any = {};  // Store the performance data
  lineChartData: ChartConfiguration<'line'>['data'] = {
    labels: [],  // To store x-axis labels
    datasets: [] // To store performance metrics as y-axis data
  };
  lineChartOptions: ChartConfiguration<'line'>['options'] = {
    responsive: true,
    maintainAspectRatio: true,
    scales: {
      x: {
        title: {
          display: true,
          text: 'Year - Quarter'
        },
        ticks: {
          autoSkip: true,
          maxTicksLimit: 10
        }
      },
      y: {
        title: {
          display: true,
          text: 'Performance Metric'
        },
        min:2,
        beginAtZero: false, // Not needed if you're explicitly setting the min value
        ticks: {
          stepSize: 0.5,
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
    this.systemUser = JSON.parse(sessionStorage.getItem("user"))
    console.log('system user',this.systemUser);
    
    this.loadPerformanceData(this.systemUser.user.pf); // Load performance data on init
  }

  ngOnDestroy(): void {
    if (this.chart) {
      this.chart.destroy();
    }
  }

  loadPerformanceData(pf:number): void {
    console.log('performanceeeee',this.performanceData);
    
    const url = `${this.server.serverUrl}performances/get-by-user/${pf}`;
    this.http.get<{ item: any[]}>(url).subscribe(response => {
      
      this.performanceData = response.item;
      this.prepareChartData();
    });
  }

  prepareChartData(): void {
    // Prepare chart labels (x-axis) and data (y-axis)
    const labels: string[] = [];
    const performanceMetrics: number[] = [];

    this.performanceData.forEach(performance => {
      const year = performance.year.value;
      const quarter = performance.quarter;
      const metric = performance.performanceMetric;

      labels.push(`${year} - Q${quarter}`);
      performanceMetrics.push(metric);
    });

    // Update line chart data
    this.lineChartData.labels = labels;
    this.lineChartData.datasets = [
      {
        data: performanceMetrics,
        label: 'Performance Metric',
        fill: false,
        borderColor: 'blue',
        backgroundColor: 'blue',
        tension: 0.5
      }
    ];

    this.createLineChart();
  }

  createLineChart(): void {
    if (this.chart) {
      this.chart.destroy();
    }

    this.chart = new Chart("PerformanceLineChart", {
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

