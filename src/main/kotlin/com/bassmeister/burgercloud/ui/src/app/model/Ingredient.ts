export interface Ingredient{
  id: string;
  name: string;
  type: IngredientType;
}

export enum IngredientType {
    BUN="BUN",
    SAUCE="SAUCE",
    VEG="VEG",
    OTHER="OTHER",
}
