package com.bassmeister.burgercloud

import com.bassmeister.burgercloud.controllers.CustomerController
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerControllerTest(@Autowired val controller:CustomerController) {

    @Test
    fun `Load All Users`() {
        val users=controller.getCustomers()
        Assertions.assertEquals(HttpStatus.OK, users.statusCode)
        Assertions.assertEquals(2, users.body?.count())
    }

    @Test
    fun `Load Hamburglar`(){
        val user=controller.getUsersByLastName("Burglar")
        user?.let{
            assertNotNull(user.body)
            assertEquals(1, user.body?.size)
            val burglar = user.body?.get(0)
            assertEquals("Ham", burglar?.firstName)
            assertEquals("Burglar", burglar?.lastName)
            assertEquals("Big Mac", burglar?.city)
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

}