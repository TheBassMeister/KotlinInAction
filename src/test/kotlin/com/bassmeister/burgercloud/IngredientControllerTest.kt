package com.bassmeister.burgercloud

import com.bassmeister.burgercloud.api.converters.IngredientConverter
import com.bassmeister.burgercloud.controllers.IngredientController
import com.bassmeister.burgercloud.data.IngredientRepo
import com.bassmeister.burgercloud.domain.IngredientType
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IngredientControllerTest() {

    @Autowired
    private lateinit var ingredientController: IngredientController
    @Autowired
    private lateinit var ingredientRepo:IngredientRepo


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
        var ingredientsAsModels= IngredientConverter.convertIngredientList(ingredientRepo.getIngredientByType(IngredientType.SAUCE))
        assertTrue(result!!.containsAll(ingredientsAsModels), "Did not load all sauces")
        ingredientsAsModels.forEach{ model ->
            assertTrue(model.hasLinks())
            var link=model.getLink("self")
            assertTrue(link.isPresent)
            if(model.id=="KETCHUP"){
                assertEquals("/api/ingredients/KETCHUP",link.get().href)
            }else{
                assertEquals("/api/ingredients/MAYO",link.get().href)
            }
        }

    }

    @Test
    fun `Get Single Ingredient`(){
        val ingredientResponse=ingredientController.getIngredientById("BAC")
        assertEquals( HttpStatus.OK,ingredientResponse.statusCode)
        val ingredient=ingredientResponse.body;
        assertEquals("Bacon", ingredient?.name)
        assertEquals(IngredientType.OTHER.name, ingredient?.type)
    }

    @Test
    fun `Get Non Existing Ingredient`(){
        val ingredientResponse=ingredientController.getIngredientById("FOO")
        assertEquals( HttpStatus.NOT_FOUND,ingredientResponse.statusCode)
    }
}