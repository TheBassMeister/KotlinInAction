import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { Burger } from '../model/Burger';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  constructor(private http: HttpClient) { }

    get(path: String) : Observable<Burger[]> {
      return this.http.get<Burger[]>('http://localhost:8080/api' + path);
    }
}
