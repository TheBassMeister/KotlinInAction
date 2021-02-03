package com.bassmeister.burgercloud.api.converters

import com.bassmeister.burgercloud.api.BurgerModel
import com.bassmeister.burgercloud.domain.User
import com.fasterxml.jackson.annotation.JsonCreator
import org.springframework.hateoas.RepresentationModel

class OrderModel @JsonCreator constructor(val orderId:Long,val user:User, val burgers:List<BurgerModel>) : RepresentationModel<OrderModel?>()