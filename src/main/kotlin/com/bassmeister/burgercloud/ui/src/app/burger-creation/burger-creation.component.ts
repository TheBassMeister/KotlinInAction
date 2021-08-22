import { Component, OnInit } from '@angular/core';
import { ApiService } from '../api/api.service';
import { Ingredient } from '../model/Ingredient';
import { IngredientType } from '../model/Ingredient';
import { Burger } from '../model/Burger';
import { Observable, of } from 'rxjs';
import { Injectable } from '@angular/core';
import { FormGroup, FormControl, FormArray } from '@angular/forms';
import { Validators } from '@angular/forms';
import { CartService } from '../cart.service';

@Component({
  selector: 'app-burger-creation',
  templateUrl: './burger-creation.component.html',
  styleUrls: ['./burger-creation.component.css']
})
export class BurgerCreationComponent implements OnInit {
  ingredients:Ingredient[];

  burgerCreationForm = new FormGroup({
    burgerName: new FormControl('', Validators.required),
    amount:new FormControl(1),
    bun: new FormControl(),
    sauces: new FormArray([]),
    veggies: new FormArray([]),
    others: new FormArray([]),
  });


  constructor(private service: ApiService, private cartService:CartService) { }

  ngOnInit(): void {
      this.service.get("/ingredients").subscribe((data:Ingredient[]) => {
          this.ingredients = data
          const initialBun= data.find(ingredient => ingredient.type==IngredientType.BUN);
          this.burgerCreationForm.get('bun').setValue(initialBun.name);
      })

  }

  onCheckChange(event, ingredientType) {
    const formArray: FormArray = this.burgerCreationForm.get(ingredientType) as FormArray;

    if(event.target.checked){
        formArray.push(new FormControl(event.target.value));
    }else{
      for(let i=0; i<formArray.controls.length; i++){
          if(formArray.controls[i].value == event.target.value){
            formArray.removeAt(i);
            return;
          }
      }
    }
  }

  selectBun(event){
    this.burgerCreationForm.get('bun').setValue(event.target.value, {onlySelf: true});
  }

  onSubmit() {
    var toSubmit=this.burgerCreationForm.value;
    var burgerIngredients:Ingredient[]=[];

    burgerIngredients.push(this.getBun());
    burgerIngredients.push(...this.getIngredients('sauces', IngredientType.SAUCE));
    burgerIngredients.push(...this.getIngredients('veggies', IngredientType.VEG));
    burgerIngredients.push(...this.getIngredients('others', IngredientType.OTHER));
    const burger: Burger = { name: this.burgerCreationForm.value.burgerName, ingredients:burgerIngredients};

    this.cartService.addToCart(burger, this.burgerCreationForm.value.amount);


  }

  private getBun():Ingredient{
      return {name:this.burgerCreationForm.get('bun').value, type:IngredientType.BUN} as Ingredient;
  }

  private getIngredients(formName:string,type:IngredientType ):Ingredient[]{
      var ingredients:Ingredient[]=[];
      var selectedIngredients=this.burgerCreationForm.get(formName).value;
      for(var ingredient of selectedIngredients){
        ingredients.push({name:ingredient, type:type} as Ingredient);
      }
      return ingredients;
  }

}
