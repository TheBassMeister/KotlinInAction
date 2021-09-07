package com.bassmeister.burgercloud.api.converters

import com.bassmeister.burgercloud.api.BurgerOrderModel
import com.bassmeister.burgercloud.api.OrderModel
import com.bassmeister.burgercloud.controllers.OrderController
import com.bassmeister.burgercloud.domain.Order
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn

object OrderConverter {

    fun convertOrders(orders: Iterable<Order>): List<OrderModel> {
        var converted = orders.map { convertOrder(it, false) }
        converted.forEach {
            it.add(
                linkTo(methodOn(OrderController::class.java).getOrderById(it.orderId)).withSelfRel()
            )
        }

        return converted
    }

    fun convertOrder(order: Order, withLink: Boolean): OrderModel {
        val convertedOrders = order.burger.map { BurgerOrderModel(it.amount, it.burger) }

        val orderModel = OrderModel(
            order.id,
            order.customer.id,
            convertedOrders
        )
        if (withLink)
            Converter.addLinkToRestHome(orderModel)
        return orderModel
    }
}