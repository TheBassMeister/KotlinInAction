import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';
import { Burger } from '../model/Burger';


@Injectable({
  providedIn: 'root'
})
export class ApiService {

  constructor(private http: HttpClient) { }

    get(path: String) : Observable<Burger[]> {
      return this.http.get<Burger[]>('http://localhost:8080/api' + path);
    }

    sendOrders(orders: Map<Burger,Number>){
      for (let [key, value] of orders) {
          const headers = new HttpHeaders()
              .set("Content-Type", "application/json");

          this.http.post<Burger>('http://localhost:8080/api/burgers', key, {headers}).subscribe({
            next: data =>{
                console.log(data);
            },
            error:error => {
              console.error('There was an error!', error);
            }

          });

      }
    }
}
