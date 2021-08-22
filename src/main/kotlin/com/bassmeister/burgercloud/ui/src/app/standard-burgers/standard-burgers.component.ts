import { Component, OnInit } from '@angular/core';
import { ApiService } from '../api/api.service';
import { Observable, of } from 'rxjs';
import { Injectable } from '@angular/core';
import { Burger } from '../model/Burger';
import { CartService } from '../cart.service';

@Component({
  selector: 'app-standard-burgers',
  templateUrl: './standard-burgers.component.html',
  styleUrls: ['./standard-burgers.component.css']
})

@Injectable()
export class StandardBurgersComponent implements OnInit {
  standardBurgers:Burger[];

  constructor(private service: ApiService, private cartService:CartService){}

  ngOnInit(): void {
    this.service.get("/burgers").subscribe((data:Burger[]) => {
        this.standardBurgers = data
    })
  }

  orderBurger(burger:Burger){
    this.cartService.addToCart(burger,1);
  }

}
