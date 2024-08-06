import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MvpsAssessmentComponent } from './mvps-assessment.component';

describe('MvpsAssessmentComponent', () => {
  let component: MvpsAssessmentComponent;
  let fixture: ComponentFixture<MvpsAssessmentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MvpsAssessmentComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MvpsAssessmentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
