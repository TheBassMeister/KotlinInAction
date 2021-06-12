import { Routes } from '@angular/router';
import { StandardBurgersComponent } from './standard-burgers/standard-burgers.component';
import { HomeComponent } from './home/home.component';


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
    path: '**',
    redirectTo: 'home',
    pathMatch: 'full'
  },
];
