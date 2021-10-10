package com.bassmeister.burgercloud.controller

import com.bassmeister.burgercloud.controllers.OrderWrapper
import com.bassmeister.burgercloud.controllers.handlers.CANNOT_CREATE_NEW_CUSTOMER
import com.bassmeister.burgercloud.data.BurgerRepo
import com.bassmeister.burgercloud.data.CustomerRepo
import com.bassmeister.burgercloud.data.IngredientRepo
import com.bassmeister.burgercloud.domain.Burger
import com.bassmeister.burgercloud.domain.BurgerOrder
import com.bassmeister.burgercloud.domain.Customer
import com.bassmeister.burgercloud.domain.Order
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import reactor.core.publisher.Mono

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderControllerTest(
    @Autowired val testClient: WebTestClient,
    @Autowired val customerRepo: CustomerRepo,
    @Autowired val burgerRepo: BurgerRepo,
    @Autowired val ingredientRepo: IngredientRepo
) {

    private val orders = "/orders"

    @Test
    fun `Get All Orders`() {
        testClient.get().uri(orders).exchange()
            .expectStatus().isOk
            .expectBody().jsonPath("$").isArray
            .jsonPath("$").isNotEmpty
            .jsonPath("$[0].burgers[0].burger.name").isEqualTo("Standard Burger")
    }

    @Test
    fun `Create New Order`() {
        val order = createTestOrder()
        order?.let {
            testClient.post().uri(orders).contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(order), Order::class.java).exchange()
                .expectStatus().isCreated.expectBody().jsonPath("$.customer.firstName").isEqualTo("Ham")
                .jsonPath("$.customer.lastName").isEqualTo("Burglar")
        }
    }

    @Test
    fun `Create Order with non existing customer`() {
        val newCustomer = ControllerTestHelper.createTestCustomer()
        val burger = burgerRepo.findAll().elementAt(0).block()
        burger?.let {
            val order = OrderWrapper(newCustomer.id, listOf(BurgerOrder(burger, 3)), "347123743137749", "07/23", "341")
            testClient.post().uri(orders).contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(order), Order::class.java)
                .exchange()
                .expectStatus().isBadRequest.expectBody<String>().isEqualTo(CANNOT_CREATE_NEW_CUSTOMER)
        }
    }

    @Test
    fun `Create Order with invalid Credit Card`() {
        val order = createTestOrder()
        order?.let {
            order.ccNumber = "332"
            testClient.post().uri(orders).contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(order), Order::class.java)
                .exchange()
                .expectStatus().isBadRequest.expectBody<String>().isEqualTo("Not a valid credit card number")
        }
    }

    @Test
    fun `Create Order with custom Burger`() {
        val customer = customerRepo.getUserByLastName("Burglar").elementAt(0).block()
        val newBurger = createNewBurger();
        val order = OrderWrapper(customer!!.id, listOf(BurgerOrder(newBurger, 2)), "347123743137749", "07/23", "341")
        testClient.post().uri(orders).contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(order), Order::class.java).exchange()
            .expectStatus().isCreated.expectBody().jsonPath("$.customer.firstName").isEqualTo("Ham")
            .jsonPath("$.burgers[0].burger.name").isEqualTo("BACON MASTER")
    }

    private fun createNewBurger(): Burger {
        val bun = ingredientRepo.findById("SES_BUN").block()
        val sauce = ingredientRepo.findById("KETCHUP").block()
        val extra = ingredientRepo.findById("TOMA").block()
        val extra2 = ingredientRepo.findById("BAC").block()
        val burger1Ingredients = listOf(bun, sauce, extra, extra2)
        return Burger("BACON MASTER", burger1Ingredients.filterNotNull(), false)
    }

    @Test
    fun `Cancel Order`() {
        val order = createTestOrder()
        order?.let {
            var orderId = ""

            testClient.post().uri(orders).contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(order), Customer::class.java).exchange()

            testClient.get().uri(orders).exchange().expectBody().jsonPath("$").value<List<LinkedHashMap<String, String>>> {
                orderId= it.last()["id"]!!
            }

            var countBefore = 0;
            testClient.get().uri(orders).exchange().expectBody().jsonPath("$.size()").value<Int> {
                countBefore = it;
            }
            testClient.delete().uri("$orders/$orderId").exchange().expectStatus().isOk

            testClient.get().uri(orders).exchange().expectBody().jsonPath("$.size()").value<Int> {
                assertEquals(countBefore - 1, it)
            }
        }
    }

    private fun createTestOrder(): OrderWrapper? {
        val customer = customerRepo.getUserByLastName("Burglar").elementAt(0).block()
        val burger = burgerRepo.findAll().elementAt(0).block()
        burger?.let {
            return OrderWrapper(customer!!.id, listOf(BurgerOrder(burger, 1)), "347123743137749", "07/23", "341")
        }
        return null
    }

}