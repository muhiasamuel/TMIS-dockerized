import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { AdminDashboardComponent } from './layouts/admin-dashboard/admin-dashboard.component';
import { SelfAssessmentComponent } from './pages/self-assessment/self-assessment.component';
import { MyAssessmentsComponent } from './pages/single-attribute/my-assessments/my-assessments.component';

import { AsessMyTeamComponent } from './pages/asess-my-team/asess-my-team.component';
import { RolesComponent } from './pages/roles/roles.component';
import { SkillsAssessmentComponent } from './pages/skills-assessment/skills-assessment.component';
import { CriticalRolesAssessmentComponent } from './pages/critical-roles-assessment-home/critical-roles-assessment/critical-roles-assessment.component';
import { SuccessionPlanComponent } from './pages/succession-plan/succession-plan.component';
import { TalentMappingComponent } from './pages/talent-mapping/talent-mapping.component';
import { UsersComponent } from './pages/users/users.component';
import { AppraisalsComponent } from './pages/appraisals/appraisals.component';
import { UserComponent } from './pages/user/user.component';
import { MvpsComponent } from './pages/mvps/mvps.component';
import { HIPOsComponent } from './pages/hipos/hipos.component';

import { SignInComponent } from './authentication/sign-in/sign-in.component';
import { ErrorPageComponent } from './pages/error-page/error-page.component';
import { UserAssessmentComponent } from './pages/user-assessment/user-assessment.component';
import { AddPotentialDescriptorComponent } from './pages/add-potential-descriptor/add-potential-descriptor.component';
import { AddAssessmentQuestionsComponent } from './pages/add-assessment-questions/add-assessment-questions.component';
import { PotentialAttributesComponent } from './pages/potential-attributes/potential-attributes.component';
import { SingleAttributeComponent } from './pages/single-attribute/single-attribute.component';
import { AttributesComponent } from './pages/attributes/attributes.component';
import { ManagerAssessComponent } from './pages/managerAssessEmployee/manager-assess/manager-assess.component';
import { ManagerAssessEmployeeComponent } from './pages/managerAssessEmployee/manager-assess-employee/manager-assess-employee.component';

import { DoneByComponent } from './pages/asess-my-team/done-by/done-by.component';
import { SkillsViewComponent } from './pages/skills-view/skills-view.component';
import { CriticalRolesAssessmenHomeComponent } from './pages/critical-roles-assessment-home/critical-roles-assessmen-home.component';
import { ProfilesComponent } from './pages/profiles/profiles.component';
import { OtpVerificationComponent } from './authentication/otp-verification/otp-verification.component';

import { MyTeamsProfileComponent } from './pages/profiles/my-teams-profile/my-teams-profile.component';
import { DepartmentComponent } from './pages/department/department.component';
import { TalentComponent } from './pages/talent-mapping/talent/talent.component';
import { canActivateChild } from './authGuard';


const routes: Routes = [
  {
    path: '',
    component: SignInComponent,
    pathMatch: 'full',
  },
  { path: 'otp-verification',      component: OtpVerificationComponent },
   {
    path: '',
    component: AdminDashboardComponent,
    children: [
      { path: 'dashboard',      component: DashboardComponent},
      { path: 'assess-my-potential',      component:UserAssessmentComponent },
      { path: 'potential-attributes',      component: PotentialAttributesComponent },

      { path: 'employeeAssess/:id', component: AsessMyTeamComponent},
      { path: 'singleAtt/:id',      component: SingleAttributeComponent },
      { path: 'assQuests/:id',      component: MyAssessmentsComponent },
      { path: 'view-assessed-employee/:id/:assId', component:AttributesComponent},
      { path: 'assess-my-employee/:id/:assId', component: ManagerAssessEmployeeComponent },
      { path: 'assess-my-employee/:id/:assId', component: ManagerAssessEmployeeComponent },
      { path: 'assess/:id/:status' , component: DoneByComponent },
      { path: 'assess-my-team',      component: ManagerAssessComponent },
      { path: 'roles',      component: RolesComponent },
      { path: 'critical-skills-assessment',      component: SkillsAssessmentComponent },
      { path: 'critical-roles-home',      component: CriticalRolesAssessmenHomeComponent },
      { path: 'critical-roles-assessment',      component: CriticalRolesAssessmentComponent },
      { path: 'succession-plan',      component: SuccessionPlanComponent },
      { path: 'talent-mapping',      component: TalentMappingComponent },
      { path: 'users/:id',      component: UsersComponent },
      { path: 'appraisals',      component: AppraisalsComponent },
      { path: 'user',      component: UserComponent },
      { path: 'skills-view', component: SkillsViewComponent},
      { path: 'mvps',      component: MvpsComponent },
      { path: 'HIPOs',      component:  HIPOsComponent },
      { path: 'profiles',      component:  ProfilesComponent },
      { path: 'profile',      component:  ProfilesComponent },
      { path: 'MyTeamsProfile',      component:  MyTeamsProfileComponent },
      { path: 'user-assessment', component:UserAssessmentComponent},
      { path: 'add-attributes',      component: AddAssessmentQuestionsComponent },
      {path: 'departments', component:DepartmentComponent},
      { path: 'talent', component: TalentComponent}
     
  ]},
  {
    path: '**',
    component: ErrorPageComponent
  }
]


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
