package com.bassmeister.burgercloud.controllers

import com.bassmeister.burgercloud.domain.Burger
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size


data class OrderWrapper(
                        @NotNull
                        val customerId:Long,
                        @field:Size(min=1, message = "An Order must contain at least one burger")
                        val burgers:List<Burger>,
                        //TODO: Should be supplied by UI when ordering
                        var ccNumber:String="378618187748325",
                        var expDate:String="03/22",
                        var ccExp:String="350"
                        )