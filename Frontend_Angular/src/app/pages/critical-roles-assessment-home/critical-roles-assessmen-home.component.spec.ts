import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CriticalRolesAssessmenHomeComponent } from './critical-roles-assessmen-home.component';

describe('CriticalRolesAssessmenHomeComponent', () => {
  let component: CriticalRolesAssessmenHomeComponent;
  let fixture: ComponentFixture<CriticalRolesAssessmenHomeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CriticalRolesAssessmenHomeComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CriticalRolesAssessmenHomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
