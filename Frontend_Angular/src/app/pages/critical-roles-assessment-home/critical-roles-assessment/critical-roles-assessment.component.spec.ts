import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CriticalRolesAssessmentComponent } from './critical-roles-assessment.component';

describe('CriticalRolesAssessmentComponent', () => {
  let component: CriticalRolesAssessmentComponent;
  let fixture: ComponentFixture<CriticalRolesAssessmentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CriticalRolesAssessmentComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CriticalRolesAssessmentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
