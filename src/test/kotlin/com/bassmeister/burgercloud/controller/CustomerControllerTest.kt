package com.bassmeister.burgercloud.controller

import com.bassmeister.burgercloud.controllers.CustomerController
import com.bassmeister.burgercloud.domain.Customer
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import javax.validation.ConstraintViolationException

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerControllerTest(@Autowired val controller:CustomerController) {
    companion object{
        var nrOfCustomers=2 //Not so gooding style need to rethink
    }


    @Test
    fun `Load All Users`() {
        val users=controller.getCustomers()
        assertEquals(HttpStatus.OK, users.statusCode)
        assertEquals(nrOfCustomers, users.body?.size)
    }

    @Test
    fun `Load Hamburglar`(){
        val user=controller.getUsersByLastName("Burglar")
        user?.let{
            assertNotNull(it.body)
            assertEquals(1, it.body?.size)
            val burglar = it.body?.get(0)
            burglar?.let {
                assertEquals("Ham", burglar.firstName)
                assertEquals("Burglar", burglar.lastName)
                assertEquals("Big Mac", burglar.city)
            }
        }
    }

    @Test
    fun `Get Hamburglars Orders`(){
        val orders=controller.getOrders(1)
        assertNotNull(orders.body, "Could not find Hamburglar's orders")
        orders.body?.let{
            assertEquals(1, orders.body?.size)
            val order= orders.body!![0]
            assertEquals("Standard Burger", order.burgers[0].burger.name)
        }
    }

    @Test
    fun `Add new Customer`(){
        val users=controller.getCustomers()
        val beforeCount=users.body?.count()

        val user=controller.addNewCustomer(ControllerTestHelper.createTestCustomer());
        assertEquals(HttpStatus.CREATED,user.statusCode)
        user.body?.let {
            assertEquals("Test", it.firstName)
            assertEquals("User", it.lastName)
        }

        val usersAfter=controller.getCustomers()
        if (beforeCount != null) {
            Assertions.assertEquals(beforeCount+1, usersAfter.body?.count())
        }
        nrOfCustomers++
    }


    @Test
    fun `Add Customer with missing info`(){
        val users=controller.getCustomers()
        val beforeCount=users.body?.count()
        val testCustomer=ControllerTestHelper.createTestCustomer()
        testCustomer.firstName=""
        testCustomer.lastName=""

        try {
            val user = controller.addNewCustomer(testCustomer)
            fail("User Validation should have failed")
        }catch (ex: ConstraintViolationException){
            ex.message?.let {
                assertTrue(it.contains("addNewCustomer.customer.firstName"))
                assertTrue(it.contains("addNewCustomer.customer.lastName"))
            }
        }

        val usersAfter=controller.getCustomers()
        if (beforeCount != null) {
            Assertions.assertEquals(beforeCount, usersAfter.body?.count())
        }
    }

    @Test
    fun `Delete Customer`(){
        val toDelete=ControllerTestHelper.createTestCustomer()
        val toDeleteModel=controller.addNewCustomer(toDelete)
        val allCustomers=controller.getCustomers()
        allCustomers.body?.let{
            val beforeSize=it.size
            val deleteOP= toDeleteModel.body?.id?.let { it1 -> controller.deleteCustomer(it1) }
            assertEquals(HttpStatus.NO_CONTENT,deleteOP?.statusCode)
            assertEquals(beforeSize-1, controller.getCustomers().body?.size)
        }
    }

    @Test
    fun `Update Existing Customer`(){
        val customer=controller.getCustomer(1)
        val newData=ControllerTestHelper.createTestCustomer()
        val updated=controller.updateCustomer(1, newData)
        assertEquals(HttpStatus.OK, updated.statusCode)
        //Test with a fresh load from repo
        val customerUpdate=controller.getCustomer(1)
        customerUpdate.body?.let {
            assertEquals("Test", it.firstName)
            assertEquals("User", it.lastName)
        }

    }




}