package com.bassmeister.burgercloud.api.converters

import com.bassmeister.burgercloud.api.IngredientModel
import com.bassmeister.burgercloud.controllers.IngredientController
import com.bassmeister.burgercloud.controllers.RootController
import com.bassmeister.burgercloud.domain.Ingredient
import org.springframework.hateoas.Link
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*
import java.util.*

object IngredientConverter {

    fun convertIngredientList(ingredients: Iterable<Ingredient>): List<IngredientModel> {
        val ingredientsAsModels =
            ingredients.map { convertIngredient(it,false) }.toCollection(ArrayList<IngredientModel>())
            ingredientsAsModels.forEach {
            it.add(linkTo(methodOn(IngredientController::class.java).getIngredientById(it.id)).withSelfRel())
        }
        return ingredientsAsModels
    }

    fun convertIngredient(ingredient: Ingredient, withLink:Boolean):IngredientModel{
        val ingredientModel=IngredientModel(ingredient.id,ingredient.name,ingredient.type.name)
        if(withLink)
            ingredientModel.add(Link.of(linkTo(RootController::class.java).toString()).withRel("root"))
        return ingredientModel
    }

}