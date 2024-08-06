import { DOCUMENT } from '@angular/common';
import { Component, Inject, OnInit } from '@angular/core';
import Chart from 'chart.js/auto';

@Component({
  selector: 'app-pie-chart',
  templateUrl: './pie-chart.component.html',
  styleUrl: './pie-chart.component.scss'
})
export class PieChartComponent implements OnInit{
  public realdata: any = [];
  public labeldata: any = [];
  public colordata: any =[];
  
  public chart: any;
   public criticalSkill = [
    {
      skill: 'programming',
      level: 6,
      colorcode: "ruby",
    },
    {
      skill: 'manager',
      level: 5,
      colorcode: "red",
    },
    {
      skill: 'dataanslysis',
      level: 6,
      colorcode: "maroon",
    },
    {
      skill: 'bookeeping',
      level: 4,
      colorcode: "brown",
    },
    {
      skill: 'accounting',
      level: 2,
      colorcode: "grey",
    }
  ]
  constructor(@Inject(DOCUMENT) private document: Document) {}

  ngOnInit(): void {
    // this.service.getChartInfo().subscribe((response) => {
    //   this.chartInfo = response;
      if (this.criticalSkill != null) {
        for (let i = 0; i < this.criticalSkill.length; i++) {
          this.labeldata.push(this.criticalSkill[i].skill);
          this.realdata.push(this.criticalSkill[i].level);
          this.colordata.push(this.criticalSkill[i].colorcode);

          
  const chartElement = this.document.getElementById('MyChart');
  if (chartElement) {
    const existingChart = Chart.getChart('MyChart');
    if (existingChart) {
      existingChart.destroy(); // Destroy existing chart if present
    }
  }
        }
        
      }
      this.chart = new Chart('MyChart', {
        type: 'pie', //this denotes tha type of chart
        data: {
          labels: this.labeldata,
          datasets: [
            {
              label: 'Critical Skill',
              data: this.realdata,
             // backgroundColor: this.colordata,
             backgroundColor: [
              'rgba(248, 41, 43, 0.8)',
              'rgba(229, 189, 12, 0.8)',
              'rgba(40, 170, 45, 0.8)'
            ],
            borderColor: [
              'rgba(248, 41, 43, 1)',
              'rgba(229, 189, 12, 1)',
              'rgba(40, 170, 45, 1)'
            ],
             
            },
          ],
        },
        options: {
          aspectRatio: 1.8,
          
        },
      });  
  }
}
