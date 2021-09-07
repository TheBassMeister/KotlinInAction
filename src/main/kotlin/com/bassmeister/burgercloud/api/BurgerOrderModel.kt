package com.bassmeister.burgercloud.api

import com.bassmeister.burgercloud.domain.Burger
import com.fasterxml.jackson.annotation.JsonCreator
import org.springframework.hateoas.RepresentationModel

class BurgerOrderModel @JsonCreator constructor(val amount: Int, val burger: Burger) : RepresentationModel<BurgerOrderModel?>() {
}