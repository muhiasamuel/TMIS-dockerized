import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { HttpServiceService } from '../../services/http-service.service';
import { Router } from '@angular/router';

@Component({
    selector: 'user-cmp',
    templateUrl: 'user.component.html'
})

export class UserComponent implements OnInit{
    systemUser: any
    managerId: any
    teamMembers: any[] = []

    constructor(private router: Router, private http: HttpClient, private server: HttpServiceService){}
    ngOnInit(){
        this.systemUser = JSON.parse(sessionStorage.getItem("user"))
        if(this.systemUser.user.role.id == 1){
            this.managerId = this.systemUser.user.userId
            //console.log(this.systemUser)
        }else{
            this.managerId = this.systemUser.user.manager.userId
        }
        this.getTeamMembers(this.managerId)
    }

    getTeamMembers(manId: any){
        const url = `${this.server.serverUrl}manager/employees?managerId=${manId}`
        const response = this.http.get(url)
        response.subscribe(
            (value: any) => {
                if(value != null){
                    if(value.item != null){
                        this.teamMembers = value.item
                        console.log(this.teamMembers)
                    }
                }
            },
            (error: any) => {
                console.error();
                (error)
            }
        )
    }

    showUserDetails(userId: any){
        console.log("we are about to finish...")
        this.router.navigate(['/users', userId] ); // Navigate to AssessMyPotentialComponent

    }
}
