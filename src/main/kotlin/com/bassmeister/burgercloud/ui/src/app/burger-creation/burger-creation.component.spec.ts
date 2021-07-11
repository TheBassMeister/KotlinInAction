import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BurgerCreationComponent } from './burger-creation.component';

describe('BurgerCreationComponent', () => {
  let component: BurgerCreationComponent;
  let fixture: ComponentFixture<BurgerCreationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BurgerCreationComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BurgerCreationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
