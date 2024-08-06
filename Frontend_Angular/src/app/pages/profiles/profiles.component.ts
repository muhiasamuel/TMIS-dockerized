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
  userId: any;
  profileData: any;
  employee: any;
  employeeInfo:any;
  employeePerformances:any
  photo1:string = ''
  profile: Profile = {
    photo: '../../../assets/img/owner_photo.PNG',
    previousRoles: [
      { positionTitle: 'Position Title 1', employer: 'Employer 1' },
      { positionTitle: 'Position Title 2', employer: 'Employer 2' },
      { positionTitle: 'Position Title 3', employer: 'Employer 3' },
      { positionTitle: 'Position Title 4', employer: '' }
    ],
    education: [
      { qualification: 'MBA', university: 'UoN' },
      { qualification: 'BSc Bio Medical Engineering', university: 'UoN' }
    ],
    accreditations: [
      { accreditation: 'Chartered MCIPD', body: 'CIPD UK' }
    ],
    certifications: [
      { certification: 'Executive Coach', body: 'AoEC' }
    ],
    skills: [
      { skill: 'Coding in Java', level: 'Advanced' },
      { skill: 'Workplace Mediation', level: 'Advanced' }
    ],
    performanceHistory: [
      { period: 'Half Year 2023', rating: 4 },
      { period: 'Full Year 2022', rating: 3.5 },
      { period: 'Half Year 2022', rating: 4.2 },
      { period: 'Full Year 2021', rating: 4.0 }
    ],
    talentDesignations: {
      potential2023: 'A',
      potential2022: 'B',
      mapping2023: 'H1',
      mapping2022: 'H1'
    },
    isRoleCritical: 'Yes',
    isMVP: 'Yes',
    succession: {
      isDesignatedSuccessor: 'Yes',
      potentialSuccessors: [
        { role: 'XXXX', readyStatus: 'R1-2' },
        { role: 'XXXXXX', readyStatus: 'R>2' },
        { role: 'XXXXX', readyStatus: 'R>2' }
      ]
    },
    recentKeyDevelopmentInterventions: [
      'Master Class in Leadership',
      'Project Management Training',
      'Assignment to DRC'
    ],
    talentDevelopmentPlans: [
      'Xxx',
      'Xxxx',
      'xxxx'
    ],
    successionDevelopmentPlans: [
      'Xxxxx',
      'Xxxxx',
      'Xxxxx'
    ],
    careerAspirations: {
      shortTerm: 'Group Head ODE',
      longTerm: 'Group Director HR Operations'
    },
    name: '',
    positionTitle: '',
    level: '',
    department: '',
    lineManagerName: '',
    lineManagerPositionTitle: ''
  };
  managerId: number;
  EmployeeTalentRating: any;


  constructor( private route: ActivatedRoute, public http:HttpServiceService) { }

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
        this.employeeInfo = res.item
        
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

}
