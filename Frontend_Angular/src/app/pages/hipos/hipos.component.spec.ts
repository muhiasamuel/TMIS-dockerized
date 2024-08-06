import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HIPOsComponent } from './hipos.component';

describe('HIPOsComponent', () => {
  let component: HIPOsComponent;
  let fixture: ComponentFixture<HIPOsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [HIPOsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(HIPOsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
