import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditCriticalRoleComponent } from './edit-critical-role.component';

describe('EditCriticalRoleComponent', () => {
  let component: EditCriticalRoleComponent;
  let fixture: ComponentFixture<EditCriticalRoleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [EditCriticalRoleComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(EditCriticalRoleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
