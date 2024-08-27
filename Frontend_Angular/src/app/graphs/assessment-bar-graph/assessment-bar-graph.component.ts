import { Component, Input, SimpleChanges } from '@angular/core';
import { Chart, ChartConfiguration } from 'chart.js';

@Component({
  selector: 'app-assessment-bar-graph',
  templateUrl: './assessment-bar-graph.component.html',
  styleUrl: './assessment-bar-graph.component.scss'
})
export class AssessmentBarGraphComponent {
  @Input() assessments: any[] = [];
  barChart: Chart<'bar'>;

  barChartData: ChartConfiguration<'bar'>['data'] = {
    labels: [],
    datasets: [
      {
        label: 'Manager Scores',
        data: [],
        backgroundColor: 'rgba(54, 162, 235, 1)'
      },
      {
        label: 'User Scores',
        data: [],
        backgroundColor: 'rgba(255, 99, 132, 1)'
      }
    ]
  };

  barChartOptions: ChartConfiguration<'bar'>['options'] = {
    responsive: true,
    maintainAspectRatio: true,
    scales: {
      x: {
        title: {
          display: true,
          text: 'Potential Attribute'
        }
      },
      y: {
        title: {
          display: true,
          text: 'Average Score'
        }
      }
    },
    plugins: {
      legend: {
        display: true,
        position: 'top'
      }
    }
  };

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['assessments'] && this.assessments.length) {
      this.prepareBarChartData();
    }
  }

  prepareBarChartData(): void {
    const attributeScores = {};
    const attributeCounts = {};

    // Aggregate scores for each potential attribute
    this.assessments.forEach(assessment => {
      assessment.assessmentStatuses.forEach(status => {
        const attribute = status.potentialAttributeName;
        if (!attributeScores[attribute]) {
          attributeScores[attribute] = { managerTotal: 0, userTotal: 0 };
          attributeCounts[attribute] = 0;
        }

        attributeScores[attribute].managerTotal += status.managerScore;
        attributeScores[attribute].userTotal += status.userScore;
        attributeCounts[attribute]++;
      });
    });

    // Calculate the averages
    const labels = [];
    const managerAverages = [];
    const userAverages = [];

    for (const attribute in attributeScores) {
      if (attributeScores.hasOwnProperty(attribute)) {
        const { managerTotal, userTotal } = attributeScores[attribute];
        const count = attributeCounts[attribute];

        labels.push(attribute);
        managerAverages.push(managerTotal / count);
        userAverages.push(userTotal / count);
      }
    }

    // Update chart data
    this.barChartData.labels = labels;
    this.barChartData.datasets[0].data = managerAverages;
    this.barChartData.datasets[1].data = userAverages;

    this.createBarChart();
  }

  createBarChart(): void {
    if (this.barChart) {
      this.barChart.destroy();
    }

    this.barChart = new Chart('AssessmentBarChart', {
      type: 'bar',
      data: this.barChartData,
      options: this.barChartOptions
    });
  }
}