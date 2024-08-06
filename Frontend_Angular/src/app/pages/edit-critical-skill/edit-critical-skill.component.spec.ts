import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditCriticalSkillComponent } from './edit-critical-skill.component';

describe('EditCriticalSkillComponent', () => {
  let component: EditCriticalSkillComponent;
  let fixture: ComponentFixture<EditCriticalSkillComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [EditCriticalSkillComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(EditCriticalSkillComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
