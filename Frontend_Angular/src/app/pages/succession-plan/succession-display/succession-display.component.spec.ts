import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SuccessionDisplayComponent } from './succession-display.component';

describe('SuccessionDisplayComponent', () => {
  let component: SuccessionDisplayComponent;
  let fixture: ComponentFixture<SuccessionDisplayComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SuccessionDisplayComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SuccessionDisplayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
