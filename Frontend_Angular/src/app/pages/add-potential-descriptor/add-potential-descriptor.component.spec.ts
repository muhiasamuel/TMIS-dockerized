import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddPotentialDescriptorComponent } from './add-potential-descriptor.component';

describe('AddPotentialDescriptorComponent', () => {
  let component: AddPotentialDescriptorComponent;
  let fixture: ComponentFixture<AddPotentialDescriptorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AddPotentialDescriptorComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AddPotentialDescriptorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
