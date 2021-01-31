package com.bassmeister.burgercloud

import com.bassmeister.burgercloud.api.IngredientToModelConverter
import com.bassmeister.burgercloud.controllers.IngredientController
import com.bassmeister.burgercloud.data.IngredientRepo
import com.bassmeister.burgercloud.domain.IngredientType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IngredientControllerTest(@Autowired val ingredientController: IngredientController,
                               @Autowired val ingredientRepo: IngredientRepo) {

    @Test
    fun `Load All Ingredients`() {
        val ingredients=ingredientController.getAllIngredients()
        //Not a good test, but I am just testing testing
        assertEquals(HttpStatus.OK, ingredients.statusCode)
        assertEquals(10, ingredients.body?.count())
    }

    @Test
    fun `Load Ingredient By Type`() {
        val sauces = ingredientController.getIngredientsByType("SAUCE")
        assertNotNull(sauces.body)
        val result=sauces.body
        assertEquals(2,result!!.count())
        var ingredientsAsModels= IngredientToModelConverter.convertIngredientList(ingredientRepo.getIngredientByType(IngredientType.SAUCE))
        assertTrue(result!!.containsAll(ingredientsAsModels), "Did not load all sauces")
        ingredientsAsModels.forEach{ model ->
            assertTrue(model.hasLinks())
            var link=model.getLink("self")
            assertTrue(link.isPresent)
            if(model.id=="KETCHUP"){
                assertEquals("/ingredients/KETCHUP",link.get().href)
            }else{
                assertEquals("/ingredients/MAYO",link.get().href)
            }
        }

    }

    @Test
    fun `Get Single Ingredient`(){
        val ingredientResponse=ingredientController.getIngredientById("BAC")
        assertEquals( HttpStatus.OK,ingredientResponse.statusCode)
        val ingredient=ingredientResponse.body;
        assertEquals("Bacon", ingredient?.name)
        assertEquals(IngredientType.OTHER, ingredient?.type)
    }

    @Test
    fun `Get Non Existing Ingredient`(){
        val ingredientResponse=ingredientController.getIngredientById("FOO")
        assertEquals( HttpStatus.NOT_FOUND,ingredientResponse.statusCode)
    }
}