import { Component, OnInit } from '@angular/core';
import { CartService } from '../cart.service';
import { ApiService } from '../api/api.service';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {
  burgers = this.cartService.getItems();

  constructor(private cartService: CartService, private apiService:ApiService) { }

  ngOnInit(): void {
  }

  checkOut(){
    this.apiService.sendOrders(this.burgers);
    this.cartService.clearCart();
    this.burgers=this.cartService.getItems();
  }



}
