package com.bassmeister.burgercloud.controllers

import com.bassmeister.burgercloud.api.IngredientModel
import com.bassmeister.burgercloud.api.converters.IngredientConverter
import com.bassmeister.burgercloud.data.IngredientRepo
import com.bassmeister.burgercloud.domain.Ingredient
import com.bassmeister.burgercloud.domain.IngredientType
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = ["/api/ingredients"], produces = ["application/hal+json"])
class IngredientController (val repo: IngredientRepo){

    fun getAllIngredients(): ResponseEntity<List<IngredientModel>> {
        var allIngredients= IngredientConverter.convertIngredientList(repo.findAll())
        return  ResponseEntity(allIngredients, HttpStatus.OK);
    }

    @GetMapping
    fun getIngredientsByType(@RequestParam(value = "type") type: String?):ResponseEntity<List<IngredientModel>>{
        if(type==null){
            return getAllIngredients()
        }

        val ingredient=when(type){
            "SAUCE" ->IngredientType.SAUCE
            "VEG" -> IngredientType.VEG
            "OTHER" -> IngredientType.OTHER
            else -> IngredientType.BUN
        }
        var mapped= IngredientConverter.convertIngredientList(repo.getIngredientByType(ingredient))
        return  ResponseEntity(mapped, HttpStatus.OK)
    }

    @GetMapping("/{id}")
    fun getIngredientById(@PathVariable id:String):ResponseEntity<IngredientModel>{
       val ingredient=repo.findById(id);
       if(ingredient.isPresent){
           return ResponseEntity(IngredientConverter.convertIngredient(ingredient.get(),true), HttpStatus.OK)
       }
       return ResponseEntity.notFound().build();
    }

}