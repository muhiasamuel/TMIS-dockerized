import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpServiceService } from '../../services/http-service.service';
import { log } from 'console';


@Component({
  selector: 'app-profiles',
  templateUrl: './profiles.component.html',
  styleUrl: './profiles.component.scss'
})
export class ProfilesComponent {
  // Define data model for the employee
  employee = {
    name: 'Joe Bloggs',
    positionTitle: 'Group Head of Employee Relations',
    level: 'General Manager',
    department: 'Group HR',
    lineManager: 'David Ssegawa',
    lineManagerPosition: 'Group Director HR Operations',
    photoUrl: 'path-to-photo',
    previousRoles: [
      { title: 'Position Title 1', employer: 'Employer 1' },
      { title: 'Position Title 2', employer: 'Employer 2' }
    ],
    education: [
      { qualification: 'MBA', university: 'UoN' },
      { qualification: 'BSc Bio Medical Engineering', university: 'UoN' }
    ],
    professionalAccreditations: [
      { accreditation: 'Chartered MCIPD', body: 'CIPD UK' }
    ],
    certifications: [
      { certification: 'Executive Coach', body: 'AoEC' }
    ],
    skillProfile: [
      { skill: 'Coding in Java', level: 'Advanced' },
      { skill: 'Workplace Mediation', level: 'Advanced' }
    ],
    performanceHistory: [
      { period: 'Half Year 2023', rating: '4.0' }
    ],
    potentialRatings: [
      { year: 2023, rating: 'A' },
      { year: 2022, rating: 'B' }
    ]
  };
}
