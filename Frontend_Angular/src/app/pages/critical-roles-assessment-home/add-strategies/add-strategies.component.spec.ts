import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddStrategiesComponent } from './add-strategies.component';

describe('AddStrategiesComponent', () => {
  let component: AddStrategiesComponent;
  let fixture: ComponentFixture<AddStrategiesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AddStrategiesComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AddStrategiesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
