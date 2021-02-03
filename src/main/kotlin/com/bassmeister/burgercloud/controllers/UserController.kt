package com.bassmeister.burgercloud.controllers

import com.bassmeister.burgercloud.api.UserModel
import com.bassmeister.burgercloud.api.converters.UserConverter
import com.bassmeister.burgercloud.data.UserRepository
import com.bassmeister.burgercloud.domain.User
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = ["/api/users"], produces = ["application/hal+json"])
class UserController(val repo:UserRepository) {

    fun getUsers():ResponseEntity<List<UserModel>> {
        var userList= UserConverter.convertUserList(repo.findAll())
        return ResponseEntity(userList, HttpStatus.OK)
    }

    @GetMapping("/{id}")
    fun getUser(@PathVariable id:Long):ResponseEntity<UserModel>{
        var user=repo.findById(id)
        if(user.isPresent){
            return ResponseEntity(UserConverter.convertUser(user.get(),true), HttpStatus.OK)
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    fun getUsersByLastName(@RequestParam(value="lastName") lastName:String?):ResponseEntity<List<UserModel>>{
        if(lastName==null){
            return getUsers();
        }
        var users = repo.getUserByLastName(lastName)
        if(users.isNotEmpty()){
            return ResponseEntity(UserConverter.convertUserList(users), HttpStatus.OK)
        }
        return ResponseEntity.notFound().build();
    }
//    @GetMapping
//    fun getUserByLastName(@RequestParam(value = "lastName") lastName: String?):ResponseEntity<UserModel>(){
//        var user=repo.getUserByLastName(lastName)
//
//
//    }
}