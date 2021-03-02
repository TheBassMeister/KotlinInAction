package com.bassmeister.burgercloud.controllers

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.LocalDateTime
import java.util.*

@ControllerAdvice
class ControllerAdviceConfig : ResponseEntityExceptionHandler() {

    override fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException, headers: HttpHeaders,
        status: HttpStatus, request: WebRequest
    ): ResponseEntity<Any> {
        val body=HashMap<String, Any>()

        body["date"]=LocalDateTime.now()
        body["title"]="Failed to create new customer"
        body["description"]=ex.allErrors.map { it.defaultMessage }

        return ResponseEntity(body, headers, status)
    }

}