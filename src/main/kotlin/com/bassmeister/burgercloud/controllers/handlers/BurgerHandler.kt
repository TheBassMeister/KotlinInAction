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

    override fun processBody(burger: Burger, request: ServerRequest): Mono<ServerResponse> {
        burgerRepo.save(burger) //TODO : Make this request non blocking (in next chapter)
        return ServerResponse.created(URI.create("")).body(BodyInserters.fromValue(burger))
    }

    fun getAllBurgers(): Mono<ServerResponse> {
        return ServerResponse.ok().body(BodyInserters.fromValue(burgerRepo.findAll()))
    }

    fun getBurger(request: ServerRequest): Mono<ServerResponse> {
        var burger = burgerRepo.findById(request.pathVariable("id").toLong())
        if (burger.isPresent) {
            return ServerResponse.ok().body(BodyInserters.fromValue(burger.get()))
        }
        return ServerResponse.notFound().build()
    }
}

