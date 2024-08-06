import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AsessMyTeamComponent } from './asess-my-team.component';

describe('AsessMyTeamComponent', () => {
  let component: AsessMyTeamComponent;
  let fixture: ComponentFixture<AsessMyTeamComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AsessMyTeamComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AsessMyTeamComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
