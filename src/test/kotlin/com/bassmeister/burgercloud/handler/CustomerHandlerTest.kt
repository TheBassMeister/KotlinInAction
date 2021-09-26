package com.bassmeister.burgercloud.handler

import com.bassmeister.burgercloud.controller.ControllerTestHelper
import com.bassmeister.burgercloud.domain.BurgerOrder
import com.bassmeister.burgercloud.domain.Customer
import com.bassmeister.burgercloud.domain.Order
import org.hamcrest.CoreMatchers.hasItems
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.springframework.http.MediaType
import org.springframework.test.util.ReflectionTestUtils
import reactor.core.publisher.Mono
import java.util.*

class CustomerHandlerTest : HandlerTests() {


    private val customers = "/customers"

    private val customer2 = Customer("Ronald", "Donald", "Whopper Avenue 2", "Friesville", "NM", "333222", "5325893")

    @Test
    fun `Load All Users`() {
        Mockito.`when`(customerRepo.findAll()).thenReturn(listOf(customerOne, customer2))

        testClient.get().uri(customers).exchange().expectStatus().isOk
            .expectBody().jsonPath("$").isArray
            .jsonPath("$[0].firstName").isEqualTo("Ham")
            .jsonPath("$[1].firstName").isEqualTo("Ronald")
    }

    @Test
    fun `Load Hamburglar`() {
        Mockito.`when`(customerRepo.getUserByLastName("Burglar")).thenReturn(listOf(customerOne))

        testClient.get().uri("$customers?lastName=Burglar").exchange().expectStatus().isOk
            .expectBody().jsonPath("$").isArray
            .jsonPath("$[0].firstName").isEqualTo("Ham")
            .jsonPath("$[0].lastName").isEqualTo("Burglar")
            .jsonPath("$[0].city").isEqualTo("BurgerTown")
    }

    @Test
    fun `Add new Customer`() {
        setupPostTests()

        val customer = ControllerTestHelper.createTestCustomer()

        Mockito.`when`(customerRepo.findAll()).thenReturn(listOf(customerOne, customer2))
            .thenReturn(listOf(customerOne, customer2, customer))

        var countBefore = 0;
        testClient.get().uri(customers).exchange().expectBody().jsonPath("$.size()").value<Int> {
            countBefore = it;
        }

        testClient.post().uri(customers).contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(customer), Customer::class.java).exchange()
            .expectStatus().isCreated
            .expectBody().jsonPath("$.firstName").isEqualTo("Test")
            .jsonPath("$.lastName").isEqualTo("User")


        testClient.get().uri("/customers").exchange().expectBody().jsonPath("$.size()").value<Int> {
            assertEquals(countBefore + 1, it)
        }
    }

    @Test
    fun `Add Customer with missing info`() {

        val customer = ControllerTestHelper.createTestCustomer()
        customer.firstName = ""
        customer.lastName = ""

        val errors = defaultValidator.validate(customer)
        assertEquals(2, errors.size)
        val errorMsgs = errors.map { err -> err.message }
        println(errorMsgs)
        assertThat(errorMsgs, hasItems("First Name must be set", "Last Name must be set"))
    }

    @Test
    fun `Update Existing Customer`() {
        setupPostTests()
        val customer = ControllerTestHelper.createTestCustomer()

        Mockito.`when`(customerRepo.findById(3)).thenReturn(Optional.of(customer))

        testClient.post().uri(customers).contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(customer), Customer::class.java).exchange()

        customer.firstName = "Updated"
        customer.lastName = "Fooo"
        testClient.put().uri("$customers/3").contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(customer), Customer::class.java).exchange().expectStatus().isOk

        Mockito.`when`(customerRepo.getUserByLastName("Fooo")).thenReturn(listOf(customer))

        testClient.get().uri("$customers?lastName=Fooo").exchange().expectStatus().isOk.expectBody()
            .jsonPath("$[0].firstName").isEqualTo("Updated")

    }

    @Test
    fun `Delete Customer`() {
        setupPostTests()
        val customer = ControllerTestHelper.createTestCustomer()

        testClient.post().uri(customers).contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(customer), Customer::class.java).exchange()

        testClient.delete().uri("$customers/3").exchange().expectStatus().isOk
    }

    @Test
    fun `Get Hamburglars Orders`(){
        customerOne.id=1L
        val orderWithId=Order(customerOne, listOf(BurgerOrder(burgerOne, 3)),"378618187748325", "10/23", "221" )

        Mockito.`when`(orderRepo.findAll()).thenReturn(listOf(orderWithId))


        testClient.get().uri("$customers/orders/1").exchange().expectStatus().isOk
            .expectBody().jsonPath("$").isArray
            .jsonPath("$.size()").isEqualTo(1)
            .jsonPath("$[0].burgers").isArray
            .jsonPath("$[0].burgers.size()").isEqualTo(1)
            .jsonPath("$[0].burgers[0].burger.name").isEqualTo("Standard Burger")
    }

    private fun setupPostTests() {
        ReflectionTestUtils.setField(customerHandler, "validator", validator)
        Mockito.`when`(customerRepo.save(ArgumentMatchers.any(Customer::class.java))).thenAnswer { it.arguments[0] }
    }
}