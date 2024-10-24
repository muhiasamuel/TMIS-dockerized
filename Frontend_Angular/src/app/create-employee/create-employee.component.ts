import { HttpClient } from '@angular/common/http';
import { Component, Inject } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { HttpServiceService } from '../services/http-service.service';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { map, Observable, startWith } from 'rxjs';

@Component({
  selector: 'app-create-employee',
  templateUrl: './create-employee.component.html',
  styleUrl: './create-employee.component.scss'
})
export class CreateEmployeeComponent {
  createEmployeeForm: FormGroup;
  manager: any;
  managerId: number;
  loading = false; // Variable to track loading state
  positions: any;
  roles: any;
  department: any;
  state: string;
  url: string;
  stateCtrl = new FormControl('');
  managers: any;
  record:any = null;
  selectedManager: any;
  filteredStates: Observable<any[]>;
  constructor(
    public dialogRef: MatDialogRef<CreateEmployeeComponent>,
    @Inject(MAT_DIALOG_DATA) public data,
    private snackBar: MatSnackBar,
    private http: HttpClient, private service: HttpServiceService) {
      
    this.filteredStates = this.stateCtrl.valueChanges.pipe(
      startWith(''),
      map(employee => (employee ? this._filterManagers(employee) : this.data.managers.slice())),
    );
  }

  private _filterManagers(value: string): any[] {
    console.log("val", value);

    this.selectedManager = this.data.managers.filter((item) => item.pf === value)[0]

    console.log(this.selectedManager);

    const filterValue = value.toLowerCase();
    return this.data.managers?.filter(manager =>
      manager.pf.toLowerCase().includes(filterValue) ||
      manager.userFullName.toLowerCase().includes(filterValue)
    );
  }

  ngOnInit(): void {
    const user = localStorage.getItem("user")
    if (user) {
      this.manager = JSON.parse(user);
      if (this.data) {
        if (this.data.record) {
          this.record = this.data.record
          this.stateCtrl.setValue(this.data?.record?.managerPF || '');

          // Then set the disabled state
            if (!!this.data?.record) {
              this.stateCtrl.disable(); // Disable if record exists
            } else {
              this.stateCtrl.enable();  // Enable if record does not exist
            }

          console.log("assdffd", this.stateCtrl);
        }
        console.log("data",this.data);     
        console.log("record",this.record);

      }
    }

    this.createEmployeeForm = new FormGroup({
      userFullName: new FormControl(
        { value: this.record?.userFullName || '', disabled: !!this.record }, 
        Validators.required
      ),
      pf: new FormControl(
        { value: this.record?.pf || '', disabled: !!this.record }, 
        Validators.required
      ),
      username: new FormControl(
        { value: this.record?.userName || '', disabled: !!this.record }, 
        Validators.required
      ),
      email: new FormControl(
        { value: this.record?.userEmail || '', disabled: !!this.record }, 
        [Validators.required, Validators.email]
      ),
      roleId: new FormControl(this.record?.roleId, Validators.required),
      positionId: new FormControl(this.record?.positionId, Validators.required),
      departmentId: new FormControl(this.record?.departmentId, Validators.required),
      
    });

    this.getDepartments();
    this.getRole();
  }

  getRole(): void {
    const url = `${this.service.serverUrl}roles/getRoles`;
    this.http.get<{ item: { id: number, roleName: string }[] }>(url).subscribe(
      response => {
        this.roles = response.item;
      },
      error => {
        console.error('Error fetching roles', error);
      }
    );
  }

  getPosition() {

    console.log("form", this.createEmployeeForm.value.department);
    const positions = this.department.filter(item => item.depId === this.createEmployeeForm.value.departmentId)
    this.positions = positions[0].departmentPositions
    console.log("positions", this.positions);


  }
  getDepartments() {
    this.service.getDepartments().subscribe(
      ((res) => {
        this.department = res.item
      }),
      ((error) => {
        console.error('Error fetching departments', error);

      }),
      () => { }
    )
  }


  onSubmit(): void {
    this.loading = true;
    const data = {
      ...this.createEmployeeForm.value,
      "locked": false,
      "enabled": true
    }

    console.log("data", data);
    if (this.record) {
      if(this.record.roleName === "Employee"){
        
        this.service.updateEmployee(this.record.userId,this.record.managerId,data).subscribe(
          ((res)=>{
            console.log('updatedd',res);
            this.loading = false
          }),
          ((error)=>{}),
          ()=>{}
        )
      } else{
        this.service.updateManager(this.record.userId, data).subscribe(
          ((res)=>{
            console.log('updatedManager',res);
            this.loading = false;
          }),
          ((error)=>{}),
          ()=>{}
        )
      }
      
    } else {
      const data = {
        ...this.createEmployeeForm.value,
        "locked": false,
        "enabled": true
      }
      if (this.data) {
        if (this.createEmployeeForm.value.roleId === 2) {
          const url = `${this.service.serverUrl}users/create-employee/${this.selectedManager.userId}`;
  
          this.postData(url, data)
        } else {
          const url = `${this.service.serverUrl}users/manager/${this.selectedManager.userId}`
          this.postData(url, data)
        }
      } else {
        const url = `${this.service.serverUrl}users/create-employee/${this.manager.user.userId}`;
        this.postData(url, data)
      }
    }
  




  }

  postData(url: string, data: any) {
    this.http.post(url, data).subscribe(
      response => {
        console.log('employee created succesfully', response);
        this.loading = false;
        this.dialogRef.close()

        // Show success snackbar
        this.snackBar.open("Employee created successfully!", "Close", {
          duration: 3000, // 3 seconds
          verticalPosition: "bottom",
          horizontalPosition: "center",
        });
      },
      error => {
        console.error('There was an error!', error)
        this.loading = false;

        // Show error snackbar
        this.snackBar.open("Error creating employee. Please try again.", "Close", {
          duration: 3000, // 3 seconds
          verticalPosition: "bottom",
          horizontalPosition: "center",
        });

      }
    );
  }

  closeDialog(): void {
    this.dialogRef.close();
  }



}
