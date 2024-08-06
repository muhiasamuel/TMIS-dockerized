import { Component, OnInit } from '@angular/core';
import { HttpServiceService } from '../../services/http-service.service';
import { Router } from '@angular/router';


@Component({
  selector: 'app-talent-mapping',
  templateUrl: './talent-mapping.component.html',
  styleUrl: './talent-mapping.component.scss'
})
export class TalentMappingComponent implements OnInit {
  managerId: any;
  talentBox: any;
  averagePerformance:number= 0;
  constructor(private http:HttpServiceService, private route: Router) {}

  ngOnInit() {   
    const user = localStorage.getItem("user");
    
      this.managerId = JSON.parse(user)
    
    console.log("manager",this.managerId);

    this.displayTalent()

  }
  displayTalent() {
    this.http.getTalent(this.managerId.user.userId).subscribe(
       (res) => {
             
        this.talentBox = res.items
        console.log('new', this.talentBox);
   
    
       },
       (err) => {
         console.log('error', err); 
       },
       () => {
        //  alert('done');
       }
    );
   } 


  }

