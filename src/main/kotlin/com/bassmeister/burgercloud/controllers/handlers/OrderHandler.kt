package com.bassmeister.burgercloud.controllers.handlers

import com.bassmeister.burgercloud.controllers.OrderWrapper
import com.bassmeister.burgercloud.controllers.validation.AbstractValidationHandler
import com.bassmeister.burgercloud.data.BurgerOrderRepo
import com.bassmeister.burgercloud.data.BurgerRepo
import com.bassmeister.burgercloud.data.CustomerRepo
import com.bassmeister.burgercloud.data.OrderRepo
import com.bassmeister.burgercloud.domain.BurgerOrder
import com.bassmeister.burgercloud.domain.Order
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.net.URI

const val CANNOT_CREATE_NEW_CUSTOMER = "Cannot create orders for unknown customer"

@Component
class OrderHandler(
    private val orderRepo: OrderRepo,
    private val customerRepo: CustomerRepo,
    private val burgerRepo: BurgerRepo,
    private val burgerOrderRepo: BurgerOrderRepo
) :
    AbstractValidationHandler<OrderWrapper>(OrderWrapper::class.java) {

    fun getAllOrders(): Mono<ServerResponse> {
        return ServerResponse.ok().body(BodyInserters.fromValue(orderRepo.findAll()))
    }

    fun getOrderById(request: ServerRequest): Mono<ServerResponse> {
        var order = orderRepo.findById(request.pathVariable("id").toLong())
        if (order.isPresent) {
            return ServerResponse.ok().body(BodyInserters.fromValue(order.get()))
        }
        return ServerResponse.notFound().build()
    }

    fun cancelRequest(request: ServerRequest): Mono<ServerResponse> {
        val order = orderRepo.findById(request.pathVariable("id").toLong())
        if (order.isPresent) {
            orderRepo.delete(order.get())
        }
        return ServerResponse.ok().build()
    }

    override fun postRequestHandler(order: OrderWrapper, request: ServerRequest): Mono<ServerResponse> {
        val customer = customerRepo.findById(order.customerId)
        if (customer.isEmpty) {
            return ServerResponse.badRequest().body(BodyInserters.fromValue(CANNOT_CREATE_NEW_CUSTOMER))
        }
        persistNewBurgers(order.burgers)
        val order =
            Order(customer.get(), order.burgers, order.ccNumber, order.ccExpDate, order.ccExp)
        val newOrder = orderRepo.save(order)
        return ServerResponse.created(URI.create("")).body(BodyInserters.fromValue(newOrder))
    }

    private fun persistNewBurgers(burgers: List<BurgerOrder>) {
        burgers.forEach {
            val existingBurger = burgerRepo.findByName(it.burger.name)
            if (existingBurger == null || !existingBurger.isStandardBurger) {
                burgerRepo.save(it.burger);
            }
            burgerOrderRepo.save(it)
        }
    }
}