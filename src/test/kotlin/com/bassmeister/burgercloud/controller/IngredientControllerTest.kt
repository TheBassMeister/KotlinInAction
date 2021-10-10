package com.bassmeister.burgercloud.controller

import com.bassmeister.burgercloud.domain.IngredientType
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IngredientControllerTest(
    @Autowired val testClient: WebTestClient
) {

    private val ingredients = "/ingredients"

    @Test
    fun `Load All Ingredients`() {
        testClient.get().uri(ingredients).exchange()
            .expectStatus().isOk
            .expectBody().jsonPath("$").isArray
            .jsonPath("$").isNotEmpty
            .jsonPath("$.length()").isEqualTo(10)
    }

    @Test
    fun `Load Ingredient By Type`() {
        testClient.get().uri("$ingredients?type=SAUCE").exchange().expectStatus().isOk
            .expectBody().jsonPath("$.length()").isEqualTo(2)
            .jsonPath("$[0].name").isEqualTo("Mayonnaise")
            .jsonPath("$[1].name").isEqualTo("Ketchup")
    }

    @Test
    fun `Get Single Ingredient`() {
        testClient.get().uri("$ingredients/BAC").exchange().expectStatus().isOk.expectBody().jsonPath("$.name")
            .isEqualTo("Bacon")
            .jsonPath("$.type").isEqualTo(IngredientType.OTHER.name)
    }

    @Test
    fun `Get Non Existing Ingredient`() {
        testClient.get().uri("$ingredients/FOO").exchange().expectStatus().isNotFound
    }
}