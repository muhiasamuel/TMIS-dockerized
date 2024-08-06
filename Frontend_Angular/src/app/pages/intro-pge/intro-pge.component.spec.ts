import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IntroPgeComponent } from './intro-pge.component';

describe('IntroPgeComponent', () => {
  let component: IntroPgeComponent;
  let fixture: ComponentFixture<IntroPgeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [IntroPgeComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(IntroPgeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
