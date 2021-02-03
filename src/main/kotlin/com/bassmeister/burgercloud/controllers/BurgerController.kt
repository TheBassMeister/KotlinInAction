package com.bassmeister.burgercloud.controllers

import com.bassmeister.burgercloud.api.BurgerModel
import com.bassmeister.burgercloud.api.converters.BurgerConverter
import com.bassmeister.burgercloud.data.BurgerRepo
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/api/burgers"], produces = ["application/hal+json"])
class BurgerController(val burgerRepo: BurgerRepo) {

    @GetMapping
    fun getBurgers():ResponseEntity<List<BurgerModel>>{
        var burgers= burgerRepo.findAll()
        var converted=BurgerConverter.convertBurgerList(burgers)
        return ResponseEntity(converted, HttpStatus.OK)
    }

    @GetMapping("/{id}")
    fun getBurger(@PathVariable id:Long):ResponseEntity<BurgerModel>{
        var burger=burgerRepo.findById(id)
        if(burger.isPresent){
            return ResponseEntity(BurgerConverter.convertBurger(burger.get(), true), HttpStatus.OK)
        }
        return ResponseEntity.notFound().build();
    }
}