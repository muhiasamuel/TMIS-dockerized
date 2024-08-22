import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AssessmentBarGraphComponent } from './assessment-bar-graph.component';

describe('AssessmentBarGraphComponent', () => {
  let component: AssessmentBarGraphComponent;
  let fixture: ComponentFixture<AssessmentBarGraphComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AssessmentBarGraphComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AssessmentBarGraphComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
