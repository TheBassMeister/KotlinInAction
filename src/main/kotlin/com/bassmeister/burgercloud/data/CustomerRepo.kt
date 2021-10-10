package com.bassmeister.burgercloud.data

import com.bassmeister.burgercloud.domain.Customer
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.data.rest.core.annotation.RestResource
import reactor.core.publisher.Flux

interface CustomerRepo : ReactiveCrudRepository<Customer, String> {

    fun getUserByLastName(lastName: String): Flux<Customer>
}