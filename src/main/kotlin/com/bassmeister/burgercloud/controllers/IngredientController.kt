package com.bassmeister.burgercloud.controllers

import com.bassmeister.burgercloud.data.IngredientRepo
import com.bassmeister.burgercloud.domain.Ingredient
import com.bassmeister.burgercloud.domain.IngredientType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = ["/ingredients"], produces = ["application/json"])
class IngredientController (val repo: IngredientRepo){

    @GetMapping
    fun getAllIngredients() = repo.findAll()

    @GetMapping("/type")
    fun getIngredientsByType(@RequestParam(value = "type", defaultValue= "BUN") type: String):List<Ingredient>{
        val ingredient=when(type){
            "SAUCE" ->IngredientType.SAUCE
            "VEG" -> IngredientType.VEG
            "OTHER" -> IngredientType.OTHER
            else -> IngredientType.BUN
        }

        return repo.getIngredientByType(ingredient)

    }

}