import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AssessmentService } from '../../services/data/assessment.service';
import { log } from 'console';
import { Validators, FormBuilder, FormControl, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-self-assessment',
  templateUrl: './self-assessment.component.html',
  styleUrls: ['./self-assessment.component.scss']
})
export class SelfAssessmentComponent implements OnInit{
  options = [1,2,3,4,5]
  potential_attributes = [{ 'potential_attributes_name':'aspiration'},{'potential_attributes_name':'judgement'}]
  data :any;
  aspirationForm!: FormGroup;
  potentials:Array<any> = [
    {id: 1, Potintial: 'Aspiration', Descriptor:'Hunger for success. Has a strong desire to work hard to achieve goals, sets a high bar for themselves'},
    {id: 2, Potintial: 'Aspiration', Descriptor:'Purposeful. Has a clear sense of purpose and knows what mark they want to make, strategically selective about what initiatives to engage in'},
    {id: 1, Potintial: 'judjement', Descriptor:'Hunger for success. Has a strong desire to work hard to achieve goals, sets a high bar for themselves'},
    {id: 2, Potintial: 'judgement', Descriptor:'Purposeful. Has a clear sense of purpose and knows what mark they want to make, strategically selective about what initiatives to engage in'},
  ];

  ngOnInit(): void {
    this.aspirationForm = this.fb.group([]);
    this.potentials.forEach(item => {
      this.aspirationForm.addControl(item.Descriptor.toString(), new FormControl('', Validators.required));
    });
  }
  
  // selectedOption:string = '';
  // radioChangeHandler ( event:any) {
    
  //   this.data = {
  //     "potential":this.potentials,
  //     "selected Option":event.target.value
  //   }
  //   this.selectedOption = event.target.value
  //   console.log(this.data)
  // }
  // firstFormGroup = this._formBuilder.group({
  //   firstCtrl: ['', Validators.required],
  // });
  secondFormGroup = this.fb.group({
    secondCtrl: '',
  });
  isOptional = true;

  constructor(private fb: FormBuilder) {}

  onSubmit() {
    console.log(this.aspirationForm.value);
  }
}
  


