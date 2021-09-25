package com.bassmeister.burgercloud.controllers.handlers

import com.bassmeister.burgercloud.controllers.validation.AbstractValidationHandler
import com.bassmeister.burgercloud.data.CustomerRepo
import com.bassmeister.burgercloud.data.OrderRepo
import com.bassmeister.burgercloud.domain.Customer
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.net.URI
import javax.transaction.Transactional

@Component
class CustomerHandler(private val customerRepo: CustomerRepo, private val orderRepo: OrderRepo) :
    AbstractValidationHandler<Customer>(Customer::class.java) {

    fun getCustomers(request: ServerRequest): Mono<ServerResponse> {
        val lastName = request.queryParam("lastName")
        if (lastName.isPresent) {
            val customers = customerRepo.getUserByLastName(lastName.get())
            return if (customers.isNotEmpty())
                ServerResponse.ok().body(BodyInserters.fromValue(customers))
            else
                ServerResponse.notFound().build()
        }
        return ServerResponse.ok().body(BodyInserters.fromValue(customerRepo.findAll()))
    }

    fun getCustomerById(request: ServerRequest): Mono<ServerResponse> {
        val customer = customerRepo.findById(request.pathVariable("id").toLong())
        if (customer.isPresent) {
            return ServerResponse.ok().body(BodyInserters.fromValue(customer.get()))
        }
        return ServerResponse.notFound().build()
    }

    @Transactional
    fun getCustomerOrders(request: ServerRequest): Mono<ServerResponse> {
        val id=request.pathVariable("id").toLong()
        var orders=orderRepo.findAll().filter { it.customer.id==id }.toCollection(ArrayList())
        return ServerResponse.ok().body(BodyInserters.fromValue(orders))
    }

    fun deleteCustomer(request: ServerRequest): Mono<ServerResponse> {
        val customer = customerRepo.findById(request.pathVariable("id").toLong())
        if (customer.isPresent) {
            customerRepo.delete(customer.get())
        }
        return ServerResponse.ok().build()
    }

    override fun postRequestHandler(customer: Customer, request: ServerRequest): Mono<ServerResponse> {
        if (HttpMethod.PUT.name == request.methodName()) {
            return handleCustomerUpdate(customer, request.pathVariable("id").toLong())
        }
        val created = customerRepo.save(customer)
        return ServerResponse.created(URI.create("")).body(BodyInserters.fromValue(created))
    }

    private fun handleCustomerUpdate(customer: Customer, customerId: Long): Mono<ServerResponse> {
        val existingCustomer = customerRepo.findById(customerId)
        return if (existingCustomer.isPresent) {
            customer.id = existingCustomer.get().id
            val updated = customerRepo.save(customer)
            ServerResponse.ok().body(BodyInserters.fromValue(updated))
        } else {
            ServerResponse.notFound().build()
        }

    }

}