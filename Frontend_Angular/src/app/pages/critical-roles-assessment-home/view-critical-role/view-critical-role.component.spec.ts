import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewCriticalRoleComponent } from './view-critical-role.component';

describe('ViewCriticalRoleComponent', () => {
  let component: ViewCriticalRoleComponent;
  let fixture: ComponentFixture<ViewCriticalRoleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ViewCriticalRoleComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ViewCriticalRoleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
