package com.bassmeister.burgercloud.data

import com.bassmeister.burgercloud.domain.Customer
import org.springframework.data.repository.CrudRepository

interface CustomerRepository :CrudRepository<Customer, Long>{

    fun getUserByLastName(lastName:String):List<Customer>
}