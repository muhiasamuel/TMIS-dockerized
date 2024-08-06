import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SingleAttributeComponent } from './single-attribute.component';

describe('SingleAttributeComponent', () => {
  let component: SingleAttributeComponent;
  let fixture: ComponentFixture<SingleAttributeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SingleAttributeComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SingleAttributeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
