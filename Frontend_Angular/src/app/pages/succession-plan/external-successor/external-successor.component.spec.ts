import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExternalSuccessorComponent } from './external-successor.component';

describe('ExternalSuccessorComponent', () => {
  let component: ExternalSuccessorComponent;
  let fixture: ComponentFixture<ExternalSuccessorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ExternalSuccessorComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ExternalSuccessorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
