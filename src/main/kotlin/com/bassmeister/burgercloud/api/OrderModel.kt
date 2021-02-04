package com.bassmeister.burgercloud.api

import com.fasterxml.jackson.annotation.JsonCreator
import org.springframework.hateoas.RepresentationModel

class OrderModel @JsonCreator constructor(val orderId:Long, val user:CustomerModel, val burgers:List<BurgerModel>) : RepresentationModel<OrderModel?>()