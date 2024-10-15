import { Component, Inject, OnInit } from '@angular/core';
import { HttpServiceService } from '../../services/http-service.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-view-critical-skill',
  templateUrl: './view-critical-skill.component.html',
  styleUrl: './view-critical-skill.component.scss'
})
export class ViewCriticalSkillComponent implements OnInit {
dataView: any;
authUser: any;
role: any;
id:any

  constructor( public dialogRef: MatDialogRef<ViewCriticalSkillComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any, private http: HttpServiceService, private snack:MatSnackBar) {}

  ngOnInit(): void {
    const user = sessionStorage.getItem("user");
    
    if(user){
      this.authUser = JSON.parse(user)
    }
    console.log(this.authUser);

    console.log('nnnnnn', this.data);
    
    if (this.data) {
      this.role = this.data.criticalSkill
    }
    console.log('roooooooooooo', this.role);
    
    console.log(this.role);
    this.criticalSkillView();
    
  }

  closeDialog() {
    this.dialogRef.close();
  }

criticalSkillView(){
  this.http.getCriticalSkill(this.role.skillAssessmentId).subscribe(
      ((res) =>{
        this.dataView = res.item
    console.log('view',this.dataView);
        
      }),
      ((e) => {
        this.snack.open(e.message.error, "Close", {duration:3600})
      }),
      () => {
        this.snack.open("sucessful", "Close", {duration:2000})
      }
    )
}
    
}
