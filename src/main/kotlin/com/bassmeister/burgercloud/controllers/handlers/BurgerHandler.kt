package com.bassmeister.burgercloud.controllers.handlers

import com.bassmeister.burgercloud.controllers.validation.AbstractValidationHandler
import com.bassmeister.burgercloud.data.BurgerRepo
import com.bassmeister.burgercloud.domain.Burger
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.net.URI

@Component
class BurgerHandler(private val burgerRepo: BurgerRepo) : AbstractValidationHandler<Burger>(Burger::class.java) {

    override fun postRequestHandler(burger: Burger, request: ServerRequest): Mono<ServerResponse> {
        val created = burgerRepo.save(burger)
        return ServerResponse.created(URI.create("")).body(created, Burger::class.java)
    }

    fun getAllBurgers(): Mono<ServerResponse> {
        return ServerResponse.ok().body(burgerRepo.findAll(), Burger::class.java)
    }

    fun getBurger(request: ServerRequest): Mono<ServerResponse> {
        return burgerRepo.findById(request.pathVariable("id")).flatMap {
            ServerResponse.ok().body(BodyInserters.fromValue(it))
        }.switchIfEmpty(
            ServerResponse.notFound().build()
        )
    }
}

