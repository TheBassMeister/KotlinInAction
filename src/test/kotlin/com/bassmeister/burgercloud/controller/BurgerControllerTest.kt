package com.bassmeister.burgercloud.controller

import com.bassmeister.burgercloud.data.IngredientRepo
import com.bassmeister.burgercloud.domain.Burger
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import reactor.core.publisher.Mono

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BurgerControllerTest(@Autowired val ingredientRepo: IngredientRepo, @Autowired val testClient: WebTestClient) {

    private val burgers="/burgers"

    @Test
    fun `Get All Burgers`() {
        testClient.get().uri(burgers).exchange()
            .expectStatus().isOk
            .expectBody().jsonPath("$").isArray
            .jsonPath("$").isNotEmpty
            .jsonPath("$[0].name").isEqualTo("Standard Burger")
            .jsonPath("$[1].name").isEqualTo("The One with everything")
    }

    //Get Burger Test removed as the id is now randomly generated, will be tested in handler Test

    @Test
    fun `Add Burger`() {
        var countBefore = 0;
        testClient.get().uri(burgers).exchange().expectBody().jsonPath("$.size()").value<Int> {
            countBefore = it;
        }

        val burger = Mono.just(createNewBurger())
        testClient.post().uri(burgers).contentType(MediaType.APPLICATION_JSON)
            .body(burger, Burger::class.java).exchange()
            .expectStatus().isCreated
            .expectBody().jsonPath("$.name").isEqualTo("BACON MASTER")

        testClient.get().uri(burgers).exchange().expectBody().jsonPath("$.size()").value<Int> {
            assertEquals(countBefore + 1, it)
        }

    }

    @Test
    fun `Not Enough Ingredients`() {
        val burger = Mono.just(createBurgerWithNotEnoughIngredients())
        testClient.post().uri(burgers).contentType(MediaType.APPLICATION_JSON)
            .body(burger, Burger::class.java).exchange()
            .expectStatus().isBadRequest
            .expectBody<String>().isEqualTo("Please select at least three ingredients")
    }


    private fun createNewBurger(): Burger {
        val bun = ingredientRepo.findById("SES_BUN").block()
        val sauce = ingredientRepo.findById("KETCHUP").block()
        val extra = ingredientRepo.findById("TOMA").block()
        val extra2 = ingredientRepo.findById("BAC").block()
        val burger1Ingredients = listOf(bun, sauce, extra, extra2)
        return Burger("BACON MASTER", burger1Ingredients.filterNotNull(), false)
    }

    private fun createBurgerWithNotEnoughIngredients(): Burger {
        val bun = ingredientRepo.findById("SES_BUN").block()
        val sauce = ingredientRepo.findById("KETCHUP").block()
        val burger1Ingredients = listOf(bun, sauce)
        return Burger("Not Enough Ingredients Burger", burger1Ingredients.filterNotNull(), false)
    }

}