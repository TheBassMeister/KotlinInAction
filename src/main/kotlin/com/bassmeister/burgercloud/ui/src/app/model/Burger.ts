import { Ingredient } from '../model/Ingredient';

export interface Burger{
  name: string;
  isStandard?: boolean;
  ingredients?:Ingredient[];
}
