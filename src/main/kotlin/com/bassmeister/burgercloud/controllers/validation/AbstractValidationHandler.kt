package com.bassmeister.burgercloud.controllers.validation

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.Errors
import org.springframework.validation.Validator
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.util.*

// Inspired by https://www.baeldung.com/spring-functional-endpoints-validation
abstract class AbstractValidationHandler<T> protected constructor(protected val validationClass: Class<T>) {
    @Autowired
    private lateinit var validator: Validator

    fun handleRequest(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono(validationClass)
            .flatMap { body ->
                val errors: Errors = BeanPropertyBindingResult(body, validationClass.name)
                validator.validate(body!!, errors)
                if (errors == null || !errors.hasErrors()) {
                    postRequestHandler(body, request)
                } else {
                    onValidationErrors(errors)
                }

            }
    }

    protected fun onValidationErrors(errors: Errors): Mono<ServerResponse> {
        val errorMessages = StringJoiner(", ")
        errors.allErrors.forEach { errorMessages.add(it.defaultMessage) }
        return ServerResponse.badRequest().bodyValue(errorMessages.toString())
    }

    protected abstract fun postRequestHandler(body: T, request: ServerRequest): Mono<ServerResponse>
}