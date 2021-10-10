package com.bassmeister.burgercloud.data

import com.bassmeister.burgercloud.domain.Burger
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface BurgerRepo : ReactiveCrudRepository<Burger, String> {

    fun findByName(name:String): Mono<Burger>

}