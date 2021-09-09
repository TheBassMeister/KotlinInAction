import { Injectable } from '@angular/core';
import { Burger } from './model/Burger';

@Injectable({
  providedIn: 'root'
})
export class CartService {
  orders= new Map();

  constructor() { }

  addToCart(burger: Burger, amount: number) {
    var previousOrders=this.orders.get(burger);
    if(previousOrders){
        this.orders.set(burger, previousOrders+amount);
    }else{
      this.orders.set(burger, amount);
    }
  }

  getItems() {
    return this.orders;
  }

  clearCart() {
    this.orders = new Map();
    return this.orders;
  }
}
