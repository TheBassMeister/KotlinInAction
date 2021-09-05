package com.bassmeister.burgercloud.controller

import com.bassmeister.burgercloud.controllers.CANNOT_CREATE_NEW_CUSTOMER
import com.bassmeister.burgercloud.controllers.CustomOrderException
import com.bassmeister.burgercloud.controllers.OrderController
import com.bassmeister.burgercloud.controllers.OrderWrapper
import com.bassmeister.burgercloud.data.BurgerRepo
import com.bassmeister.burgercloud.data.CustomerRepository
import com.bassmeister.burgercloud.domain.Order
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.transaction.TransactionSystemException
import javax.validation.ConstraintViolationException

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderControllerTest(@Autowired val controller: OrderController,
                          @Autowired val customerRepo:CustomerRepository,
                          @Autowired val burgerRepo: BurgerRepo) {


    @Test
    fun `Create New Order`() {
        val order=createTestOrder()
        val orderResponse=controller.addNewOrder(order)
        assertEquals(HttpStatus.CREATED, orderResponse.statusCode)
        orderResponse.body?.let {
            assertEquals("Ham",it.user.firstName)
            assertEquals("Burglar",it.user.lastName)
            assertEquals("Standard Burger", it.burgers[0].name)
        }
    }

    @Test
    fun `Create Order with non existing customer`() {
        val newCustomer=ControllerTestHelper.createTestCustomer()
        val burger=burgerRepo.findAll().elementAt(0)
        val order=OrderWrapper(newCustomer.id, listOf(burger),"347123743137749", "07/23", "341" )
        try {
            controller.addNewOrder(order)
            fail("Creation of an order for a not persisted customer should not have been possible")
        }catch(customOrderException:CustomOrderException){
            assertEquals(CANNOT_CREATE_NEW_CUSTOMER, customOrderException.message)
        }
    }

    @Test
    fun `Create Order with invalid Credit Card`() {
        val order=createTestOrder()
        order.ccNumber="332"
        try{
            controller.addNewOrder(order)
            fail("Credit Card Number check should not have allowed a save")
        }catch(ex:TransactionSystemException){
            ex.cause?.let {
                Assertions.assertTrue(it.cause is ConstraintViolationException)
                val constraintEx=it.cause as ConstraintViolationException
                val ingredientsEx=constraintEx.constraintViolations.iterator().next()
                assertEquals("Not a valid credit card number", ingredientsEx.message)
            }
        }
    }

    @Test
    fun `Cancel Order`(){
        val order=controller.addNewOrder(createTestOrder())
        val allOrdersBefore=controller.getAllOrders()
        allOrdersBefore.body?.let{
            val beforeSize=it.size
            order?.body?.orderId?.let {
                    it1 -> val deleteResponse=controller.cancelOrder(it1)
                    assertEquals(HttpStatus.NO_CONTENT, deleteResponse.statusCode)
            }
            val afterSize=controller.getAllOrders().body?.size
            assertEquals(beforeSize-1, afterSize)
        }
    }

    private fun createTestOrder():OrderWrapper{
        val customer=customerRepo.getUserByLastName("Burglar")[0]
        val burger=burgerRepo.findAll().elementAt(0)
        return OrderWrapper(customer.id, listOf(burger),"347123743137749", "07/23", "341" )
    }

}