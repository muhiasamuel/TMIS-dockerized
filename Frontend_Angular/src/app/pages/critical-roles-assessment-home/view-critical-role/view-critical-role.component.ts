import { DIALOG_DATA } from '@angular/cdk/dialog';
import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';
import { HttpServiceService } from '../../../services/http-service.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { EditCriticalRoleComponent } from '../edit-critical-role/edit-critical-role.component';

@Component({
  selector: 'app-view-critical-role',
  templateUrl: './view-critical-role.component.html',
  styleUrl: './view-critical-role.component.scss'
})
export class ViewCriticalRoleComponent {

  role:any;
  authUser:any;
  criticalRole:any;
  constructor(  public dialogRef: MatDialogRef<ViewCriticalRoleComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,private dialog:MatDialog, private http:HttpServiceService, private snack:MatSnackBar){}

    ngOnInit(): void {
      const user = localStorage.getItem("user");
console.log('YESSSSSSSS', this.data);
      
      if(user){
        this.authUser = JSON.parse(user)
      }
      console.log('eeeeeeee',this.authUser);
      
      if (this.data) {
        this.role = this.data.criticalRole
      }
      console.log('roooooooooooo', this.role);
      
      //Called after the constructor, initializing input properties, and the first call to ngOnChanges.
      //Add 'implements OnInit' to the class.
      this.criticalRoleView()
      
    }

  criticalRoleEdit(row:any){
    this.dialogRef.close()
      this.dialog.open(EditCriticalRoleComponent, {
      width:"85%",
      height:"90%",
      position:{
        right:"2em",
        bottom:"30px"
      },
      data:{
        role:row
      }
    })
  }

    //close dialog
    closeDialog(){
      this.dialogRef.close()
    }

    //view critical role info
    criticalRoleView(){
      this.http.getCriticalRoleByID(this.role.criticalRoleId).subscribe(
        ((response) =>{
          console.log("tabby",response);
          this.criticalRole = response.item
        }),
        ((error) =>{
          this.snack.open("an error occurred viewing this critical role", "close", {duration:3600})
        }),
        ()=>{

        }
      )
    }


}
