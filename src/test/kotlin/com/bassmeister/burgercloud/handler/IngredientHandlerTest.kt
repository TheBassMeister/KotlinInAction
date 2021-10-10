package com.bassmeister.burgercloud.handler

import com.bassmeister.burgercloud.domain.IngredientType
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*


class IngredientHandlerTest : HandlerTests() {

    private val ingredientPath = "/ingredients"
    private val ingredients = listOf(regBun, ketchup, mayo, toma, bacon)

    @Test
    fun `Load All Ingredients`() {
        Mockito.`when`(ingredientRepo.findAll()).thenReturn(Flux.fromIterable(ingredients))

        testClient.get().uri(ingredientPath).exchange()
            .expectStatus().isOk
            .expectBody().jsonPath("$").isArray
            .jsonPath("$").isNotEmpty
            .jsonPath("$.length()").isEqualTo(5)
    }

    @Test
    fun `Load Ingredient By Type`() {
        Mockito.`when`(ingredientRepo.getIngredientByType(IngredientType.SAUCE)).thenReturn(Flux.just(ketchup, mayo))

        testClient.get().uri("$ingredientPath?type=SAUCE").exchange().expectStatus().isOk
            .expectBody().jsonPath("$.length()").isEqualTo(2)
            .jsonPath("$[0].name").isEqualTo("Ketchup")
            .jsonPath("$[1].name").isEqualTo("Mayonnaise")
    }

    @Test
    fun `Get Single Ingredient`() {
        Mockito.`when`(ingredientRepo.findById("BAC")).thenReturn(Mono.just(bacon))

        testClient.get().uri("$ingredientPath/BAC").exchange().expectStatus().isOk.expectBody().jsonPath("$.name")
            .isEqualTo("Bacon")
            .jsonPath("$.type").isEqualTo(IngredientType.OTHER.name)
    }

    @Test
    fun `Get Non Existing Ingredient`() {
        Mockito.`when`(ingredientRepo.findById("FOO")).thenReturn(Mono.empty())
        testClient.get().uri("$ingredientPath/FOO").exchange().expectStatus().isNotFound
    }


}