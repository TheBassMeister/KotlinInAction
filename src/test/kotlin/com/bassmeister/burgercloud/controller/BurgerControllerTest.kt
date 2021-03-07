package com.bassmeister.burgercloud.controller

import com.bassmeister.burgercloud.controllers.BurgerController
import com.bassmeister.burgercloud.data.IngredientRepo
import com.bassmeister.burgercloud.domain.Burger
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.transaction.TransactionSystemException
import javax.validation.ConstraintViolationException

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BurgerControllerTest(@Autowired val controller:BurgerController, @Autowired val ingredientRepo: IngredientRepo) {
    companion object{
        var nrOfBurgers=1 //Not so gooding style need to rethink
    }


    @Test
    fun `Get All Burgers`() {
        val burgers=controller.getBurgers()
        assertEquals(HttpStatus.OK, burgers.statusCode)
        assertEquals(nrOfBurgers, burgers.body?.size)
    }

    @Test
    fun `Get Burger`() {
        val burger=controller.getBurger(3);
        Assertions.assertEquals(HttpStatus.OK, burger.statusCode)
        burger.body?.let {
            assertEquals("Standard Burger",it.name)
            val ingredients=it.ingredients
            assertTrue(ingredients.size==4)
        }
    }

    @Test
    fun `Add Burger`(){
        val countBefore=controller.getBurgers().body?.size
        val newBurger=controller.addNewBurger(createNewBurger())
        val countAfter=controller.getBurgers().body?.size
        val burgerFromRepo=controller.getBurger(newBurger.body!!.id)
        burgerFromRepo.body?.let {
            assertEquals("BACON MASTER", it.name)
        }
        if(countBefore!=null) {
            assertEquals((countBefore + 1), countAfter)
        }
        nrOfBurgers++
    }


    @Test
    fun `Not Enough Ingredients`(){
        try{
            controller.addNewBurger(createBurgerWithNotEnoughIngredients())
            fail("Adding a new Burger like this should not have been possible")
        }catch (ex: TransactionSystemException){
            ex.cause?.let {
                assertTrue(it.cause is ConstraintViolationException)
                val constraintEx=it.cause as ConstraintViolationException
                val ingredientsEx=constraintEx.constraintViolations.iterator().next()
                assertEquals("Please select at least three ingredients", ingredientsEx.message)
            }
        }
    }

    @Test
    fun `Delete Burger`(){
        val newBurger=createNewBurger()
        val created=controller.addNewBurger(newBurger)
        val totalBurgers=controller.getBurgers()
        totalBurgers.body?.let {
            val beforeDeleteSize=it.size
            created.body?.id?.let {
                it1 -> val deleteResponse=controller.deleteBurger(it1)
                assertEquals(HttpStatus.NO_CONTENT, deleteResponse.statusCode)
            }
            val afterDeleteSize=controller.getBurgers().body?.size
            assertEquals(beforeDeleteSize-1, afterDeleteSize)
        }


    }

    private fun createNewBurger(): Burger {
        val bun=ingredientRepo.findById("SES_BUN").get()
        val sauce=ingredientRepo.findById("KETCHUP").get()
        val extra=ingredientRepo.findById("TOMA").get()
        val extra2=ingredientRepo.findById("BAC").get()
        val burger1Ingredients= listOf(bun, sauce,extra, extra2)
        return Burger("BACON MASTER", burger1Ingredients)
    }

    private fun createBurgerWithNotEnoughIngredients(): Burger {
        val bun=ingredientRepo.findById("SES_BUN").get()
        val sauce=ingredientRepo.findById("KETCHUP").get()
        val burger1Ingredients= listOf(bun, sauce)
        return Burger("Not Enough Ingredients Burger", burger1Ingredients)
    }

}