import { NgModule } from '@angular/core';
import { BrowserModule, provideClientHydration } from '@angular/platform-browser';

import {FormBuilder, Validators, ReactiveFormsModule} from '@angular/forms';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatStepperModule} from '@angular/material/stepper';
import {MatButtonModule} from '@angular/material/button';
import {MatSelectModule} from '@angular/material/select';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FooterComponent } from './sharedLayouts/footer/footer.component';
import { NavbarComponent } from './sharedLayouts/navbar/navbar.component';
import { SidebarComponent } from './sidebar/sidebar/sidebar.component';
import { AdminDashboardComponent } from './layouts/admin-dashboard/admin-dashboard.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatGridListModule} from '@angular/material/grid-list';

import { PluginComponent } from './sharedLayouts/plugin/plugin.component';
import { ToastrModule } from 'ngx-toastr';
import { SelfAssessmentComponent } from './pages/self-assessment/self-assessment.component';
import { AsessMyTeamComponent } from './pages/asess-my-team/asess-my-team.component';
import { SkillsAssessmentComponent } from './pages/skills-assessment/skills-assessment.component';
import { CriticalRolesAssessmentComponent } from './pages/critical-roles-assessment-home/critical-roles-assessment/critical-roles-assessment.component';
import { SuccessionPlanComponent } from './pages/succession-plan/succession-plan.component';
import { TalentMappingComponent } from './pages/talent-mapping/talent-mapping.component';
import { AppraisalsComponent } from './pages/appraisals/appraisals.component';
import { UsersComponent } from './pages/users/users.component';
import { RolesComponent } from './pages/roles/roles.component';
import { MvpsComponent } from './pages/mvps/mvps.component';
import { SignInComponent } from './authentication/sign-in/sign-in.component';
import { ErrorPageComponent } from './pages/error-page/error-page.component';
import { FormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatIconModule} from '@angular/material/icon';
import {MatRadioModule} from '@angular/material/radio';
import { UserAssessmentComponent } from './pages/user-assessment/user-assessment.component';
import { HTTP_INTERCEPTORS, HttpClientModule, provideHttpClient, withFetch, withInterceptorsFromDi } from '@angular/common/http';
import {MatDialogModule} from '@angular/material/dialog';
import {MatTabsModule} from '@angular/material/tabs';
import { DialogViewComponent } from './pages/dialog-view/dialog-view.component';
import { MatCardModule } from '@angular/material/card';
import {MatListModule} from '@angular/material/list';
import {MatExpansionModule} from '@angular/material/expansion';
import { provideCharts, withDefaultRegisterables } from 'ng2-charts';

import { BaseChartDirective  } from 'ng2-charts';

import { MatOptionModule } from '@angular/material/core';
import { MatPaginatorModule, MatPaginator } from '@angular/material/paginator';
import { MatTableModule } from '@angular/material/table';
import { AddAssessmentQuestionsComponent } from './pages/add-assessment-questions/add-assessment-questions.component';
import { AddPotentialDescriptorComponent } from './pages/add-potential-descriptor/add-potential-descriptor.component';
import { AsyncPipe, CommonModule } from '@angular/common';
import {provideNativeDateAdapter} from '@angular/material/core';
import { ViewDialogComponent } from './pages/asess-my-team/components/view-dialog/view-dialog.component';
import { MatTooltipModule } from '@angular/material/tooltip';
import { SkillsViewComponent } from './pages/skills-view/skills-view.component';
import { PotentialAttributesComponent } from './pages/potential-attributes/potential-attributes.component';
import { SingleAttributeComponent } from './pages/single-attribute/single-attribute.component';
import { AttributesComponent } from './pages/attributes/attributes.component';
import { TimerDialogComponent } from './pages/timer-dialog/timer-dialog.component';
import { ManagerAssessComponent } from './pages/managerAssessEmployee/manager-assess/manager-assess.component';
import { ManagerAssessEmployeeComponent } from './pages/managerAssessEmployee/manager-assess-employee/manager-assess-employee.component';
import { AddAttributeComponent } from './pages/add-attribute/add-attribute.component';
import { MyAssessmentsComponent } from './pages/single-attribute/my-assessments/my-assessments.component';
import { UserDoneAssessmentsComponent } from './pages/managerAssessEmployee/user-done-assessments/user-done-assessments.component';
import { DoneByComponent } from './pages/asess-my-team/done-by/done-by.component';
import { BarChartComponent } from './bar-chart/bar-chart.component';
import { PieChartComponent } from './bar-chart/pie-chart/pie-chart.component';
import { LineChartComponent } from './bar-chart/pie-chart/line-chart/line-chart.component';
import { ViewCriticalSkillComponent } from './pages/view-critical-skill/view-critical-skill.component';
import { CriticalRolesAssessmenHomeComponent } from './pages/critical-roles-assessment-home/critical-roles-assessmen-home.component';
import { ViewCriticalRoleComponent } from './pages/critical-roles-assessment-home/view-critical-role/view-critical-role.component';
import { EditCriticalRoleComponent } from './pages/critical-roles-assessment-home/edit-critical-role/edit-critical-role.component';
import { EditCriticalSkillComponent } from './pages/edit-critical-skill/edit-critical-skill.component';
import { AddStrategiesComponent } from './pages/critical-roles-assessment-home/add-strategies/add-strategies.component';
import { IntroPgeComponent } from './pages/intro-pge/intro-pge.component';
import { UserComponent } from './pages/user/user.component';
import { HIPOsComponent } from './pages/hipos/hipos.component';
import { EmployeesListingComponent } from './pages/employees-listing/employees-listing.component';
import { HiposInterventionsComponent } from './pages/hipos/hipos-interventions/hipos-interventions.component';
import { MvpsAssessmentComponent } from './pages/mvps/mvps-assessment/mvps-assessment.component';
import { CriticalRolesBarComponent } from './critical-roles-bar/critical-roles-bar.component';
import { ProfilesComponent } from './pages/profiles/profiles.component';
import { OtpVerificationComponent } from './authentication/otp-verification/otp-verification.component';
import { MyTeamsProfileComponent } from './pages/profiles/my-teams-profile/my-teams-profile.component';
import { AuthInterceptorsService } from './services/auth-interceptors.service';
import { DepartmentComponent } from './pages/department/department.component';
import { AddDepartmentComponent } from './pages/department/add-department/add-department.component';
import { TalentComponent } from './pages/talent-mapping/talent/talent.component';
import { ReadyNowDialogComponent } from './succession-plan/ready-now-dialog/ready-now-dialog.component';
import { CreateEmployeeComponent } from './create-employee/create-employee.component';

import { AssessmentHistoryInfoComponent } from './assessment-history-info/assessment-history-info.component';
import { AssessmentLineGraphComponent } from './graphs/assessment-line-graph/assessment-line-graph.component';
import { AssessmentBarGraphComponent } from './graphs/assessment-bar-graph/assessment-bar-graph.component';



@NgModule({
  declarations: [
     AppComponent,
     FooterComponent,
     NavbarComponent,
     SidebarComponent,
    AdminDashboardComponent,
    PluginComponent,
    SelfAssessmentComponent,
    AsessMyTeamComponent,
    SkillsAssessmentComponent,
    CriticalRolesAssessmentComponent,
    HiposInterventionsComponent,
    SuccessionPlanComponent,
    TalentMappingComponent,
    ProfilesComponent ,
    MyTeamsProfileComponent ,
    AppraisalsComponent,
    UsersComponent,
    RolesComponent,
    MvpsComponent,
    ViewDialogComponent,
    SignInComponent,
    ErrorPageComponent,
    UserAssessmentComponent,
    ReadyNowDialogComponent,
    AddAssessmentQuestionsComponent,
    AddPotentialDescriptorComponent,
    SkillsViewComponent,
    PotentialAttributesComponent,
    SingleAttributeComponent,
    AttributesComponent,
    TimerDialogComponent,
    ManagerAssessComponent,
    AssessmentHistoryInfoComponent,
    ManagerAssessEmployeeComponent,
    AddAttributeComponent,
    MyAssessmentsComponent,
    UserDoneAssessmentsComponent,
    DoneByComponent,
    DialogViewComponent,
    DashboardComponent,
    BarChartComponent,
    PieChartComponent,
    LineChartComponent,
    ViewCriticalSkillComponent,
    CriticalRolesAssessmenHomeComponent,
    ViewCriticalRoleComponent,
    EditCriticalRoleComponent,
    EditCriticalSkillComponent,
    AddStrategiesComponent,
    OtpVerificationComponent,
    IntroPgeComponent,
    UserComponent,
    HIPOsComponent,
    EmployeesListingComponent,
    HiposInterventionsComponent,
    MvpsAssessmentComponent,
    CriticalRolesBarComponent,
    ProfilesComponent,
    MyTeamsProfileComponent,
    DepartmentComponent,
    AddDepartmentComponent,
    TalentComponent,
    CreateEmployeeComponent,
    AssessmentHistoryInfoComponent,
    AssessmentLineGraphComponent,
    AssessmentBarGraphComponent,
    
   
  ],
  imports: [
    BrowserModule,
    MatStepperModule,
    MatDatepickerModule,
    AsyncPipe,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatStepperModule,
    MatTableModule,
    BaseChartDirective,
    MatRadioModule,
    MatPaginatorModule,
    MatTableModule,
    FormsModule,
    MatListModule,
    MatExpansionModule,
    MatSelectModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatPaginator,
    MatTableModule,
    MatCardModule,
    NgbModule,
    MatIconModule,
    MatSelectModule,
    HttpClientModule,
    MatIconModule,
    MatSelectModule,
    HttpClientModule,
    MatPaginatorModule,
    FormsModule,
    MatDialogModule,
    MatTooltipModule,
    MatGridListModule,
    MatTabsModule,
    MatGridListModule,
    CommonModule,
    
    ToastrModule.forRoot()
    
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptorsService, multi: true },
    provideHttpClient(),
    provideCharts(withDefaultRegisterables())
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }



