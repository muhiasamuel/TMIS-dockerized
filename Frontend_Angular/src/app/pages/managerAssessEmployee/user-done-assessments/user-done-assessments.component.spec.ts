import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserDoneAssessmentsComponent } from './user-done-assessments.component';

describe('UserDoneAssessmentsComponent', () => {
  let component: UserDoneAssessmentsComponent;
  let fixture: ComponentFixture<UserDoneAssessmentsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UserDoneAssessmentsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(UserDoneAssessmentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
