package com.bassmeister.burgercloud

import com.bassmeister.burgercloud.controllers.CustomerController
import com.bassmeister.burgercloud.domain.Customer
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import javax.validation.ConstraintViolationException

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerControllerTest(@Autowired val controller:CustomerController) {

    @Test
    fun `Load All Users`() {
        val users=controller.getCustomers()
        Assertions.assertEquals(HttpStatus.OK, users.statusCode)
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
            assertEquals("Standard Burger", order.burgers[0].name)
        }
    }

    @Test
    fun `Add new Customer`(){
        val users=controller.getCustomers()
        val beforeCount=users.body?.count()

        val user=controller.addNewUser(createTestCustomer());
        assertEquals(HttpStatus.CREATED,user.statusCode)
        user.body?.let {
            assertEquals("Test", it.firstName)
            assertEquals("User", it.lastName)
        }

        val usersAfter=controller.getCustomers()
        if (beforeCount != null) {
            Assertions.assertEquals(beforeCount+1, usersAfter.body?.count())
        }
    }


    @Test
    fun `Add Customer with missing info`(){
        val users=controller.getCustomers()
        val beforeCount=users.body?.count()
        val testCustomer=createTestCustomer()
        testCustomer.firstName=""
        testCustomer.lastName=""

        try {
            val user = controller.addNewUser(testCustomer)
            fail("User Validation should have failed")
        }catch (ex: ConstraintViolationException){
            ex.message?.let {
                assertTrue(it.contains("addNewUser.customer.firstName"))
                assertTrue(it.contains("addNewUser.customer.lastName"))
            }
        }

        val usersAfter=controller.getCustomers()
        if (beforeCount != null) {
            Assertions.assertEquals(beforeCount, usersAfter.body?.count())
        }

    }


    private fun createTestCustomer():Customer{
        val testCustomer= Customer("Test", "User",
            "211 Main Street", "FOO", "AZ",
            "23421", "123-123-1234")
        testCustomer.password="BurglarHam"
        return testCustomer
    }

}