import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AssessmentLineGraphComponent } from './assessment-line-graph.component';

describe('AssessmentLineGraphComponent', () => {
  let component: AssessmentLineGraphComponent;
  let fixture: ComponentFixture<AssessmentLineGraphComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AssessmentLineGraphComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AssessmentLineGraphComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
