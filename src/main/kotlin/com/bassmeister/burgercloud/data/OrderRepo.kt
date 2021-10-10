package com.bassmeister.burgercloud.data

import com.bassmeister.burgercloud.domain.Order
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.data.repository.CrudRepository

interface OrderRepo : ReactiveMongoRepository<Order, String> {
}