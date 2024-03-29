package com.bassmeister.burgercloud.handler

import com.bassmeister.burgercloud.controller.ControllerTestHelper
import com.bassmeister.burgercloud.controllers.OrderWrapper
import com.bassmeister.burgercloud.controllers.handlers.CANNOT_CREATE_NEW_CUSTOMER
import com.bassmeister.burgercloud.domain.Burger
import com.bassmeister.burgercloud.domain.BurgerOrder
import com.bassmeister.burgercloud.domain.Customer
import com.bassmeister.burgercloud.domain.Order
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.*
import org.mockito.Mockito
import org.springframework.http.MediaType
import org.springframework.test.util.ReflectionTestUtils
import org.springframework.test.web.reactive.server.expectBody
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

class OrderHandlerTest : HandlerTests() {

    private val orders = "/orders"

    @Test
    fun `Get All Orders`() {
        Mockito.`when`(orderRepo.findAll()).thenReturn(Flux.fromIterable(listOf(testOrder)))

        testClient.get().uri(orders).exchange()
            .expectStatus().isOk
            .expectBody().jsonPath("$").isArray
            .jsonPath("$").isNotEmpty
            .jsonPath("$[0].burgers[0].burger.name").isEqualTo("Standard Burger")
    }

    @Test
    fun `Create New Order`() {
        setupPostTests()
        val order = createTestOrder()
        val orderWrapper = transformToWrapper(order)
        Mockito.`when`(customerRepo.findById(anyString())).thenReturn(Mono.just(customerOne))
        Mockito.`when`(orderRepo.save(any(Order::class.java))).thenAnswer { Mono.just(it.arguments[0]) }
        Mockito.`when`(burgerRepo.findByName(burgerOne.name)).thenReturn(Mono.just(burgerOne))

        Mockito.`when`(burgerRepo.save(any(Burger::class.java))).thenAnswer { Mono.just(it.arguments[0]) }
        Mockito.`when`(burgerOrderRepo.save(any(BurgerOrder::class.java))).thenAnswer { Mono.just(it.arguments[0]) }

        testClient.post().uri(orders).contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(orderWrapper), OrderWrapper::class.java::class.java).exchange()
            .expectStatus().isCreated.expectBody().jsonPath("$.customer.firstName").isEqualTo("Ham")
            .jsonPath("$.customer.lastName").isEqualTo("Burglar")
    }

    @Test
    fun `Create Order with non existing customer`() {
        setupPostTests()

        Mockito.`when`(customerRepo.findById(anyString())).thenReturn(Mono.empty())

        val newCustomer = ControllerTestHelper.createTestCustomer()
        val order = OrderWrapper(newCustomer.id, listOf(BurgerOrder(burgerOne, 3)), "347123743137749", "07/23", "341")
        testClient.post().uri(orders).contentType(MediaType.APPLICATION_JSON).body(Mono.just(order), Order::class.java)
            .exchange()
            .expectStatus().isBadRequest.expectBody<String>().isEqualTo(CANNOT_CREATE_NEW_CUSTOMER)
    }

    @Test
    fun `Create Order with invalid Credit Card`() {

        val order = transformToWrapper(createTestOrder())
        order.ccNumber = "332"

        val errors = defaultValidator.validate(order)
        assertEquals(1, errors.size)
        assertEquals("Not a valid credit card number", errors.iterator().next().message)
    }

    //Custom Burger Test not needed here as it is more of a repository than handler test

    @Test
    fun `Cancel Order`() {
        setupPostTests()
        val order =createTestOrder()
        val orderW = transformToWrapper(createTestOrder())

        Mockito.`when`(orderRepo.findById(anyString())).thenReturn(Mono.just(order))
        Mockito.`when`(orderRepo.delete(any(Order::class.java))).thenReturn(Mono.empty())
        Mockito.`when`(customerRepo.findById(anyString())).thenReturn(Mono.just(customerOne))

        testClient.post().uri(orders).contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(orderW), OrderWrapper::class.java::class.java).exchange()

        testClient.delete().uri("$orders/1").exchange().expectStatus().isOk

    }

    private fun createTestOrder(): Order {
        return Order(customerOne, listOf(BurgerOrder(burgerOne, 4)), "347123743137749", "07/23", "341")
    }

    private fun setupPostTests() {
        ReflectionTestUtils.setField(orderHandler, "validator", validator)
        Mockito.`when`(orderRepo.save(any(Order::class.java))).thenAnswer { it.arguments[0] }
    }

    private fun transformToWrapper(order: Order): OrderWrapper {
        return OrderWrapper("1", order.burgers, order.ccNumber, order.ccExpDate, order.ccCVC)
    }
}