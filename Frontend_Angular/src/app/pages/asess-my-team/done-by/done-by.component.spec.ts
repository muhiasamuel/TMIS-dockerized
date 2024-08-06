import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DoneByComponent } from './done-by.component';

describe('DoneByComponent', () => {
  let component: DoneByComponent;
  let fixture: ComponentFixture<DoneByComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DoneByComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DoneByComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
