import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StandardBurgersComponent } from './standard-burgers.component';

describe('StandardBurgersComponent', () => {
  let component: StandardBurgersComponent;
  let fixture: ComponentFixture<StandardBurgersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StandardBurgersComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StandardBurgersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
