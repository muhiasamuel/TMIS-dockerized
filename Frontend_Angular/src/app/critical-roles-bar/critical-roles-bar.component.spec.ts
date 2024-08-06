import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CriticalRolesBarComponent } from './critical-roles-bar.component';

describe('CriticalRolesBarComponent', () => {
  let component: CriticalRolesBarComponent;
  let fixture: ComponentFixture<CriticalRolesBarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CriticalRolesBarComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CriticalRolesBarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
