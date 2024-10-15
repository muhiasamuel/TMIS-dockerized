import { Component, inject } from '@angular/core';
import { FormGroup, FormBuilder, FormArray, FormControl } from '@angular/forms';
import { HttpServiceService } from '../../services/http-service.service';
import { HttpClient } from '@angular/common/http';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';

@Component({
  selector: 'app-otp-verification',
  templateUrl: './otp-verification.component.html',
  styleUrl: './otp-verification.component.scss'
})
export class OtpVerificationComponent {
  otpForm: FormGroup;
  digits: FormControl[] = [];
  isLoading:boolean = false;

  systemUser:any
  constructor(private fb: FormBuilder

  ) { }
  serve:HttpServiceService = inject(HttpServiceService);
  http: HttpClient = inject(HttpClient );
  snackBar: MatSnackBar = inject(MatSnackBar);
  route: Router = inject(Router)

  ngOnInit() {
    this.otpForm = this.fb.group({
      digits: this.fb.array(new Array(6).fill('').map(() => '')),
      username:['']
    });

    this.digits = (this.otpForm.controls['digits'].value as string[]).map(() => new FormControl('')) as FormControl[];
  }

  handleInput(event: any, index: number) {
    const input = event.target as HTMLInputElement;
    const value = input.value;

    if (value.length > 1) {
      input.value = value.charAt(0);
    }

    this.digits[index].setValue(input.value);

    if (value && index < this.digits.length - 1) {
      this.focusNextInput(index);
    }
  }

  handleKeyDown(event: KeyboardEvent, index: number) {
    const input = event.target as HTMLInputElement;

    if (event.key === 'Backspace' && !input.value && index > 0) {
      this.focusPrevInput(index);
    }
  }

  handlePaste(event: ClipboardEvent) {
    const pasteData = event.clipboardData.getData('text').slice(0, 6).split('');
    pasteData.forEach((char, index) => {
      this.digits[index].setValue(char);
      (document.getElementById('otp' + index) as HTMLInputElement).value = char;
    });
    event.preventDefault();
    const nextIndex = pasteData.length < 6 ? pasteData.length : 5;
    this.focusNextInput(nextIndex - 1);
  }

  private focusNextInput(index: number) {
    setTimeout(() => {
      (document.getElementById('otp' + (index + 1)) as HTMLInputElement).focus();
    }, 10);
  }

  private focusPrevInput(index: number) {
    setTimeout(() => {
      (document.getElementById('otp' + (index - 1)) as HTMLInputElement).focus();
    }, 10);
  }

  logOtp() {
    const otpValues = this.digits.map(control => control.value).join('');
    const username = this.otpForm.value.username
    console.log('Entered OTP:', otpValues + "username", username);
    if (otpValues == null || otpValues == undefined || otpValues == "" || otpValues.length < 6) {
      this.snackBar.open("OTP Field not filled", "Close", {duration:3600, horizontalPosition:"center", panelClass:"danger"})
    } else if(!username) {
      this.snackBar.open("OTP Field not filled", "Close", {duration:3600, horizontalPosition:"center", panelClass:"danger"})
    }else{
      this.isLoading = true;
      const otpData = {
        "username": username,
        "otp": otpValues
      }
      console.log(otpData);
      
    //http://192.168.88.36:8080/v1/api/auth/validateOtp
    const url = `${this.serve.serverUrl}auth/validateOtp`
     this.serve.postData(url,otpData ).subscribe({
      next: (res) =>{
        console.log("samiii",res);
        if(res.status === 200) {
          this.isLoading = false;
          this.systemUser = res;
         
          
          sessionStorage.setItem("user", JSON.stringify(this.systemUser))
          console.log(res);
          this.route.navigate(['/dashboard'])
        }
      
      },
      error: (error) => {
        this.isLoading = false;
        console.log(error);
        this.snackBar.open(error.error.message , "Close" , {duration: 2000})
      },
      complete: () =>{
        this.isLoading = false;
        this.snackBar.open("successful", "Close", {duration: 2000})
      }
    })
    }


   
  }
}