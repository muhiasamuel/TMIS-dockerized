import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HiposInterventionsComponent } from './hipos-interventions.component';

describe('HiposInterventionsComponent', () => {
  let component: HiposInterventionsComponent;
  let fixture: ComponentFixture<HiposInterventionsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [HiposInterventionsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(HiposInterventionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
