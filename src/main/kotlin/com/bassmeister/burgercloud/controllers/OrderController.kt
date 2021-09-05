package com.bassmeister.burgercloud.controllers

import com.bassmeister.burgercloud.api.converters.OrderConverter
import com.bassmeister.burgercloud.api.OrderModel
import com.bassmeister.burgercloud.data.BurgerRepo
import com.bassmeister.burgercloud.data.CustomerRepository
import com.bassmeister.burgercloud.data.OrderRepo
import com.bassmeister.burgercloud.domain.Burger
import com.bassmeister.burgercloud.domain.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.*


@RestController
@CrossOrigin(origins = ["*"])
@RequestMapping(path = ["/api/orders"], produces = ["application/hal+json"])
class OrderController(private val orderRepo: OrderRepo, private val customerRepo:CustomerRepository,
                      private val burgerRepo: BurgerRepo) {

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

    @PostMapping(consumes = ["application/json"])
    fun addNewOrder(@Validated @RequestBody orderWrapper: OrderWrapper):ResponseEntity<OrderModel>{
        val customer=customerRepo.findById(orderWrapper.customerId)
        if(!customer.isPresent){
            throw CustomOrderException(CANNOT_CREATE_NEW_CUSTOMER)
        }
        //TODO: Adjust tests
        persistBurgers(orderWrapper.burgers);

        val order=Order(customer.get(), orderWrapper.burgers, orderWrapper.ccNumber, orderWrapper.expDate, orderWrapper.ccExp)
        val newOrder=orderRepo.save(order)
        return ResponseEntity(OrderConverter.convertOrder(newOrder, true), HttpStatus.CREATED)
    }

    //TODO: Maybe this can be handled through cascading in JPA
    private fun persistBurgers(burgers: List<Burger>) {
        burgers.forEach{
            if(!it.isStandardBurger){
                burgerRepo.save(it);
            }
        }
    }


    @DeleteMapping("/{id}")
    fun cancelOrder(@PathVariable id:Long):ResponseEntity<OrderModel>{
        orderRepo.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}