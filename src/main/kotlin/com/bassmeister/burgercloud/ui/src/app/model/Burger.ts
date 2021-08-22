import { Ingredient } from '../model/Ingredient';

export interface Burger{
  name: string;
  ingredients?:Ingredient[];
}
