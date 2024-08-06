import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AssessmentService {
  aspiration:any=[
    {"id":1,"name":"Hunger for Success.", "Descriptor":"has strong desire to work hard"},
    {"id":2,"name":"Purposeful.","Descriptor":"Purposeful. Has a clear sense of purpose and knows what mark they want to make, strategically selective about what initiatives to engage in"},
    {"id":3,"name":"Activity.", "descriptor":"Looks for roles that require a personal commitment above the norm, prepared to offer discretionary effort beyond their role"},
    {"id":4,"name":"Power."}
  ]
  judgement:any=[
    {"id":1,"name":"accurate and indepth grasp of information.", "Descriptor":"has strong desire to work hard"},
    {"id":2,"name":"thinking creatively.",},
    {"id":3,"name":"converging on actions."},
    {"id":4,"name":"Balances Caution And courage."}
  ]
  drive:any=[
    {"id":1,"name":"Hunger for Success.", "Descriptor":"has strong desire to work hard"},
    {"id":2,"name":"Purposeful.",},
    {"id":3,"name":"Activity."},
    {"id":4,"name":"Power."}
  ]
  data:any[] = [
    this.aspiration,
    this.judgement,
    this.drive
  ]

  constructor() {}

  getData(){
    return this.data
  }
}
