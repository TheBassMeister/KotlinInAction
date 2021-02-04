package com.bassmeister.burgercloud.api.converters

import com.bassmeister.burgercloud.controllers.BurgerController
import com.bassmeister.burgercloud.controllers.OrderController
import com.bassmeister.burgercloud.controllers.RootController
import com.bassmeister.burgercloud.domain.Order
import org.springframework.hateoas.Link
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*

object OrderConverter {

    fun convertOrders(orders:Iterable<Order>): List<OrderModel>{
        var converted=orders.map { convertOrder(it, false)}
        converted.forEach {
            it.add(
                linkTo(methodOn(OrderController::class.java).getOrderById(it.orderId)).withSelfRel()
            )
        }

        return converted
    }

    fun convertOrder(order:Order, withLink: Boolean):OrderModel{
        val orderModel=OrderModel(order.id, order.customer, BurgerConverter.convertBurgerList(order.burger))
        if(withLink)
            orderModel.add(Link.of(linkTo(RootController::class.java).toString()).withRel("root"))
        return orderModel
    }
}