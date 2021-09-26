package com.bassmeister.burgercloud.handler

import com.bassmeister.burgercloud.domain.Burger
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.springframework.http.MediaType
import org.springframework.test.util.ReflectionTestUtils
import reactor.core.publisher.Mono
import java.util.*

class BurgerHandlerTest : HandlerTests() {

    private val burgers = "/burgers"


    private val burgerTwo = Burger("The One with everything", listOf(regBun, ketchup, mayo, toma, bacon), true)

    @Test
    fun `Get All Burgers`() {
        Mockito.`when`(burgerRepo.findAll()).thenReturn(listOf(burgerOne, burgerTwo))

        testClient.get().uri(burgers).exchange()
            .expectStatus().isOk
            .expectBody().jsonPath("$").isArray
            .jsonPath("$").isNotEmpty
            .jsonPath("$[0].name").isEqualTo("Standard Burger")
            .jsonPath("$[1].name").isEqualTo("The One with everything")
    }

    @Test
    fun `Get Burger`() {
        Mockito.`when`(burgerRepo.findById(3)).thenReturn(Optional.of(burgerOne))

        testClient.get().uri("$burgers/3").exchange()
            .expectStatus().isOk
            .expectBody().jsonPath("$.name").isEqualTo("Standard Burger")
            .jsonPath("$.ingredients").isArray
            .jsonPath("$.ingredients.length()").isEqualTo(3)
    }

    @Test
    fun `Add Burger`() {
        ReflectionTestUtils.setField(burgerHandler, "validator", validator)

        val burger = createNewBurger()

        Mockito.`when`(burgerRepo.findAll()).thenReturn(listOf(burgerOne, burgerTwo))
            .thenReturn(listOf(burgerOne, burgerTwo, burger))
        Mockito.`when`(burgerRepo.save(any(Burger::class.java))).thenAnswer { it.arguments[0] }

        var countBefore = 0;
        testClient.get().uri(burgers).exchange().expectBody().jsonPath("$.size()").value<Int> {
            countBefore = it;
        }

        testClient.post().uri(burgers).contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(burger), Burger::class.java).exchange()
            .expectStatus().isCreated
            .expectBody().jsonPath("$.name").isEqualTo("BACON MASTER")

        testClient.get().uri(burgers).exchange().expectBody().jsonPath("$.size()").value<Int> {
            assertEquals(countBefore + 1, it)
        }

    }

    @Test
    fun `Not Enough Ingredients`() {

        val burger = createBurgerWithNotEnoughIngredients()
        val errors = defaultValidator.validate(burger)
        assertEquals(1, errors.size)
        val notEnoughIngredientError = errors.iterator().next()
        assertEquals("Please select at least three ingredients", notEnoughIngredientError.message)
    }

    private fun createNewBurger(): Burger {
        val burger1Ingredients = listOf(regBun, ketchup, bacon, toma)
        return Burger("BACON MASTER", burger1Ingredients, false)
    }

    private fun createBurgerWithNotEnoughIngredients(): Burger {
        val burger1Ingredients = listOf(regBun, ketchup)
        return Burger("Not Enough Ingredients Burger", burger1Ingredients, false)
    }
}