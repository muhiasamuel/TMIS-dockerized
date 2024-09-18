import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PerformanceLineGraphComponent } from './performance-line-graph.component';

describe('PerformanceLineGraphComponent', () => {
  let component: PerformanceLineGraphComponent;
  let fixture: ComponentFixture<PerformanceLineGraphComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PerformanceLineGraphComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PerformanceLineGraphComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
