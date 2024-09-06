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
  talentDesignations: { potentialrating2023: string; potentialrating2022: string; mapping2023: string; mapping2022: string };
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
  
  authUser:any;
   profile: Profile = {
    name: "Jane Doe",
    positionTitle: "Senior Software Engineer",
    level: "Senior",
    department: "Engineering",
    lineManagerName: "John Smith",
    lineManagerPositionTitle: "Engineering Manager",
    photo: "https://example.com/photos/jane_doe.jpg",
    previousRoles: [
      { positionTitle: "Software Engineer", employer: "Tech Solutions Inc." },
      { positionTitle: "Junior Software Developer", employer: "Innovate Ltd." }
    ],
    education: [
      { qualification: "Master of Science in Computer Science", university: "Stanford University" },
      { qualification: "Bachelor of Science in Computer Science", university: "University of California, Berkeley" }
    ],
    accreditations: [
      { accreditation: "Certified Scrum Master", body: "Scrum Alliance" },
      { accreditation: "AWS Certified Solutions Architect", body: "Amazon Web Services" }
    ],
    certifications: [
      { certification: "Certified Ethical Hacker", body: "EC-Council" },
      { certification: "PMP", body: "Project Management Institute" }
    ],
    skills: [
      { skill: "JavaScript", level: "Expert" },
      { skill: "Angular", level: "Advanced" },
      { skill: "Python", level: "Intermediate" }
    ],
    performanceHistory: [
      { period: "Q1 2024", rating: 4.5 },
      { period: "Q4 2023", rating: 4.7 }
    ],
    talentDesignations: {
      potentialrating2023: "High Potential",
      potentialrating2022: "High Potential",
      mapping2023: "Leadership",
      mapping2022: "Technical Expert"
    },
    isRoleCritical: "Yes",
    isMVP: "No",
    succession: {
      isDesignatedSuccessor: "Yes",
      potentialSuccessors: [
        { role: "Engineering Lead", readyStatus: "Ready" },
        { role: "Principal Engineer", readyStatus: "Ready Soon" }
      ]
    },
    recentKeyDevelopmentInterventions: [
      "Leadership Training Program",
      "Advanced Angular Workshop"
    ],
    talentDevelopmentPlans: [
      "Complete Advanced Leadership Training",
      "Gain additional certifications in cloud technologies"
    ],
    successionDevelopmentPlans: [
      "Mentor potential successors",
      "Prepare detailed succession plan for critical projects"
    ],
    careerAspirations: {
      shortTerm: "Lead a major product development project",
      longTerm: "Become a Chief Technology Officer (CTO)"
    }
  };
  

  constructor( private route: ActivatedRoute, private http:HttpServiceService, private router:Router) { }

  ngOnInit(): void {

    console.log("sadsfdf", this.profile);
    
    const user = localStorage.getItem("user")
    if (user) {
      this.authUser = JSON.parse(user)
      this.managerId = this.authUser.user.userId
    }

    this.route.queryParams.subscribe(params => {
      this.userId = params['userId'];
    });

    if (this.userId) {
      this.getEmployeeInfo(this.userId)

    } else {
      this.getEmployeeInfo(this.managerId)

    }
  
    console.log(this.userId);
    
    this.getEmployeePerformance()
    this.getEmployeeTalentRating()
    this.getEmployeeSuccessionInfo()

   }

   getEmployeeInfo(id:any){
    this.http.getEmployeeById(id).subscribe(
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