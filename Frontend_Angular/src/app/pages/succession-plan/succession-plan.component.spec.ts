import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SuccessionPlanComponent } from './succession-plan.component';

describe('SuccessionPlanComponent', () => {
  let component: SuccessionPlanComponent;
  let fixture: ComponentFixture<SuccessionPlanComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SuccessionPlanComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SuccessionPlanComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
