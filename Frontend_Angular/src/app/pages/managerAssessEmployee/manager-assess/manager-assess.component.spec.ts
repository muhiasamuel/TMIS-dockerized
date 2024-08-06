import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManagerAssessComponent } from './manager-assess.component';

describe('ManagerAssessComponent', () => {
  let component: ManagerAssessComponent;
  let fixture: ComponentFixture<ManagerAssessComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ManagerAssessComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ManagerAssessComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
