import { Component } from '@angular/core';
import { HttpServiceService } from '../../services/http-service.service';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-dialog-view',
  templateUrl: './dialog-view.component.html',
  styleUrl: './dialog-view.component.scss'
})
export class DialogViewComponent {
  displayedColumns = ['First name', 'Last name', 'Average score','Manager score', 'action']
  assName: any
  assDescription: any
  systemUser: any
  dataSource: any[] = []
  assId: any


  constructor(private router: Router, private http: HttpClient, private server: HttpServiceService, private route: ActivatedRoute){}

  ngOnInit(){
    this.systemUser = JSON.parse(localStorage.getItem('user'))
    this.route.params.subscribe(params => {
      this.assId = params['id']; // Access the 'id' parameter from the URL
      console.log('Test ID:', this.assId);
      console.log('manager ID:', this.systemUser.userId);

    });

    this.getAssessedAndUnassessed(this.assId, this.systemUser.user.userId)
  }

  getAssessedAndUnassessed(assId: any, userId: any){
    const url = `${this.server.serverUrl}getAssessedAndUnAssessed?assId=${assId}&managerId=${userId}`
    const response = this.http.get(url);

    response.subscribe(
      (value: any) => {
        console.log(value)
        this.assName = value.item.assName
        this.assDescription = value.item.assDescription
        this.dataSource = value.item.assessed
      },
      (error: any) => {
        console.log(error)

      }
    )
  }

   // method to handle button click
   goToAssessMyPotential(employeeId:number): void {
    console.log('Button clicked!')
    this.router.navigate(['/view-assessed-employee', employeeId, this.assId] ); // Navigate to AssessMyPotentialComponent
    
  }

}
