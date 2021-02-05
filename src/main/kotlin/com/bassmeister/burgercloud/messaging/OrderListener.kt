package com.bassmeister.burgercloud.messaging

import com.bassmeister.burgercloud.data.OrderRepo
import com.bassmeister.burgercloud.domain.Order
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class OrderListener(val orderRepo: OrderRepo) {

    @KafkaListener(topics=["burger.orders"])
    fun handle(order: Order){
        println("Received an Order")
        //orderRepo.save(order)
    }

}