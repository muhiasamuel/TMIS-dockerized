import { DOCUMENT } from '@angular/common';
import { Component, Inject, Input, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import Chart from 'chart.js/auto';
import { HttpServiceService } from '../services/http-service.service';

@Component({
  selector: 'app-bar-chart',
  templateUrl: './bar-chart.component.html',
  styleUrl: './bar-chart.component.scss'
})
export class BarChartComponent implements OnInit {
  @Input() criticalSkills: any[] = [];
  @Input() items: any[] = []
  labelList: string[] = [];
  totalCountList: string[] = [];
   chart: any;
   authUser:any
lineChartData: any;
lineChartOptions: any;
  
  constructor(
    @Inject(DOCUMENT) private document: Document,
    private snack: MatSnackBar,
    private http: HttpServiceService,
  ) {
    // const i = this.item.forEach(e => {
    //   console.log(e)
    // })
  }

  ngOnDestroy(): void {
    //Called once, before the instance is destroyed.
    //Add 'implements OnDestroy' to the class.
        // Ensure the chart is destroyed when the component is destroyed
        if (this.chart) {
          this.chart.destroy();
        }

  }


  ngOnInit(): void { 
    const user = sessionStorage.getItem("user")
    if (user) {
      this.authUser = JSON.parse(user)
    }
  

    this.getGraphData()
  }

  ngAfterViewInit(): void {
    //Called after ngAfterContentInit when the component's view has been initialized. Applies to components only.
    //Add 'implements AfterViewInit' to the class.

  }

  getGraphData(){
    this.http.getCriticalSkills(this.authUser.user.userId).subscribe(
      ((res)=>{
        if (res.item) {
          const i = res.item.forEach((e:any) =>{
            if (e.averageRating >= 3.5) {
              this.calculateAndPushData(
                e.averageRating || 0,
                e.skillName
              )   
            }
           
          })
          this.setupChart(this.labelList, this.totalCountList)
    
        }
      }),
      ((error)=>{
        this.snack.open(error.error.message, "Close", {duration:3600})
      }),
      () =>{}
    )
    const criticalSkills = sessionStorage.getItem("criticalSkills")

   
  }
  calculateAndPushData(total: any, label: string) {
    this.totalCountList.push(total);
    this.labelList.push(label);
  }

  private setupChart(labelList: string[], totalCountList: string[]): void {
    let ctx;

  // Destroy existing chart if it exists
  if (this.chart) {
    this.chart.destroy();
    ctx = null
  }

  ctx = document.getElementById('barChart') as HTMLCanvasElement;
    this.chart = new Chart(ctx, {
      type: 'bar',
      data: {
        labels: labelList,
        datasets: [
          {
            data: totalCountList,
            backgroundColor: [
              'rgba(8, 1, 3, 0.5)',
              'rgba(9, 89, 212, 0.5)',
              'rgba(40, 170, 45, 0.5)'
            ],
            borderColor: [
              'rgba(248, 41, 43, 0.1)',
              'rgba(229, 189, 12, 0.1)',
              'rgba(40, 170, 45, 0.1)'
            ],
            borderWidth: 1,
            label: 'Critical skill',
            
          },
        ],
      },
      options: {
        responsive: true,
        maintainAspectRatio: true,
        indexAxis:'y'
      },
    });
  }
}
