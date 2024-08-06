import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-error-page',
  templateUrl: './error-page.component.html',
  styleUrl: './error-page.component.scss'
})
export class ErrorPageComponent  {

  constructor(private route:Router){}
  goBack(){
    this.route.navigate(['/dashboard'])
  }


}
