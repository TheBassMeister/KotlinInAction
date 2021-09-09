import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BurgerHeaderComponent } from './burger-header.component';

describe('BurgerHeaderComponent', () => {
  let component: BurgerHeaderComponent;
  let fixture: ComponentFixture<BurgerHeaderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BurgerHeaderComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BurgerHeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
