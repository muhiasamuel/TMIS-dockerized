import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AssessmentHistoryInfoComponent } from './assessment-history-info.component';

describe('AssessmentHistoryInfoComponent', () => {
  let component: AssessmentHistoryInfoComponent;
  let fixture: ComponentFixture<AssessmentHistoryInfoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AssessmentHistoryInfoComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AssessmentHistoryInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
