package com.bassmeister.burgercloud.controllers

import com.bassmeister.burgercloud.domain.BurgerOrder
import org.hibernate.validator.constraints.CreditCardNumber
import javax.validation.constraints.Digits
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size


data class OrderWrapper(
    @NotNull
    val customerId: Long,
    @field:Size(min = 1, message = "An Order must contain at least one burger")
    val burgers: List<BurgerOrder>,
    //TODO: Should be supplied by UI when ordering
    @field:CreditCardNumber(message = "Not a valid credit card number")
    var ccNumber: String = "378618187748325",
    @field:Pattern(regexp = "^(0[1-9]|1[0-2])([\\/])([1-9][0-9])$", message = "Must be formatted MM/YY")
    var expDate: String = "03/22",
    @field:Digits(integer = 3, fraction = 0, message = "Invalid CVV")
    var ccExp: String = "350"
)