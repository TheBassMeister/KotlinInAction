package com.bassmeister.burgercloud.domain

import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Ingredient(@Id var id:String, val name:String, val type:IngredientType){

}

enum class IngredientType{
    BUN,
    SAUCE,
    VEG,
    OTHER,
}
