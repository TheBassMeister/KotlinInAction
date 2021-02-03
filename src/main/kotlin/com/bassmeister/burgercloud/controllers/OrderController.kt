package com.bassmeister.burgercloud.controllers

import com.bassmeister.burgercloud.api.converters.OrderConverter
import com.bassmeister.burgercloud.api.converters.OrderModel
import com.bassmeister.burgercloud.data.OrderRepo
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping(path = ["/api/orders"], produces = ["application/hal+json"])
class OrderController(val orderRepo: OrderRepo) {

    @GetMapping
    fun getAllOrders():ResponseEntity<List<OrderModel>>{
        val allOrders=orderRepo.findAll()
        return ResponseEntity(OrderConverter.convertOrders(allOrders), HttpStatus.OK)
    }

    @GetMapping("/{id}")
    fun getOrderById(@PathVariable id:Long) :ResponseEntity<OrderModel>{
        val order=orderRepo.findById(id)
        if(order.isPresent){
            return ResponseEntity(OrderConverter.convertOrder(order.get(), true), HttpStatus.OK)
        }
        return ResponseEntity.notFound().build();
    }
}