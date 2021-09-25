package com.bassmeister.burgercloud.controller

import com.bassmeister.burgercloud.domain.Customer
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import reactor.core.publisher.Mono

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerControllerTest(@Autowired val testClient: WebTestClient) {

    private val customers="/customers"

    @Test
    fun `Load All Users`() {
        testClient.get().uri(customers).exchange().expectStatus().isOk
            .expectBody().jsonPath("$").isArray
            .jsonPath("$[0].firstName").isEqualTo("Ham")
            .jsonPath("$[1].firstName").isEqualTo("Ronald")
    }

    @Test
    fun `Load Hamburglar`() {
        testClient.get().uri("$customers?lastName=Burglar").exchange().expectStatus().isOk
            .expectBody().jsonPath("$").isArray
            .jsonPath("$[0].firstName").isEqualTo("Ham")
            .jsonPath("$[0].lastName").isEqualTo("Burglar")
            .jsonPath("$[0].city").isEqualTo("Big Mac")
    }

    @Test
    fun `Add new Customer`() {
        var countBefore = 0;
        testClient.get().uri(customers).exchange().expectBody().jsonPath("$.size()").value<Int> {
            countBefore = it;
        }

        val customer = Mono.just(ControllerTestHelper.createTestCustomer())

        testClient.post().uri(customers).contentType(MediaType.APPLICATION_JSON)
            .body(customer, Customer::class.java).exchange()
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

        var result = testClient.post().uri(customers).contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(customer), Customer::class.java).exchange()
            .expectStatus().isBadRequest
            .expectBody<String>().returnResult().responseBody

        assertNotNull(result, "There should have been failed validation message")
        if (result != null) {
            assertTrue(
                result.contains("First Name must be set"),
                "There was no validation message about missing first name"
            )
            assertTrue(
                result.contains("Last Name must be set"),
                "There was no validation message about missing last name"
            )
        }
    }

    @Test
    fun `Update Existing Customer`() {
        var id = 0
        val customer = ControllerTestHelper.createTestCustomer()
        testClient.post().uri(customers).contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(customer), Customer::class.java).exchange().expectBody().jsonPath("$.id").value<Int> {
                id = it
            }
        customer.firstName = "Updated"
        customer.lastName = "Fooo"
        testClient.put().uri("$customers/$id").contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(customer), Customer::class.java).exchange().expectStatus().isOk

        testClient.get().uri("$customers?lastName=Fooo").exchange().expectStatus().isOk.expectBody().jsonPath("$[0].id")
            .value<Int> {
                assertEquals(id, it)
            }.jsonPath("$[0].firstName").isEqualTo("Updated")

    }

    @Test
    fun `Delete Customer`() {
        var id = 0
        val customer = ControllerTestHelper.createTestCustomer()
        testClient.post().uri(customers).contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(customer), Customer::class.java).exchange().expectBody().jsonPath("$.id").value<Int> {
                id = it
            }

        var countBeforeDelete = 0;
        testClient.get().uri(customers).exchange().expectBody().jsonPath("$.size()").value<Int> {
            countBeforeDelete = it;
        }

        testClient.delete().uri("$customers/$id").exchange().expectStatus().isOk
        testClient.get().uri(customers).exchange().expectBody().jsonPath("$.size()").value<Int> {
            assertEquals(countBeforeDelete-1, it)
        }
    }

    @Test
    fun `Get Hamburglars Orders`(){
        testClient.get().uri("$customers/orders/1").exchange().expectStatus().isOk
            .expectBody().jsonPath("$").isArray
            .jsonPath("$.size()").isEqualTo(1)
            .jsonPath("$[0].burgers").isArray
            .jsonPath("$[0].burgers.size()").isEqualTo(2)
            .jsonPath("$[0].burgers[0].burger.name").isEqualTo("Standard Burger")
    }

}