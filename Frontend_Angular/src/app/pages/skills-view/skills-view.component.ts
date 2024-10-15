import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { SkillsAssessmentComponent } from '../skills-assessment/skills-assessment.component';
import { HttpServiceService } from '../../services/http-service.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Toast } from 'ngx-toastr';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { ViewCriticalSkillComponent } from '../view-critical-skill/view-critical-skill.component';
import { EditCriticalSkillComponent } from '../edit-critical-skill/edit-critical-skill.component';

export interface UserData {
  skillName:string
  skillDescription: string,
  skillDevelopmentStrategy:string,
  businessPriority: string,
  currentSkillState: string,
  scarcityParameter: string,
  marketFluidity: string,
  developmentCostAndTimeCommitment: string,
  futureMarketAndTechRelevance: string,
  averageRating: string,
  actions: string
}

@Component({
  selector: 'app-skills-view',
  templateUrl: './skills-view.component.html',
  styleUrl: './skills-view.component.scss'
})


export class SkillsViewComponent implements OnInit {
  
  authUser:any;
  userData:any[] = [];
  updata:any;
  dataView:any[] = [];
  skillId:number 
  critical:any[]=[]
  medium:any[]=[]
  low:any[]=[]
  state:string;

  displayedColumns: string[] = [
    'index',
    "skillName",
    "averageRating",
    "currentSkillState",
    "actions"
    // "businessPriority",    
    // "scarcityParameter",
    // "marketFluidity",
    // "developmentCostAndTimeCommitment",
    // "futureMarketAndTechRelevance",
    // "averageRating"
];
  dataSource: MatTableDataSource<UserData>;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;


  constructor(private dialog:MatDialog, private http:HttpServiceService, private snack:MatSnackBar, ){
    
  }

  ngOnInit(){
    const user = sessionStorage.getItem("user")
    if (user) {
      this.authUser = JSON.parse(user);
    }
    this.getAssessedSkills();

  }
 
  // onViewClick(data:any) {
  //   this.dialog.open(ViewCriticalRoleComponent, {
  //     width:"80%",
  //     height:"65%",
  //     position:{
  //       right:"20px"
  //     },
  //     data:{
  //       criticalRole:data
  //     }

  //   })
  //   }

  viewSkill(data:any) {
    this.dialog.open(ViewCriticalSkillComponent,{
      width:"75%",
      height:"70%",
      position:{
        // bottom:"0",
        right:"1em"
      },
      data: {
        criticalSkill:data
      }
    })

  }


  editSkill(row:any) {

     const dialogRef:MatDialogRef<EditCriticalSkillComponent> = this.dialog.open(EditCriticalSkillComponent,{
      width: '75%',
      height:'85%',
      position:{
        right: '1em'
      },
      data: {
        roleEdit:row
      }
      
    })
    
    dialogRef.afterClosed().subscribe(
      ((res) =>{
        this.ngOnInit()
      })
    )
  }

    applyFilter(event: Event) {
      const filterValue = (event.target as HTMLInputElement).value;
      this.dataSource.filter = filterValue.trim().toLowerCase();
  
      if (this.dataSource.paginator) {
        this.dataSource.paginator.firstPage();
      }
    }

  assessCriticalSkills(){
   const ref:MatDialogRef<SkillsAssessmentComponent> = this.dialog.open(SkillsAssessmentComponent, {
      width:"75%",
      height:"90%",
      position:{
        bottom:"1em",
        right:"5em"
      }
    })

    ref.afterClosed().subscribe(
      ((res) => {
        this.ngOnInit()
      })
    )
  }



  getAssessedSkills(){
    this.http.getCriticalSkills(this.authUser.user.userId).subscribe(
      ((res) =>{
        if (res) {
          this.userData = res.item.sort((a,b) =>b.skillAssessmentId - a.skillAssessmentId)
          let data = []
          let critical = []
          let medium = []
          let low = []
          const i = res.item.forEach(element =>{
            if (element.averageRating >= 3.5) {
             const item = {
                ...element,
                state:"critical"
              }
              this.state = "critical"
              critical.push(item)

            }else if (element.averageRating < 3.5  && element.averageRating >=2.5) {
              const item = {
                ...element,
                state:"medium"
              }
              this.state = "medium"
              medium.push(item)
            } else {
              const item = {
                ...element,
                state:"low"
              }
              this.state = "low"
              low.push(item)
            }

            const item = {
              ...element,
              state:this.state
            }
            data.push(item)
          })


          this.dataSource = new MatTableDataSource(critical);
          this.critical = critical;
          this.medium = medium;
          this.low = low

          console.log("all",data );
          console.log("critical",this.critical );
          console.log("medium",this.medium );
          console.log("low",this.low );
          
        }
       
        
      }),
      ((e) => {
        this.snack.open(e.message.error, "Close", {duration:3600})
      }),
      () => {
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      }
    )
  }



}
