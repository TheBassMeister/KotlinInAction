package com.bassmeister.burgercloud.api

import com.bassmeister.burgercloud.domain.Ingredient
import com.fasterxml.jackson.annotation.JsonCreator
import org.springframework.hateoas.RepresentationModel
import java.time.LocalDateTime

class BurgerModel @JsonCreator constructor(val id: Long,val name:String, val createdAt:LocalDateTime?,
val ingredients:List<IngredientModel>):RepresentationModel<BurgerModel?>(){
}