import { Component, OnInit } from '@angular/core';
import { ApiService } from '../api/api.service';
import { Ingredient } from '../model/Ingredient';
import { Observable, of } from 'rxjs';
import { Injectable } from '@angular/core';
import { FormGroup, FormControl, FormArray } from '@angular/forms';
import { Validators } from '@angular/forms';

@Component({
  selector: 'app-burger-creation',
  templateUrl: './burger-creation.component.html',
  styleUrls: ['./burger-creation.component.css']
})
export class BurgerCreationComponent implements OnInit {
  ingredients:Ingredient[];

  burgerCreationForm = new FormGroup({
    burgerName: new FormControl('', Validators.required),
    bun: new FormControl(),
    sauces: new FormArray([]),
    veggies: new FormArray([]),
    others: new FormArray([]),
  });


  constructor(private service: ApiService) { }

  ngOnInit(): void {
      this.service.get("/ingredients").subscribe((data:Ingredient[]) => {
          this.ingredients = data
          const initialBun= data.find(ingredient => ingredient.type=='BUN');
          this.burgerCreationForm.get('bun').setValue(initialBun.name);
      })

  }

  onCheckChange(event, ingredientType) {
    const formArray: FormArray = this.burgerCreationForm.get(ingredientType) as FormArray;

    if(event.target.checked){
        formArray.push(new FormControl(event.target.value));
    }else{
      for(let i=0; i<formArray.controls.length; i++){
          console.log(formArray.controls[i].value);
          if(formArray.controls[i].value == event.target.value){
            formArray.removeAt(i);
            return;
          }
      }
    }
  }

  selectBun(event){
    console.log(event.target.value)
    this.burgerCreationForm.get('bun').setValue(event.target.value, {onlySelf: true});
  }

  onSubmit() {
    // TODO: Use EventEmitter with form value
    console.warn(this.burgerCreationForm.value);
  }

}
