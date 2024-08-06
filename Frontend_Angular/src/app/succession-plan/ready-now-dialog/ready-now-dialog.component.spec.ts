import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReadyNowDialogComponent } from './ready-now-dialog.component';

describe('ReadyNowDialogComponent', () => {
  let component: ReadyNowDialogComponent;
  let fixture: ComponentFixture<ReadyNowDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ReadyNowDialogComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ReadyNowDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
