import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';
import { Burger } from '../model/Burger';
import { Order } from '../model/Order';
import { BurgerOrder } from '../model/BurgerOrder';


@Injectable({
  providedIn: 'root'
})
export class ApiService {

  constructor(private http: HttpClient) { }

    get(path: String) : Observable<Burger[]> {
      return this.http.get<Burger[]>('http://localhost:8080/api' + path);
    }

    sendOrders(orders: Map<Burger,Number>, ccNumber:String, ccExpDate:String, ccCVC:String){
        const headers = new HttpHeaders()
          .set("Content-Type", "application/json");
        let burgerOrders: BurgerOrder[]=[];
        orders.forEach((amount: Number, burger: Burger) =>{
              burgerOrders.push({amount:amount, burger:burger} as BurgerOrder)
            }
        )
        var order={customerId:1, burgers:burgerOrders, ccNumber:ccNumber, ccExpDate:ccExpDate, ccCVC:ccCVC} as Order
        this.http.post<Order>('http://localhost:8080/api/orders', order, {headers}).subscribe({
          next: data =>{
              console.log("Successfully persisted order");
          },
          error:error => {
              console.error('There was an error!', error);
          }
        });
    }

}
