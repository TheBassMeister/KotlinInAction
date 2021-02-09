package com.bassmeister.burgercloud.messaging

import com.bassmeister.burgercloud.domain.Order

interface OrderService {

    fun sendOrder(order: Order)
}