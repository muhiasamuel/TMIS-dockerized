import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewSinglePlanComponent } from './view-single-plan.component';

describe('ViewSinglePlanComponent', () => {
  let component: ViewSinglePlanComponent;
  let fixture: ComponentFixture<ViewSinglePlanComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ViewSinglePlanComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ViewSinglePlanComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
