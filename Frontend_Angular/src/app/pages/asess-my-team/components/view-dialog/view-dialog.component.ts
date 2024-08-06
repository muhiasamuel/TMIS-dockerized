import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { IPotentialIdentification } from '../../../../../IPotentialIdentification';

@Component({
  selector: 'app-view-dialog',
  templateUrl: './view-dialog.component.html',
  styleUrl: './view-dialog.component.scss'
})
export class ViewDialogComponent {

  constructor(public dialogRef: MatDialogRef<ViewDialogComponent>) { }
  potential=[
    {
      id: 1,
     potentialattribute: 'Aspiration', 
     descriptor: 'Hunger for success',
     score: 1,
     managerscore: 5, 
    },

    {
      id: 2,
      potentialattribute: 'Aspiration',
      descriptor: 'Purposeful',
      score: 2,
      managerscore: 4,
    },
    {
      id: 3,
      potentialattribute: 'Aspiration',
      descriptor: 'Immersion: Looks for roles that require a personal commitment above the norm, prepared to offer discretionary effort beyond their role',
      score:5,
      managerscore: 3,
    },
    {
      id:4,
      potentialattribute: 'Aspiration',
      descriptor: 'Activity: Prefers fast-paced, multi-tasking work environments that offer regular learning opportunities and execution challenge',
      score:4,
      managerscore: 1,
    },
    {
      id:5,
      potentialattribute:'Aspiration',
      descriptor: 'Power: Wants the opportunity to exercise influence and shape how things are done, eager to put their ideas into practice and learn',
      score: 2,
      managerscore: 4,
    },
   
    {
      id:7,
      potentialattribute:'Aspiration',
      descriptor: 'Autonomy: Attracted to roles that allow them autonomy in how they execute their responsibilities. Thrive is self regulated roles',
      score: 3,
      managerscore: 2,
    },
    {
      id:8,
      potentialattribute:'Aspiration',
      descriptor: 'Autonomy: Attracted to roles that allow them autonomy in how they execute their responsibilities. Thrive is self regulated roles',
      score: 3,
      managerscore: 2,
    },
    {
      id:9,
      potentialattribute:'Aspiration',
      descriptor: 'Autonomy: Attracted to roles that allow them autonomy in how they execute their responsibilities. Thrive is self regulated roles',
      score: 3,
      managerscore: 2,
    }, 
    {
      id:10,
      potentialattribute:'Aspiration',
      descriptor: 'Autonomy: Attracted to roles that allow them autonomy in how they execute their responsibilities. Thrive is self regulated roles',
      score: 3,
      managerscore: 2,
    },
    {
      id:11,
      potentialattribute:'Aspiration',
      descriptor: 'Autonomy: Attracted to roles that allow them autonomy in how they execute their responsibilities. Thrive is self regulated roles',
      score: 3,
      managerscore: 2,
    },
    {
      id:12,
      potentialattribute:'Aspiration',
      descriptor: 'Autonomy: Attracted to roles that allow them autonomy in how they execute their responsibilities. Thrive is self regulated roles',
      score: 3,
      managerscore: 2,
    },
    {
      id:13,
      potentialattribute:'Aspiration',
      descriptor: 'Autonomy: Attracted to roles that allow them autonomy in how they execute their responsibilities. Thrive is self regulated roles',
      score: 3,
      managerscore: 2,
    }
  ];
}