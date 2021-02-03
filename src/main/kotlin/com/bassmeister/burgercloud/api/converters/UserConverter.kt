package com.bassmeister.burgercloud.api.converters

import com.bassmeister.burgercloud.api.UserModel
import com.bassmeister.burgercloud.controllers.RootController
import com.bassmeister.burgercloud.controllers.UserController
import com.bassmeister.burgercloud.domain.User
import org.springframework.hateoas.Link
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*
import java.util.*

object UserConverter {

    fun convertUserList(users: Iterable<User>): List<UserModel> {
        val usersAsModels = users.map { convertUser(it, false)}.toCollection(ArrayList<UserModel>())
        usersAsModels.forEach {
            it.add(linkTo(methodOn(UserController::class.java).getUser(it.id)).withSelfRel())
        }
        return usersAsModels
    }

    fun convertUser(user:User, withLink:Boolean):UserModel{
        val userModel=UserModel(
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
            userModel.add(Link.of(linkTo(RootController::class.java).toString()).withRel("root"))
        }

        return userModel
    }
}