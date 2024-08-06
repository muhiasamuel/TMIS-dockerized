import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewCriticalSkillComponent } from './view-critical-skill.component';

describe('ViewCriticalSkillComponent', () => {
  let component: ViewCriticalSkillComponent;
  let fixture: ComponentFixture<ViewCriticalSkillComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ViewCriticalSkillComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ViewCriticalSkillComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
