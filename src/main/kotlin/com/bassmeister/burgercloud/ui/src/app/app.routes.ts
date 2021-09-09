import { Routes } from '@angular/router';
import { StandardBurgersComponent } from './standard-burgers/standard-burgers.component';
import { BurgerCreationComponent} from './burger-creation/burger-creation.component';
import { HomeComponent } from './home/home.component';
import { CartComponent } from './cart/cart.component';


export const routes: Routes = [
  {
    path: 'home',
    component: HomeComponent
  },
  {
    path: 'standardBurgers',
    component: StandardBurgersComponent
  },
  {
    path: 'burgerCreation',
    component: BurgerCreationComponent
  },
  {
    path: 'shoppingCart',
    component: CartComponent

  },
  {
    path: '**',
    redirectTo: 'home',
    pathMatch: 'full'
  },
];
