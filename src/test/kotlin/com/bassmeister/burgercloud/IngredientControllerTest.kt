package com.bassmeister.burgercloud

import com.bassmeister.burgercloud.controllers.IngredientController
import com.bassmeister.burgercloud.data.IngredientRepo
import com.bassmeister.burgercloud.domain.IngredientType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IngredientControllerTest(@Autowired val ingredientController: IngredientController,
                               @Autowired val ingredientRepo: IngredientRepo) {

    @Test
    fun `Load All Ingredients`() {
        val ingredients=ingredientController.getAllIngredients()
        //Not a good test, but I am just testing testing
        assertEquals(8, ingredients.count())
    }

    @Test
    fun `Load Ingredient By Type`() {
        val sauces = ingredientController.getIngredientsByType("SAUCE")
        assertEquals(2, sauces.count())
        assertTrue(sauces.containsAll(ingredientRepo.getIngredientByType(IngredientType.SAUCE)), "Did not load all sauces")
    }
}