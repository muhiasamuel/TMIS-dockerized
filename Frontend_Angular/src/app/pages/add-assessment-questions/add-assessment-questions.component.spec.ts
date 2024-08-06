import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddAssessmentQuestionsComponent } from './add-assessment-questions.component';

describe('AddAssessmentQuestionsComponent', () => {
  let component: AddAssessmentQuestionsComponent;
  let fixture: ComponentFixture<AddAssessmentQuestionsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AddAssessmentQuestionsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AddAssessmentQuestionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
