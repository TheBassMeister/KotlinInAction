package com.bassmeister.burgercloud

import com.bassmeister.burgercloud.controllers.UserController
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest() {

    @Autowired
    private lateinit var controller:UserController

    @Test
    fun `Load All Users`() {
        val users=controller!!.getUsers()
        Assertions.assertEquals(HttpStatus.OK, users.statusCode)
        Assertions.assertEquals(2, users.body?.count())
    }

    @Test
    fun `Load Hamburglar`(){
        val user=controller?.getUsersByLastName("Burglar")
        if(user!=null) {
            assertNotNull(user.body)
            assertEquals(1, user.body?.size)
            val burglar = user.body?.get(0)
            assertEquals("Ham", burglar?.firstName)
            assertEquals("Burglar", burglar?.lastName)
            assertEquals("Big Mac", burglar?.city)
        }
    }

}