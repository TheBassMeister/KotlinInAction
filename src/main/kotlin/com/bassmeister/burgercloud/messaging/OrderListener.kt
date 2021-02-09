package com.bassmeister.burgercloud.messaging

import com.bassmeister.burgercloud.data.OrderRepo
import com.bassmeister.burgercloud.domain.Order
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import java.util.concurrent.CountDownLatch

@Component
class OrderListener(val orderRepo: OrderRepo) {
    val countDownLatch:CountDownLatch= CountDownLatch(1)
    var order:Order?=null

    @KafkaListener(topics=["burger.orders"])
    fun handle(order: Order){
        println("Received an Order")
        //Need to fix the save on orderRepo, probably wrong cascading
        //orderRepo.save(order)
        countDownLatch.countDown();
        this.order=order
    }

}