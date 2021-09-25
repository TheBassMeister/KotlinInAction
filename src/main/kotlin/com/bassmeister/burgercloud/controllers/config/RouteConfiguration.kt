package com.bassmeister.burgercloud.controllers.config

import com.bassmeister.burgercloud.controllers.handlers.BurgerHandler
import com.bassmeister.burgercloud.controllers.handlers.CustomerHandler
import com.bassmeister.burgercloud.controllers.handlers.IngredientHandler
import com.bassmeister.burgercloud.controllers.handlers.OrderHandler
import org.springframework.boot.autoconfigure.web.reactive.WebFluxProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Mono


@Configuration
class RouteConfiguration(
    private val burgerHandler: BurgerHandler,
    private val ingredientHandler: IngredientHandler,
    private val customerHandler: CustomerHandler,
    private val orderHandler: OrderHandler
) {

    @Bean
    fun route() = router {
        GET("") { _ -> getDefaultRoutes() }

        //Burgers
        val burgerRoot = "/burgers"
        GET("$burgerRoot") { _ -> burgerHandler.getAllBurgers() }
        GET("$burgerRoot/{id}").invoke(burgerHandler::getBurger)
        contentType(APPLICATION_JSON).nest {
            POST("$burgerRoot").invoke(burgerHandler::handleRequest)
        }
        //Ingredients
        val ingredientRoot = "/ingredients"
        GET("$ingredientRoot").invoke(ingredientHandler::getAllIngredients)
        GET("$ingredientRoot/{id}").invoke(ingredientHandler::getIngredientById)
        //Customers
        val customerRoot = "/customers"
        GET("$customerRoot").invoke(customerHandler::getCustomers)
        GET("$customerRoot/{id}").invoke(customerHandler::getCustomerById)
        GET("$customerRoot/orders/{id}").invoke(customerHandler::getCustomerOrders)
        contentType(APPLICATION_JSON).nest {
            POST("$customerRoot").invoke(customerHandler::handleRequest)
            PUT("$customerRoot/{id}").invoke(customerHandler::handleRequest)
        }
        DELETE("$customerRoot/{id}").invoke(customerHandler::deleteCustomer)
        //Orders
        val orderRoot = "/orders"
        GET("$orderRoot").invoke { _ -> orderHandler.getAllOrders() }
        GET("$orderRoot/{id}").invoke(orderHandler::getOrderById)
        contentType(APPLICATION_JSON).nest {
            POST("$orderRoot").invoke(orderHandler::handleRequest)
        }
        DELETE("$orderRoot/{id}").invoke(orderHandler::cancelRequest)

        //FALLBACK
        GET("/**") { _ -> ServerResponse.notFound().build() }

    }

    fun getDefaultRoutes(): Mono<ServerResponse> {
        //Not the most elegant solution, but HATEOAS and router functions do not work well together
        val burgerRoute = "http://localhost:8080/api/burgers"
        val ingredientRoute = "http://localhost:8080/api/ingredients"
        val customerRoute = "http://localhost:8080/api/customers"
        val orderRoot = "http://localhost:8080/api/orders"
        var routes = listOf(burgerRoute, ingredientRoute, customerRoute, orderRoot)
        return ServerResponse.ok().body(BodyInserters.fromValue(routes))
    }

    @Bean
    fun webFluxProperties(): WebFluxProperties {
        return WebFluxProperties()
    }
}