import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TalentMappingComponent } from './talent-mapping.component';

describe('TalentMappingComponent', () => {
  let component: TalentMappingComponent;
  let fixture: ComponentFixture<TalentMappingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [TalentMappingComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(TalentMappingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
