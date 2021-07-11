import { Component, OnInit } from '@angular/core';
import { ApiService } from '../api/api.service';
import { Ingredient } from '../model/Ingredient';
import { Observable, of } from 'rxjs';
import { Injectable } from '@angular/core';

@Component({
  selector: 'app-burger-creation',
  templateUrl: './burger-creation.component.html',
  styleUrls: ['./burger-creation.component.css']
})
export class BurgerCreationComponent implements OnInit {
  ingredients:Ingredient[];

  constructor(private service: ApiService) { }

  ngOnInit(): void {
      this.service.get("/ingredients").subscribe((data:Ingredient[]) => {
          this.ingredients = data
      })
  }

}
