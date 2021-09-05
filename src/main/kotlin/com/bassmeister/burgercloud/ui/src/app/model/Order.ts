import { Burger } from '../model/Burger';

export interface Order{
  customerId: Number;
  burgers: Burger[];
}
