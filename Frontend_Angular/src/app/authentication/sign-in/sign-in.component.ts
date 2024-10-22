import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HttpServiceService } from '../../services/http-service.service';
import { HttpClient } from '@angular/common/http';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html',
  styleUrl: './sign-in.component.scss'
})
export class SignInComponent {

  isLoading: boolean = false;
  loginForm:FormGroup
  user = {
    "username":"CM0002Ka",
    "password":"katana"
    //"username":"GK0001Ch",
    //"password":"1234"

  }
 
  constructor(private route:Router, private _fb: FormBuilder,
      private snackbar:MatSnackBar,
     private http: HttpClient, private server: HttpServiceService){
      this.loginForm = new FormGroup({
        username :new FormControl('', Validators.required),
        password : new FormControl('', Validators.required),
      })
     }
  login(){
    this.isLoading = true;
    //http://192.168.88.36:8080/v1/api/auth/authenticate
    const url = `${this.server.serverUrl}auth/authenticate`
    const response = this.http.post(url, this.loginForm.value)

    
    response.subscribe(
      (response: any) => {
        this.isLoading = false;
        if(response.status = 200){
          
            // localStorage.setItem("user", JSON.stringify(this.systemUser));
            console.log(response)
            this.route.navigate(['/otp-verification'])
            this.snackbar.open(response.message, "Close", {duration:2000} )
        }
      },
      (error: any) => {
        this.isLoading = false;
        this.snackbar.open(error.error.message, "Close", {duration:2000} )
        console.log(error)
      }
    )
  }

  // getUserDetails(user: any){
    
  // }

}
