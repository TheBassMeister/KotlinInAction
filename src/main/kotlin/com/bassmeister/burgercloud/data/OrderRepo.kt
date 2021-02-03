package com.bassmeister.burgercloud.data

import com.bassmeister.burgercloud.domain.Order
import org.springframework.data.repository.CrudRepository

interface OrderRepo :CrudRepository<Order, Long> {
}