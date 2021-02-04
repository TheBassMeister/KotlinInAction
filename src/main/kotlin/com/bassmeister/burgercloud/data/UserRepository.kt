package com.bassmeister.burgercloud.data

import com.bassmeister.burgercloud.domain.Customer
import org.springframework.data.repository.CrudRepository

interface UserRepository :CrudRepository<Customer, Long>{

    fun getUserByLastName(lastName:String):List<Customer>
}