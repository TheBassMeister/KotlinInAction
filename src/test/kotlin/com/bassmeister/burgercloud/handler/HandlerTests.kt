package com.bassmeister.burgercloud.handler

import com.bassmeister.burgercloud.controllers.config.RouteConfiguration
import com.bassmeister.burgercloud.controllers.handlers.BurgerHandler
import com.bassmeister.burgercloud.controllers.handlers.CustomerHandler
import com.bassmeister.burgercloud.controllers.handlers.IngredientHandler
import com.bassmeister.burgercloud.controllers.handlers.OrderHandler
import com.bassmeister.burgercloud.data.*
import com.bassmeister.burgercloud.domain.*
import org.mockito.Mockito
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.validation.Validator
import javax.validation.Validation

abstract class HandlerTests {
    protected val ingredientRepo: IngredientRepo = Mockito.mock(IngredientRepo::class.java)
    protected val burgerRepo: BurgerRepo = Mockito.mock(BurgerRepo::class.java)
    protected val customerRepo: CustomerRepo = Mockito.mock(CustomerRepo::class.java)
    protected val orderRepo: OrderRepo = Mockito.mock(OrderRepo::class.java)
    protected val burgerOrderRepo: BurgerOrderRepo = Mockito.mock(BurgerOrderRepo::class.java)

    protected val burgerHandler = BurgerHandler(burgerRepo)
    protected val customerHandler = CustomerHandler(customerRepo, orderRepo)
    protected val orderHandler = OrderHandler(orderRepo, customerRepo, burgerRepo, burgerOrderRepo)
    private val ingredientHandler = IngredientHandler(ingredientRepo)

    private val routes = RouteConfiguration(burgerHandler, ingredientHandler, customerHandler, orderHandler)

    protected val testClient = WebTestClient.bindToRouterFunction(routes.route()).build()

    protected val regBun = Ingredient("REG_BUN", "Regular Bun", IngredientType.BUN)
    protected val ketchup = Ingredient("KETCHUP", "Ketchup", IngredientType.SAUCE)
    protected val mayo = Ingredient("MAYO", "Mayonnaise", IngredientType.SAUCE)
    protected val toma = Ingredient("TOMA", "Tomatoes", IngredientType.VEG)
    protected val bacon = Ingredient("BAC", "Bacon", IngredientType.OTHER)

    protected val validator: Validator = Mockito.mock(Validator::class.java)

    protected val burgerOne = Burger("Standard Burger", listOf(regBun, ketchup, bacon), true)
    protected val customerOne = Customer("Ham", "Burglar", "McDonalds Drive 3", "BurgerTown", "CA", "21132", "44332211")
    protected val testOrder = Order(customerOne, listOf(BurgerOrder(burgerOne, 3)), "378618187748325", "10/23", "221")

    protected val defaultValidator = Validation.buildDefaultValidatorFactory().validator

}