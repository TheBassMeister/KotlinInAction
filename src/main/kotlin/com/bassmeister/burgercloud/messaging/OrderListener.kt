package com.bassmeister.burgercloud.messaging

import com.bassmeister.burgercloud.data.BurgerRepo
import com.bassmeister.burgercloud.data.CustomerRepository
import com.bassmeister.burgercloud.data.OrderRepo
import com.bassmeister.burgercloud.domain.Order
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import java.util.concurrent.CountDownLatch

@Component
class OrderListener(val orderRepo: OrderRepo, val customerRepository: CustomerRepository, val burgerRepo: BurgerRepo) {
    val countDownLatch:CountDownLatch= CountDownLatch(1)
    var order:Order?=null

    @KafkaListener(topics=["burger.orders"])
    fun handle(order: Order){
        //Could have been handled by hibernate, need to refresh my hibernate logic
        if(!customerRepository.findById(order.customer.id).isPresent){
            customerRepository.save(order.customer)
        }
        order.burger.forEach{ burgerRepo.save(it.burger)};

        orderRepo.save(order)
        countDownLatch.countDown()
        this.order=order
    }

}