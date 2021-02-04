package com.bassmeister.burgercloud.api.converters

import com.bassmeister.burgercloud.api.CustomerModel
import com.bassmeister.burgercloud.controllers.CustomerController
import com.bassmeister.burgercloud.domain.Customer
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*
import java.util.*

object CustomerConverter {

    fun convertUserList(users: Iterable<Customer>): List<CustomerModel> {
        val usersAsModels = users.map { convertUser(it, false)}.toCollection(ArrayList<CustomerModel>())
        usersAsModels.forEach {
            it.add(linkTo(methodOn(CustomerController::class.java).getCustomer(it.id)).withSelfRel())
        }
        return usersAsModels
    }

    fun convertUser(user:Customer, withLink:Boolean):CustomerModel{
        val userModel=CustomerModel(
            user.id,
            user.firstName,
            user.lastName,
            user.street,
            user.city,
            user.state,
            user.zip,
            user.phoneNumber
        )
        if(withLink){
            Converter.addLinkToRestHome(userModel)
        }

        return userModel
    }
}