import { Ingredient } from '../model/Ingredient';

export interface Burger{
  name: string;
  isStandardBurger?: boolean;
  ingredients?:Ingredient[];
}
