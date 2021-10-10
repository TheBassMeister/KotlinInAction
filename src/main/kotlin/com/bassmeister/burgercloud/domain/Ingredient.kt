package com.bassmeister.burgercloud.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document


@Document(collection = "ingredients")
data class Ingredient(@Id var id:String, val name:String, val type:IngredientType)

enum class IngredientType{
    BUN,
    SAUCE,
    VEG,
    OTHER,
}
