package com.bassmeister.burgercloud.controllers

import com.bassmeister.burgercloud.api.OrderModel
import com.bassmeister.burgercloud.api.CustomerModel
import com.bassmeister.burgercloud.api.converters.OrderConverter
import com.bassmeister.burgercloud.api.converters.CustomerConverter
import com.bassmeister.burgercloud.data.OrderRepo
import com.bassmeister.burgercloud.data.CustomerRepository
import com.bassmeister.burgercloud.domain.Customer
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.transaction.Transactional
import javax.validation.Valid

@RestController @Validated
@RequestMapping(path = ["/api/customers"], produces = ["application/hal+json"])
class CustomerController(private val repo:CustomerRepository, private val orderRepo: OrderRepo) {

    fun getCustomers():ResponseEntity<List<CustomerModel>> {
        var userList= CustomerConverter.convertUserList(repo.findAll())
        return ResponseEntity(userList, HttpStatus.OK)
    }

    @GetMapping("/{id}")
    fun getCustomer(@PathVariable id:Long):ResponseEntity<CustomerModel>{
        var user=repo.findById(id)
        if(user.isPresent){
            return ResponseEntity(CustomerConverter.convertUser(user.get(),true), HttpStatus.OK)
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    fun getUsersByLastName(@RequestParam(value="lastName") lastName:String?):ResponseEntity<List<CustomerModel>>{
        if(lastName==null){
            return getCustomers();
        }
        var users = repo.getUserByLastName(lastName)
        if(users.isNotEmpty()){
            return ResponseEntity(CustomerConverter.convertUserList(users), HttpStatus.OK)
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping(consumes = ["application/json"])
    fun addNewCustomer(@Valid @RequestBody customer:Customer):ResponseEntity<CustomerModel>{
        val newCustomer=repo.save(customer)
        return ResponseEntity(CustomerConverter.convertUser(newCustomer, true), HttpStatus.CREATED)
    }

    @PutMapping("/{id}",consumes=["application/json"])
    fun updateCustomer(@PathVariable id:Long, @Valid @RequestBody customerUpdate:Customer):ResponseEntity<CustomerModel>{
        val customerOpt=repo.findById(id);
        if(customerOpt.isPresent){
            val customer=customerOpt.get()
            customerUpdate.id=customer.id;
            val updatedUser=repo.save(customerUpdate);
            return ResponseEntity(CustomerConverter.convertUser(updatedUser, true), HttpStatus.OK)
        }
        return addNewCustomer(customerUpdate)
    }

    @DeleteMapping("/{id}")
    fun deleteCustomer(@PathVariable id:Long):ResponseEntity<CustomerModel>{
        val customer=repo.findById(id)
        if(customer.isPresent){
            repo.delete(customer.get())
        }
        return ResponseEntity.noContent().build<CustomerModel>()
    }

    @GetMapping("/{id}/orders")
    @Transactional
    fun getOrders(@PathVariable id:Long):ResponseEntity<List<OrderModel>>{
        var orders=orderRepo.findAll().filter { it.customer.id==id }.toCollection(ArrayList())
        return ResponseEntity(OrderConverter.convertOrders(orders), HttpStatus.OK)
    }
}