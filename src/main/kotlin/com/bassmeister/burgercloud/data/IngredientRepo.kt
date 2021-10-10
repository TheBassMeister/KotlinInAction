package com.bassmeister.burgercloud.data

import com.bassmeister.burgercloud.domain.Ingredient
import com.bassmeister.burgercloud.domain.IngredientType
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface IngredientRepo :ReactiveCrudRepository<Ingredient, String>{

    fun getIngredientByType(type:IngredientType):Flux<Ingredient>
}