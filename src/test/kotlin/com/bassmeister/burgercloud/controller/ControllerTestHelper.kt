package com.bassmeister.burgercloud.controller

import com.bassmeister.burgercloud.domain.Customer

object ControllerTestHelper {

    fun createTestCustomer(): Customer {
        val testCustomer= Customer("Test", "User",
            "211 Main Street", "FOO", "AZ",
            "23421", "123-123-1234")
        testCustomer.password="BurglarHam"
        return testCustomer
    }

}