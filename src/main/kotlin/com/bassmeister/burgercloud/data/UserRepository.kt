package com.bassmeister.burgercloud.data

import com.bassmeister.burgercloud.domain.User
import org.springframework.data.repository.CrudRepository

interface UserRepository :CrudRepository<User, Long>{

    fun getUserByLastName(lastName:String):List<User>
}