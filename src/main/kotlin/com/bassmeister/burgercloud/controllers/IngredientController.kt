package com.bassmeister.burgercloud.controllers

import com.bassmeister.burgercloud.api.IngredientModel
import com.bassmeister.burgercloud.api.IngredientToModelConverter
import com.bassmeister.burgercloud.data.IngredientRepo
import com.bassmeister.burgercloud.domain.Ingredient
import com.bassmeister.burgercloud.domain.IngredientType
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = ["/ingredients"], produces = ["application/hal+json"])
class IngredientController (val repo: IngredientRepo){

    @GetMapping
    fun getAllIngredients(): ResponseEntity<List<IngredientModel>> {
        var allIngredients=IngredientToModelConverter.convertIngredientList(repo.findAll())
        return  ResponseEntity<List<IngredientModel>>(allIngredients, HttpStatus.OK);
    }

    @GetMapping("/type")
    fun getIngredientsByType(@RequestParam(value = "type", defaultValue= "BUN") type: String):ResponseEntity<List<IngredientModel>>{
        val ingredient=when(type){
            "SAUCE" ->IngredientType.SAUCE
            "VEG" -> IngredientType.VEG
            "OTHER" -> IngredientType.OTHER
            else -> IngredientType.BUN
        }
        var mapped=IngredientToModelConverter.convertIngredientList(repo.getIngredientByType(ingredient))
        return  ResponseEntity<List<IngredientModel>>(mapped, HttpStatus.OK)
    }

    @GetMapping("/{id}")
    fun getIngredientById(@PathVariable id:String):ResponseEntity<Ingredient>{
       val ingredient=repo.findById(id);
       if(ingredient.isPresent){
           return ResponseEntity<Ingredient>(ingredient.get(), HttpStatus.OK)
       }
       return ResponseEntity.notFound().build();
    }

}