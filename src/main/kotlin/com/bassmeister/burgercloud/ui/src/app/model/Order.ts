import { BurgerOrder } from '../model/BurgerOrder';

 export interface Order{
   customerId: Number;
   burgers: BurgerOrder[];
   ccNumber: String;
   ccExpDate: String;
   ccCVC: String;
 }
