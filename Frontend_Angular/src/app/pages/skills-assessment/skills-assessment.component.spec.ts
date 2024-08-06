import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SkillsAssessmentComponent } from './skills-assessment.component';

describe('SkillsAssessmentComponent', () => {
  let component: SkillsAssessmentComponent;
  let fixture: ComponentFixture<SkillsAssessmentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SkillsAssessmentComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SkillsAssessmentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
