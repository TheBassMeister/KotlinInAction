package com.bassmeister.burgercloud.controllers.handlers

import com.bassmeister.burgercloud.controllers.validation.AbstractValidationHandler
import com.bassmeister.burgercloud.data.CustomerRepo
import com.bassmeister.burgercloud.data.OrderRepo
import com.bassmeister.burgercloud.domain.Customer
import com.bassmeister.burgercloud.domain.Order
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.net.URI

@Component
class CustomerHandler(private val customerRepo: CustomerRepo, private val orderRepo: OrderRepo) :
    AbstractValidationHandler<Customer>(Customer::class.java) {

    fun getCustomers(request: ServerRequest): Mono<ServerResponse> {
        val lastName = request.queryParam("lastName")
        if (lastName.isPresent) {
            val customers = customerRepo.getUserByLastName(lastName.get())
            return customers.hasElements().flatMap {
                if (it)
                    ServerResponse.ok().body(customers, Customer::class.java)
                else
                    ServerResponse.notFound().build()

            }
        }
        return ServerResponse.ok().body(customerRepo.findAll(), Customer::class.java)
    }

    fun getCustomerById(request: ServerRequest): Mono<ServerResponse> {
        return customerRepo.findById(request.pathVariable("id")).flatMap {
            ServerResponse.ok().body(BodyInserters.fromValue(it))
        }.switchIfEmpty(
            ServerResponse.notFound().build()
        )
    }

    fun getCustomerOrders(request: ServerRequest): Mono<ServerResponse> {
        val id=request.pathVariable("id")
        val orders=orderRepo.findAll().filter {
            it.customer.id==id
        }
        return ServerResponse.ok().body(orders, Order::class.java)
    }

    fun deleteCustomer(request: ServerRequest): Mono<ServerResponse> {
        return customerRepo.findById(request.pathVariable("id")).flatMap {
            customerRepo.delete(it).then(
            ServerResponse.ok().build())
        }
        return ServerResponse.ok().build()
    }

    override fun postRequestHandler(customer: Customer, request: ServerRequest): Mono<ServerResponse> {
        if (HttpMethod.PUT.name == request.methodName()) {
            return handleCustomerUpdate(customer, request.pathVariable("id"))
        }
        val created = customerRepo.save(customer)
        return ServerResponse.created(URI.create("")).body(created, Customer::class.java)
    }

    private fun handleCustomerUpdate(customer: Customer, customerId: String): Mono<ServerResponse> {
        return customerRepo.findById(customerId).flatMap {
            customer.id=it.id
            val created=customerRepo.save(customer)
            ServerResponse.ok().body(created, Customer::class.java)
        }.switchIfEmpty(
            ServerResponse.notFound().build()
        )
    }

}