import { Component, OnInit } from '@angular/core';
import { HttpServiceService } from '../../../services/http-service.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-view-single-plan',
  templateUrl: './view-single-plan.component.html',
  styleUrls: ['./view-single-plan.component.scss']
})
export class ViewSinglePlanComponent implements OnInit {
  createNewPlan() {
    throw new Error('Method not implemented.');
  }

  planId: number;
  plans: any[] = [];
  distinctPlans: any[] = []; // Holds the restructured plan data.
  editingStates: { [key: number]: { [section: string]: boolean } } = {}; // Tracks editing state.
  newEntryStates: { [key: number]: { [section: string]: boolean } } = {}; // Tracks new entry state.
  currentStage: number = 1;
  constructor(
    private service: HttpServiceService,
    private route: ActivatedRoute,
    private snack: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.planId = params['planId'];
      this.getSinglePlan();
    });
  }

  onUpdateStage(stage: number): void {
    // Logic to handle stage updates, e.g., advancing the stage or displaying a modal
    console.log('Updating stage:', stage);
    this.currentStage = stage; // For demo purposes
  }
  getSinglePlan() {
    this.service.getSuccessionPlanById(this.planId).subscribe(
      (res) => {
        this.plans = res.item;  // Assuming `item` holds the array of plans
        this.distinctPlans = this.restructurePlans(this.plans); // Restructure data
        console.log("Structured Plans:", this.distinctPlans);
      },
      (error) => {
        this.snack.open(error.error.message, 'Close', { duration: 3600 });
      }
    );
  }

  // Function to restructure the plans data
  restructurePlans(plans: any[]): any[] {
    const planMap: { [key: number]: any } = {};

    plans.forEach(plan => {
      if (!planMap[plan.planId]) {
        planMap[plan.planId] = {
          ...plan,
          readyUsers: plan.readyUsers || [],
          externalSuccessors: plan.externalSuccessors || []
        };
      } else {
        // Avoid duplicating users
        planMap[plan.planId].readyUsers = [
          ...planMap[plan.planId].readyUsers,
          ...plan.readyUsers.filter(user => 
            !planMap[plan.planId].readyUsers.some(existingUser => existingUser.readyUserName === user.readyUserName)
          )
        ];
        
        // Avoid duplicating successors
        planMap[plan.planId].externalSuccessors = [
          ...planMap[plan.planId].externalSuccessors,
          ...plan.externalSuccessors.filter(successor =>
            !planMap[plan.planId].externalSuccessors.some(existingSuccessor => existingSuccessor.externalSuccessorName === successor.externalSuccessorName)
          )
        ];
      }
    });

    return Object.values(planMap);
  }

  // Function to check if a section is in editing mode
  isEditing(planId: number, section: string): boolean {
    return this.editingStates[planId] && this.editingStates[planId][section];
  }

  // Function to toggle between editing and view mode
  toggleEdit(planId: number, section: string): void {
    if (!this.editingStates[planId]) {
      this.editingStates[planId] = {};
    }
    this.editingStates[planId][section] = !this.editingStates[planId][section];

    // If saving, you can trigger an API call to update the plan data here.
    if (!this.editingStates[planId][section]) {
      this.savePlan(planId);
    }
  }

  // Function to add a new ready user or external successor
  addNewEntry(planId: number, section: string): void {
    const plan = this.distinctPlans.find(p => p.planId === planId);
    if (!this.newEntryStates[planId]) {
      this.newEntryStates[planId] = {};
    }
    this.newEntryStates[planId][section] = true;

    if (section === 'readyUsers') {
      if (!plan.readyUsers.some(user => user.readyUserName === '')) {
        plan.readyUsers.push({
          readyUserName: '',
          readinessLevel: '',
          developmentNeeds: [{ developmentNeedType: '', developmentNeedDescription: '' }],
          interventions: [{ interventionDescription: '', interventionType: '' }]
        });
      } else {
        this.snack.open('User already exists. Please enter unique user details.', 'Close', { duration: 3000 });
      }
    } else if (section === 'externalSuccessors') {
      if (!plan.externalSuccessors.some(successor => successor.externalSuccessorName === '')) {
        plan.externalSuccessors.push({
          externalSuccessorContact: '',
          externalSuccessorCurrentComp: '',
          externalSuccessorPosition: '',
          externalSuccessorName: '',
          externalSuccessorSelectionReason: ''
        });
      } else {
        this.snack.open('Successor already exists. Please enter unique successor details.', 'Close', { duration: 3000 });
      }
    }
  }

  // Function to remove a ready user or external successor
  removeEntry(planId: number, section: string, index: number): void {
    const plan = this.distinctPlans.find(p => p.planId === planId);
    if (section === 'readyUsers') {
      plan.readyUsers.splice(index, 1);
    } else if (section === 'externalSuccessors') {
      plan.externalSuccessors.splice(index, 1);
    }
  }

  // Function to save the updated plan (send updated data to the backend)
  savePlan(planId: number): void {
    const updatedPlan = this.distinctPlans.find(p => p.planId === planId);
    // Perform API call to save the updated plan details.
    console.log('Updated Plan:', updatedPlan);
    // Uncomment and modify the API call below based on your backend.
    /*
    this.service.updatePlan(updatedPlan).subscribe(
      () => {
        this.snack.open('Plan updated successfully', 'Close', { duration: 3600 });
      },
      (error) => {
        this.snack.open(error.error.message, 'Close', { duration: 3600 });
      }
    );
    */
  }

  // Function to cancel adding a new entry
  cancelNewEntry(planId: number, section: string): void {
    if (this.newEntryStates[planId] && this.newEntryStates[planId][section]) {
      this.newEntryStates[planId][section] = false;
      const plan = this.distinctPlans.find(p => p.planId === planId);
      if (section === 'readyUsers') {
        plan.readyUsers.pop(); // Remove the last entry
      } else if (section === 'externalSuccessors') {
        plan.externalSuccessors.pop(); // Remove the last entry
      }
    }
  }

  // Remove a development need from a user
  removeDevelopmentNeed(user: any, index: number): void {
    user.developmentNeeds.splice(index, 1);
  }

  // Add a new development need to a user
  addDevelopmentNeed(user: any): void {
    user.developmentNeeds.push({
      developmentNeedType: '',
      developmentNeedDescription: ''
    });
  }

  // Remove an intervention from a user
  removeIntervention(user: any, index: number): void {
    user.interventions.splice(index, 1);
  }

  // Add a new intervention to a user
  addIntervention(user: any): void {
    user.interventions.push({
      interventionDescription: '',
      interventionType: ''
    });
  }

  // Remove a ready user from a plan
  removeUser(plan: any, index: number): void {
    plan.readyUsers.splice(index, 1);
  }
}
