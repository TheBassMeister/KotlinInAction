import { Burger } from '../model/Burger';
import { BurgerOrder } from '../model/BurgerOrder';

 export interface Order{
   customerId: Number;
   burgers: BurgerOrder[];
 }
