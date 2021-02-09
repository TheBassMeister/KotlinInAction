package com.bassmeister.burgercloud.messaging

import com.bassmeister.burgercloud.domain.Order
import org.slf4j.LoggerFactory.getLogger
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class KafkaOrderService(val kafkaTemplate:KafkaTemplate<String, Order>) : OrderService{

    override fun sendOrder(order: Order) {
        println("Sending order: $order")
        kafkaTemplate.send("burger.orders", order)
    }
}