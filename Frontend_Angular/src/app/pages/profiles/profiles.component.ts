import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpServiceService } from '../../services/http-service.service';
import { log } from 'console';


interface Profile {
  name: string;
  positionTitle: string;
  level: string;
  department: string;
  lineManagerName: string;
  lineManagerPositionTitle: string;
  photo: string;
  previousRoles: { positionTitle: string; employer: string }[];
  education: { qualification: string; university: string }[];
  accreditations: { accreditation: string; body: string }[];
  certifications: { certification: string; body: string }[];
  skills: { skill: string; level: string }[];
  performanceHistory: { period: string; rating: number }[];
  talentDesignations: { potential2023: string; potential2022: string; mapping2023: string; mapping2022: string };
  isRoleCritical: string;
  isMVP: string;
  succession: { isDesignatedSuccessor: string; potentialSuccessors: { role: string; readyStatus: string }[] };
  recentKeyDevelopmentInterventions: string[];
  talentDevelopmentPlans: string[];
  successionDevelopmentPlans: string[];
  careerAspirations: { shortTerm: string; longTerm: string };

}

@Component({
  selector: 'app-profiles',
  templateUrl: './profiles.component.html',
  styleUrl: './profiles.component.scss'
})
export class ProfilesComponent implements OnInit {
  userId: string;
  profileData: any;
  employee: any;
  employeeInfo:any;
  employeePerformances:any
  photo1:string = ''
  managerId: number;
  EmployeeTalentRating: any;
  profile: any;


  constructor( private route: ActivatedRoute, private http:HttpServiceService, private router:Router) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.userId = params['userId'];
    });
  
    console.log(this.userId);
    
    this.getEmployeeInfo()
    this.getEmployeePerformance()
    this.getEmployeeTalentRating()
    this.getEmployeeSuccessionInfo()

   }

   getEmployeeInfo(){
    this.http.getEmployeeById(this.userId).subscribe(
      ((res) => {
        console.log("stexooooo",res.item);
        this.employeeInfo = res.item;
        this.updateProfileWithEmployeeInfo(res.item);
        
      }),
      ((error) => {}),
      () => {}
    )
   }

   getEmployeePerformance(){
this.http.getThreeYearsEmpPerformances(this.managerId).subscribe(
  ((res)=>{
    console.log(res.item);
    this.employeePerformances=res.item
  }),
  ((error) => {}),
  () => {}
)
   }

   getEmployeeTalentRating(){
    this.http.getTalent(this.managerId).subscribe(
      ((res)=>{
        console.log(res.item);
        this.EmployeeTalentRating=res.item
      }),
      ((error) => {}),
      () => {}
    )
   }

   getEmployeeSuccessionInfo(){

   }
   updateProfileWithEmployeeInfo(employeeInfo: any) {
    this.profile.name = employeeInfo.userFullName || '';
    this.profile.positionTitle = employeeInfo.positionName || '';
    this.profile.level = employeeInfo.employeeLevel || '';
    this.profile.department = employeeInfo.departmentName || '';
    this.profile.lineManagerName = employeeInfo.employeeDetails?.lineManagerName || '';
    this.profile.lineManagerPositionTitle = employeeInfo.employeeDetails?.lineManagerPositionTitle || '';
    this.photo1 = employeeInfo.photo || '../../../assets/img/owner_photo.PNG';
  }

}