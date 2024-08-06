import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PotentialAttributesComponent } from './potential-attributes.component';

describe('PotentialAttributesComponent', () => {
  let component: PotentialAttributesComponent;
  let fixture: ComponentFixture<PotentialAttributesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PotentialAttributesComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PotentialAttributesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
