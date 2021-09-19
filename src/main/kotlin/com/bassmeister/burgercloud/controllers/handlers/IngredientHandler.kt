package com.bassmeister.burgercloud.controllers.handlers

import com.bassmeister.burgercloud.data.IngredientRepo
import com.bassmeister.burgercloud.domain.IngredientType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class IngredientHandler(private val ingredientRepo: IngredientRepo) {

    fun getAllIngredients(request:ServerRequest): Mono<ServerResponse>{
        val type=request.queryParam("type")
        if(type.isPresent){
            val ingredientType=when(type.get()){
                "SAUCE" -> IngredientType.SAUCE
                "VEG" -> IngredientType.VEG
                "OTHER" -> IngredientType.OTHER
                else -> IngredientType.BUN
            }
            return ServerResponse.ok().body(BodyInserters.fromValue(ingredientRepo.getIngredientByType(ingredientType)))
        }
        return ServerResponse.ok().body(BodyInserters.fromValue(ingredientRepo.findAll()))
    }

    fun getIngredientById(request: ServerRequest):Mono<ServerResponse>{
        var ingredient=ingredientRepo.findById(request.pathVariable("id"))
        if(ingredient.isPresent){
            return ServerResponse.ok().body(BodyInserters.fromValue(ingredient.get()))
        }
        return ServerResponse.notFound().build()
    }

}