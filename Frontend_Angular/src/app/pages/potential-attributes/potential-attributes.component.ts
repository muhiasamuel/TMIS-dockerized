import { Component } from '@angular/core';
import { HttpServiceService } from '../../services/http-service.service';
import { HttpClient } from '@angular/common/http';
import { Dialog } from '@angular/cdk/dialog';
import { AttributesComponent } from '../attributes/attributes.component';
import { AddPotentialDescriptorComponent } from '../add-potential-descriptor/add-potential-descriptor.component';
import { AddAttributeComponent } from '../add-attribute/add-attribute.component';
import {  Router } from '@angular/router';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { log } from 'console';
import { AddAssessmentQuestionsComponent } from '../add-assessment-questions/add-assessment-questions.component';
import { url } from 'inspector';
import moment from 'moment';
import { MatSnackBar } from '@angular/material/snack-bar';
import { PageEvent } from '@angular/material/paginator';

@Component({
  selector: 'app-potential-attributes',
  templateUrl: './potential-attributes.component.html',
  styleUrl: './potential-attributes.component.scss'
})
export class PotentialAttributesComponent {
  systemUser: any
  userRoleId: any
  status:boolean= false;
  title = "Potential Attributes"
  name="Assesements"
  currentPage: number = 1;
  itemsPerPage: number = 6;
  totalPages: number = 1;
  potentialAttribute: any[] = []
  Assesement: any[] = []
  Assesements: any[] = []
  paginatedAssessments:any[] = []
  selectedAssessmentType: string | null = null;
  filterTerm:string=''

    // Paginator settings
    pageSize = 5;
    pageIndex = 0;

  constructor(private route: Router, 
    private http: HttpServiceService, 
    private httpClient: HttpClient,
    private snackBar: MatSnackBar, 
    private dialog: MatDialog){}
  ngOnInit(){
    this.systemUser = JSON.parse(localStorage.getItem("user"))
    console.log(this.systemUser)
    if(this.systemUser != null){
      this.userRoleId = this.systemUser.user.role.id
      if(this.systemUser.user.role.id == 1){
        this.getManagerAttributes(this.systemUser.user.userId)
      }else{
        this.getManagerAttributes(this.systemUser.user.manager?.userId)

      }

    }else{
      this.route.navigate([''])
    }
    this.getAssesements()
  }

  handlePageEvent(event: PageEvent): void {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
  }

  filteredAssesements(){
    console.log('filter',this.filteredAssesements);
    
    if(!this.filterTerm){
      return this.Assesement;
    }
    return this.Assesement.filter(assesement=>
      assesement.assesmentName.toLowerCase().includes(this.filterTerm.toLowerCase()) ||
      assesement.assesmentDescription.toLowerCase().include(this.filterTerm.toLowerCase())
    )
  }
  
  getManagerAttributes(managerId: any){
    //http://192.168.2.21:8080/getManagerAttributes?managerId=1

    const url = `${this.http.serverUrl}getManagerAttributes?managerId=${managerId}`
    console.log("url",url)

    const res = this.httpClient.get(url)

    res.subscribe(
      (response: any) => {
        console.log("Api response", response)
        this.potentialAttribute = response.item
      },

      (error: any) => {
        console.log("Api response", error)

      }
    )

  }

  getAssesements(){
    this.http.getAllAssesements().subscribe(
    ((res)=>{
      this.paginatedAssessments = res.item.sort((a,b) => b.assessmentId - a.assessmentId)
      this.Assesement = res.item.sort((a,b) => b.assessmentId - a.assessmentId)
      
      console.log('assessements',this.paginatedAssessments);
      const i = this.paginatedAssessments.forEach((assessment) => {
        console.log(assessment);
        
        if (assessment.status == "Active") {
          this.status = true
        }
      })
      this.updatePagination()
    }),
    ((error)=>{
      console.log(error);
      
    }),
    ()=>{}
    )
  }

  openDialog(){
    // this.dialog.open(AddAttributeComponent)
      this.dialog.open(AddPotentialDescriptorComponent,{
       width: "50vw"
      })
  }

  openAssesementDialog(){
    const dialogref:MatDialogRef<AddAssessmentQuestionsComponent> = this.dialog.open(AddAssessmentQuestionsComponent,{
      width:"60vw",
      position:{
        right:"20em"
      } 
    
    })
    dialogref.afterClosed().subscribe(
      ((res) => {
        this.getAssesements()
      })
    )
  }

  sendAssessment(){
    const formattedEndDate = moment().add(1, 'month').format('YYYY-MM-DD');
    const targetData = {
      assessmentDescription:"This assessment evaluates employees on four key potential attributes: Aspiration, Judgement, Drive, and Change Agility." ,
      assessmentName: "Quick Assessment on" + ' ' + moment().format('YYYY-MM-DD hh:mm A'),
      target:"All Employees",
      endDate: formattedEndDate,
      createdAt: moment().format('YYYY-MM-DD')
    };

   //this.addAssessmentQuestions(targetData,serverData)


    console.log("target data",targetData);
    
    
    this.http.postAssesement(targetData, this.systemUser.user.userId).subscribe(
      ((res) =>{
        console.log("");
        let assessments = res.item.assessments;
        console.log("assess",res);
        
      }),
      ((error) =>{
        this.snackBar.open(error.error.message, "Close", {duration: 3000})
        console.error(error);
        
      }),
      ()=>{
        this.snackBar.open("Assesment added sucessfully", "Close", {duration: 3000})
        this.getAssesements()
      }  
    
  )}

  filterSearchData(): void {
   
    if (this.filterTerm.trim()) {
      this.Assesement = this.paginatedAssessments.filter(item =>
        item.assessmentName.toLowerCase().includes(this.filterTerm.toLowerCase())
      );
    } else {
      this.Assesement = [...this.paginatedAssessments]; // Reset to original data if filterText is empty
    }
     this.updatePagination();
  }

  updatePagination(): void {
    this.totalPages = Math.ceil(this.paginatedAssessments.length / this.itemsPerPage);
    this.paginate();
  }

  paginate(): void {
    const start = (this.currentPage - 1) * this.itemsPerPage;
    const end = start + this.itemsPerPage;
    this.Assesement = this.paginatedAssessments.slice(start, end);
  }

  prevPage(): void {
    if (this.currentPage > 1) {
      this.currentPage--;
      this.paginate();
    }
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
      this.paginate();
    }
  }
  

}
