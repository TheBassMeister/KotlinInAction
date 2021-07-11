import { Pipe, PipeTransform } from '@angular/core';
import { Ingredient } from '../model/Ingredient';

@Pipe({
  name: 'ingredientFilter'
})
export class IngredientFilterPipe implements PipeTransform {

  transform(items: Ingredient[], type: String): Ingredient[] {
    if (!items) {
        return items;
    }
    return items.filter(item => item.type == type);
  }

}
