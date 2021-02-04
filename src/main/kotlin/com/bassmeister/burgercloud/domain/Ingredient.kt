package com.bassmeister.burgercloud.domain

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity @Table(name="ingredient")//Needs to be open and not a data class to make it work with HATEOAS
open class Ingredient(@Id open var id:String, open val name:String, open val type:IngredientType)

enum class IngredientType{
    BUN,
    SAUCE,
    VEG,
    OTHER,
}
