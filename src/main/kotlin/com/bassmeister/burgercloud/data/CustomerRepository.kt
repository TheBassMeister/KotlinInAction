package com.bassmeister.burgercloud.data

import com.bassmeister.burgercloud.domain.Customer
import org.springframework.data.repository.CrudRepository
import org.springframework.data.rest.core.annotation.RestResource

@RestResource(exported = false)
interface CustomerRepository :CrudRepository<Customer, Long>{

    fun getUserByLastName(lastName:String):List<Customer>
}