package com.bassmeister.burgercloud.data

import com.bassmeister.burgercloud.domain.Ingredient
import com.bassmeister.burgercloud.domain.IngredientType
import org.springframework.data.repository.CrudRepository

interface IngredientRepo :CrudRepository<Ingredient, String>{

    fun getIngredientByType(type:IngredientType):List<Ingredient>
}