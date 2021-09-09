import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule, Routes } from '@angular/router'

import { AppComponent } from './app.component';
import { BurgerHeaderComponent } from './burger-header/burger-header.component';
import { StandardBurgersComponent } from './standard-burgers/standard-burgers.component';
import { routes } from './app.routes';
import { HomeComponent } from './home/home.component';
import { FooterComponent } from './footer/footer.component';
import { HttpClientModule } from '@angular/common/http';
import { BurgerCreationComponent } from './burger-creation/burger-creation.component';
import { IngredientFilterPipe } from './pipes/ingredient-filter.pipe';
import { ReactiveFormsModule } from '@angular/forms';
import { CartComponent } from './cart/cart.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { CreditCardDirectivesModule } from 'angular-cc-library';

@NgModule({
  declarations: [
    AppComponent,
    BurgerHeaderComponent,
    StandardBurgersComponent,
    HomeComponent,
    FooterComponent,
    BurgerCreationComponent,
    IngredientFilterPipe,
    CartComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    RouterModule.forRoot(routes),
    ReactiveFormsModule,
    NgbModule,
    CreditCardDirectivesModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
