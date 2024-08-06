import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MyTeamsProfileComponent } from './my-teams-profile.component';

describe('MyTeamsProfileComponent', () => {
  let component: MyTeamsProfileComponent;
  let fixture: ComponentFixture<MyTeamsProfileComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MyTeamsProfileComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MyTeamsProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
