package com.bassmeister.burgercloud.api

import com.bassmeister.burgercloud.controllers.IngredientController
import com.bassmeister.burgercloud.domain.Ingredient
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder

object IngredientToModelConverter {

    fun convertIngredientList(ingredients: Iterable<Ingredient>):List<IngredientModel>{
        val ingredientsAsModels=ingredients.map { ingredient -> IngredientModel(ingredient.id, ingredient.name, ingredient.type.name)}
            .toCollection(ArrayList<IngredientModel>())
        ingredientsAsModels.forEach{
            it.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(IngredientController::class.java).getIngredientById(it.id)).withSelfRel())
        }
        return ingredientsAsModels
    }

}