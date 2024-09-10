import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, FormArray, Validators, FormControl } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { HttpServiceService } from '../../../../services/http-service.service';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { State } from '@popperjs/core';
import { map, Observable, startWith } from 'rxjs';

@Component({
  selector: 'app-add-individual-performance',
  templateUrl: './add-individual-performance.component.html',
  styleUrls: ['./add-individual-performance.component.scss']
})
export class AddIndividualPerformanceComponent {
  performanceForm: FormGroup;
  pfNumber: string = ''; // To store the PF number input by the user
  employees: any;
  stateCtrl = new FormControl('');
  filteredStates: Observable<any[]>;
  constructor(
    private fb: FormBuilder, 
    private http: HttpClient, 
    @Inject(MAT_DIALOG_DATA) public data,
    private server:HttpServiceService) {
      this.filteredStates = this.stateCtrl.valueChanges.pipe(
        startWith(''),
        map(employee => (employee ? this._filterEmployee(employee) : this.data.emp.slice())),
      );
    // Initialize the form with one performance entry by default
    this.performanceForm = this.fb.group({
      performances: this.fb.array([this.createPerformanceEntry()])
    });
  }
  ngOnInit(){
    if(this.data){
      this.employees = this.data

    }
    console.log(this.data.emp);
    
  }

  private _filterEmployee(value: string): any[] {
    console.log("val", value);
    
    const filterValue = value.toLowerCase();

    return this.data?.emp.filter(employee => employee.userFullName.toLowerCase().includes(filterValue));
  }

  // Getter for the performances FormArray
  performances(): FormArray {
    return this.performanceForm.get('performances') as FormArray;
  }

  // Create a new performance entry group
  createPerformanceEntry(): FormGroup {
    return this.fb.group({
      yearValue: [0, Validators.required], // Year value
      quarter: [0, Validators.required],   // Quarter
      performanceMetric: [0, Validators.required] // Performance metric
    });
  }

  // Add a new performance entry
  addPerformance(): void {
    this.performances().push(this.createPerformanceEntry());
  }

  // Remove a performance entry at a specific index
  removePerformance(index: number): void {
    this.performances().removeAt(index);
  }

  //check if year is leap
  checkIfLeapYR(year:any){
    if (year/4) {
      return true
    } else {
      return false
    }
  }

  // Handle form submission
  onSubmit(): void {
    if (!this.stateCtrl.value) {
      alert('Please enter a PF Number.');
      return;
    }

    // Prepare the data in the required format for the backend
    const performanceData = this.performances().value.map((performance: any) => ({
  
      year: {
        value: performance.yearValue,
        leap: this.checkIfLeapYR(performance.leap)
      },
      quarter: performance.quarter,
      performanceMetric: performance.performanceMetric
    }));

    // Endpoint URL (replace with your actual endpoint)
    //http://192.168.91.51:8080/v1/api/performances/add-for-user/ereer

    const endpoint = `${this.server.serverUrl}performances/add-for-user/${this.stateCtrl.value}`;

    // Make the POST request
    this.http.post(endpoint, performanceData).subscribe(
      (response) => {
        alert('Performance data submitted successfully.');
        this.performanceForm.reset();
        this.performances().clear();
        this.addPerformance(); // Add an empty entry after submission
      },
      (error) => {
        console.error('Error submitting performance data:', error);
        alert('An error occurred while submitting performance data.');
      }
    );
  }
}
