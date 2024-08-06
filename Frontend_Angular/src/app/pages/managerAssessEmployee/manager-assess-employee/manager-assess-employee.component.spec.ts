import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManagerAssessEmployeeComponent } from './manager-assess-employee.component';

describe('ManagerAssessEmployeeComponent', () => {
  let component: ManagerAssessEmployeeComponent;
  let fixture: ComponentFixture<ManagerAssessEmployeeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ManagerAssessEmployeeComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ManagerAssessEmployeeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
