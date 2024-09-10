import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddIndividualPerformanceComponent } from './add-individual-performance.component';

describe('AddIndividualPerformanceComponent', () => {
  let component: AddIndividualPerformanceComponent;
  let fixture: ComponentFixture<AddIndividualPerformanceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AddIndividualPerformanceComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AddIndividualPerformanceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
